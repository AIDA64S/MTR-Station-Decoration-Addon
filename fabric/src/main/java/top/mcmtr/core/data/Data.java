package top.mcmtr.core.data;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.SerializedDataBaseWithId;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashBigSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import top.mcmtr.mod.Init;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;

public class Data {
    public final ObjectOpenHashBigSet<Catenary> catenaries = new ObjectOpenHashBigSet<>();
    public final Object2ObjectOpenHashMap<String, Catenary> catenaryIdMap = new Object2ObjectOpenHashMap<>();
    public final Object2ObjectOpenHashMap<Position, Object2ObjectOpenHashMap<Position, Catenary>> positionsToCatenary = new Object2ObjectOpenHashMap<>();

    public void sync() {
        try {
            positionsToCatenary.clear();
            catenaries.forEach(catenary -> catenary.writePositionsToCatenaryCache(positionsToCatenary));
            catenaries.forEach(catenary -> catenary.writeConnectedCatenariesCacheFromMap(positionsToCatenary));
            mapIds(catenaryIdMap, catenaries);
        } catch (Exception e) {
            Init.MSD_LOGGER.log(Level.WARNING, "msd data sync fail", e);
        }
    }

    public static <T, U, V, W extends Map<T, X>, X extends Map<U, V>> V tryGet(W map, T key1, U key2, V defaultValue) {
        final V result = tryGet(map, key1, key2);
        return result == null ? defaultValue : result;
    }

    public static <T, U, V, W extends Map<T, X>, X extends Map<U, V>> V tryGet(W map, T key1, U key2) {
        final Map<U, V> innerMap = map.get(key1);
        if (innerMap == null) {
            return null;
        } else {
            return innerMap.get(key2);
        }
    }

    public static <T, U, V extends Map<T, W>, W extends Collection<U>> void put(V map, T key, U newValue, Supplier<W> innerSetSupplier) {
        final W innerSet = map.get(key);
        final W newInnerSet;
        if (innerSet == null) {
            newInnerSet = innerSetSupplier.get();
            map.put(key, newInnerSet);
        } else {
            newInnerSet = innerSet;
        }
        newInnerSet.add(newValue);
    }

    public static <T, U, V extends Map<T, W>, W extends Collection<U>, X extends Collection<U>> void put(V map, T key, X newValue, Supplier<W> innerSetSupplier) {
        final W innerSet = map.get(key);
        final W newInnerSet;
        if (innerSet == null) {
            newInnerSet = innerSetSupplier.get();
            map.put(key, newInnerSet);
        } else {
            newInnerSet = innerSet;
        }
        newInnerSet.addAll(newValue);
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
