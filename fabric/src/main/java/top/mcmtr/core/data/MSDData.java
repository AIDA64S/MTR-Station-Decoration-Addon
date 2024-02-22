package top.mcmtr.core.data;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.SerializedDataBaseWithId;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import top.mcmtr.core.MSDMain;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class MSDData {
    public final ObjectArraySet<Catenary> catenaries = new ObjectArraySet<>();
    public final Object2ObjectOpenHashMap<String, Catenary> catenaryIdMap = new Object2ObjectOpenHashMap<>();
    public final Object2ObjectOpenHashMap<Position, Object2ObjectOpenHashMap<Position, Catenary>> positionsToCatenary = new Object2ObjectOpenHashMap<>();

    public void sync() {
        try {
            positionsToCatenary.clear();
            catenaries.forEach(catenary -> catenary.writePositionsToCatenaryCache(positionsToCatenary));
            mapIds(catenaryIdMap, catenaries);
        } catch (Exception e) {
            MSDMain.MSD_CORE_LOG.error("MSD Data sync error", e);
        }
    }

    public static <T, U, V, W extends Map<T, X>, X extends Map<U, V>> void put(W map, T key1, U key2, Function<V, V> putValue, Supplier<X> innerMapSupplier) {
        final X innerMap = map.get(key1);
        final X newInnerMap;
        if (innerMap == null) {
            newInnerMap = innerMapSupplier.get();
            map.put(key1, newInnerMap);
        } else {
            newInnerMap = innerMap;
        }
        newInnerMap.put(key2, putValue.apply(newInnerMap.get(key2)));
    }

    private static <U extends SerializedDataBaseWithId> void mapIds(Object2ObjectMap<String, U> map, ObjectSet<U> source) {
        map.clear();
        source.forEach(data -> map.put(data.getHexId(), data));
    }
}