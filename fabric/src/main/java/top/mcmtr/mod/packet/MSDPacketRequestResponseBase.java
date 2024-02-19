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

    /**
     * 发送信息到核心服务器，如果将结果发送给所有玩家，则{@link ServerPlayerEntity}可以为空
     */
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

    /**
     * 服务器收到请求后进行的操作
     */
    protected abstract void runServer(ServerWorld serverWorld, String content);

    /**
     * 客户端收到响应后进行的操作
     */
    protected abstract void runClient(Response response);

    /**
     * 获取此次操作实例
     */
    protected abstract MSDPacketRequestResponseBase getInstance(String content);

    /**
     * 获取此次操作的目的
     */
    @Nonnull
    protected abstract String getEndpoint();

    /**
     * 得到此操作会给响应给谁
     */
    protected abstract ResponseType responseType();

    /**
     * 此操作的响应目标
     */
    protected enum ResponseType {
        /**
         * 无需响应
         */
        NONE,
        /**
         * 响应给某个玩家
         */
        PLAYER,
        /**
         * 响应给所有玩家
         */
        ALL
    }
}