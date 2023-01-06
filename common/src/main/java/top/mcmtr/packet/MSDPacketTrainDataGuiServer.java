package top.mcmtr.packet;

import io.netty.buffer.Unpooled;
import mtr.Registry;
import mtr.block.BlockRouteSignBase;
import mtr.data.RailwayData;
import mtr.data.RailwayDataLoggingModule;
import mtr.data.SerializedDataBase;
import mtr.mappings.BlockEntityMapper;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import top.mcmtr.MSDKeys;
import top.mcmtr.blocks.BlockYamanoteRailwaySign;
import top.mcmtr.data.Catenary;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static top.mcmtr.packet.MSDPacket.*;

public class MSDPacketTrainDataGuiServer extends PacketTrainDataGuiServer {
    public static void versionMSDCheckS2C(ServerPlayer player) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeUtf(MSDKeys.MOD_VERSION.split("-hotfix-")[0]);
        Registry.sendToPlayer(player, PACKET_MSD_VERSION_CHECK, packet);
    }

    public static void openYamanoteRailwaySignScreenS2C(ServerPlayer player, BlockPos signPos) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(signPos);
        Registry.sendToPlayer(player, PACKET_OPEN_YAMANOTE_RAILWAY_SIGN_SCREEN, packet);
    }

    public static void receiveSignIdsC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
        final BlockPos signPos = packet.readBlockPos();
        final int selectedIdsLength = packet.readInt();
        final Set<Long> selectedIds = new HashSet<>();
        for (int i = 0; i < selectedIdsLength; i++) {
            selectedIds.add(packet.readLong());
        }
        final int signLength = packet.readInt();
        final String[] signIds = new String[signLength];
        for (int i = 0; i < signLength; i++) {
            final String signId = packet.readUtf(SerializedDataBase.PACKET_STRING_READ_LENGTH);
            signIds[i] = signId.isEmpty() ? null : signId;
        }
        minecraftServer.execute(() -> {
            final BlockEntity entity = player.level.getBlockEntity(signPos);
            if (entity instanceof BlockYamanoteRailwaySign.TileEntityRailwaySign) {
                setTileEntityDataAndWriteUpdate(player, entity2 -> entity2.setData(selectedIds, signIds), (BlockYamanoteRailwaySign.TileEntityRailwaySign) entity);
            } else if (entity instanceof BlockRouteSignBase.TileEntityRouteSignBase) {
                final long platformId = selectedIds.isEmpty() ? 0 : (long) selectedIds.toArray()[0];
                final BlockEntity entityAbove = player.level.getBlockEntity(signPos.above());
                if (entityAbove instanceof BlockRouteSignBase.TileEntityRouteSignBase) {
                    setTileEntityDataAndWriteUpdate(player, entity2 -> entity2.setPlatformId(platformId), ((BlockRouteSignBase.TileEntityRouteSignBase) entityAbove), (BlockRouteSignBase.TileEntityRouteSignBase) entity);
                } else {
                    setTileEntityDataAndWriteUpdate(player, entity2 -> entity2.setPlatformId(platformId), (BlockRouteSignBase.TileEntityRouteSignBase) entity);
                }
            }
        });
    }

    @SafeVarargs
    private static <T extends BlockEntityMapper> void setTileEntityDataAndWriteUpdate(ServerPlayer player, Consumer<T> setData, T... entities) {
        final RailwayData railwayData = RailwayData.getInstance(player.level);
        if (railwayData != null && entities.length > 0) {
            final CompoundTag compoundTagOld = new CompoundTag();
            entities[0].writeCompoundTag(compoundTagOld);
            BlockPos blockPos = null;
            long posLong = 0;
            for (final T entity : entities) {
                setData.accept(entity);
                final BlockPos entityPos = entity.getBlockPos();
                if (blockPos == null || entityPos.asLong() > posLong) {
                    blockPos = entityPos;
                    posLong = entityPos.asLong();
                }
            }
            final CompoundTag compoundTagNew = new CompoundTag();
            entities[0].writeCompoundTag(compoundTagNew);
            railwayData.railwayDataLoggingModule.addEvent(player, entities[0].getClass(), RailwayDataLoggingModule.getData(compoundTagOld), RailwayDataLoggingModule.getData(compoundTagNew), blockPos);
        }
    }

    public static void createCatenaryS2C(Level world, BlockPos pos1, BlockPos pos2, Catenary catenary1, Catenary catenary2) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(pos1);
        packet.writeBlockPos(pos2);
        catenary1.writePacket(packet);
        catenary2.writePacket(packet);
        world.players().forEach(worldPlayer -> Registry.sendToPlayer((ServerPlayer) worldPlayer, PACKET_CREATE_CATENARY, packet));
    }

    public static void removeCatenaryNodeS2C(Level world, BlockPos pos) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(pos);
        world.players().forEach(worldPlayer -> Registry.sendToPlayer((ServerPlayer) worldPlayer, PACKET_REMOVE_CATENARY_NODE, packet));
    }

    public static void removeCatenaryConnectionS2C(Level world, BlockPos pos1, BlockPos pos2) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(pos1);
        packet.writeBlockPos(pos2);
        world.players().forEach(worldPlayer -> Registry.sendToPlayer((ServerPlayer) worldPlayer, PACKET_REMOVE_CATENARY, packet));
    }
}