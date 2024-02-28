package top.mcmtr.mod.packet;

import org.mtr.core.integration.Response;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.tool.PacketBufferReceiver;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.operation.MSDUpdateDataRequest;
import top.mcmtr.core.operation.MSDUpdateDataResponse;
import top.mcmtr.core.servlet.OperationType;
import top.mcmtr.mod.client.MSDMinecraftClientData;

import javax.annotation.Nonnull;

public final class MSDPacketUpdateData extends MSDPacketRequestResponseBase {
    public MSDPacketUpdateData(PacketBufferReceiver packetBufferReceiver) {
        super(packetBufferReceiver);
    }

    public MSDPacketUpdateData(MSDUpdateDataRequest updateDataRequest) {
        super(Utilities.getJsonObjectFromData(updateDataRequest).toString());
    }

    public MSDPacketUpdateData(String content) {
        super(content);
    }

    @Override
    protected void runServerInbound(ServerWorld serverWorld, String content) {
    }

    @Override
    protected void runClientInbound(Response response) {
        response.getData(jsonReader -> new MSDUpdateDataResponse(jsonReader, MSDMinecraftClientData.getInstance())).write();
    }

    @Override
    protected MSDPacketRequestResponseBase getInstance(String content) {
        return new MSDPacketUpdateData(content);
    }

    @Nonnull
    @Override
    protected String getEndpoint() {
        return "operation/" + OperationType.UPDATE_DATA;
    }

    @Override
    protected ResponseType responseType() {
        return ResponseType.ALL;
    }

    /**
     * 发送连接的接触网数据到服务器
     */
    public static void sendDirectlyToServerCatenary(ServerWorld serverWorld, Catenary catenary) {
        new MSDPacketUpdateData(new MSDUpdateDataRequest(new MSDMinecraftClientData()).addCatenary(catenary)).runServerOutbound(serverWorld, null);
    }
}