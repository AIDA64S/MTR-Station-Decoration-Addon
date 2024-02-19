package top.mcmtr.core.generated.operation;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.serializer.WriterBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

public abstract class MSDDeleteDataResponseSchema implements SerializedDataBase {
    protected final ObjectArrayList<String> catenaryIds = new ObjectArrayList<>();
    protected final ObjectArrayList<Position> catenaryNodePositions = new ObjectArrayList<>();
    private static final String KEY_CATENARY_IDS = "catenary_ids";
    private static final String KEY_CATENARY_NODE_POSITIONS = "catenary_node_positions";

    protected MSDDeleteDataResponseSchema() {
    }

    protected MSDDeleteDataResponseSchema(ReaderBase readerBase) {
    }

    @Override
    public void updateData(ReaderBase readerBase) {
        readerBase.iterateStringArray(KEY_CATENARY_IDS, catenaryIds::clear, catenaryIds::add);
        readerBase.iterateReaderArray(KEY_CATENARY_NODE_POSITIONS, catenaryNodePositions::clear, readerBaseChild -> catenaryNodePositions.add(new Position(readerBaseChild)));
    }

    @Override
    public void serializeData(WriterBase writerBase) {
        serializeCatenaryIds(writerBase);
        serializeCatenaryNodePositions(writerBase);
    }

    protected void serializeCatenaryIds(WriterBase writerBase) {
        final WriterBase.Array catenaryIdsWriterBaseArray = writerBase.writeArray(KEY_CATENARY_IDS);
        catenaryIds.forEach(catenaryIdsWriterBaseArray::writeString);
    }

    protected void serializeCatenaryNodePositions(WriterBase writerBase) {
        writerBase.writeDataset(catenaryNodePositions, KEY_CATENARY_NODE_POSITIONS);
    }
}