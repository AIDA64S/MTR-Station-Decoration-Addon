package top.mcmtr.mod.client;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mod.Init;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.data.Data;
import top.mcmtr.core.data.TwoPositionsBase;
import top.mcmtr.mod.render.OcclusionCullingThread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ClientData extends Data {
    public static ClientData instance = new ClientData();
    public final ConcurrentHashMap<String, Boolean> catenaryCulling = new ConcurrentHashMap<>();

    @Override
    public void sync() {
        super.sync();
        checkAndRemoveFromMap(catenaryCulling, catenaries, TwoPositionsBase::getHexId);
        OcclusionCullingThread.CATENARIES.clear();
        positionsToCatenary.forEach((startPosition, catenaryMap) -> catenaryMap.forEach((endPosition, catenary) -> OcclusionCullingThread.CATENARIES.add(new OcclusionCullingThread.CatenaryWrapper(startPosition, endPosition, catenary))));
    }

    public static ClientData getInstance() {
        return instance;
    }

    public static void reset() {
        ClientData.instance = new ClientData();
    }

    public static Catenary getCatenary(BlockPos blockPos1, BlockPos blockPos2) {
        return tryGet(ClientData.getInstance().positionsToCatenary, Init.blockPosToPosition(blockPos1), Init.blockPosToPosition(blockPos2));
    }

    private static <T, U, V> void checkAndRemoveFromMap(Map<T, U> map, ObjectSet<V> dataSet, Function<V, T> getId) {
        final ObjectAVLTreeSet<T> idSet = dataSet.stream().map(getId).collect(Collectors.toCollection(ObjectAVLTreeSet::new));
        final ObjectArrayList<T> idsToRemove = new ObjectArrayList<>();
        map.keySet().forEach(id -> {
            if (!idSet.contains(id)) {
                idsToRemove.add(id);
            }
        });
        idsToRemove.forEach(map::remove);
    }
}
