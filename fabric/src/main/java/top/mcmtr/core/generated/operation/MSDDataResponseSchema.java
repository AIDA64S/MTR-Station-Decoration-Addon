package top.mcmtr.core.generated.operation;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.serializer.WriterBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.data.RigidCatenary;

public abstract class MSDDataResponseSchema implements SerializedDataBase {
    protected final ObjectArrayList<Catenary> catenaries = new ObjectArrayList<>();
    protected final ObjectArrayList<RigidCatenary> rigidCatenaries = new ObjectArrayList<>();
    protected final ObjectArrayList<String> catenariesToKeep = new ObjectArrayList<>();
    protected final ObjectArrayList<String> rigidCatenariesToKeep = new ObjectArrayList<>();
    private static final String KEY_CATENARIES = "catenaries";
    private static final String KEY_RIGID_CATENARIES = "rigid_catenaries";
    private static final String KEY_CATENARIES_TO_KEEP = "catenaries_to_keep";
    private static final String KEY_RIGID_CATENARIES_TO_KEEP = "rigid_catenaries_to_keep";

    protected MSDDataResponseSchema() {
    }

    protected MSDDataResponseSchema(ReaderBase readerBase) {
    }

    @Override
    public void updateData(ReaderBase readerBase) {
        readerBase.iterateReaderArray(KEY_CATENARIES, catenaries::clear, readerBaseChild -> catenaries.add(new Catenary(readerBaseChild)));
        readerBase.iterateReaderArray(KEY_RIGID_CATENARIES, rigidCatenaries::clear, readerBaseChild -> rigidCatenaries.add(new RigidCatenary(readerBaseChild)));
        readerBase.iterateStringArray(KEY_CATENARIES_TO_KEEP, catenariesToKeep::clear, catenariesToKeep::add);
        readerBase.iterateStringArray(KEY_RIGID_CATENARIES_TO_KEEP, rigidCatenariesToKeep::clear, rigidCatenariesToKeep::add);
    }

    @Override
    public void serializeData(WriterBase writerBase) {
        serializeCatenaries(writerBase);
        serializeRigidCatenaries(writerBase);
        serializeCatenariesToKeep(writerBase);
        serializeRigidCatenariesToKeep(writerBase);
    }

    protected void serializeCatenaries(WriterBase writerBase) {
        writerBase.writeDataset(catenaries, KEY_CATENARIES);
    }

    protected void serializeRigidCatenaries(WriterBase writerBase) {
        writerBase.writeDataset(rigidCatenaries, KEY_RIGID_CATENARIES);
    }

    protected void serializeCatenariesToKeep(final WriterBase writerBase) {
        final WriterBase.Array catenariesToKeepWriterBaseArray = writerBase.writeArray(KEY_CATENARIES_TO_KEEP);
        catenariesToKeep.forEach(catenariesToKeepWriterBaseArray::writeString);
    }

    protected void serializeRigidCatenariesToKeep(final WriterBase writerBase) {
        final WriterBase.Array rigidCatenariesToKeepWriterBaseArray = writerBase.writeArray(KEY_RIGID_CATENARIES_TO_KEEP);
        rigidCatenariesToKeep.forEach(rigidCatenariesToKeepWriterBaseArray::writeString);
    }
}