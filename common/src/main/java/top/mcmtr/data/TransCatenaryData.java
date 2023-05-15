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
import top.mcmtr.packet.MSDPacket;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

import static mtr.packet.IPacket.MAX_PACKET_BYTES;

public class TransCatenaryData extends LineDataBase {
    private static final int CATENARY_UPDATE_DISTANCE = 128;
    private static final int PLAYER_MOVE_UPDATE_THRESHOLD = 16;
    private static final String NAME = "msd_trans_catenary_data";
    private final Map<BlockPos, Map<BlockPos, TransCatenary>> catenaries = new HashMap<>();
    private final TransCatenaryDataFileSaveModule transCatenaryDataFileSaveModule;

    private TransCatenaryData(Level world) {
        super(NAME, world);
        final ResourceLocation dimensionLocation = world.dimension().location();
        final Path savePath = ((ServerLevel) world).getServer().getWorldPath(LevelResource.ROOT).resolve("msd").resolve(dimensionLocation.getNamespace()).resolve(dimensionLocation.getPath());
        transCatenaryDataFileSaveModule = new TransCatenaryDataFileSaveModule(world, catenaries, savePath);
    }

    /**
     * 同步玩家附近的连接线到客户端，以提供渲染
     * 获取世界内所有玩家的列表，然后遍历连接线Map，使用AABB将处于玩家一定范围内(暂时设定为128)的数据存入要发送到客户端的Map中
     * 之后使用网络工具包将这些写好的Map打包，发送到客户端侧
     * 保存数据到文件
     */
    public void simulateTransCatenaries() {
        final List<? extends Player> players = world.players();
        players.forEach(player -> {
            final BlockPos playerBlockPos = player.blockPosition();
            final Vec3 playerPos = player.position();
            if (!playerLastUpdatedPositions.containsKey(player) || playerLastUpdatedPositions.get(player).distManhattan(playerBlockPos) > PLAYER_MOVE_UPDATE_THRESHOLD) {
                final Map<BlockPos, Map<BlockPos, TransCatenary>> catenariesWillAddToClient = new HashMap<>();
                catenaries.forEach((startPos, catenaryMap) -> catenaryMap.forEach((endPos, catenary) -> {
                    if (new AABB(startPos, endPos).inflate(CATENARY_UPDATE_DISTANCE).contains(playerPos)) {
                        if (!catenariesWillAddToClient.containsKey(startPos)) {
                            if (!catenariesWillAddToClient.containsKey(endPos)) {
                                catenariesWillAddToClient.put(startPos, new HashMap<>());
                                catenariesWillAddToClient.get(startPos).put(endPos, catenary);
                            } else {
                                if (!catenariesWillAddToClient.get(endPos).containsKey(startPos)) {
                                    catenariesWillAddToClient.put(startPos, new HashMap<>());
                                    catenariesWillAddToClient.get(startPos).put(endPos, catenary);
                                }
                            }
                        } else {
                            if (!catenariesWillAddToClient.containsKey(endPos)) {
                                catenariesWillAddToClient.get(startPos).put(endPos, catenary);
                            } else {
                                if (!catenariesWillAddToClient.get(endPos).containsKey(startPos)) {
                                    catenariesWillAddToClient.get(startPos).put(endPos, catenary);
                                }
                            }
                        }
                    }
                }));
                final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
                packet.writeInt(catenariesWillAddToClient.size());
                catenariesWillAddToClient.forEach((posStart, catenaryMap) -> {
                    packet.writeBlockPos(posStart);
                    packet.writeInt(catenaryMap.size());
                    catenaryMap.forEach((posEnd, catenary) -> {
                        packet.writeBlockPos(posEnd);
                        catenary.writePacket(packet);
                    });
                });
                if (packet.readableBytes() <= MAX_PACKET_BYTES) {
                    Registry.sendToPlayer((ServerPlayer) player, MSDPacket.PACKET_WRITE_TRANS_CATENARY, packet);
                }
                playerLastUpdatedPositions.put(player, playerBlockPos);
            }
        });
        transCatenaryDataFileSaveModule.autoSaveTick();
    }

    public static TransCatenaryData getInstance(Level world) {
        return getInstance(world, () -> new TransCatenaryData(world), NAME);
    }

    public void disconnectPlayer(Player player) {
        playerLastUpdatedPositions.remove(player);
    }

    public boolean addTransCatenary(BlockPos posStart, BlockPos posEnd, TransCatenary transCatenary) {
        return addTrnasCatenary(catenaries, posStart, posEnd, transCatenary);
    }

    public void removeTransCatenaryConnection(BlockPos pos1, BlockPos pos2) {
        removeTransCatenaryConnection(world, catenaries, pos1, pos2);
    }

    public void removeTransCatenaryNode(BlockPos pos) {
        removeTransCatenaryNode(world, catenaries, pos);
    }

