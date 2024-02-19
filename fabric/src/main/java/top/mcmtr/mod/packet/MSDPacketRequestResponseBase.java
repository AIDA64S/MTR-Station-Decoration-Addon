package top.mcmtr.mod.packet;

import org.mtr.core.integration.Response;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.MinecraftServerHelper;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import top.mcmtr.mod.Init;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class MSDPacketRequestResponseBase extends PacketHandler {
    private final String content;

    public MSDPacketRequestResponseBase(PacketBufferReceiver packetBufferReceiver) {
        this.content = packetBufferReceiver.readString();
    }

    public MSDPacketRequestResponseBase(String content) {
        this.content = content;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeString(content);
    }

    @Override
    public final void runClient() {
        runClient(Response.create(Utilities.parseJson(content)));
    }

    protected final void runServer(ServerWorld serverWorld, @Nullable ServerPlayerEntity serverPlayerEntity) {
        Init.sendHttpRequest(getEndpoint(), new World(serverWorld.data), content, responseType() == MSDPacketRequestResponseBase.ResponseType.NONE ? null : response -> {
            if (responseType() == MSDPacketRequestResponseBase.ResponseType.PLAYER) {
                if (serverPlayerEntity != null) {
                    Init.REGISTRY.sendPacketToClient(serverPlayerEntity, getInstance(response));
                }
            } else {
                MinecraftServerHelper.iteratePlayers(serverWorld, serverPlayerEntityNew -> Init.REGISTRY.sendPacketToClient(serverPlayerEntityNew, getInstance(response)));
            }
            runServer(serverWorld, response);
        });
    }

    protected abstract void runServer(ServerWorld serverWorld, String content);

    protected abstract void runClient(Response response);

    protected abstract MSDPacketRequestResponseBase getInstance(String content);

    @Nonnull
    protected abstract String getEndpoint();

    protected abstract ResponseType responseType();

    protected enum ResponseType {
        NONE, PLAYER, ALL
    }
}