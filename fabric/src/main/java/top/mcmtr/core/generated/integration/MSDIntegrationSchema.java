package top.mcmtr.core.generated.integration;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.serializer.WriterBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import top.mcmtr.core.data.Catenary;

public class MSDIntegrationSchema implements SerializedDataBase {
    protected final ObjectArrayList<Catenary> catenaries = new ObjectArrayList<>();

    protected final ObjectArrayList<Position> catenaryNodePositions = new ObjectArrayList<>();
    private static final String KEY_CATENARIES = "catenaries";
    private static final String KEY_CATENARY_NODE_POSITIONS = "catenaryNodePositions";

    protected MSDIntegrationSchema() {
    }

    protected MSDIntegrationSchema(final ReaderBase readerBase) {
    }

    @Override
    public void updateData(ReaderBase readerBase) {
        readerBase.iterateReaderArray(KEY_CATENARIES, catenaries::clear, readerBaseChild -> catenaries.add(new Catenary(readerBaseChild)));
        readerBase.iterateReaderArray(KEY_CATENARY_NODE_POSITIONS, catenaryNodePositions::clear, readerBaseChild -> catenaryNodePositions.add(new Position(readerBaseChild)));
    }

    @Override
    public void serializeData(WriterBase writerBase) {
        serializeCatenaries(writerBase);
        serializeCatenaryNodePositions(writerBase);
    }

    protected void serializeCatenaries(final WriterBase writerBase) {
        writerBase.writeDataset(catenaries, KEY_CATENARIES);
    }

    protected void serializeCatenaryNodePositions(final WriterBase writerBase) {
        writerBase.writeDataset(catenaryNodePositions, KEY_CATENARY_NODE_POSITIONS);
    }
}