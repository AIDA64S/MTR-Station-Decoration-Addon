package top.mcmtr.data;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import io.netty.buffer.Unpooled;
import mtr.Registry;
import mtr.data.MessagePackHelper;
import mtr.data.SerializedDataBase;
import mtr.mappings.PersistentStateMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;
import top.mcmtr.blocks.BlockCatenaryNode;
import top.mcmtr.packet.MSDPacket;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static mtr.packet.IPacket.MAX_PACKET_BYTES;

public class CatenaryData extends PersistentStateMapper {
    private final Level world;
    private final Map<BlockPos, Map<BlockPos, Catenary>> catenaries = new HashMap<>();
    private final CatenaryDataFileSaveModule catenaryDataFileSaveModule;
    private static final int DATA_VERSION = 1;
    private static final String NAME = "msd_catenary_data";
    private static final String KEY_RAW_MESSAGE_PACK = "msd_raw_message_pack";
    private static final String KEY_DATA_VERSION = "msd_data_version";
    private static final String KEY_CATENARIES = "catenaries";
    private static final int CATENARY_UPDATE_DISTANCE = 128;
    private final Map<Player, BlockPos> playerLastUpdatedPositions = new HashMap<>();
    private static final int PLAYER_MOVE_UPDATE_THRESHOLD = 16;

