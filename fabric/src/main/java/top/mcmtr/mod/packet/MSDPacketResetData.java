package top.mcmtr.mod.packet;

import org.jetbrains.annotations.NotNull;
import org.mtr.core.integration.Response;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.tool.PacketBufferReceiver;
import top.mcmtr.core.operation.MSDResetDataRequest;
import top.mcmtr.core.operation.MSDResetDataResponse;
import top.mcmtr.core.servlet.OperationType;
import top.mcmtr.mod.client.MSDMinecraftClientData;

public final class MSDPacketResetData extends MSDPacketRequestResponseBase {
    public MSDPacketResetData(PacketBufferReceiver packetBufferReceiver) {
        super(packetBufferReceiver);
    }

    public MSDPacketResetData(MSDResetDataRequest resetDataRequest) {
        super(Utilities.getJsonObjectFromData(resetDataRequest).toString());
    }

    public MSDPacketResetData(String content) {
        super(content);
    }

    @Override
    protected void runServerInbound(ServerWorld serverWorld, String content) {

    }

    @Override
    protected void runClientInbound(Response response) {
        response.getData(jsonReader -> new MSDResetDataResponse(jsonReader, MSDMinecraftClientData.getInstance())).write();
    }

    @Override
    protected MSDPacketRequestResponseBase getInstance(String content) {
        return new MSDPacketResetData(content);
    }

    @NotNull
    @Override
    protected String getEndpoint() {
        return "operation/" + OperationType.RESET_DATA;
    }

    @Override
    protected ResponseType responseType() {
        return ResponseType.ALL;
    }
}