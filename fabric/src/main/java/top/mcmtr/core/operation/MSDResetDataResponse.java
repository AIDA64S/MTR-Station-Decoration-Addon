package top.mcmtr.core.operation;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.data.MSDData;
import top.mcmtr.core.generated.operation.MSDResetDataResponseSchema;

import javax.annotation.Nullable;

public final class MSDResetDataResponse extends MSDResetDataResponseSchema {
    private final MSDData data;

    public MSDResetDataResponse(MSDData data) {
        this.data = data;
    }

    public MSDResetDataResponse(ReaderBase readerBase, MSDData data) {
        super(readerBase);
        this.data = data;
        updateData(readerBase);
    }

    public void write() {
        catenaries.forEach(catenary -> update(catenary, data.catenaries, data.catenaryIdMap.get(catenary.getHexId())));
        data.sync();
    }

    ObjectArrayList<Catenary> getCatenaries() {
        return catenaries;
    }

    private static <T extends SerializedDataBase> void update(T newData, ObjectSet<T> dataSet, @Nullable T existingData) {
        if (existingData != null) {
            dataSet.remove(existingData);
        }
        dataSet.add(newData);
    }
}