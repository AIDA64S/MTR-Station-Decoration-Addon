package top.mcmtr.core.operation;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.generated.operation.CatenariesRequestSchema;
import top.mcmtr.core.simulation.MSDSimulator;

public final class CatenariesRequest extends CatenariesRequestSchema {
    public CatenariesRequest() {
        super();
    }

    public CatenariesRequest(ReaderBase readerBase) {
        super(readerBase);
        updateData(readerBase);
    }
    public JsonObject query(MSDSimulator simulator){
        final CatenariesResponse catenariesResponse = new CatenariesResponse();
        catenaryIds.forEach(catenaryId -> {
            final Catenary catenary = simulator.catenaryIdMap.get(catenaryId);
            if(catenary != null){
                catenariesResponse.add(catenary);
            }
        });
        return Utilities.getJsonObjectFromData(catenariesResponse);
    }

    public CatenariesRequest addCatenaryId(String catenaryId) {
        catenaryIds.add(catenaryId);
        return this;
    }
}