package top.mcmtr.data;

import io.netty.buffer.Unpooled;
import mtr.Registry;
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
import top.mcmtr.blocks.BlockRigidCatenaryNode;
import top.mcmtr.packet.MSDPacket;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

import static mtr.packet.IPacket.MAX_PACKET_BYTES;

public class RigidCatenaryData extends LineDataBase {
    private static final int CATENARY_UPDATE_DISTANCE = 128;
    private static final int PLAYER_MOVE_UPDATE_THRESHOLD = 16;
    private static final String NAME = "msd_rigid_catenary_data";
    private final Map<BlockPos, Map<BlockPos, RigidCatenary>> rigidCatenaries = new HashMap<>();
    private final RigidCatenaryDataFileSaveModule rigidCatenaryDataFileSaveModule;
    private final Map<Player, BlockPos> playerLastUpdatedPositions = new HashMap<>();

    public RigidCatenaryData(Level world) {
        super(NAME, world);
        final ResourceLocation dimensionLocation = world.dimension().location();
        final Path savePath = ((ServerLevel) world).getServer().getWorldPath(LevelResource.ROOT).resolve("msd").resolve(dimensionLocation.getNamespace()).resolve(dimensionLocation.getPath());
        rigidCatenaryDataFileSaveModule = new RigidCatenaryDataFileSaveModule(world, rigidCatenaries, savePath);
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
                            if (!rigidCatenariesToAdd.containsKey(endPos)) {
                                rigidCatenariesToAdd.put(startPos, new HashMap<>());
                                rigidCatenariesToAdd.get(startPos).put(endPos, rigidCatenary);
                            } else {
                                if (!rigidCatenariesToAdd.get(endPos).containsKey(startPos)) {
                                    rigidCatenariesToAdd.put(startPos, new HashMap<>());
                                    rigidCatenariesToAdd.get(startPos).put(endPos, rigidCatenary);
                                }
                            }
                        } else {
                            if (!rigidCatenariesToAdd.containsKey(endPos)) {
                                rigidCatenariesToAdd.get(startPos).put(endPos, rigidCatenary);
                            } else {
                                if (!rigidCatenariesToAdd.get(endPos).containsKey(startPos)) {
                                    rigidCatenariesToAdd.get(startPos).put(endPos, rigidCatenary);
                                }
                            }
                        }
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
        rigidCatenaryDataFileSaveModule.autoSaveTick();
    }

    public static RigidCatenaryData getInstance(Level world) {
        return getInstance(world, () -> new RigidCatenaryData(world), NAME);
    }

    public void disconnectPlayer(Player player) {
        playerLastUpdatedPositions.remove(player);
    }

    public void removeRigidCatenaryNode(BlockPos pos) {
        removeRigidCatenaryNode(world, rigidCatenaries, pos);
    }

    public void removeRigidCatenaryConnection(BlockPos pos1, BlockPos pos2) {
        removeRigidCatenaryConnection(world, rigidCatenaries, pos1, pos2);
    }

    public boolean addRigidCatenary(BlockPos posStart, BlockPos posEnd, RigidCatenary rigidCatenary) {
        return addRigidCatenary(rigidCatenaries, posStart, posEnd, rigidCatenary);
    }

    public static boolean addRigidCatenary(Map<BlockPos, Map<BlockPos, RigidCatenary>> rigidCatenaries, BlockPos posStart, BlockPos posEnd, RigidCatenary rigidCatenary) {
        try {
            if (checkPosEquals(posStart, posEnd)) {
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

    public static void removeRigidCatenaryConnection(Level world, Map<BlockPos, Map<BlockPos, RigidCatenary>> rigidCatenaries, BlockPos pos1, BlockPos pos2) {
        try {
            if (rigidCatenaries.containsKey(pos1)) {
                rigidCatenaries.get(pos1).remove(pos2);
                if (rigidCatenaries.get(pos1).isEmpty() && world != null) {
                    BlockRigidCatenaryNode.resetNode(world, pos1);
                }
            }
            if (rigidCatenaries.containsKey(pos2)) {
                rigidCatenaries.get(pos2).remove(pos1);
                if (rigidCatenaries.get(pos2).isEmpty() && world != null) {
                    BlockRigidCatenaryNode.resetNode(world, pos2);
                }
            }
            if (world != null) {
                validateCatenaries(world, rigidCatenaries);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeRigidCatenaryNode(Level world, Map<BlockPos, Map<BlockPos, RigidCatenary>> rigidCatenaries, BlockPos pos) {
        try {
            rigidCatenaries.remove(pos);
            rigidCatenaries.forEach((startPos, rigidCatenaryMap) -> {
                rigidCatenaryMap.remove(pos);
                if (rigidCatenaryMap.isEmpty() && world != null) {
                    BlockRigidCatenaryNode.resetNode(world, startPos);
                }
            });
            if (world != null) {
                validateCatenaries(world, rigidCatenaries);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(CompoundTag compoundTag) {
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
}