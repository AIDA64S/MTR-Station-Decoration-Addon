package top.mcmtr.mod.packet;

import org.mtr.core.integration.Response;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.tool.PacketBufferReceiver;
import top.mcmtr.core.operation.MSDDataRequest;
import top.mcmtr.core.operation.MSDDataResponse;
import top.mcmtr.mod.client.MSDMinecraftClientData;

import javax.annotation.Nonnull;

public final class MSDPacketRequestData extends MSDPacketRequestResponseBase {

    public MSDPacketRequestData(PacketBufferReceiver packetBufferReceiver) {
        super(packetBufferReceiver);
    }

    public MSDPacketRequestData(MSDDataRequest dataRequest) {
        super(Utilities.getJsonObjectFromData(dataRequest).toString());
    }

    public MSDPacketRequestData(String content) {
        super(content);
    }

    @Override
    protected void runServer(ServerWorld serverWorld, String content) {
    }

    @Override
    protected void runClient(Response response) {
        response.getData(jsonReader -> new MSDDataResponse(jsonReader, MSDMinecraftClientData.getInstance())).write();
    }

    @Override
    protected MSDPacketRequestResponseBase getInstance(String content) {
        return new MSDPacketRequestData(content);
    }

    @Nonnull
    @Override
    protected String getEndpoint() {
        return "operation/get-data";
    }

    @Override
    protected ResponseType responseType() {
        return ResponseType.PLAYER;
    }
}