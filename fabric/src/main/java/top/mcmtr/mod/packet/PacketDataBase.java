package top.mcmtr.mod.packet;

import org.apache.commons.io.IOUtils;
import org.mtr.core.integration.Response;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.tool.EnumHelper;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.mapper.MinecraftServerHelper;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.data.Data;
import top.mcmtr.core.integration.Integration;
import top.mcmtr.core.servlet.IntegrationServlet;
import top.mcmtr.mod.Init;
import top.mcmtr.mod.blocks.BlockCatenaryNode;
import top.mcmtr.mod.client.ClientData;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;

public class PacketDataBase extends PacketHandler {
    protected final IntegrationServlet.Operation operation;
    protected final Integration integration;
    private final boolean updateClientDataInstance;

    public PacketDataBase(IntegrationServlet.Operation operation, Integration integration, boolean updateClientDataInstance) {
        this.operation = operation;
        this.integration = integration;
        this.updateClientDataInstance = updateClientDataInstance;
    }

    protected static <T extends PacketDataBase> T create(PacketBufferReceiver packetBufferReceiver, PacketDataBaseInstance<T> packetDataBaseInstance) {
        final IntegrationServlet.Operation operation = EnumHelper.valueOf(IntegrationServlet.Operation.UPDATE, packetBufferReceiver.readString());
        final JsonReader integrationJsonReader = new JsonReader(Utilities.parseJson(packetBufferReceiver.readString()));
        final boolean updateClientDataInstance = packetBufferReceiver.readBoolean();
        return packetDataBaseInstance.create(operation, new Integration(integrationJsonReader, ClientData.getInstance()), updateClientDataInstance);
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeString(operation.toString());
        packetBufferSender.writeString(Utilities.getJsonObjectFromData(integration).toString());
        packetBufferSender.writeBoolean(updateClientDataInstance);
    }

    @Override
    public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
        sendHttpRequestAndBroadcastResultToAllPlayers(serverPlayerEntity.getServerWorld());
    }

    @Override
    public void runClientQueued() {
        if (updateClientDataInstance) {
            updateClientForClientData(ClientData.getInstance());
        }
    }

    protected void sendHttpRequestAndBroadcastResultToAllPlayers(ServerWorld serverWorld) {
        sendHttpDataRequest(operation, integration, newIntegration -> {
            newIntegration.iterateCatenaryNodePositions(catenaryNodePosition -> BlockCatenaryNode.resetCatenaryNode(serverWorld, Init.positionToBlockPos(catenaryNodePosition)));
            MinecraftServerHelper.iteratePlayers(serverWorld, worldPlayer -> Init.REGISTRY.sendPacketToClient(worldPlayer, new PacketData(operation, newIntegration, updateClientDataInstance)));
        });
    }

    private void updateClientForClientData(ClientData clientData) {
        if (integration.hasData()) {
            writeJsonObjectToDataSet(clientData.catenaries, integration::iterateCatenaries, Catenary::getHexId);
        }
        if (integration.hasData()) {
            clientData.sync();
        }
        //TODO better checking if routes have changed
    }

    private <T extends SerializedDataBase, U> void writeJsonObjectToDataSet(ObjectSet<T> dataSet, Consumer<Consumer<T>> iterator, Function<T, U> getId) {
        final ObjectArraySet<T> newData = new ObjectArraySet<>();
        final ObjectOpenHashSet<U> idList = new ObjectOpenHashSet<>();
        iterator.accept(data -> {
            newData.add(data);
            idList.add(getId.apply(data));
        });
        if (operation == IntegrationServlet.Operation.LIST) {
            dataSet.clear();
        }
        if ((operation == IntegrationServlet.Operation.UPDATE || operation == IntegrationServlet.Operation.DELETE) && !idList.isEmpty()) {
            dataSet.removeIf(data -> idList.contains(getId.apply(data)));
        }
        if (operation == IntegrationServlet.Operation.UPDATE || operation == IntegrationServlet.Operation.LIST) {
            dataSet.addAll(newData);
        }
    }

    public static void sendHttpRequest(String endpoint, JsonObject contentObject, Consumer<JsonObject> consumer) {
        try {
            final HttpURLConnection connection = (HttpURLConnection) new URL(String.format("http://localhost:%s/msd/api/%s", Init.getPort(), endpoint)).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type", "application/json");
            connection.setDoOutput(true);
            try (final OutputStream dataOutputStream = connection.getOutputStream()) {
                dataOutputStream.write(contentObject.toString().getBytes(StandardCharsets.UTF_8));
                dataOutputStream.flush();
            }
            try (final InputStream inputStream = connection.getInputStream()) {
                consumer.accept(Utilities.parseJson(IOUtils.toString(inputStream, StandardCharsets.UTF_8)));
            }
        } catch (Exception e) {
            Init.MSD_LOGGER.log(Level.WARNING, "MSD send http request error", e);
        }
    }

    protected static void sendHttpDataRequest(IntegrationServlet.Operation operation, Integration integration, Consumer<Integration> consumer) {
        sendHttpRequest("data/" + operation.getEndpoint(), Utilities.getJsonObjectFromData(integration), data -> consumer.accept(Response.create(data).getData(jsonReader -> new Integration(jsonReader, new Data()))));
    }

    @FunctionalInterface
    protected interface PacketDataBaseInstance<T extends PacketDataBase> {
        T create(IntegrationServlet.Operation operation, Integration integration, boolean updateClientDataInstance);
    }
}