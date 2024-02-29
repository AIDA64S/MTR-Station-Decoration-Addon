package top.mcmtr.core.operation;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import top.mcmtr.core.data.MSDClientData;
import top.mcmtr.core.generated.operation.MSDDataRequestSchema;
import top.mcmtr.core.simulation.MSDSimulator;

public final class MSDDataRequest extends MSDDataRequestSchema {
    public MSDDataRequest(String clientId, Position clientPosition, long requestRadius) {
        super(clientId, clientPosition, requestRadius);
    }

    public MSDDataRequest(ReaderBase readerBase) {
        super(readerBase);
        updateData(readerBase);
    }

    public JsonObject getData(MSDSimulator simulator) {
        final MSDDataResponse dataResponse = new MSDDataResponse(simulator);
        simulator.catenaryIdMap.forEach((catenaryId, catenary) -> {
            if (catenary.closeTo(clientPosition, requestRadius)) {
                if (existingCatenaryIds.contains(catenaryId)) {
                    dataResponse.addCatenary(catenary.getHexId());
                } else {
                    dataResponse.addCatenary(catenary);
                }
            }
        });
        simulator.rigidCatenaryIdMap.forEach((rigidCatenaryID, rigidCatenary) -> {
            if (rigidCatenary.closeTo(clientPosition, requestRadius)) {
                if (existingRigidCatenaryIds.contains(rigidCatenaryID)) {
                    dataResponse.addRigidCatenary(rigidCatenary.getHexId());
                } else {
                    dataResponse.addRigidCatenary(rigidCatenary);
                }
            }
        });
        return Utilities.getJsonObjectFromData(dataResponse);
    }

    public void writeExistingIds(MSDClientData clientData) {
        existingCatenaryIds.addAll(clientData.catenaryIdMap.keySet());
        existingRigidCatenaryIds.addAll(clientData.rigidCatenaryIdMap.keySet());
    }
}