package top.mcmtr.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import top.mcmtr.data.Catenary;

import java.util.HashMap;
import java.util.Map;

public class MSDClientData {
    public static final Map<BlockPos, Map<BlockPos, Catenary>> CATENARIES = new HashMap<>();

    public static void writeCatenaries(Minecraft client, FriendlyByteBuf packet) {
        final Map<BlockPos, Map<BlockPos, Catenary>> catenariesTemp = new HashMap<>();
        final int catenariesCount = packet.readInt();
        for (int i = 0; i < catenariesCount; i++) {
            final BlockPos startPos = packet.readBlockPos();
            final Map<BlockPos, Catenary> catenaryMap = new HashMap<>();
            final int catenaryCount = packet.readInt();
            for (int j = 0; j < catenaryCount; j++) {
                catenaryMap.put(packet.readBlockPos(), new Catenary(packet));
            }
            catenariesTemp.put(startPos, catenaryMap);
        }
        client.execute(() -> clearAndAddAll(CATENARIES, catenariesTemp));
    }

    private static <U, V> void clearAndAddAll(Map<U, V> target, Map<U, V> source) {
        target.clear();
        target.putAll(source);
    }
}