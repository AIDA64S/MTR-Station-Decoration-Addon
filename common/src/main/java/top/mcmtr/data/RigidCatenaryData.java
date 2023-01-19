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
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;
import top.mcmtr.blocks.BlockRigidCatenaryNode;
import top.mcmtr.packet.MSDPacket;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static mtr.packet.IPacket.MAX_PACKET_BYTES;

public class RigidCatenaryData extends PersistentStateMapper {
    private final Level world;
    private final Map<BlockPos, Map<BlockPos, RigidCatenary>> rigidCatenaries = new HashMap<>();
    private final RigidCatenaryDataFileSaveModule rigidCatenaryDataFileSaveModule;
    private static final String NAME = "msd_rigid_catenary_data";
    private static final String KEY_CATENARIES = "rigid_catenaries";
    private static final int CATENARY_UPDATE_DISTANCE = 128;
    private final Map<Player, BlockPos> playerLastUpdatedPositions = new HashMap<>();
    private static final int PLAYER_MOVE_UPDATE_THRESHOLD = 16;

    public RigidCatenaryData(Level world) {
        super(NAME);
        this.world = world;
        final ResourceLocation dimensionLocation = world.dimension().location();
        final Path savePath = ((ServerLevel) world).getServer().getWorldPath(LevelResource.ROOT).resolve("msd").resolve(dimensionLocation.getNamespace()).resolve(dimensionLocation.getPath());
        rigidCatenaryDataFileSaveModule = new RigidCatenaryDataFileSaveModule(this, world, rigidCatenaries, savePath);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        try {
            final CompoundTag tagNewCatenaries = compoundTag.getCompound(KEY_CATENARIES);
            for (final String key : tagNewCatenaries.getAllKeys()) {
                final RigidCatenaryEntry rigidCatenaryEntry = new RigidCatenaryEntry(tagNewCatenaries.getCompound(key));
                rigidCatenaries.put(rigidCatenaryEntry.pos, rigidCatenaryEntry.connections);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        rigidCatenaryDataFileSaveModule.load();
    }

    @Override
    public void save(File file) {
        final MinecraftServer minecraftServer = ((ServerLevel) world).getServer();
        if (minecraftServer.isStopped() || !minecraftServer.isRunning()) {
            rigidCatenaryDataFileSaveModule.fullSave();
        } else {
            rigidCatenaryDataFileSaveModule.autoSave();
        }
        setDirty();
        super.save(file);
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        return compoundTag;
    }

    public void simulateRigidCatenaries() {
        final List<? extends Player> players = world.players();
        players.forEach(player -> {
            final BlockPos playerBlockPos = player.blockPosition();
            final Vec3 playerPos = player.position();
            if (!playerLastUpdatedPositions.containsKey(player) || playerLastUpdatedPositions.get(player).distManhattan(playerBlockPos) > PLAYER_MOVE_UPDATE_THRESHOLD) {
                final Map<BlockPos, Map<BlockPos, RigidCatenary>> rigidCatenariesToAdd = new HashMap<>();
                rigidCatenaries.forEach((startPos, blockPosRigidCatenaryMap) -> blockPosRigidCatenaryMap.forEach((endPos, rigidCatenary) -> {
                    if (new AABB(startPos, endPos).inflate(CATENARY_UPDATE_DISTANCE).contains(playerPos)) {
                        if (!rigidCatenariesToAdd.containsKey(startPos)) {
                            rigidCatenariesToAdd.put(startPos, new HashMap<>());
                        }
                        rigidCatenariesToAdd.get(startPos).put(endPos, rigidCatenary);
                    }
                }));
                final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
                packet.writeInt(rigidCatenariesToAdd.size());
                rigidCatenariesToAdd.forEach((posStart, rigidCatenaryMap) -> {
                    packet.writeBlockPos(posStart);
                    packet.writeInt(rigidCatenaryMap.size());
                    rigidCatenaryMap.forEach((posEnd, rigidCatenary) -> {
                        packet.writeBlockPos(posEnd);
                        rigidCatenary.writePacket(packet);
                    });
                });
                if (packet.readableBytes() <= MAX_PACKET_BYTES) {
                    Registry.sendToPlayer((ServerPlayer) player, MSDPacket.PACKET_WRITE_RIGID_CATENARY, packet);
                }
                playerLastUpdatedPositions.put(player, playerBlockPos);
            }
        });
    }

    public void disconnectPlayer(Player player) {
        playerLastUpdatedPositions.remove(player);
    }

    public boolean addRigidCatenary(BlockPos posStart, BlockPos posEnd, RigidCatenary rigidCatenary) {
        return addRigidCatenary(rigidCatenaries, posStart, posEnd, rigidCatenary);
    }

    public void removeRigidCatenaryNode(BlockPos pos) {
        removeRigidCatenaryNode(world, rigidCatenaries, pos);
    }

    public void removeRigidCatenaryConnection(BlockPos pos1, BlockPos pos2) {
        removeRigidCatenaryConnection(world, rigidCatenaries, pos1, pos2);
    }

    public static boolean addRigidCatenary(Map<BlockPos, Map<BlockPos, RigidCatenary>> rigidCatenaries, BlockPos posStart, BlockPos posEnd, RigidCatenary rigidCatenary) {
        try {
            if (posStart.getX() == posEnd.getX() && posStart.getY() == posEnd.getY() && posStart.getZ() == posEnd.getZ()) {
                return false;
            }
            if (!rigidCatenaries.containsKey(posStart)) {
                rigidCatenaries.put(posStart, new HashMap<>());
            } else {
                if (rigidCatenaries.get(posStart).containsKey(posEnd)) {
                    return false;
                }
            }
            rigidCatenaries.get(posStart).put(posEnd, rigidCatenary);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void removeRigidCatenaryNode(Level world, Map<BlockPos, Map<BlockPos, RigidCatenary>> rigidCatenaries, BlockPos pos) {
        try {
            rigidCatenaries.remove(pos);
            rigidCatenaries.forEach((startPos, rigidCatenaryMap) -> {
                rigidCatenaryMap.remove(pos);
                if (rigidCatenaryMap.isEmpty() && world != null) {
                    BlockRigidCatenaryNode.resetRigidCatenaryNode(world, pos);
                }
            });
            if (world != null) {
                validateCatenaries(world, rigidCatenaries);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeRigidCatenaryConnection(Level world, Map<BlockPos, Map<BlockPos, RigidCatenary>> rigidCatenaries, BlockPos pos1, BlockPos pos2) {
        try {
            if (rigidCatenaries.containsKey(pos1)) {
                rigidCatenaries.get(pos1).remove(pos2);
                if (rigidCatenaries.get(pos1).isEmpty() && world != null) {
                    BlockRigidCatenaryNode.resetRigidCatenaryNode(world, pos1);
                }
            }
            if (rigidCatenaries.containsKey(pos2)) {
                rigidCatenaries.get(pos2).remove(pos1);
                if (rigidCatenaries.get(pos2).isEmpty() && world != null) {
                    BlockRigidCatenaryNode.resetRigidCatenaryNode(world, pos2);
                }
            }
            if (world != null) {
                validateCatenaries(world, rigidCatenaries);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static RigidCatenaryData getInstance(Level world) {
        return getInstance(world, () -> new RigidCatenaryData(world), NAME);
    }

    private static void validateCatenaries(Level world, Map<BlockPos, Map<BlockPos, RigidCatenary>> rigidCatenaries) {
        final Set<BlockPos> rigidCatenariesToRemove = new HashSet<>();
        final Set<BlockPos> rigidCatenariesNodesToRemove = new HashSet<>();
        rigidCatenaries.forEach((startPos, rigidCatenaryMap) -> {
            final boolean chunkLoaded = chunkLoaded(world, startPos);
            if (chunkLoaded && !(world.getBlockState(startPos).getBlock() instanceof BlockRigidCatenaryNode)) {
                rigidCatenariesNodesToRemove.add(startPos);
            }
            if (rigidCatenaryMap.isEmpty()) {
                rigidCatenariesToRemove.add(startPos);
            }
        });
        rigidCatenariesToRemove.forEach(rigidCatenaries::remove);
        rigidCatenariesNodesToRemove.forEach(pos -> removeRigidCatenaryNode(null, rigidCatenaries, pos));
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
    private static class RigidCatenaryEntry extends SerializedDataBase {
        public final BlockPos pos;
        public final Map<BlockPos, RigidCatenary> connections;
        private static final String KEY_NODE_POS = "rigid_catenary_node_pos";
        private static final String KEY_CATENARY_CONNECTIONS = "rigid_catenary_connections";

        public RigidCatenaryEntry(BlockPos pos, Map<BlockPos, RigidCatenary> connections) {
            this.pos = pos;
            this.connections = connections;
        }

        public RigidCatenaryEntry(CompoundTag compoundTag) {
            this.pos = BlockPos.of(compoundTag.getLong(KEY_NODE_POS));
            this.connections = new HashMap<>();
            final CompoundTag tagConnections = compoundTag.getCompound(KEY_CATENARY_CONNECTIONS);
            for (final String keyConnection : tagConnections.getAllKeys()) {
                connections.put(BlockPos.of(tagConnections.getCompound(keyConnection).getLong(KEY_NODE_POS)), new RigidCatenary(tagConnections.getCompound(keyConnection)));
            }
        }

        public RigidCatenaryEntry(Map<String, Value> map) {
            final MessagePackHelper messagePackHelper = new MessagePackHelper(map);
            pos = BlockPos.of(messagePackHelper.getLong(KEY_NODE_POS));
            connections = new HashMap<>();
            messagePackHelper.iterateArrayValue(KEY_CATENARY_CONNECTIONS, value -> {
                final Map<String, Value> mapSK = CatenaryData.castMessagePackValueToSKMap(value);
                connections.put(BlockPos.of(new MessagePackHelper(mapSK).getLong(KEY_NODE_POS)), new RigidCatenary(mapSK));
            });
        }

        @Override
        public void toMessagePack(MessagePacker messagePacker) throws IOException {
            messagePacker.packString(KEY_NODE_POS).packLong(pos.asLong());
            messagePacker.packString(KEY_CATENARY_CONNECTIONS).packArrayHeader(connections.size());
            for (final Map.Entry<BlockPos, RigidCatenary> entry : connections.entrySet()) {
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
