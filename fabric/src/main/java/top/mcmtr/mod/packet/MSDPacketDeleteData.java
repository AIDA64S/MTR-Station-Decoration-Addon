package top.mcmtr.mod.packet;

import org.mtr.core.data.Position;
import org.mtr.core.integration.Response;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.tool.PacketBufferReceiver;
import top.mcmtr.core.operation.MSDDeleteDataRequest;
import top.mcmtr.core.operation.MSDDeleteDataResponse;
import top.mcmtr.mod.Init;
import top.mcmtr.mod.blocks.BlockNodeBase;
import top.mcmtr.mod.client.MSDMinecraftClientData;

import javax.annotation.Nonnull;

public final class MSDPacketDeleteData extends MSDPacketRequestResponseBase {
    public MSDPacketDeleteData(PacketBufferReceiver packetBufferReceiver) {
        super(packetBufferReceiver);
    }

    public MSDPacketDeleteData(MSDDeleteDataRequest deleteDataRequest) {
        super(Utilities.getJsonObjectFromData(deleteDataRequest).toString());
    }

    public MSDPacketDeleteData(String content) {
        super(content);
    }

    @Override
    protected void runServer(ServerWorld serverWorld, String content) {
        Response.create(Utilities.parseJson(content)).getData(MSDDeleteDataResponse::new).iterateCatenaryNodePosition(catenaryNodePosition -> BlockNodeBase.resetCatenaryNode(serverWorld, Init.positionToBlockPos(catenaryNodePosition)));
    }

    @Override
    protected void runClient(Response response) {
        final MSDDeleteDataResponse deleteDataResponse = response.getData(MSDDeleteDataResponse::new);
        deleteDataResponse.write(MSDMinecraftClientData.getInstance());
    }

    @Override
    protected MSDPacketRequestResponseBase getInstance(String content) {
        return new MSDPacketDeleteData(content);
    }

    @Nonnull
    @Override
    protected String getEndpoint() {
        return "operation/delete-data";
    }

    @Override
    protected ResponseType responseType() {
        return ResponseType.ALL;
    }

    /**
     * 发送要删除的接触网节点数据到服务器
     */
    public static void sendDirectlyToServerCatenaryNodePosition(ServerWorld serverWorld, Position catenaryNodePosition) {
        new MSDPacketDeleteData(new MSDDeleteDataRequest().addCatenaryNodePosition(catenaryNodePosition)).runServer(serverWorld, (ServerPlayerEntity) null);
    }

    /**
     * 发送要删除的接触网数据到服务器
     */
    public static void sendDirectlyToServerCatenaryId(ServerWorld serverWorld, String catenaryId) {
        new MSDPacketDeleteData(new MSDDeleteDataRequest().addCatenaryId(catenaryId)).runServer(serverWorld, (ServerPlayerEntity) null);
    }
}