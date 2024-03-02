package top.mcmtr.core.operation;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.data.RigidCatenary;
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

    public MSDDeleteDataRequest addRigidCatenaryId(String catenaryId) {
        rigidCatenaryIds.add(catenaryId);
        return this;
    }

    public JsonObject delete(MSDSimulator simulator) {
        final MSDDeleteDataResponse deleteDataResponse = new MSDDeleteDataResponse();
        final ObjectArraySet<Position> nodePositionsToUpdate = new ObjectArraySet<>();
        catenaryIds.forEach(catenaryId ->
                delete(simulator.catenaryIdMap.get(catenaryId), simulator.catenaries, catenaryId, deleteDataResponse.getCatenaryIds(), nodePositionsToUpdate));
        rigidCatenaryIds.forEach(rigidCatenaryId ->
                delete(simulator.rigidCatenaryIdMap.get(rigidCatenaryId), simulator.rigidCatenaries, rigidCatenaryId, deleteDataResponse.getRigidCatenaryIds(), nodePositionsToUpdate));

        catenaryNodePositions.forEach(catenaryNodePosition -> {
            simulator.positionsToCatenary.getOrDefault(catenaryNodePosition, new Object2ObjectOpenHashMap<>()).values().forEach(catenary ->
                    delete(catenary, simulator.catenaries, catenary.getHexId(), deleteDataResponse.getCatenaryIds(), nodePositionsToUpdate));
            simulator.positionsToRigidCatenary.getOrDefault(catenaryNodePosition, new Object2ObjectOpenHashMap<>()).values().forEach(rigidCatenary ->
                    delete(rigidCatenary, simulator.rigidCatenaries, rigidCatenary.getHexId(), deleteDataResponse.getRigidCatenaryIds(), nodePositionsToUpdate));
        });

        simulator.sync();

        nodePositionsToUpdate.forEach(nodePosition -> {
            if (simulator.positionsToCatenary.getOrDefault(nodePosition, new Object2ObjectOpenHashMap<>()).isEmpty()
                    && simulator.positionsToRigidCatenary.getOrDefault(nodePosition, new Object2ObjectOpenHashMap<>()).isEmpty()) {
                deleteDataResponse.getCatenaryNodePositions().add(nodePosition);
            }
        });
        return Utilities.getJsonObjectFromData(deleteDataResponse);
    }

    private static void delete(@Nullable Catenary catenary, ObjectArraySet<Catenary> catenaries, String catenaryId, ObjectArrayList<String> catenariesIdsToUpdate, ObjectArraySet<Position> catenaryNodePositionsToUpdate) {
        if (catenary != null) {
            catenaries.remove(catenary);
            catenariesIdsToUpdate.add(catenaryId);
            catenary.writePositions(catenaryNodePositionsToUpdate);
        }
    }

    private static void delete(@Nullable RigidCatenary rigidCatenary, ObjectArraySet<RigidCatenary> rigidCatenaries, String rigidCatenaryId, ObjectArrayList<String> rigidCatenariesIdsToUpdate, ObjectArraySet<Position> rigidCatenaryNodePositionsToUpdate) {
        if (rigidCatenary != null) {
            rigidCatenaries.remove(rigidCatenary);
            rigidCatenariesIdsToUpdate.add(rigidCatenaryId);
            rigidCatenary.writePositions(rigidCatenaryNodePositionsToUpdate);
        }
    }
}