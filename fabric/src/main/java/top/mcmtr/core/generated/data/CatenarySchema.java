package top.mcmtr.core.generated.data;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.WriterBase;
import org.mtr.core.tool.EnumHelper;
import top.mcmtr.core.data.CatenaryType;
import top.mcmtr.core.data.OffsetPosition;
import top.mcmtr.core.data.TwoPositionsBase;

public abstract class CatenarySchema extends TwoPositionsBase {
    protected final Position position1;
    protected final Position position2;
    protected final OffsetPosition offsetPosition1;
    protected final OffsetPosition offsetPosition2;
    public final CatenaryType catenaryType;
    public static final String KEY_POSITION_1 = "catenary_position_1";
    public static final String KEY_POSITION_2 = "catenary_position_2";
    public static final String KEY_OFFSET_1 = "catenary_offset_1";
    public static final String KEY_OFFSET_2 = "catenary_offset_2";
    public static final String KEY_CATENARY_TYPE = "catenary_type";

    public CatenarySchema(Position position1, Position position2, OffsetPosition offsetPosition1, OffsetPosition offsetPosition2, CatenaryType catenaryType) {
        this.position1 = position1;
        this.position2 = position2;
        this.offsetPosition1 = offsetPosition1;
        this.offsetPosition2 = offsetPosition2;
        this.catenaryType = catenaryType;
    }

    public CatenarySchema(ReaderBase readerBase) {
        this.position1 = new Position(readerBase.getChild(KEY_POSITION_1));
        this.position2 = new Position(readerBase.getChild(KEY_POSITION_2));
        this.offsetPosition1 = new OffsetPosition(readerBase.getChild(KEY_OFFSET_1));
        this.offsetPosition2 = new OffsetPosition(readerBase.getChild(KEY_OFFSET_2));
        this.catenaryType = EnumHelper.valueOf(CatenaryType.CATENARY, readerBase.getString(KEY_CATENARY_TYPE, ""));
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
        writerBase.writeString(KEY_CATENARY_TYPE, catenaryType.toString());
    }

    @Override
    public String toString() {
        return "CatenarySchema{" +
                "position1=" + position1 +
                ", position2=" + position2 +
                ", offsetPosition1=" + offsetPosition1 +
                ", offsetPosition2=" + offsetPosition2 +
                ", catenaryType=" + catenaryType +
                '}';
    }
}