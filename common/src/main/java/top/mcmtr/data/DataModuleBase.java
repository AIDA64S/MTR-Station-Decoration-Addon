package top.mcmtr.data;

import mtr.data.IReducedSaveData;
import mtr.data.SerializedDataBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class DataModuleBase {
    protected final Path filePath;
    protected final Level world;
    /**
     * 已经存在的文件，Key为文件路径，Value为数据的Hash值
     */
    protected final Map<Path, Integer> existingFiles = new HashMap<>();
    protected boolean canAutoSave = false;
    protected boolean dataLoaded = false;
    protected boolean useReducedHash = true;
    /**
     * 已写入文件数量
     */
    protected int filesWritten;
    /**
     * 已经删除的文件数量
     */
    protected int filesDeleted;
    protected long autoSaveStartMillis;
    protected final List<BlockPos> dirtyPositions = new ArrayList<>();
    protected final List<Path> checkFilesToDelete = new ArrayList<>();

    public DataModuleBase(Path filePath, Level world) {
        this.filePath = filePath;
        this.world = world;
    }

    /**
     * 此方法用于主动存档(全量)
     */
    public void fullSave() {
        useReducedHash = false;
        dirtyPositions.clear();
        checkFilesToDelete.clear();
        autoSave();
        while (true) {
            if (autoSaveTick()) {
                break;
            }
        }
        canAutoSave = false;
    }

    public void checkDataLoaded() {
        if (!dataLoaded) {
            dataLoaded = true;
            canAutoSave = true;
        }
    }

    public void enableAutoSave() {
        autoSaveStartMillis = System.currentTimeMillis();
        filesWritten = 0;
        filesDeleted = 0;
        checkFilesToDelete.addAll(existingFiles.keySet());
    }

    /**
     * 此方法用于自动保存的具体执行过程，需要保存的数据全部完成后返回true
     */
    public abstract boolean autoSaveTick();

    /**
     * 此方法用于调用自动保存前的准备工作
     */
    public abstract void autoSave();

    /**
     * @Return 此方法从文件读取数据写入MessagePack
     * 读取路径下的所有文件和文件夹，并逐层遍历到文件层
     * 循环读取文件，读取文件数据到MessagePack，然后读取写入到HashMap，返回到参数列表中的getData,并将此文件路径及其数据的Hash存到已存在的文件列表
     */
    protected <T extends SerializedDataBase> void readMessagePackFromFile(Path path, Function<Map<String, Value>, T> getData, Consumer<T> callback) {
        try (final Stream<Path> pathStream = Files.list(path)) {
            pathStream.forEach(idFolder -> {
                try (final Stream<Path> folderStream = Files.list(idFolder)) {
                    folderStream.forEach(idFile -> {
                        try (final InputStream inputStream = Files.newInputStream(idFile)) {
                            try (final MessageUnpacker messageUnpacker = MessagePack.newDefaultUnpacker(inputStream)) {
                                final int size = messageUnpacker.unpackMapHeader();
                                final HashMap<String, Value> result = new HashMap<>(size);
                                for (int i = 0; i < size; i++) {
                                    result.put(messageUnpacker.unpackString(), messageUnpacker.unpackValue());
                                }
                                final T data = getData.apply(result);
                                callback.accept(data);
                                existingFiles.put(idFile, getHash(data, true));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 此方法从MessagePack读取数据写入文件
     * 首先创建其父路径，然后创建数据本身存储路径，并通过{@link DataModuleBase#getHash(SerializedDataBase, boolean)}获取数据的Hash值
     * 如果文件路径没有被创建过，或者比较数据Hash发现数据有更新，则创建新的文件，将数据写入后返回文件路径，否则直接返回文件路径
     */
    private Path writeMessagePackToFile(SerializedDataBase data, long id, Path path) {
        final Path parentPath = path.resolve(String.valueOf(id % 100));
        try {
            Files.createDirectories(parentPath);
            final Path dataPath = parentPath.resolve(String.valueOf(id));
            final int hash = getHash(data, useReducedHash);
            if (!existingFiles.containsKey(dataPath) || hash != existingFiles.get(dataPath)) {
                final MessagePacker messagePacker = MessagePack.newDefaultPacker(Files.newOutputStream(dataPath, StandardOpenOption.CREATE));
                messagePacker.packMapHeader(data.messagePackLength());
                data.toMessagePack(messagePacker);
                messagePacker.close();
                existingFiles.put(dataPath, hash);
                filesWritten++;
            }
            return dataPath;
        } catch (IOException e) {
            System.out.println("File/Folder creation error");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 此方法将在自动保存时间间隙产生的新的数据写入到文件
     * 如果这段时间的更改不为空，则从其头部获取数据ID并移除，然后通过ID得到数据，返回到函数列表中的getId
     * 如果数据不为空，则通过{@link DataModuleBase#writeMessagePackToFile(SerializedDataBase, long, Path)}将数据写入文件,并将这个文件的路径从要删除的文件列表中移除
     * 如果进行以上步骤的时间超过2毫秒，则返回false，否则返回true
     */
    protected <T extends SerializedDataBase, U> boolean writeDirtyDataToFile(List<U> dirtyData, Function<U, T> getId, Function<U, Long> idToLong, Path path) {
        final long millis = System.currentTimeMillis();
        while (!dirtyData.isEmpty()) {
            final U id = dirtyData.remove(0);
            final T data = getId.apply(id);
            if (data != null) {
                final Path newPath = writeMessagePackToFile(data, idToLong.apply(id), path);
                if (newPath != null) {
                    checkFilesToDelete.remove(newPath);
                }
            }
            if (System.currentTimeMillis() - millis >= 2) {
                return false;
            }
        }
        return true;
    }

    /**
     * 此方法用于获取数据的Hash
     * 如果使用简化数据包，则通过简化数据包获取简化数据包的Hash，否则通过完整数据包获取Hash
     */
    private static int getHash(SerializedDataBase data, boolean useReducedHash) {
        try {
            final MessageBufferPacker messageBufferPacker = MessagePack.newDefaultBufferPacker();
            if (useReducedHash && data instanceof IReducedSaveData) {
                messageBufferPacker.packMapHeader(((IReducedSaveData) data).reducedMessagePackLength());
                ((IReducedSaveData) data).toReducedMessagePack(messageBufferPacker);
            } else {
                messageBufferPacker.packMapHeader(data.messagePackLength());
                data.toMessagePack(messageBufferPacker);
            }
            final int hash = Arrays.hashCode(messageBufferPacker.toByteArray());
            messageBufferPacker.close();
            return hash;
        } catch (IOException e) {
            System.out.println("The data may be empty or the data is wrong");
            e.printStackTrace();
        }
        return 0;
    }
}