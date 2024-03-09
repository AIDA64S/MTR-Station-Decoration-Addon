package top.mcmtr.mod.packet;

import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import top.mcmtr.mod.blocks.BlockCustomTextSignBase;

public final class
MSDPacketUpdateCustomText extends PacketHandler {
    private final BlockPos blockPos;
    private final String[] messages;

    public MSDPacketUpdateCustomText(PacketBufferReceiver packetBufferReceiver) {
        blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
        final int maxMessages = packetBufferReceiver.readInt();
        messages = new String[maxMessages];
        for (int i = 0; i < maxMessages; i++) {
            messages[i] = packetBufferReceiver.readString();
        }
    }

    public MSDPacketUpdateCustomText(BlockPos blockPos, String[] messages) {
        this.blockPos = blockPos;
        this.messages = messages;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(blockPos.asLong());

        packetBufferSender.writeInt(messages.length);
        for (final String message : messages) {
            packetBufferSender.writeString(message);
        }
    }

    @Override
    public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
        final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
        if (entity != null && entity.data instanceof BlockCustomTextSignBase.BlockCustomTextSignBaseEntity) {
            ((BlockCustomTextSignBase.BlockCustomTextSignBaseEntity) entity.data).setData(messages);
        }
    }
}
