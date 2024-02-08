package top.mcmtr.core.data;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.serializer.WriterBase;

public class OffsetPosition implements SerializedDataBase {
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

    public OffsetPosition(ReaderBase readerBase) {
        this.x = readerBase.getDouble(KEY_X, 0);
        this.y = readerBase.getDouble(KEY_Y, 0);
        this.z = readerBase.getDouble(KEY_Z, 0);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public void updateData(ReaderBase readerBase) {
    }

    @Override
    public void serializeData(WriterBase writerBase) {
        writerBase.writeDouble(KEY_X, this.x);
        writerBase.writeDouble(KEY_Y, this.y);
        writerBase.writeDouble(KEY_Z, this.z);
    }

    @Override
    public String toString() {
        return "OffsetPosition{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}