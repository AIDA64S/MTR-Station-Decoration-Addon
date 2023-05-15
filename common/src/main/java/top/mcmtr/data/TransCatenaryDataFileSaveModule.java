package top.mcmtr.data;

import mtr.data.MessagePackHelper;
import mtr.data.SerializedDataBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class TransCatenaryDataFileSaveModule extends DataModuleBase {
    private final Map<BlockPos, Map<BlockPos, TransCatenary>> catenaries;

    public TransCatenaryDataFileSaveModule(Level world, Map<BlockPos, Map<BlockPos, TransCatenary>> catenaries, Path savePath) {
        super(savePath.resolve("trans_catenaries"), world);
        this.catenaries = catenaries;
        try {
            Files.createDirectories(filePath);
        } catch (IOException e) {
            System.out.println("Trans Catenary file exception");
            e.printStackTrace();
        }
    }

    public void load() {
        existingFiles.clear();
        readMessagePackFromFile(filePath, TransCatenaryEntry::new, transCatenaryEntry -> catenaries.put(transCatenaryEntry.pos, transCatenaryEntry.connections));
        System.out.println("MTR Station Decoration TransCatenary data successfully load for " + world.dimension().location());
        canAutoSave = true;
        dataLoaded = true;
    }

    @Override
    public void autoSave() {
        checkDataLoaded();
        if (canAutoSave && checkFilesToDelete.isEmpty()) {
            enableAutoSave();
            dirtyPositions.addAll(catenaries.keySet());
        }
    }

    /**
     * 执行流程
     * 首先获取到要删除的文件列表是否为空
     * 写入数据到文件，获取到执行时间是否<2毫秒，以及检查这些数据是否被全部写入，以及在写入文件后要删除的文件列表是否不为空
     * 如果满足上述条件，则表示全部执行完毕同时有空的文件需要删除，执行删除文件，同时从已存在的文件列表中删除此数据，已删除的文件列表+1
     * 之后判断写入文件前的文件删除列表是否为空，以及现在的删除文件列表是否为空，如果之前不为空但是现在为空，则表示任务已经执行完毕
     * 如果useReducedHash为false，则表示已经写入保存数据，或者已经写入的文件列表数量不为0，或者已经删除的文件数量列表不为0，则表示执行了全保存或者文件有更新
     * 如果最后数据没有完全写入完毕，或者要删除的文件列表没有被清空，说明任务并未完全执行完毕
     */
    @Override
    public boolean autoSaveTick() {
        if (canAutoSave) {
            final boolean deleteEmptyOld = checkFilesToDelete.isEmpty();
            boolean hasSpareTime = writeDirtyDataToFile(dirtyPositions, pos -> catenaries.containsKey(pos) ? new TransCatenaryEntry(pos, catenaries.get(pos)) : null, BlockPos::asLong, filePath);
            final boolean doneWriting = dirtyPositions.isEmpty();
            if (hasSpareTime && doneWriting && !checkFilesToDelete.isEmpty()) {
                final Path path = checkFilesToDelete.remove(0);
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    System.out.println("File delete fail");
                    e.printStackTrace();
                }
                existingFiles.remove(path);
                filesDeleted++;
            }
            if (!deleteEmptyOld && checkFilesToDelete.isEmpty()) {
                if (!useReducedHash || filesWritten > 0 || filesDeleted > 0) {
                    System.out.println("MTR Station Decoration TransCatenary save complete for " + world.dimension().location() + " in " + (System.currentTimeMillis() - autoSaveStartMillis) / 1000 + " second(s)");
                    if (filesWritten > 0) {
                        System.out.println("- Change: " + filesWritten);
                    }
                    if (filesDeleted > 0) {
                        System.out.println("- Deleted: " + filesDeleted);
                    }
                }
            }
            return doneWriting && checkFilesToDelete.isEmpty();
        } else {
            return true;
        }
    }

    public static class TransCatenaryEntry extends SerializedDataBase {
        public final BlockPos pos;
        public final Map<BlockPos, TransCatenary> connections;
        private static final String KEY_NODE_POS = "trans_catenary_node_pos";
        private static final String KEY_CATENARY_CONNECTIONS = "trans_catenary_connections";

        public TransCatenaryEntry(BlockPos pos, Map<BlockPos, TransCatenary> connections) {
            this.pos = pos;
            this.connections = connections;
        }

        /**
         * 此构造方法从MessagePack中获取数据构建为一个完整的连接线需要的BlockPos, Map<BlockPos, TransCatenary>
         * 从文件读取器获取到的map获取数据后获取连接点的数据，然后通过{@link TransCatenaryData#castMessagePackValueToSKMap(Value)}获取到连接信息
         */
        public TransCatenaryEntry(Map<String, Value> map) {
            final MessagePackHelper messagePackHelper = new MessagePackHelper(map);
            this.pos = BlockPos.of(messagePackHelper.getLong(KEY_NODE_POS));
            this.connections = new HashMap<>();
            messagePackHelper.iterateArrayValue(KEY_CATENARY_CONNECTIONS, value -> {
                final Map<String, Value> mapSK = TransCatenaryData.castMessagePackValueToSKMap(value);
                connections.put(BlockPos.of(new MessagePackHelper(mapSK).getLong(KEY_NODE_POS)), new TransCatenary(mapSK));
            });
        }

        /**
         * 此方法用于将生成的完整的连接线BlockPos, Map<BlockPos, TransCatenary>写入到参数列表中的MessagePack中
         */
        @Override
        public void toMessagePack(MessagePacker messagePacker) throws IOException {
            messagePacker.packString(KEY_NODE_POS).packLong(pos.asLong());
            messagePacker.packString(KEY_CATENARY_CONNECTIONS).packArrayHeader(connections.size());
            for (final Map.Entry<BlockPos, TransCatenary> entry : connections.entrySet()) {
                final BlockPos endNodePos = entry.getKey();
                messagePacker.packMapHeader(entry.getValue().messagePackLength() + 1);
                messagePacker.packString(KEY_NODE_POS).packLong(endNodePos.asLong());
                entry.getValue().toMessagePack(messagePacker);
            }
        }

        @Override
        public int messagePackLength() {
            return 2;
        }

        @Override
        public void writePacket(FriendlyByteBuf friendlyByteBuf) {
        }
    }
}