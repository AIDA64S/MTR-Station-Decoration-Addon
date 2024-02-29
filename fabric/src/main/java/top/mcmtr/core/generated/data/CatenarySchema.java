package top.mcmtr.core.generated.data;

import org.mtr.core.data.Position;
import org.mtr.core.data.TwoPositionsBase;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.WriterBase;
import org.mtr.core.tool.EnumHelper;
import top.mcmtr.core.data.CatenaryType;
import top.mcmtr.core.data.OffsetPosition;

public abstract class CatenarySchema extends TwoPositionsBase {
    protected final Position positionStart;
    protected final Position positionEnd;
    protected final OffsetPosition offsetPositionStart;
    protected final OffsetPosition offsetPositionEnd;
    protected final CatenaryType catenaryType;
    private static final String KEY_POSITION_START = "position_start";
    private static final String KEY_POSITION_END = "position_end";
    private static final String KEY_OFFSET_POSITION_START = "offset_position_start";
    private static final String KEY_OFFSET_POSITION_END = "offset_position_end";
    private static final String KEY_CATENARY_TYPE = "catenary_type";

    public CatenarySchema(Position positionStart, Position positionEnd, OffsetPosition offsetPositionStart, OffsetPosition offsetPositionEnd, CatenaryType catenaryType) {
        this.positionStart = positionStart;
        this.positionEnd = positionEnd;
        this.offsetPositionStart = offsetPositionStart;
        this.offsetPositionEnd = offsetPositionEnd;
        this.catenaryType = catenaryType;
    }

    public CatenarySchema(ReaderBase readerBase) {
        this.positionStart = new Position(readerBase.getChild(KEY_POSITION_START));
        this.positionEnd = new Position(readerBase.getChild(KEY_POSITION_END));
        this.offsetPositionStart = new OffsetPosition(readerBase.getChild(KEY_OFFSET_POSITION_START));
        this.offsetPositionEnd = new OffsetPosition(readerBase.getChild(KEY_OFFSET_POSITION_END));
        this.catenaryType = EnumHelper.valueOf(CatenaryType.CATENARY, readerBase.getString(KEY_CATENARY_TYPE, ""));
    }

    @Override
    public void updateData(ReaderBase readerBase) {
    }

    @Override
    public void serializeData(WriterBase writerBase) {
        if (positionStart != null) {
            positionStart.serializeData(writerBase.writeChild(KEY_POSITION_START));
        }
        if (positionEnd != null) {
            positionEnd.serializeData(writerBase.writeChild(KEY_POSITION_END));
        }
        if (offsetPositionStart != null) {
            offsetPositionStart.serializeData(writerBase.writeChild(KEY_OFFSET_POSITION_START));
        }
        if (offsetPositionEnd != null) {
            offsetPositionEnd.serializeData(writerBase.writeChild(KEY_OFFSET_POSITION_END));
        }
        writerBase.writeString(KEY_CATENARY_TYPE, catenaryType.toString());
    }
}