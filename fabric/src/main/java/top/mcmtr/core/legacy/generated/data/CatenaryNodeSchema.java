package top.mcmtr.core.legacy.generated.data;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBaseWithId;
import org.mtr.core.serializer.WriterBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import top.mcmtr.core.legacy.data.CatenaryNodeConnection;

public abstract class CatenaryNodeSchema implements SerializedDataBaseWithId {
    protected long catenaryNodePos;
    protected final ObjectArrayList<CatenaryNodeConnection> catenaryConnections = new ObjectArrayList<>();
    private static final String KEY_NODE_POS = "catenary_node_pos";
    private static final String KEY_CATENARY_CONNECTIONS = "catenary_connections";

    protected CatenaryNodeSchema() {
    }

    protected CatenaryNodeSchema(ReaderBase readerBase) {
    }

    @Override
    public void updateData(ReaderBase readerBase) {
        readerBase.unpackLong(KEY_NODE_POS, value -> catenaryNodePos = value);
        readerBase.iterateReaderArray(KEY_CATENARY_CONNECTIONS, catenaryConnections::clear, readerBaseChild -> catenaryConnections.add(new CatenaryNodeConnection(readerBaseChild)));
    }

    @Override
    public void serializeData(WriterBase writerBase) {
        writerBase.writeLong(KEY_NODE_POS, catenaryNodePos);
        writerBase.writeDataset(catenaryConnections, KEY_CATENARY_CONNECTIONS);
    }
}