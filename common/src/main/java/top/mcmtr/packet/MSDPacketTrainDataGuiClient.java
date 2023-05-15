package top.mcmtr.packet;

import io.netty.buffer.Unpooled;
import mtr.RegistryClient;
import mtr.mappings.UtilitiesClient;
import mtr.packet.PacketTrainDataBase;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import top.mcmtr.client.MSDClientData;
import top.mcmtr.data.*;
import top.mcmtr.screen.BlockNodeScreen;
import top.mcmtr.screen.CustomTextSignScreen;
import top.mcmtr.screen.YamanoteRailwaySignScreen;

import java.util.Set;

import static top.mcmtr.packet.MSDPacket.*;

public class MSDPacketTrainDataGuiClient extends PacketTrainDataBase {
    public static void openYamanoteRailwaySignScreenS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
        final BlockPos pos = packet.readBlockPos();
        minecraftClient.execute(() -> {
            if (!(minecraftClient.screen instanceof YamanoteRailwaySignScreen)) {
                UtilitiesClient.setScreen(minecraftClient, new YamanoteRailwaySignScreen(pos));
            }
        });
    }

    public static void sendMSDSignIdsC2S(BlockPos signPos, Set<Long> selectedIds, String[] signIds) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(signPos);
        packet.writeInt(selectedIds.size());
        selectedIds.forEach(packet::writeLong);
        packet.writeInt(signIds.length);
        for (final String signType : signIds) {
            packet.writeUtf(signType == null ? "" : signType);
        }
        RegistryClient.sendToServer(PACKET_YAMANOTE_SIGN_TYPES, packet);
    }

    public static void createCatenaryS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
        final BlockPos pos1 = packet.readBlockPos();
        final BlockPos pos2 = packet.readBlockPos();
        final Catenary catenary1 = new Catenary(packet);
        final Catenary catenary2 = new Catenary(packet);
        minecraftClient.execute(() -> {
            CatenaryData.addCatenary(MSDClientData.CATENARIES, pos1, pos2, catenary1);
            CatenaryData.addCatenary(MSDClientData.CATENARIES, pos2, pos1, catenary2);
        });
    }

    public static void removeCatenaryNodeS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
        final BlockPos pos = packet.readBlockPos();
        minecraftClient.execute(() -> CatenaryData.removeCatenaryNode(null, MSDClientData.CATENARIES, pos));
    }

    public static void removeCatenaryConnectionS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
        final BlockPos pos1 = packet.readBlockPos();
        final BlockPos pos2 = packet.readBlockPos();
        minecraftClient.execute(() -> CatenaryData.removeCatenaryConnection(null, MSDClientData.CATENARIES, pos1, pos2));
    }

    public static void createRigidCatenaryS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
        final BlockPos pos1 = packet.readBlockPos();
        final BlockPos pos2 = packet.readBlockPos();
        final RigidCatenary catenary1 = new RigidCatenary(packet);
        final RigidCatenary catenary2 = new RigidCatenary(packet);
        minecraftClient.execute(() -> {
            RigidCatenaryData.addRigidCatenary(MSDClientData.RIGID_CATENARIES, pos1, pos2, catenary1);
            RigidCatenaryData.addRigidCatenary(MSDClientData.RIGID_CATENARIES, pos2, pos1, catenary2);
        });
    }

    public static void removeRigidCatenaryNodeS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
        final BlockPos pos = packet.readBlockPos();
        minecraftClient.execute(() -> RigidCatenaryData.removeRigidCatenaryNode(null, MSDClientData.RIGID_CATENARIES, pos));
    }

    public static void removeRigidCatenaryConnectionS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
        final BlockPos pos1 = packet.readBlockPos();
        final BlockPos pos2 = packet.readBlockPos();
        minecraftClient.execute(() -> RigidCatenaryData.removeRigidCatenaryConnection(null, MSDClientData.RIGID_CATENARIES, pos1, pos2));
    }

    public static void openCustomTextSignConfigScreenS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
        final BlockPos pos = packet.readBlockPos();
        final int maxArrivals = packet.readInt();
        minecraftClient.execute(() -> {
            if (!(minecraftClient.screen instanceof CustomTextSignScreen)) {
                UtilitiesClient.setScreen(minecraftClient, new CustomTextSignScreen(pos, maxArrivals));
            }
        });
    }

    public static void sendCustomTextSignConfigC2S(BlockPos pos, String[] messages) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(pos);
        packet.writeInt(messages.length);
        for (String message : messages) {
            packet.writeUtf(message);
        }
        RegistryClient.sendToServer(PACKET_CUSTOM_TEXT_SIGN_UPDATE, packet);
    }

    public static void openBlockNodeScreenS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
        final BlockPos pos = packet.readBlockPos();
        final double locationX = packet.readDouble();
        final double locationY = packet.readDouble();
        final double locationZ = packet.readDouble();
        final BlockLocation location = new BlockLocation(locationX, locationY, locationZ);
        minecraftClient.execute(() -> {
            if (!(minecraftClient.screen instanceof BlockNodeScreen)) {
                UtilitiesClient.setScreen(minecraftClient, new BlockNodeScreen(location, pos));
            }
        });
    }

    public static void sendBlockNodeLocationC2S(BlockLocation location, BlockPos pos) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(pos);
        packet.writeDouble(location.getX());
        packet.writeDouble(location.getY());
        packet.writeDouble(location.getZ());
        RegistryClient.sendToServer(PACKET_BLOCK_NODE_POS_UPDATE, packet);
    }

    public static void createTransCatenaryS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
        final BlockPos pos1 = packet.readBlockPos();
        final BlockPos pos2 = packet.readBlockPos();
        final TransCatenary catenary1 = new TransCatenary(packet);
        final TransCatenary catenary2 = new TransCatenary(packet);
        minecraftClient.execute(() -> {
            TransCatenaryData.addTrnasCatenary(MSDClientData.TRANS_CATENARIES, pos1, pos2, catenary1);
            TransCatenaryData.addTrnasCatenary(MSDClientData.TRANS_CATENARIES, pos2, pos1, catenary2);
        });
    }

    public static void removeTransCatenaryNodeS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
        final BlockPos pos = packet.readBlockPos();
        minecraftClient.execute(() -> TransCatenaryData.removeTransCatenaryNode(null, MSDClientData.TRANS_CATENARIES, pos));
    }

    public static void removeTransCatenaryConnectionS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
        final BlockPos pos1 = packet.readBlockPos();
        final BlockPos pos2 = packet.readBlockPos();
        minecraftClient.execute(() -> TransCatenaryData.removeTransCatenaryConnection(null, MSDClientData.TRANS_CATENARIES, pos1, pos2));
    }
}