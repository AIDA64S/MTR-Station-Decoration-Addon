package top.mcmtr.core.servlet;

import org.mtr.core.serializer.JsonReader;
import org.mtr.core.tool.EnumHelper;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.webserver.Webserver;
import top.mcmtr.core.integration.Integration;
import top.mcmtr.core.simulation.Simulator;

import java.util.Locale;

public final class IntegrationServlet extends ServletBase {
    public IntegrationServlet(Webserver webserver, String path, ObjectImmutableList<Simulator> simulators) {
        super(webserver, path, simulators);
    }

    @Override
    protected JsonObject getContent(String endpoint, String data, Object2ObjectAVLTreeMap<String, String> parameters, JsonReader jsonReader, long currentMillis, Simulator simulator) {
        final IntegrationResponse integrationResponse = new IntegrationResponse(data, parameters, new Integration(jsonReader, simulator), currentMillis, simulator);
        switch (EnumHelper.valueOf(Operation.UPDATE, endpoint.toUpperCase(Locale.ROOT))) {
            case GET:
                return Utilities.getJsonObjectFromData(integrationResponse.get());
            case UPDATE:
                return Utilities.getJsonObjectFromData(integrationResponse.update());
            case DELETE:
                return Utilities.getJsonObjectFromData(integrationResponse.delete());
            case LIST:
                return Utilities.getJsonObjectFromData(integrationResponse.list());
            default:
                return new JsonObject();
        }
    }

    public enum Operation {
        UPDATE, GET, DELETE, LIST;

        public String getEndpoint() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }
}
