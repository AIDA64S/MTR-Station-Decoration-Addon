package top.mcmtr.core.operation;

import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.data.MSDData;
import top.mcmtr.core.data.RigidCatenary;
import top.mcmtr.core.generated.operation.MSDUpdateDataRequestSchema;

import javax.annotation.Nullable;

public final class MSDUpdateDataRequest extends MSDUpdateDataRequestSchema {
    private final MSDData data;

    public MSDUpdateDataRequest(MSDData data) {
        this.data = data;
    }

    public MSDUpdateDataRequest(ReaderBase readerBase, MSDData data) {
        super(readerBase);
        this.data = data;
        updateData(readerBase);
    }

    public MSDUpdateDataRequest addCatenary(Catenary catenary) {
        catenaries.add(catenary);
        return this;
    }

    public MSDUpdateDataRequest addRigidCatenary(RigidCatenary rigidCatenary) {
        rigidCatenaries.add(rigidCatenary);
        return this;
    }

    public JsonObject update() {
        final MSDUpdateDataResponse updateDataResponse = new MSDUpdateDataResponse(data);
        catenaries.forEach(catenary -> update(catenary, true, data.catenaryIdMap.get(catenary.getHexId()), data.catenaries, updateDataResponse.getCatenaries()));
        rigidCatenaries.forEach(rigidCatenary -> update(rigidCatenary, true, data.rigidCatenaryIdMap.get(rigidCatenary.getHexId()), data.rigidCatenaries, updateDataResponse.getRigidCatenaries()));
        data.sync();
        return Utilities.getJsonObjectFromData(updateDataResponse);
    }

    private static <T extends SerializedDataBase> void update(T newData, boolean addNewData, @Nullable T existingData, ObjectSet<T> dataSet, ObjectArrayList<T> dataToUpdate) {
        final boolean isCatenary = newData instanceof Catenary;
        final boolean isValid = !isCatenary || ((Catenary) newData).isValid();
        if (existingData == null) {
            if (addNewData && isValid) {
                dataSet.add(newData);
                dataToUpdate.add(newData);
            }
        } else if (isValid) {
            if (isCatenary) {
                dataSet.remove(existingData);
                dataSet.add(newData);
                dataToUpdate.add(newData);
            } else {
                existingData.updateData(new JsonReader(Utilities.getJsonObjectFromData(newData)));
                dataToUpdate.add(existingData);
            }
        }
    }
}