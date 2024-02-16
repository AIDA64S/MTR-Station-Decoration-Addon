package top.mcmtr.core.data;

import org.mtr.core.data.Position;

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
    private final CatenaryType catenaryType;
    private final List<VectorLocation> corner = new ArrayList<>();
    public static final double CATENARY_WIDTH = 0.015625;
    private static final int ELECTRIC_CURVATURE_SCALE = 300;
    private static final int MAX_ELECTRIC_DIP = 8;

    public CatenaryMath(Position positionStart, Position positionEnd, OffsetPosition offsetStart, OffsetPosition offsetEnd, CatenaryType catenaryType) {
        this.xStart = positionStart.getX() + offsetStart.getX();
        this.yStart = positionStart.getY() + offsetStart.getY();
        this.zStart = positionStart.getZ() + offsetStart.getZ();
        this.xEnd = positionEnd.getX() + offsetEnd.getX();
        this.yEnd = positionEnd.getY() + offsetEnd.getY();
        this.zEnd = positionEnd.getZ() + offsetEnd.getZ();
        this.catenaryType = catenaryType;
        this.count = getLength();
        double increment = count / Math.round(count / 2);
        this.increment2 = increment - 0.5;
        this.sinX = getSin(zStart, zEnd, count);
        this.sinZ = getSin(xStart, xEnd, count);
        for (double i = 0; i < count - 0.1; i += increment) {
            final VectorLocation cornerTemp = new VectorLocation(getPositionXZ(i, xStart, xEnd) + 0.5, getPositionY(i, yStart, yEnd), getPositionXZ(i, zStart, zEnd) + 0.5,
                    getPositionXZ(i + increment, xStart, xEnd) + 0.5, getPositionY(i + increment, yStart, yEnd), getPositionXZ(i + increment, zStart, zEnd) + 0.5, i);
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

    private double getPositionXZ(double value, double start, double end) {
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

    private double getPositionY(double value, double start, double end) {
        if (catenaryType == CatenaryType.ELECTRIC) {
            if (value < 0.5) {
                return start;
            } else if (value > count - 0.5) {
                return end;
            }
            final double offsetValue = value - 0.5;
            final double offsetLength = count - 1;
            final double posY = start + (end - start) * offsetValue / offsetLength;
            final double dip = offsetLength * offsetLength / 4 / ELECTRIC_CURVATURE_SCALE;
            return posY + (dip > MAX_ELECTRIC_DIP ? MAX_ELECTRIC_DIP / dip : 1) * (offsetValue - offsetLength) * offsetValue / ELECTRIC_CURVATURE_SCALE;
        } else {
            return getPositionXZ(value, start, end);
        }
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
        void renderCatenary(double x1, double y1, double z1, double x2, double y2, double z2, double count, double i, double base, double sinX, double sinZ, double increment);
    }
}