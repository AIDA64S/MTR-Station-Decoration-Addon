package top.mcmtr.core.simulation;

import org.mtr.core.data.ClientGroup;
import org.mtr.core.serializer.SerializedDataBaseWithId;
import org.mtr.core.simulation.FileLoader;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.data.Data;

import java.nio.file.Path;
import java.util.logging.Level;

import static top.mcmtr.mod.Init.MSD_LOGGER;

public class Simulator extends Data implements Utilities {
    private long currentMillis;
    private boolean autoSave = false;
    private final String dimension;
    public final ClientGroup clientGroup = new ClientGroup();
    private final FileLoader<Catenary> fileLoaderCatenary;
    private final ObjectArrayList<Runnable> queuedRuns = new ObjectArrayList<>();

    public static final String KEY_CATENARIES = "catenaries";

    public Simulator(String dimension, Path rootPath) {
        this.dimension = dimension;
        final long startMillis = System.currentTimeMillis();
        final Path savePath = rootPath.resolve(dimension);
        fileLoaderCatenary = new FileLoader<>(catenaries, Catenary::new, savePath, KEY_CATENARIES);
        currentMillis = System.currentTimeMillis();
        MSD_LOGGER.info(String.format("MSD Data loading complete for %s in %s second(s)", dimension, (float) (currentMillis - startMillis) / MILLIS_PER_SECOND));
        sync();
    }

    public void tick() {
        try {
            currentMillis = System.currentTimeMillis();
            clientGroup.tick();
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
            MSD_LOGGER.log(Level.WARNING, "MSD tick has error", e);
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
        save(fileLoaderCatenary, useReducedHash);
        MSD_LOGGER.info(String.format("MSD Save complete for %s in %s second(s)", dimension, (System.currentTimeMillis() - startMillis) / 1000F));
    }

    private <T extends SerializedDataBaseWithId> void save(FileLoader<T> fileLoader, boolean useReducedHash) {
        final IntIntImmutablePair saveCounts = fileLoader.save(useReducedHash);
        if (saveCounts.leftInt() > 0) {
            MSD_LOGGER.info(String.format("- Changed %s: %s", fileLoader.key, saveCounts.leftInt()));
        }
        if (saveCounts.rightInt() > 0) {
            MSD_LOGGER.info(String.format("- Deleted %s: %s", fileLoader.key, saveCounts.rightInt()));
        }
    }
}