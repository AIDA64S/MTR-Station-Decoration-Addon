package top.mcmtr.mod.packet;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import top.mcmtr.core.data.OffsetPosition;

public final class MSDPacketOpenCatenaryScreen extends PacketHandler {
    private final BlockPos blockPos;

    private final boolean isConnected;
    private final OffsetPosition offsetPosition;

    public MSDPacketOpenCatenaryScreen(PacketBufferReceiver packetBufferReceiver) {
        this.blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
        final double x = packetBufferReceiver.readDouble();
        final double y = packetBufferReceiver.readDouble();
        final double z = packetBufferReceiver.readDouble();
        this.offsetPosition = new OffsetPosition(x, y, z);
        this.isConnected = packetBufferReceiver.readBoolean();
    }

    public MSDPacketOpenCatenaryScreen(BlockPos blockPos, boolean isConnected, OffsetPosition offsetPosition) {
        this.blockPos = blockPos;
        this.isConnected = isConnected;
        this.offsetPosition = offsetPosition;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(blockPos.asLong());
        packetBufferSender.writeDouble(offsetPosition.getX());
        packetBufferSender.writeDouble(offsetPosition.getY());
        packetBufferSender.writeDouble(offsetPosition.getZ());
        packetBufferSender.writeBoolean(isConnected);
    }

    @Override
    public void runClient() {
        MSDClientPacketHelper.openCatenaryScreen(isConnected, blockPos, offsetPosition);
    }
}