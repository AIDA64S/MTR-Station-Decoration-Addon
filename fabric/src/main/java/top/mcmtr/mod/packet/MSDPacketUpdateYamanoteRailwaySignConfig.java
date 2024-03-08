package top.mcmtr.mod.packet;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import top.mcmtr.mod.blocks.BlockYamanoteRailwaySign;

public final class MSDPacketUpdateYamanoteRailwaySignConfig extends PacketHandler {
    private final BlockPos blockPos;
    private final LongAVLTreeSet selectedIds;
    private final String[] signIds;

    public MSDPacketUpdateYamanoteRailwaySignConfig(PacketBufferReceiver packetBufferReceiver) {
        blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
        final int selectedIdsLength = packetBufferReceiver.readInt();
        selectedIds = new LongAVLTreeSet();
        for (int i = 0; i < selectedIdsLength; i++) {
            selectedIds.add(packetBufferReceiver.readLong());
        }
        final int signLength = packetBufferReceiver.readInt();
        signIds = new String[signLength];
        for (int i = 0; i < signLength; i++) {
            final String signId = packetBufferReceiver.readString();
            signIds[i] = signId.isEmpty() ? null : signId;
        }
    }

    public MSDPacketUpdateYamanoteRailwaySignConfig(BlockPos blockPos, LongAVLTreeSet selectedIds, String[] signIds) {
        this.blockPos = blockPos;
        this.selectedIds = selectedIds;
        this.signIds = signIds;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(blockPos.asLong());
        packetBufferSender.writeInt(selectedIds.size());
        selectedIds.forEach(packetBufferSender::writeLong);
        packetBufferSender.writeInt(signIds.length);
        for (final String signId : signIds) {
            packetBufferSender.writeString(signId == null ? "" : signId);
        }
    }

    @Override
    public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
        final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
        if (entity != null) {
            if (entity.data instanceof BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity) {
                ((BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity) entity.data).setData(selectedIds, signIds);
            }
        }
    }
}