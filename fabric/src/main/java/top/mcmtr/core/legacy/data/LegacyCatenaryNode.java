package top.mcmtr.core.legacy.data;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.legacy.data.DataFixer;
import top.mcmtr.core.legacy.generated.data.CatenaryNodeSchema;

import java.util.function.Consumer;

public final class LegacyCatenaryNode extends CatenaryNodeSchema {
    public LegacyCatenaryNode(ReaderBase readerBase) {
        super(readerBase);
        updateData(readerBase);
    }

    @Override
    public String getHexId() {
        return Utilities.numberToPaddedHexString(catenaryNodePos);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public Position getStartPosition() {
        return DataFixer.fromLong(catenaryNodePos);
    }

    public long getStartPositionLong() {
        return catenaryNodePos;
    }

    public void iterateConnections(Consumer<CatenaryNodeConnection> consumer) {
        catenaryConnections.forEach(consumer);
    }
}