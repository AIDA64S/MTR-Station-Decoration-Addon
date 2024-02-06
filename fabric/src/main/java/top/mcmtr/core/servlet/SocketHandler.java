package top.mcmtr.core.servlet;

import org.mtr.core.serializer.JsonReader;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.webserver.Webserver;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.integration.Integration;
import top.mcmtr.core.simulation.Simulator;

import java.util.logging.Level;

import static top.mcmtr.core.servlet.ServletBase.PARAMETER_DIMENSION;
import static top.mcmtr.mod.Init.MSD_LOGGER;

public class SocketHandler {
    private static final String CHANNEL = "update";

    public static void register(Webserver webserver, ObjectImmutableList<Simulator> simulators) {
        webserver.addSocketListener(CHANNEL, ((socketIOClient, id, jsonObject) -> {
            final Simulator simulator = simulators.get(jsonObject.get(PARAMETER_DIMENSION).getAsInt());
            simulator.run(() -> {
                try {
                    simulator.clientGroup.updateData(new JsonReader(jsonObject));
                    simulator.clientGroup.setSendToClient(webserver, socketIOClient, CHANNEL);
                    final double updateRadius = simulator.clientGroup.getUpdateRadius();
                    final JsonObject responseObject = new JsonObject();
                    simulator.clientGroup.iterateClients(client -> {
                        try {
                            final ObjectArraySet<Catenary> catenaries = new ObjectArraySet<>();
                            simulator.catenaries.forEach(catenary -> {
                                if (catenary.closeTo(client.getPosition(), updateRadius)) {
                                    catenaries.add(catenary);
                                }
                            });
                            final Integration integration = new Integration(simulator);
                            integration.add(catenaries, null);
                            responseObject.add(client.uuid.toString(), Utilities.getJsonObjectFromData(integration));
                        } catch (Exception e) {
                            MSD_LOGGER.log(Level.WARNING, "MSD Client handler error", e);
                        }
                    });
                    simulator.clientGroup.sendToClient(responseObject);
                } catch (Exception e) {
                    MSD_LOGGER.log(Level.WARNING, "MSD socket handler register error", e);
                }
            });
        }));
    }
}
