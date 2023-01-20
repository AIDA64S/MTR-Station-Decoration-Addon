package top.mcmtr.packet;

import io.netty.buffer.Unpooled;
import mtr.RegistryClient;
import mtr.mappings.UtilitiesClient;
import mtr.packet.PacketTrainDataBase;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import top.mcmtr.client.MSDClientData;
import top.mcmtr.data.Catenary;
import top.mcmtr.data.CatenaryData;
import top.mcmtr.data.RigidCatenary;
import top.mcmtr.data.RigidCatenaryData;
import top.mcmtr.screen.YamanoteRailwaySignScreen;

import java.util.Set;

import static top.mcmtr.packet.MSDPacket.PACKET_YAMANOTE_SIGN_TYPES;

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
}