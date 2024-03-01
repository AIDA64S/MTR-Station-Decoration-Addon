package top.mcmtr.core.legacy.data;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.legacy.data.DataFixer;
import top.mcmtr.core.legacy.generated.data.RigidCatenaryNodeSchema;

import java.util.function.Consumer;

public final class LegacyRigidCatenaryNode extends RigidCatenaryNodeSchema {
    public LegacyRigidCatenaryNode(ReaderBase readerBase) {
        super(readerBase);
        updateData(readerBase);
    }

    @Override
    public String getHexId() {
        return Utilities.numberToPaddedHexString(rigidCatenaryNodePos);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public Position getStartPosition() {
        return DataFixer.fromLong(rigidCatenaryNodePos);
    }

    public long getStartPositionLong() {
        return rigidCatenaryNodePos;
    }

    public void iterateConnections(Consumer<RigidCatenaryNodeConnection> consumer) {
        rigidCatenaryNodeConnections.forEach(consumer);
    }
}