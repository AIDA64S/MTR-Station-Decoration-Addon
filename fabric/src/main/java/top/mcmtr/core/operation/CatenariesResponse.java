package top.mcmtr.core.operation;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.generated.operation.CatenariesResponseSchema;

public final class CatenariesResponse extends CatenariesResponseSchema {
    public CatenariesResponse() {
        super();
    }

    public CatenariesResponse(ReaderBase readerBase) {
        super(readerBase);
        updateData(readerBase);
    }

    public ObjectImmutableList<Catenary> getCatenaries() {
        return new ObjectImmutableList<>(catenaries);
    }

    void add(Catenary catenary) {
        catenaries.add(catenary);
    }
}