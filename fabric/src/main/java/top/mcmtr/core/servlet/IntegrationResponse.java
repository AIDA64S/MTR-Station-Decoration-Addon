package top.mcmtr.core.servlet;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.integration.Integration;
import top.mcmtr.core.simulation.Simulator;
import top.mcmtr.mod.Init;

import java.util.logging.Level;

public final class IntegrationResponse extends ResponseBase<Integration> {

    public IntegrationResponse(String data, Object2ObjectAVLTreeMap<String, String> parameters, Integration body, long currentMillis, Simulator simulator) {
        super(data, parameters, body, currentMillis, simulator);
    }

    public Integration update() {
        return parseBody(IntegrationResponse::update, CatenaryNodePositionCallback.EMPTY, false);
    }

    public Integration get() {
        return parseBody(IntegrationResponse::get, CatenaryNodePositionCallback.EMPTY, false);
    }

    public Integration delete() {
        return parseBody(IntegrationResponse::delete, (catenaryNodePosition, catenariesToUpdate, catenaryNodePositionsToUpdate) -> simulator.positionsToCatenary.getOrDefault(catenaryNodePosition, new Object2ObjectOpenHashMap<>()).forEach((connectedCatenaryNodePosition, catenary) -> {
            simulator.catenaries.remove(catenary);
            catenariesToUpdate.add(catenary);
            catenaryNodePositionsToUpdate.add(connectedCatenaryNodePosition);
        }), true);
    }

    public Integration list() {
        return new Integration(this.simulator);
    }

    private Integration parseBody(BodyCallback bodyCallback, CatenaryNodePositionCallback catenaryNodePositionCallback, boolean shouldSync) {
        final ObjectOpenHashSet<Catenary> catenariesToUpdate = new ObjectOpenHashSet<>();
        final ObjectOpenHashSet<Position> catenaryNodePositionsToUpdate = new ObjectOpenHashSet<>();
        try {
            body.iterateCatenaries(catenary -> bodyCallback.accept(catenary, true, catenary.getCatenaryFromData(simulator, catenaryNodePositionsToUpdate), simulator.catenaries, catenariesToUpdate));
            body.iterateCatenaryNodePositions(catenaryNodePosition -> {
                catenaryNodePositionsToUpdate.add(catenaryNodePosition);
                catenaryNodePositionCallback.accept(catenaryNodePosition, catenariesToUpdate, catenaryNodePositionsToUpdate);
            });
        } catch (Exception e) {
            Init.MSD_LOGGER.log(Level.WARNING, "MSD integration response parse body fail", e);
        }
        if (shouldSync) {
            simulator.sync();
        }
        catenaryNodePositionsToUpdate.removeIf(catenaryNodePosition -> !simulator.positionsToCatenary.getOrDefault(catenaryNodePosition, new Object2ObjectOpenHashMap<>()).isEmpty());
        final Integration integration = new Integration(simulator);
        integration.add(catenariesToUpdate, catenaryNodePositionsToUpdate);
        return integration;
    }

    private static <T extends SerializedDataBase> void update(T bodyData, boolean addNewData, T existingData, ObjectSet<T> dataSet, ObjectSet<T> dataToUpdate) {
        final boolean isCatenary = bodyData instanceof Catenary;
        final boolean isValid = !isCatenary || ((Catenary) bodyData).isValid();
        if (existingData == null) {
            if (addNewData && isValid) {
                dataSet.add(bodyData);
                dataToUpdate.add(bodyData);
            }
        } else if (isValid) {
            dataSet.remove(existingData);
            if (isCatenary) {
                dataSet.add(bodyData);
                dataToUpdate.add(bodyData);
            } else {
                existingData.updateData(new JsonReader(Utilities.getJsonObjectFromData(bodyData)));
                dataSet.add(existingData);
                dataToUpdate.add(existingData);
            }
        }
    }

    private static <T extends SerializedDataBase> void get(T bodyData, boolean addNewData, T existingData, ObjectSet<T> dataSet, ObjectSet<T> dataToUpdate) {
        if (existingData != null) {
            dataToUpdate.add(existingData);
        }
    }

    private static <T extends SerializedDataBase> void delete(T bodyData, boolean addNewData, T existingData, ObjectSet<T> dataSet, ObjectSet<T> dataToUpdate) {
        if (existingData != null && dataSet.remove(existingData)) {
            dataToUpdate.add(existingData);
        }
    }

    @FunctionalInterface
    private interface BodyCallback {
        <T extends SerializedDataBase> void accept(T bodyData, boolean addNewData, T existingData, ObjectSet<T> dataSet, ObjectSet<T> dataToUpdate);
    }

    @FunctionalInterface
    private interface CatenaryNodePositionCallback {
        CatenaryNodePositionCallback EMPTY = (catenaryNodePosition, catenariesToUpdate, catenaryNodePositionsToUpdate) -> {
        };

        void accept(Position catenaryNodePosition, ObjectOpenHashSet<Catenary> catenariesToUpdate, ObjectOpenHashSet<Position> catenaryNodePositionsToUpdate);
    }
}