package top.mcmtr.mod.packet;

import org.mtr.core.data.Position;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.tool.PacketBufferReceiver;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.data.Data;
import top.mcmtr.core.integration.Integration;
import top.mcmtr.core.servlet.IntegrationServlet;

public final class PacketData extends PacketDataBase {
    private static PacketData fromCatenary(IntegrationServlet.Operation operation, Catenary catenary) {
        return PacketData.create(operation, ObjectSet.of(catenary), null);
    }

    private static PacketData fromCatenaryNode(Position position) {
        return PacketData.create(IntegrationServlet.Operation.DELETE, null, ObjectSet.of(position));
    }

    private static PacketData create(IntegrationServlet.Operation operation, ObjectSet<Catenary> catenaries, ObjectSet<Position> positions) {
        final Integration integration = new Integration(new Data());
        integration.add(catenaries, positions);
        return new PacketData(operation, integration, true);
    }

    public PacketData(IntegrationServlet.Operation operation, Integration integration, boolean updateClientDataInstance) {
        super(operation, integration, updateClientDataInstance);
    }

    public static PacketData create(PacketBufferReceiver packetBufferReceiver) {
        return create(packetBufferReceiver, PacketData::new);
    }

    public static void updateCatenary(ServerWorld serverWorld, Catenary catenary) {
        fromCatenary(IntegrationServlet.Operation.UPDATE, catenary).sendHttpRequestAndBroadcastResultToAllPlayers(serverWorld);
    }

    public static void deleteCatenary(ServerWorld serverWorld, Catenary catenary) {
        fromCatenary(IntegrationServlet.Operation.DELETE, catenary).sendHttpRequestAndBroadcastResultToAllPlayers(serverWorld);
    }

    public static void deleteCatenaryNode(ServerWorld serverWorld, Position position) {
        fromCatenaryNode(position).sendHttpRequestAndBroadcastResultToAllPlayers(serverWorld);
    }
}