package top.mcmtr.core.legacy.generated.data;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.serializer.WriterBase;

public abstract class CatenaryNodeConnectionSchema implements SerializedDataBase {
    protected long catenaryNodePos;
    protected int xStart;
    protected int yStart;
    protected int zStart;
    protected int xEnd;
    protected int yEnd;
    protected int zEnd;
    protected String catenaryType;
    private static final String KEY_NODE_POS = "catenary_node_pos";
    private static final String KEY_X_START = "c_x_start";
    private static final String KEY_Y_START = "c_y_start";
    private static final String KEY_Z_START = "c_z_start";
    private static final String KEY_X_END = "c_x_end";
    private static final String KEY_Y_END = "c_y_end";
    private static final String KEY_Z_END = "c_z_end";
    private static final String KEY_CATENARY_TYPE = "catenary_type";

    protected CatenaryNodeConnectionSchema() {
    }

    protected CatenaryNodeConnectionSchema(ReaderBase readerBase) {
    }

    @Override
    public void updateData(ReaderBase readerBase) {
        readerBase.unpackLong(KEY_NODE_POS, value -> catenaryNodePos = value);
        readerBase.unpackInt(KEY_X_START, value -> xStart = value);
        readerBase.unpackInt(KEY_Y_START, value -> yStart = value);
        readerBase.unpackInt(KEY_Z_START, value -> zStart = value);
        readerBase.unpackInt(KEY_X_END, value -> xEnd = value);
        readerBase.unpackInt(KEY_Y_END, value -> yEnd = value);
        readerBase.unpackInt(KEY_Z_END, value -> zEnd = value);
        readerBase.unpackString(KEY_CATENARY_TYPE, value -> catenaryType = value);
    }

    @Override
    public void serializeData(WriterBase writerBase) {
        writerBase.writeLong(KEY_NODE_POS, catenaryNodePos);
        writerBase.writeInt(KEY_X_START, xStart);
        writerBase.writeInt(KEY_Y_START, yStart);
        writerBase.writeInt(KEY_Z_START, zStart);
        writerBase.writeInt(KEY_X_END, xEnd);
        writerBase.writeInt(KEY_Y_END, yEnd);
        writerBase.writeInt(KEY_Z_END, zEnd);
        writerBase.writeString(KEY_CATENARY_TYPE, catenaryType);
    }
}