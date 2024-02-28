package top.mcmtr.core.operation;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.data.MSDData;
import top.mcmtr.core.data.OffsetPosition;
import top.mcmtr.core.generated.operation.MSDResetDataRequestSchema;

import javax.annotation.Nullable;

public final class MSDResetDataRequest extends MSDResetDataRequestSchema {
    private final MSDData data;

    public MSDResetDataRequest(MSDData data) {
        super();
        this.data = data;
    }

    public MSDResetDataRequest(ReaderBase readerBase, MSDData data) {
        super(readerBase);
        this.data = data;
        updateData(readerBase);
    }

    public MSDResetDataRequest addCatenaryNodePosition(Position position) {
        catenaryNodePositions.add(position);
        return this;
    }

    public MSDResetDataRequest addOffsetPosition(OffsetPosition offsetPosition){
        offsetPositions.add(offsetPosition);
        return this;
    }

    public JsonObject reset() {
        MSDResetDataResponse resetDataResponse = new MSDResetDataResponse(data);
        catenaryNodePositions.forEach(catenaryNodePosition ->
                data.positionsToCatenary.getOrDefault(catenaryNodePosition, new Object2ObjectOpenHashMap<>()).values().forEach(catenary -> {
                    Catenary newCatenary = catenary.getPositionStart().equals(catenaryNodePosition) ?
                            new Catenary(catenary.getPositionStart(), catenary.getPositionEnd(), offsetPositions.isEmpty() ? catenary.getOffsetPositionStart() : offsetPositions.get(0), catenary.getOffsetPositionEnd(), catenary.getCatenaryType()) :
                            new Catenary(catenary.getPositionStart(), catenary.getPositionEnd(), catenary.getOffsetPositionStart(), offsetPositions.isEmpty() ? catenary.getOffsetPositionEnd() : offsetPositions.get(0), catenary.getCatenaryType());
                    update(newCatenary, data.catenaryIdMap.get(catenary.getHexId()), data.catenaries, resetDataResponse.getCatenaries());
                }));
        data.sync();
        return Utilities.getJsonObjectFromData(resetDataResponse);
    }

    private static <T extends SerializedDataBase> void update(T newData, @Nullable T existingData, ObjectSet<T> dataSet, ObjectArrayList<T> dataToUpdate) {
        if (existingData != null) {
            dataSet.remove(existingData);
        }
        dataSet.add(newData);
        dataToUpdate.add(newData);
    }
}