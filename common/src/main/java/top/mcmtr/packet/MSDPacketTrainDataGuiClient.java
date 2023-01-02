package top.mcmtr.packet;

import io.netty.buffer.Unpooled;
import mtr.Keys;
import mtr.RegistryClient;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import top.mcmtr.MSDKeys;
import top.mcmtr.client.MSDClientData;
import top.mcmtr.data.Catenary;
import top.mcmtr.data.CatenaryData;
import top.mcmtr.screen.YamanoteRailwaySignScreen;

import java.util.Set;

import static top.mcmtr.packet.MSDPacket.PACKET_YAMANOTE_SIGN_TYPES;

public class MSDPacketTrainDataGuiClient extends PacketTrainDataGuiClient {
    public static void openMSDVersionCheckS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
        final String version = packet.readUtf();
        minecraftClient.execute(() -> {
            if (!MSDKeys.MOD_VERSION.split("-hotfix-")[0].equals(version)) {
                final ClientPacketListener connection = minecraftClient.getConnection();
                if (connection != null) {
                    final int widthDifference1 = minecraftClient.font.width(Text.translatable("gui.msd.mismatched_versions_your_version")) - minecraftClient.font.width(Text.translatable("gui.msd.mismatched_versions_server_version"));
                    final int widthDifference2 = minecraftClient.font.width(MSDKeys.MOD_VERSION) - minecraftClient.font.width(version);
                    final int spaceWidth = minecraftClient.font.width(" ");
                    final StringBuilder text = new StringBuilder();
                    for (int i = 0; i < -widthDifference1 / spaceWidth; i++) {
                        text.append(" ");
                    }
                    text.append(Text.translatable("gui.msd.mismatched_versions_your_version", Keys.MOD_VERSION).getString());
                    for (int i = 0; i < -widthDifference2 / spaceWidth; i++) {
                        text.append(" ");
                    }
                    text.append("\n");
                    for (int i = 0; i < widthDifference1 / spaceWidth; i++) {
                        text.append(" ");
                    }
                    text.append(Text.translatable("gui.msd.mismatched_versions_server_version", version).getString());
                    for (int i = 0; i < widthDifference2 / spaceWidth; i++) {
                        text.append(" ");
                    }
                    text.append("\n\n");
                    connection.getConnection().disconnect(Text.literal(text.toString()).append(Text.translatable("gui.msd.mismatched_versions")));
                }
            }
        });
    }

    public static void openYamanoteRailwaySignScreenS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
        final BlockPos pos = packet.readBlockPos();
        minecraftClient.execute(() -> {
            if (!(minecraftClient.screen instanceof YamanoteRailwaySignScreen)) {
                UtilitiesClient.setScreen(minecraftClient, new YamanoteRailwaySignScreen(pos));
            }
        });
    }

    public static void sendSignIdsC2S(BlockPos signPos, Set<Long> selectedIds, String[] signIds) {
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
}