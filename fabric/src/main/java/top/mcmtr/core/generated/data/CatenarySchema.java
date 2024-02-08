package top.mcmtr.core.generated.data;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.WriterBase;
import org.mtr.core.tool.EnumHelper;
import top.mcmtr.core.data.CatenaryType;
import top.mcmtr.core.data.OffsetPosition;
import top.mcmtr.core.data.TwoPositionsBase;

public abstract class CatenarySchema extends TwoPositionsBase {
    protected final Position positionStart;
    protected final Position positionEnd;
    protected final OffsetPosition offsetStart;
    protected final OffsetPosition offsetEnd;
    protected final CatenaryType catenaryType;
    private static final String KEY_POSITION_START = "positionStart";
    private static final String KEY_POSITION_END = "positionEnd";
    private static final String KEY_OFFSET_START = "offsetStart";
    private static final String KEY_OFFSET_END = "offsetEnd";
    private static final String KEY_CATENARY_TYPE = "catenaryType";

    protected CatenarySchema(final Position positionStart, final Position positionEnd, final OffsetPosition offsetStart, final OffsetPosition offsetEnd, final CatenaryType catenaryType) {
        this.positionStart = positionStart;
        this.positionEnd = positionEnd;
        this.offsetStart = offsetStart;
        this.offsetEnd = offsetEnd;
        this.catenaryType = catenaryType;
    }

    protected CatenarySchema(final ReaderBase readerBase) {
        positionStart = new Position(readerBase.getChild(KEY_POSITION_START));
        positionEnd = new Position(readerBase.getChild(KEY_POSITION_END));
        offsetStart = new OffsetPosition(readerBase.getChild(KEY_OFFSET_START));
        offsetEnd = new OffsetPosition(readerBase.getChild(KEY_OFFSET_END));
        catenaryType = EnumHelper.valueOf(CatenaryType.values()[0], readerBase.getString(KEY_CATENARY_TYPE, ""));
    }

    public void updateData(final ReaderBase readerBase) {
    }

    public void serializeData(final WriterBase writerBase) {
        if (positionStart != null) {
            positionStart.serializeData(writerBase.writeChild(KEY_POSITION_START));
        }
        if (positionEnd != null) {
            positionEnd.serializeData(writerBase.writeChild(KEY_POSITION_END));
        }
        if (offsetStart != null) {
            offsetStart.serializeData(writerBase.writeChild(KEY_OFFSET_START));
        }
        if (offsetEnd != null) {
            offsetEnd.serializeData(writerBase.writeChild(KEY_OFFSET_END));
        }
        writerBase.writeString(KEY_CATENARY_TYPE, catenaryType.toString());
    }
}