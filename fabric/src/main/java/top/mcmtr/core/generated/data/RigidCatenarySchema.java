package top.mcmtr.core.generated.data;

import org.mtr.core.data.Position;
import org.mtr.core.data.TwoPositionsBase;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.WriterBase;
import org.mtr.core.tool.Angle;
import org.mtr.core.tool.EnumHelper;
import top.mcmtr.core.data.RigidCatenary;

public abstract class RigidCatenarySchema extends TwoPositionsBase {
    protected final Position position1;
    protected final Angle angle1;
    protected final Position position2;
    protected final Angle angle2;
    protected final RigidCatenary.Shape shape;
    protected final double verticalRadius;
    private static final String KEY_POSITION_1 = "position_1";
    private static final String KEY_POSITION_2 = "position_2";
    private static final String KEY_ANGLE_1 = "angle_1";
    private static final String KEY_ANGLE_2 = "angle_2";
    private static final String KEY_SHAPE = "shape";
    private static final String KEY_VERTICAL_RADIUS = "vertical_radius";

    protected RigidCatenarySchema(Position position1, Angle angle1, Position position2, Angle angle2, RigidCatenary.Shape shape, double verticalRadius) {
        this.position1 = position1;
        this.angle1 = angle1;
        this.position2 = position2;
        this.angle2 = angle2;
        this.shape = shape;
        this.verticalRadius = verticalRadius;
    }

    protected RigidCatenarySchema(ReaderBase readerBase) {
        this.position1 = new Position(readerBase.getChild(KEY_POSITION_1));
        this.position2 = new Position(readerBase.getChild(KEY_POSITION_2));
        this.angle1 = EnumHelper.valueOf(Angle.values()[0], readerBase.getString(KEY_ANGLE_1, ""));
        this.angle2 = EnumHelper.valueOf(Angle.values()[0], readerBase.getString(KEY_ANGLE_2, ""));
        this.shape = EnumHelper.valueOf(RigidCatenary.Shape.values()[0], readerBase.getString(KEY_SHAPE, ""));
        this.verticalRadius = readerBase.getDouble(KEY_VERTICAL_RADIUS, 0);
    }

    @Override
    public void updateData(final ReaderBase readerBase) {
    }

    @Override
    public void serializeData(WriterBase writerBase) {
        if (position1 != null) {
            position1.serializeData(writerBase.writeChild(KEY_POSITION_1));
        }
        if (position2 != null) {
            position2.serializeData(writerBase.writeChild(KEY_POSITION_2));
        }
        writerBase.writeString(KEY_ANGLE_1, angle1.toString());
        writerBase.writeString(KEY_ANGLE_2, angle2.toString());
        writerBase.writeString(KEY_SHAPE, shape.toString());
        writerBase.writeDouble(KEY_VERTICAL_RADIUS, verticalRadius);
    }
}