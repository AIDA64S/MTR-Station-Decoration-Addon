package top.mcmtr.core.generated.operation;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.serializer.WriterBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

public abstract class CatenariesRequestSchema implements SerializedDataBase {
    protected final ObjectArrayList<String> catenaryIds = new ObjectArrayList<>();
    private static final String KEY_CATENARY_IDS = "catenary_ids";

    protected CatenariesRequestSchema() {
    }

    protected CatenariesRequestSchema(final ReaderBase readerBase) {
    }

    public void updateData(ReaderBase readerBase) {
        readerBase.iterateStringArray(KEY_CATENARY_IDS, catenaryIds::clear, catenaryIds::add);
    }

    public void serializeData(WriterBase writerBase) {
        serializeCatenaryIds(writerBase);
    }

    protected void serializeCatenaryIds(WriterBase writerBase) {
        final WriterBase.Array catenaryIdsWriterBaseArray = writerBase.writeArray(KEY_CATENARY_IDS);
        catenaryIds.forEach(catenaryIdsWriterBaseArray::writeString);
    }
}