    /**
     * 首先判断两个连接点位置是否相同，如果相同则不创建连接
     * 其次判断两个节点之间是否已经连接，如果已经连接则不创建连接
     * 满足上述条件后创建链接
     */
    public static boolean addTrnasCatenary(Map<BlockPos, Map<BlockPos, TransCatenary>> catenaries, BlockPos posStart, BlockPos posEnd, TransCatenary catenary) {
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
            System.out.println("BlockPos maybe is null!");
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 此方法用与使用移除器删除两个点之间的连接时相关操作
     * 分别判断两个点是否都作为起始点，如果是则获取与该点的所有连接信息，并删除与另一个点的连接信息
     * 如果起始点在删除连接后无任何连接，则将其所在方块设置为未连接状态
     * 之后调用{@link TransCatenaryData#validateCatenary(Level, Map)}验证剩余节点的连接是否合法
     */
    public static void removeTransCatenaryConnection(Level world, Map<BlockPos, Map<BlockPos, TransCatenary>> catenaries, BlockPos pos1, BlockPos pos2) {
        try {
            if (catenaries.containsKey(pos1)) {
                catenaries.get(pos1).remove(pos2);
                if (catenaries.get(pos1).isEmpty() && world != null) {
                    BlockNodeBase.resetNode(world, pos1);
                }
            }
            if (catenaries.containsKey(pos2)) {
                catenaries.get(pos2).remove(pos1);
                if (catenaries.get(pos2).isEmpty() && world != null) {
                    BlockNodeBase.resetNode(world, pos2);
                }
            }
            if (world != null) {
                validateCatenary(world, catenaries);
            }
        } catch (Exception e) {
            System.out.println("BlockPos maybe is null!");
            e.printStackTrace();
        }
    }

    /**
     * 此方法用于当移除节点时删除连接线相关操作
     * 首先移除连接线中所有以目标位置作为起始位置的连接线
     * 之后遍历剩下的连接线，删除所有以目标位置作为终点位置的连接线，如果删除后这个节点已经没有连接线与其连接，则将其所在位置的方块恢复为未连接状态
     * 之后调用{@link TransCatenaryData#validateCatenary(Level, Map)}验证剩余节点的连接是否合法
     */
    public static void removeTransCatenaryNode(Level world, Map<BlockPos, Map<BlockPos, TransCatenary>> catenaries, BlockPos pos) {
        try {
            catenaries.remove(pos);
            catenaries.forEach((startPos, catenaryMap) -> {
                catenaryMap.remove(pos);
                if (catenaryMap.isEmpty() && world != null) {
                    BlockNodeBase.resetNode(world, startPos);
                }
            });
            if (world != null) {
                validateCatenary(world, catenaries);
            }
        } catch (Exception e) {
            System.out.println("BlockPos maybe is null!");
            e.printStackTrace();
        }
    }

    @Override
    public void load(CompoundTag compoundTag) {
        transCatenaryDataFileSaveModule.load();
    }

    @Override
    public void save(File file) {
        final MinecraftServer minecraftServer = ((ServerLevel) world).getServer();
        if (minecraftServer.isStopped() || !minecraftServer.isRunning()) {
            transCatenaryDataFileSaveModule.fullSave();
        } else {
            transCatenaryDataFileSaveModule.autoSave();
        }
        setDirty();
        super.save(file);
    }

    /**
     * @Return 此静态方法用于验证数据
     * 创建两个Set用于存储需要删除的线路所在的点位置数据
     * 遍历接触网数据
     * 首先检测是否位于已加载的区块内检测这个点所在位置的方块是否不为目标节点方块，如果满足上述条件，则将该点存入需要删除的节点列表
     * 其次检测这个点所连接的线路是否为0，如果是则将这个点所在的位置放入需要删除的连接线的列表
     * 之后遍历需要删除的节点集合，调用{@link TransCatenaryData#removeTransCatenaryNode(Level, Map, BlockPos)}删除与此点有关的信息
     * 遍历需要删除的连接集合，并删除与此节点有关的连接点信息
     */
    private static void validateCatenary(Level world, Map<BlockPos, Map<BlockPos, TransCatenary>> catenaries) {
        final Set<BlockPos> catenariesToRemove = new HashSet<>();
        final Set<BlockPos> catenariesNodeToRemove = new HashSet<>();
        catenaries.forEach((startPos, catenaryMap) -> {
            final boolean chunkLoaded = chunkLoaded(world, startPos);
            if (chunkLoaded && !(world.getBlockState(startPos).getBlock() instanceof BlockNodeBase)) {
                catenariesNodeToRemove.add(startPos);
            }
            if (catenaryMap.isEmpty()) {
                catenariesToRemove.add(startPos);
            }
        });
        catenariesNodeToRemove.forEach(pos -> removeTransCatenaryNode(null, catenaries, pos));
        catenariesToRemove.forEach(catenaries::remove);
    }
}