package top.mcmtr.core.generated.operation;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.serializer.WriterBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.data.RigidCatenary;

public abstract class MSDUpdateDataResponseSchema implements SerializedDataBase {
    protected final ObjectArrayList<Catenary> catenaries = new ObjectArrayList<>();
    private static final String KEY_CATENARIES = "catenaries";
    protected final ObjectArrayList<RigidCatenary> rigidCatenaries = new ObjectArrayList<>();
    private static final String KEY_RIGID_CATENARIES = "rigid_catenaries";

    protected MSDUpdateDataResponseSchema() {
    }

    protected MSDUpdateDataResponseSchema(ReaderBase readerBase) {
    }

    @Override
    public void updateData(ReaderBase readerBase) {
        readerBase.iterateReaderArray(KEY_CATENARIES, catenaries::clear, readerBaseChild -> catenaries.add(new Catenary(readerBaseChild)));
        readerBase.iterateReaderArray(KEY_RIGID_CATENARIES, rigidCatenaries::clear, readerBaseChild -> rigidCatenaries.add(new RigidCatenary(readerBaseChild)));
    }

    @Override
    public void serializeData(WriterBase writerBase) {
        serializeCatenaries(writerBase);
        serializeRigidCatenaries(writerBase);
    }

    protected void serializeCatenaries(WriterBase writerBase) {
        writerBase.writeDataset(catenaries, KEY_CATENARIES);
    }

    protected void serializeRigidCatenaries(WriterBase writerBase) {
        writerBase.writeDataset(rigidCatenaries, KEY_RIGID_CATENARIES);
    }
}