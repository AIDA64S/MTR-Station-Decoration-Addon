package top.mcmtr.mod.packet;

import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import top.mcmtr.mod.Init;

public class PacketRequestData extends PacketHandler {
    private final boolean forceUpdate;

    public PacketRequestData(PacketBufferReceiver packetBufferReceiver) {
        this.forceUpdate = packetBufferReceiver.readBoolean();
    }

    public PacketRequestData(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeBoolean(forceUpdate);
    }

    @Override
    public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
        Init.schedulePlayerUpdate(serverPlayerEntity, forceUpdate);
    }
}