package top.mcmtr.packet;

import io.netty.buffer.Unpooled;
import mtr.RegistryClient;
import mtr.mappings.UtilitiesClient;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import top.mcmtr.screen.YamanoteRailwaySignScreen;

import java.util.Set;

import static top.mcmtr.packet.MSDPacket.PACKET_YAMANOTE_SIGN_TYPES;

public class MSDPacketTrainDataGuiClient extends PacketTrainDataGuiClient {
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
}
