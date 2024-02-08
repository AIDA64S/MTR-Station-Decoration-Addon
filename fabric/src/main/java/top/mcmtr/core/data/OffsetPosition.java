package top.mcmtr.core.data;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.serializer.WriterBase;

public final class OffsetPosition implements SerializedDataBase {
    private double x;
    private double y;
    private double z;
    public static final String KEY_X = "x";
    public static final String KEY_Y = "y";
    public static final String KEY_Z = "z";

    public OffsetPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public OffsetPosition(final ReaderBase readerBase) {
        this.x = readerBase.getDouble(KEY_X, 0);
        this.y = readerBase.getDouble(KEY_Y, 0);
        this.z = readerBase.getDouble(KEY_Z, 0);
        updateData(readerBase);
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public void updateData(final ReaderBase readerBase) {
    }

    public void serializeData(final WriterBase writerBase) {
        writerBase.writeDouble(KEY_X, this.x);
        writerBase.writeDouble(KEY_Y, this.y);
        writerBase.writeDouble(KEY_Z, this.z);
    }
}