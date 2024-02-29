package top.mcmtr.core.servlet;

import org.mtr.core.serializer.JsonReader;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import top.mcmtr.core.operation.*;
import top.mcmtr.core.simulation.MSDSimulator;

public final class MSDOperationServlet extends MSDServletBase {
    public MSDOperationServlet(ObjectImmutableList<MSDSimulator> simulators) {
        super(simulators);
    }

    @Override
    public JsonObject getContent(String endpoint, String data, Object2ObjectAVLTreeMap<String, String> parameters, JsonReader jsonReader, long currentMillis, MSDSimulator simulator) {
        switch (endpoint) {
            case OperationType.GET_DATA:
                return new MSDDataRequest(jsonReader).getData(simulator);
            case OperationType.UPDATE_DATA:
                return new MSDUpdateDataRequest(jsonReader, simulator).update();
            case OperationType.DELETE_DATA:
                return new MSDDeleteDataRequest(jsonReader).delete(simulator);
            case OperationType.RESET_DATA:
                return new MSDResetDataRequest(jsonReader, simulator).reset();
            default:
                return null;
        }
    }
}