    public CatenaryData(Level world) {
        super(NAME);
        this.world = world;
        final ResourceLocation dimensionLocation = world.dimension().location();
        final Path savePath = ((ServerLevel) world).getServer().getWorldPath(LevelResource.ROOT).resolve("msd").resolve(dimensionLocation.getNamespace()).resolve(dimensionLocation.getPath());
        catenaryDataFileSaveModule = new CatenaryDataFileSaveModule(this, world, catenaries, savePath);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        if (compoundTag.contains(KEY_RAW_MESSAGE_PACK)) {
            try {
                final MessageUnpacker messageUnpacker = MessagePack.newDefaultUnpacker(compoundTag.getByteArray(KEY_RAW_MESSAGE_PACK));
                final int mapSize = messageUnpacker.unpackMapHeader();
                for (int i = 0; i < mapSize; ++i) {
                    final String key = messageUnpacker.unpackString();
                    if (key.equals(KEY_DATA_VERSION)) {
                        if (messageUnpacker.unpackInt() > DATA_VERSION) {
                            throw new IllegalArgumentException("MSD:Unsupported data version");
                        }
                        continue;
                    }
                    final int arraySize = messageUnpacker.unpackArrayHeader();
                    if (key.equals(KEY_CATENARIES)) {
                        for (int j = 0; j < arraySize; ++j) {
                            final CatenaryEntry catenaryEntry = new CatenaryEntry(readMessagePackSKMap(messageUnpacker));
                            catenaries.put(catenaryEntry.pos, catenaryEntry.connections);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                final CompoundTag tagNewCatenaries = compoundTag.getCompound(KEY_CATENARIES);
                for (final String key : tagNewCatenaries.getAllKeys()) {
                    final CatenaryEntry catenaryEntry = new CatenaryEntry(tagNewCatenaries.getCompound(key));
                    catenaries.put(catenaryEntry.pos, catenaryEntry.connections);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catenaryDataFileSaveModule.load();
    }

    @Override
    public void save(File file) {
        final MinecraftServer minecraftServer = ((ServerLevel) world).getServer();
        if (minecraftServer.isStopped() || !minecraftServer.isRunning()) {
            catenaryDataFileSaveModule.fullSave();
        } else {
            catenaryDataFileSaveModule.autoSave();
        }
        setDirty();
        super.save(file);
    }

    public void simulateCatenaries() {
        final List<? extends Player> players = world.players();
        players.forEach(player -> {
            final BlockPos playerBlockPos = player.blockPosition();
            final Vec3 playerPos = player.position();
            if (!playerLastUpdatedPositions.containsKey(player) || playerLastUpdatedPositions.get(player).distManhattan(playerBlockPos) > PLAYER_MOVE_UPDATE_THRESHOLD) {
                final Map<BlockPos, Map<BlockPos, Catenary>> catenariesToAdd = new HashMap<>();
                catenaries.forEach((startPos, blockPosCatenaryMap) -> blockPosCatenaryMap.forEach((endPos, catenary) -> {
                    if (new AABB(startPos, endPos).inflate(CATENARY_UPDATE_DISTANCE).contains(playerPos)) {
                        if (!catenariesToAdd.containsKey(startPos)) {
                            catenariesToAdd.put(startPos, new HashMap<>());
                        }
                        catenariesToAdd.get(startPos).put(endPos, catenary);
                    }
                }));
                final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
                packet.writeInt(catenariesToAdd.size());
                catenariesToAdd.forEach((posStart, catenaryMap) -> {
                    packet.writeBlockPos(posStart);
                    packet.writeInt(catenaryMap.size());
                    catenaryMap.forEach((posEnd, catenary) -> {
                        packet.writeBlockPos(posEnd);
                        catenary.writePacket(packet);
                    });
                });
                if (packet.readableBytes() <= MAX_PACKET_BYTES) {
                    Registry.sendToPlayer((ServerPlayer) player, MSDPacket.PACKET_WRITE_CATENARY, packet);
                }
                playerLastUpdatedPositions.put(player, playerBlockPos);
            }
        });
    }

    public void disconnectPlayer(Player player) {
        playerLastUpdatedPositions.remove(player);
    }

    public boolean addCatenary(BlockPos posStart, BlockPos posEnd, Catenary catenary) {
        return addCatenary(catenaries, posStart, posEnd, catenary);
    }

    public void removeCatenaryNode(BlockPos pos) {
        removeCatenaryNode(world, catenaries, pos);
    }

    public void removeCatenaryConnection(BlockPos pos1, BlockPos pos2) {
        removeCatenaryConnection(world, catenaries, pos1, pos2);
    }

    public static boolean addCatenary(Map<BlockPos, Map<BlockPos, Catenary>> catenaries, BlockPos posStart, BlockPos posEnd, Catenary catenary) {
        try {
            if (posStart.getX() == posEnd.getX() && posStart.getY() == posEnd.getY() && posStart.getZ() == posEnd.getZ()) {
                return false;
            }
            if (!catenaries.containsKey(posStart)) {
                catenaries.put(posStart, new HashMap<>());
            } else {
                if (catenaries.get(posStart).containsKey(posEnd)) {
                    return false;
                }
            }
            catenaries.get(posStart).put(posEnd, catenary);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void removeCatenaryNode(Level world, Map<BlockPos, Map<BlockPos, Catenary>> catenaries, BlockPos pos) {
        try {
            catenaries.remove(pos);
            catenaries.forEach((startPos, catenaryMap) -> {
                catenaryMap.remove(pos);
                if (catenaryMap.isEmpty() && world != null) {
                    BlockCatenaryNode.resetCatenaryNode(world, pos);
                }
            });
            if (world != null) {
                validateCatenaries(world, catenaries);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeCatenaryConnection(Level world, Map<BlockPos, Map<BlockPos, Catenary>> catenaries, BlockPos pos1, BlockPos pos2) {
        try {
            if (catenaries.containsKey(pos1)) {
                catenaries.get(pos1).remove(pos2);
                if (catenaries.get(pos1).isEmpty() && world != null) {
                    BlockCatenaryNode.resetCatenaryNode(world, pos1);
                }
            }
            if (catenaries.containsKey(pos2)) {
                catenaries.get(pos2).remove(pos1);
                if (catenaries.get(pos2).isEmpty() && world != null) {
                    BlockCatenaryNode.resetCatenaryNode(world, pos2);
                }
            }
            if (world != null) {
                validateCatenaries(world, catenaries);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        return compoundTag;
    }

    public static Map<String, Value> castMessagePackValueToSKMap(Value value) {
        final Map<Value, Value> oldMap = value == null ? new HashMap<>() : value.asMapValue().map();
        final HashMap<String, Value> resultMap = new HashMap<>(oldMap.size());
        oldMap.forEach((key, newValue) -> resultMap.put(key.asStringValue().asString(), newValue));
        return resultMap;
    }

    public static boolean chunkLoaded(Level world, BlockPos pos) {
        return world.getChunkSource().getChunkNow(pos.getX() / 16, pos.getZ() / 16) != null && world.hasChunk(pos.getX() / 16, pos.getZ() / 16);
    }

    public static String prettyPrint(JsonElement jsonElement) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
    }

    public static CatenaryData getInstance(Level world) {
        return getInstance(world, () -> new CatenaryData(world), NAME);
    }

    private static void validateCatenaries(Level world, Map<BlockPos, Map<BlockPos, Catenary>> catenaries) {
        final Set<BlockPos> catenariesToRemove = new HashSet<>();
        final Set<BlockPos> catenariesNodesToRemove = new HashSet<>();
        catenaries.forEach((startPos, catenaryMap) -> {
            final boolean chunkLoaded = chunkLoaded(world, startPos);
            if (chunkLoaded && !(world.getBlockState(startPos).getBlock() instanceof BlockCatenaryNode)) {
                catenariesNodesToRemove.add(startPos);
            }
            if (catenaryMap.isEmpty()) {
                catenariesToRemove.add(startPos);
            }
        });
        catenariesToRemove.forEach(catenaries::remove);
        catenariesNodesToRemove.forEach(pos -> removeCatenaryNode(null, catenaries, pos));
    }

    public static Map<String, Value> readMessagePackSKMap(MessageUnpacker messageUnpacker) throws IOException {
        final int size = messageUnpacker.unpackMapHeader();
        final HashMap<String, Value> result = new HashMap<>(size);
        for (int i = 0; i < size; ++i) {
            result.put(messageUnpacker.unpackString(), messageUnpacker.unpackValue());
        }
        return result;
    }

    public static boolean isBetween(double value, double value1, double value2, double padding) {
        return value >= Math.min(value1, value2) - padding && value <= Math.max(value1, value2) + padding;
    }

    @Deprecated
    private static class CatenaryEntry extends SerializedDataBase {
        public final BlockPos pos;
        public final Map<BlockPos, Catenary> connections;
        private static final String KEY_NODE_POS = "catenary_node_pos";
        private static final String KEY_CATENARY_CONNECTIONS = "catenary_connections";

        public CatenaryEntry(BlockPos pos, Map<BlockPos, Catenary> connections) {
            this.pos = pos;
            this.connections = connections;
        }

        public CatenaryEntry(CompoundTag compoundTag) {
            this.pos = BlockPos.of(compoundTag.getLong(KEY_NODE_POS));
            this.connections = new HashMap<>();
            final CompoundTag tagConnections = compoundTag.getCompound(KEY_CATENARY_CONNECTIONS);
            for (final String keyConnection : tagConnections.getAllKeys()) {
                connections.put(BlockPos.of(tagConnections.getCompound(keyConnection).getLong(KEY_NODE_POS)), new Catenary(tagConnections.getCompound(keyConnection)));
            }
        }

        public CatenaryEntry(Map<String, Value> map) {
            final MessagePackHelper messagePackHelper = new MessagePackHelper(map);
            pos = BlockPos.of(messagePackHelper.getLong(KEY_NODE_POS));
            connections = new HashMap<>();
            messagePackHelper.iterateArrayValue(KEY_CATENARY_CONNECTIONS, value -> {
                final Map<String, Value> mapSK = CatenaryData.castMessagePackValueToSKMap(value);
                connections.put(BlockPos.of(new MessagePackHelper(mapSK).getLong(KEY_NODE_POS)), new Catenary(mapSK));
            });
        }

        @Override
        public void toMessagePack(MessagePacker messagePacker) throws IOException {
            messagePacker.packString(KEY_NODE_POS).packLong(pos.asLong());
            messagePacker.packString(KEY_CATENARY_CONNECTIONS).packArrayHeader(connections.size());
            for (final Map.Entry<BlockPos, Catenary> entry : connections.entrySet()) {
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
        public void writePacket(FriendlyByteBuf packet) {

        }
    }
}