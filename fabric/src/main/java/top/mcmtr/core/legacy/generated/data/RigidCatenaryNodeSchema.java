package top.mcmtr.core.legacy.generated.data;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBaseWithId;
import org.mtr.core.serializer.WriterBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import top.mcmtr.core.legacy.data.RigidCatenaryNodeConnection;

public abstract class RigidCatenaryNodeSchema implements SerializedDataBaseWithId {
    protected long rigidCatenaryNodePos;
    protected ObjectArrayList<RigidCatenaryNodeConnection> rigidCatenaryNodeConnections = new ObjectArrayList<>();
    private static final String KEY_NODE_POS = "rigid_catenary_node_pos";
    private static final String KEY_CATENARY_CONNECTIONS = "rigid_catenary_connections";

    public RigidCatenaryNodeSchema() {
    }

    public RigidCatenaryNodeSchema(ReaderBase readerBase) {
    }

    @Override
    public void updateData(ReaderBase readerBase) {
        readerBase.unpackLong(KEY_NODE_POS, value -> rigidCatenaryNodePos = value);
        readerBase.iterateReaderArray(KEY_CATENARY_CONNECTIONS, rigidCatenaryNodeConnections::clear, readerBaseChild -> rigidCatenaryNodeConnections.add(new RigidCatenaryNodeConnection(readerBaseChild)));
    }

    @Override
    public void serializeData(WriterBase writerBase) {
        writerBase.writeLong(KEY_NODE_POS, rigidCatenaryNodePos);
        writerBase.writeDataset(rigidCatenaryNodeConnections, KEY_CATENARY_CONNECTIONS);
    }
}