package top.mcmtr.core.operation;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashBigSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.generated.operation.MSDDeleteDataRequestSchema;
import top.mcmtr.core.simulation.MSDSimulator;

import javax.annotation.Nullable;

public final class MSDDeleteDataRequest extends MSDDeleteDataRequestSchema {
    public MSDDeleteDataRequest() {
        super();
    }

    public MSDDeleteDataRequest(ReaderBase readerBase) {
        super(readerBase);
        updateData(readerBase);
    }

    public MSDDeleteDataRequest addCatenaryId(String catenaryId) {
        catenaryIds.add(catenaryId);
        return this;
    }

    public MSDDeleteDataRequest addCatenaryNodePosition(Position position) {
        catenaryNodePositions.add(position);
        return this;
    }

    public JsonObject delete(MSDSimulator simulator) {
        final MSDDeleteDataResponse deleteDataResponse = new MSDDeleteDataResponse();
        final ObjectOpenHashSet<Position> catenaryNodePositionsToUpdate = new ObjectOpenHashSet<>();
        catenaryIds.forEach(catenaryId ->
                delete(simulator.catenaryIdMap.get(catenaryId), simulator.catenaries, catenaryId, deleteDataResponse.getCatenaryIds(), catenaryNodePositionsToUpdate));
        catenaryNodePositions.forEach(catenaryNodePosition ->
                simulator.positionsToCatenary.getOrDefault(catenaryNodePosition, new Object2ObjectOpenHashMap<>()).values().forEach(catenary ->
                        delete(catenary, simulator.catenaries, catenary.getHexId(), deleteDataResponse.getCatenaryIds(), catenaryNodePositionsToUpdate)));
        simulator.sync();
        catenaryNodePositionsToUpdate.forEach(catenaryNodePosition -> {
            if (simulator.positionsToCatenary.getOrDefault(catenaryNodePosition, new Object2ObjectOpenHashMap<>()).isEmpty()) {
                deleteDataResponse.getCatenaryNodePositions().add(catenaryNodePosition);
            }
        });
        return Utilities.getJsonObjectFromData(deleteDataResponse);
    }

    private static void delete(@Nullable Catenary catenary, ObjectOpenHashBigSet<Catenary> catenaries, String catenaryId, ObjectArrayList<String> catenariesIdsToUpdate, ObjectOpenHashSet<Position> catenaryNodePositionsToUpdate) {
        if (catenary != null) {
            catenaries.remove(catenary);
            catenariesIdsToUpdate.add(catenaryId);
            catenary.writePositions(catenaryNodePositionsToUpdate);
        }
    }
}