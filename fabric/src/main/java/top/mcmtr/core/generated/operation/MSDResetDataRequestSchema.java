package top.mcmtr.core.generated.operation;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.serializer.WriterBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import top.mcmtr.core.data.OffsetPosition;

public abstract class MSDResetDataRequestSchema implements SerializedDataBase {
    /**
     * 要进行操作的接触网节点，一般情况下容量为 1
     */
    protected final ObjectArrayList<Position> catenaryNodePositions = new ObjectArrayList<>();
    /**
     * 要进行重制的接触网节点的新偏移值，一般情况下容量为 1
     */
    protected final ObjectArrayList<OffsetPosition> offsetPositions = new ObjectArrayList<>();
    private static final String KEY_CATENARY_NODE_POSITIONS = "catenary_node_positions";
    private static final String KEY_OFFSET_POSITIONS = "offset_positions";

    public MSDResetDataRequestSchema() {
    }

    public MSDResetDataRequestSchema(ReaderBase readerBase) {
    }

    @Override
    public void updateData(ReaderBase readerBase) {
        readerBase.iterateReaderArray(KEY_CATENARY_NODE_POSITIONS, catenaryNodePositions::clear, readerBaseChild -> catenaryNodePositions.add(new Position(readerBaseChild)));
        readerBase.iterateReaderArray(KEY_OFFSET_POSITIONS, offsetPositions::clear, readerBaseChild -> offsetPositions.add(new OffsetPosition(readerBaseChild)));
    }

    @Override
    public void serializeData(WriterBase writerBase) {
        serializeCatenaryNodePositions(writerBase);
        serializeOffsetPositions(writerBase);
    }

    protected void serializeCatenaryNodePositions(WriterBase writerBase) {
        writerBase.writeDataset(catenaryNodePositions, KEY_CATENARY_NODE_POSITIONS);
    }

    protected void serializeOffsetPositions(WriterBase writerBase) {
        writerBase.writeDataset(offsetPositions, KEY_OFFSET_POSITIONS);
    }
}