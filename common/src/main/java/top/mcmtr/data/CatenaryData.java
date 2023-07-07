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
import top.mcmtr.blocks.BlockNodeBase;
import top.mcmtr.blocks.BlockRigidCatenaryNode;
import top.mcmtr.packet.MSDPacket;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

import static mtr.packet.IPacket.MAX_PACKET_BYTES;

public class CatenaryData extends LineDataBase {
    private static final int CATENARY_UPDATE_DISTANCE = 128;
    private static final int PLAYER_MOVE_UPDATE_THRESHOLD = 16;
    private static final String NAME = "msd_catenary_data";
    private final Map<BlockPos, Map<BlockPos, Catenary>> catenaries = new HashMap<>();
    private final CatenaryDataFileSaveModule catenaryDataFileSaveModule;
    private final Map<Player, BlockPos> playerLastUpdatedPositions = new HashMap<>();

    public CatenaryData(Level world) {
        super(NAME, world);
        final ResourceLocation dimensionLocation = world.dimension().location();
        final Path savePath = ((ServerLevel) world).getServer().getWorldPath(LevelResource.ROOT).resolve("msd").resolve(dimensionLocation.getNamespace()).resolve(dimensionLocation.getPath());
        catenaryDataFileSaveModule = new CatenaryDataFileSaveModule(world, catenaries, savePath);
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
                            if (!catenariesToAdd.containsKey(endPos)) {
                                catenariesToAdd.put(startPos, new HashMap<>());
                                catenariesToAdd.get(startPos).put(endPos, catenary);
                            } else {
                                if (!catenariesToAdd.get(endPos).containsKey(startPos)) {
                                    catenariesToAdd.put(startPos, new HashMap<>());
                                    catenariesToAdd.get(startPos).put(endPos, catenary);
                                }
                            }
                        } else {
                            if (!catenariesToAdd.containsKey(endPos)) {
                                catenariesToAdd.get(startPos).put(endPos, catenary);
                            } else {
                                if (!catenariesToAdd.get(endPos).containsKey(startPos)) {
                                    catenariesToAdd.get(startPos).put(endPos, catenary);
                                }
                            }
                        }
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
        catenaryDataFileSaveModule.autoSaveTick();
    }

    public static CatenaryData getInstance(Level world) {
        return getInstance(world, () -> new CatenaryData(world), NAME);
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
            if (checkPosEquals(posStart, posEnd)) {
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
                    BlockNodeBase.resetNode(world, startPos);
                    BlockRigidCatenaryNode.resetNode(world, startPos);
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
                    BlockNodeBase.resetNode(world, pos1);
                    BlockRigidCatenaryNode.resetNode(world, pos1);
                }
            }
            if (catenaries.containsKey(pos2)) {
                catenaries.get(pos2).remove(pos1);
                if (catenaries.get(pos2).isEmpty() && world != null) {
                    BlockNodeBase.resetNode(world, pos2);
                    BlockRigidCatenaryNode.resetNode(world, pos2);
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
    public void load(CompoundTag compoundTag) {
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

    private static void validateCatenaries(Level world, Map<BlockPos, Map<BlockPos, Catenary>> catenaries) {
        final Set<BlockPos> catenariesToRemove = new HashSet<>();
        final Set<BlockPos> catenariesNodesToRemove = new HashSet<>();
        catenaries.forEach((startPos, catenaryMap) -> {
            final boolean chunkLoaded = chunkLoaded(world, startPos);
            if (chunkLoaded && !((world.getBlockState(startPos).getBlock() instanceof BlockNodeBase) || (world.getBlockState(startPos).getBlock() instanceof BlockRigidCatenaryNode))) {
                catenariesNodesToRemove.add(startPos);
            }
            if (catenaryMap.isEmpty()) {
                catenariesToRemove.add(startPos);
            }
        });
        catenariesToRemove.forEach(catenaries::remove);
        catenariesNodesToRemove.forEach(pos -> removeCatenaryNode(null, catenaries, pos));
    }
}