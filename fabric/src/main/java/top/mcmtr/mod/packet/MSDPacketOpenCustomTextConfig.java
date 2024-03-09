package top.mcmtr.mod.packet;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;

public final class MSDPacketOpenCustomTextConfig extends PacketHandler {
    private final BlockPos blockPos;
    private final int maxArrivals;

    public MSDPacketOpenCustomTextConfig(PacketBufferReceiver packetBufferReceiver) {
        blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
        maxArrivals = packetBufferReceiver.readInt();
    }

    public MSDPacketOpenCustomTextConfig(BlockPos blockPos, int maxArrivals) {
        this.blockPos = blockPos;
        this.maxArrivals = maxArrivals;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(blockPos.asLong());
        packetBufferSender.writeInt(maxArrivals);
    }

    @Override
    public void runClient() {
        MSDClientPacketHelper.openCustomTextConfigScreen(blockPos, maxArrivals);
    }
}