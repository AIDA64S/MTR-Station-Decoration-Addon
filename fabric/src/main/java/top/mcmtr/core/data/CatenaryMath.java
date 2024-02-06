package top.mcmtr.core.data;

import org.mtr.core.data.Position;
import top.mcmtr.mod.data.OffsetPosition;
import top.mcmtr.mod.data.VectorLocation;

import java.util.ArrayList;
import java.util.List;

public class CatenaryMath {
    private final double xStart;
    private final double yStart;
    private final double zStart;
    private final double xEnd;
    private final double yEnd;
    private final double zEnd;
    private final double count;
    private final double increment2;
    private final double sinX;
    private final double sinZ;
    private final List<VectorLocation> corner = new ArrayList<>();
    public static final double CATENARY_WIDTH = 0.015625;

    public CatenaryMath(Position position1, Position position2, OffsetPosition offset1, OffsetPosition offset2) {
        this.xStart = position1.getX() + offset1.x;
        this.yStart = position1.getY() + offset1.y;
        this.zStart = position1.getZ() + offset1.z;
        this.xEnd = position2.getX() + offset2.x;
        this.yEnd = position2.getY() + offset2.y;
        this.zEnd = position2.getZ() + offset2.z;
        this.count = getLength();
        double increment = count / Math.round(count / 2);
        this.increment2 = increment - 0.5;
        this.sinX = getSin(zStart, zEnd, count);
        this.sinZ = getSin(xStart, xEnd, count);
        for (double i = 0; i < count - 0.1; i += increment) {
            final VectorLocation cornerTemp = new VectorLocation(getPositionXYZ(i, xStart, xEnd) + 0.5, getPositionXYZ(i, yStart, yEnd), getPositionXYZ(i, zStart, zEnd) + 0.5,
                    getPositionXYZ(i + increment, xStart, xEnd) + 0.5, getPositionXYZ(i + increment, yStart, yEnd), getPositionXYZ(i + increment, zStart, zEnd) + 0.5, i);
            corner.add(cornerTemp);
        }
    }

    public void render(RenderCatenary callback) {
        double base = 0.6;
        if (count < 8) {
            for (VectorLocation cornerResult : corner) {
                callback.renderCatenary(cornerResult.x1, cornerResult.y1, cornerResult.z1, cornerResult.x2, cornerResult.y2, cornerResult.z2, count, cornerResult.i, base, sinX, sinZ, increment2);
            }
        } else {
            for (VectorLocation cornerResult : corner) {
                callback.renderCatenary(cornerResult.x1, cornerResult.y1, cornerResult.z1, cornerResult.x2, cornerResult.y2, cornerResult.z2, count, cornerResult.i, base, sinX, sinZ, increment2);
                if (cornerResult.i < (count / 2 - increment2)) {
                    base *= 0.5;
                } else if (cornerResult.i >= (count / 2)) {
                    base /= 0.5;
                }
            }
        }
    }

    private double getPositionXYZ(double value, double start, double end) {
        final double intercept = count / 2;
        final double change;
        final double initial;
        final double offsetValue;
        if (value < intercept) {
            change = (end - start) / 2D;
            initial = start;
            offsetValue = value;
        } else {
            change = (start - end) / 2D;
            initial = end;
            offsetValue = count - value;
        }
        return change / intercept * offsetValue + initial;
    }

    private double getLength() {
        return Math.sqrt(((xEnd - xStart) * (xEnd - xStart)) + ((yEnd - yStart) * (yEnd - yStart)) + ((zEnd - zStart) * (zEnd - zStart)));
    }

    public boolean verify() {
        return getLength() > 0;
    }

    private double getSin(double start, double end, double length) {
        return ((end - start) / length) * CATENARY_WIDTH;
    }

    @FunctionalInterface
    public interface RenderCatenary {
        void renderCatenary(double x1, double y1, double z1, double x2, double y2, double z2, double count, double i, double base, double sinX, double sinY, double increment);
    }
}