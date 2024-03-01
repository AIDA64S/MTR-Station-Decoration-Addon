package top.mcmtr.mod.packet;

import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import top.mcmtr.core.data.OffsetPosition;
import top.mcmtr.mod.blocks.BlockCatenaryNode;

public final class MSDPacketUpdateCatenaryNode extends PacketHandler {
    private final BlockPos blockPos;
    private final OffsetPosition offsetPosition;

    public MSDPacketUpdateCatenaryNode(PacketBufferReceiver packetBufferReceiver) {
        this.blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
        final double x = packetBufferReceiver.readDouble();
        final double y = packetBufferReceiver.readDouble();
        final double z = packetBufferReceiver.readDouble();
        this.offsetPosition = new OffsetPosition(x, y, z);
    }

    public MSDPacketUpdateCatenaryNode(BlockPos blockPos, OffsetPosition offsetPosition) {
        this.blockPos = blockPos;
        this.offsetPosition = offsetPosition;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(blockPos.asLong());
        packetBufferSender.writeDouble(offsetPosition.getX());
        packetBufferSender.writeDouble(offsetPosition.getY());
        packetBufferSender.writeDouble(offsetPosition.getZ());
    }

    @Override
    public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
        final BlockEntity blockEntity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
        if (blockEntity != null && blockEntity.data instanceof BlockCatenaryNode.BlockCatenaryNodeEntity) {
            ((BlockCatenaryNode.BlockCatenaryNodeEntity) blockEntity.data).setOffsetPosition(offsetPosition);
        }
    }
}