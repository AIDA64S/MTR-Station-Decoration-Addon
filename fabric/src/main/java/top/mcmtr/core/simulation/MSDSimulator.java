package top.mcmtr.core.simulation;

import org.mtr.core.serializer.SerializedDataBaseWithId;
import org.mtr.core.simulation.FileLoader;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import top.mcmtr.core.MSDMain;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.data.MSDData;

import java.nio.file.Path;

public class MSDSimulator extends MSDData implements Utilities {
    private boolean autoSave = false;
    private final String dimension;
    private final FileLoader<Catenary> fileLoaderCatenaries;
    private final ObjectArrayList<Runnable> queuedRuns = new ObjectArrayList<>();
    private static final String KEY_CATENARIES = "catenaries";

    public MSDSimulator(String dimension, Path rootPath) {
        this.dimension = dimension;
        final long startMillis = System.currentTimeMillis();

        final Path savePath = rootPath.resolve(dimension);
        this.fileLoaderCatenaries = new FileLoader<>(catenaries, Catenary::new, savePath, KEY_CATENARIES);

        final long endMillis = System.currentTimeMillis();
        MSDMain.MSD_CORE_LOG.info(String.format("MSD Data loading complete for %s in %s second(s)", dimension, (float) (endMillis - startMillis) / MILLIS_PER_SECOND));
        sync();
    }

    public void tick() {
        try {
            if (autoSave) {
                save(true);
                autoSave = false;
            }

            if (!queuedRuns.isEmpty()) {
                final Runnable runnable = queuedRuns.remove(0);
                if (runnable != null) {
                    runnable.run();
                }
            }
        } catch (Exception e) {
            MSDMain.MSD_CORE_LOG.error("MSD Simulator tick error", e);
            throw e;
        }
    }

    public void save() {
        autoSave = true;
    }

    public void stop() {
        save(false);
    }

    public void run(Runnable runnable) {
        queuedRuns.add(runnable);
    }

    private void save(boolean useReducedHash) {
        final long startMillis = System.currentTimeMillis();
        save(fileLoaderCatenaries, useReducedHash);
        MSDMain.MSD_CORE_LOG.info(String.format("MSD Save complete for %s in %s second(s)", dimension, (System.currentTimeMillis() - startMillis) / 1000F));
    }

    private <T extends SerializedDataBaseWithId> void save(FileLoader<T> fileLoader, boolean useReducedHash) {
        final IntIntImmutablePair saveCounts = fileLoader.save(useReducedHash);
        if (saveCounts.leftInt() > 0) {
            MSDMain.MSD_CORE_LOG.info(String.format("- MSD Changed %s: %s", fileLoader.key, saveCounts.leftInt()));
        }
        if (saveCounts.rightInt() > 0) {
            MSDMain.MSD_CORE_LOG.info(String.format("- MSD Deleted %s: %s", fileLoader.key, saveCounts.rightInt()));
        }
    }
}