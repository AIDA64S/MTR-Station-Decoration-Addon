package top.mcmtr.core.generated.integration;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.serializer.WriterBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import top.mcmtr.core.data.Catenary;

public abstract class IntegrationSchema implements SerializedDataBase {
    public static final String KEY_CATENARIES = "catenaries";
    public static final String KEY_CATENARY_NODE = "catenary_code_positions";
    protected final ObjectArrayList<Catenary> catenaries = new ObjectArrayList<>();
    protected final ObjectArrayList<Position> catenaryNodePositions = new ObjectArrayList<>();

    protected IntegrationSchema() {
    }

    protected IntegrationSchema(ReaderBase readerBase) {
    }

    @Override
    public void updateData(ReaderBase readerBase) {
        readerBase.iterateReaderArray(KEY_CATENARIES, this.catenaries::clear, catenary -> this.catenaries.add(new Catenary(catenary)));
        readerBase.iterateReaderArray(KEY_CATENARY_NODE, this.catenaryNodePositions::clear, position -> this.catenaryNodePositions.add(new Position(position)));
    }

    @Override
    public void serializeData(final WriterBase writerBase) {
        this.serializeCatenaries(writerBase);
        this.serializeCatenaryNodePositions(writerBase);
    }

    protected void serializeCatenaries(final WriterBase writerBase) {
        writerBase.writeDataset(this.catenaries, KEY_CATENARIES);
    }

    protected void serializeCatenaryNodePositions(final WriterBase writerBase) {
        writerBase.writeDataset(this.catenaryNodePositions, KEY_CATENARY_NODE);
    }

    @Override
    public String toString() {
        return "IntegrationSchema{" +
                "catenaries=" + catenaries +
                ", catenaryNodePositions=" + catenaryNodePositions +
                '}';
    }
}