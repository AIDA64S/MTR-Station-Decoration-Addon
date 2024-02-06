package top.mcmtr.core.generated.data;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.WriterBase;
import top.mcmtr.core.data.TwoPositionsBase;
import top.mcmtr.mod.data.OffsetPosition;

public abstract class CatenarySchema extends TwoPositionsBase {
    protected final Position position1;
    protected final Position position2;
    protected final OffsetPosition offsetPosition1;
    protected final OffsetPosition offsetPosition2;
    public static final String KEY_POSITION_1 = "catenary_position_1";
    public static final String KEY_POSITION_2 = "catenary_position_2";
    public static final String KEY_OFFSET_1 = "catenary_offset_1";
    public static final String KEY_OFFSET_2 = "catenary_offset_2";

    public CatenarySchema(Position position1, Position position2, OffsetPosition offsetPosition1, OffsetPosition offsetPosition2) {
        this.position1 = position1;
        this.position2 = position2;
        this.offsetPosition1 = offsetPosition1;
        this.offsetPosition2 = offsetPosition2;
    }

    public CatenarySchema(ReaderBase readerBase) {
        this.position1 = new Position(readerBase.getChild(KEY_POSITION_1));
        this.position2 = new Position(readerBase.getChild(KEY_POSITION_2));
        this.offsetPosition1 = new OffsetPosition(readerBase.getChild(KEY_OFFSET_1));
        this.offsetPosition2 = new OffsetPosition(readerBase.getChild(KEY_OFFSET_2));
    }

    @Override
    public void updateData(ReaderBase readerBase) {
    }

    @Override
    public void serializeData(WriterBase writerBase) {
        if (this.position1 != null) {
            this.position1.serializeData(writerBase.writeChild(KEY_POSITION_1));
        }
        if (this.position2 != null) {
            this.position2.serializeData(writerBase.writeChild(KEY_POSITION_2));
        }
        if (this.offsetPosition1 != null) {
            this.offsetPosition1.serializeData(writerBase.writeChild(KEY_OFFSET_1));
        }
        if (this.offsetPosition2 != null) {
            this.offsetPosition2.serializeData(writerBase.writeChild(KEY_OFFSET_2));
        }
    }

    @Override
    public String toString() {
        return "CatenarySchema{position1=" + position1 +
                ", position2=" + position2 +
                ", offsetPosition1=" + offsetPosition1 +
                ", offsetPosition2=" + offsetPosition2 +
                "}";
    }
}