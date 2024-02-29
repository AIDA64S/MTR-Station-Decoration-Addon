package top.mcmtr.core.data;

import org.mtr.core.data.Position;
import org.mtr.core.tool.Angle;
import org.mtr.core.tool.Vector;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

public final class RigidCatenaryMath {
    private final RigidCatenary.Shape shape;
    private final double verticalRadius;
    private final double h1;
    private final double k1;
    private final double h2;
    private final double k2;
    private final double r1;
    private final double r2;
    private final double tStart1;
    private final double tEnd1;
    private final double tStart2;
    private final double tEnd2;
    private final long yStart;
    private final long yEnd;
    private final boolean reverseT1;
    private final boolean reverseT2;
    private final boolean isStraight1;
    private final boolean isStraight2;
    private boolean checkRun = false;
    private final ObjectArrayList<SolidVectorLocation> corners1 = new ObjectArrayList<>();
    private final ObjectArrayList<SolidVectorLocation> corners2 = new ObjectArrayList<>();
    private static final double ACCEPT_THRESHOLD = 1E-4;
    private static final int CABLE_CURVATURE_SCALE = 1000;
    private static final int MAX_CABLE_DIP = 8;

    public RigidCatenaryMath(Position position1, Angle angle1, Position position2, Angle angle2, RigidCatenary.Shape shape, double verticalRadius) {
        final long xStart = position1.getX();
        final long zStart = position1.getZ();
        final long xEnd = position2.getX();
        final long zEnd = position2.getZ();

        // Coordinate system translation and rotation
        final Vector vecDifference = new Vector(position2.getX() - position1.getX(), 0, position2.getZ() - position1.getZ());
        final Vector vecDifferenceRotated = vecDifference.rotateY((float) angle1.angleRadians);

        // First we check the Delta Side > 0
        // 1. If they are same angle
        // 1. a. If aligned -> Use One Segment
        // 1. b. If not aligned -> Use two Circle, r = (dv^2 + dp^2) / (4dv).
        // 2. If they are right angle -> r = min ( dx,dz ), work around, actually equation 3. can be used.
        // 3. Check if one segment and one circle is available
        // 3. a. If available -> (Segment First) r2 = dv / ( sin(diff) * tan(diff/2) ) = dv / ( 1 - cos(diff)
        // 							for case 2, diff = 90 degrees, r = dv
        //					-> (Circle First) r1 = ( dp - dv / tan(diff) ) / tan (diff/2)
        // TODO 3. b. If not -> r = very complex one. In this case, we need two circles to connect.
        final double deltaForward = vecDifferenceRotated.z;
        final double deltaSide = vecDifferenceRotated.x;
        if (angle1.isParallel(angle2)) { // 1
            if (Math.abs(deltaForward) < ACCEPT_THRESHOLD) { // 1. a.
                h1 = angle1.cos;
                k1 = angle1.sin;
                if (Math.abs(h1) >= 0.5 && Math.abs(k1) >= 0.5) {
                    r1 = (h1 * zStart - k1 * xStart) / h1 / h1;
                    tStart1 = xStart / h1;
                    tEnd1 = xEnd / h1;
                } else {
                    final double div = angle1.add(angle1).cos;
                    r1 = (h1 * zStart - k1 * xStart) / div;
                    tStart1 = (h1 * xStart - k1 * zStart) / div;
                    tEnd1 = (h1 * xEnd - k1 * zEnd) / div;
                }
                h2 = k2 = r2 = 0;
                reverseT1 = tStart1 > tEnd1;
                reverseT2 = false;
                isStraight1 = isStraight2 = true;
                tStart2 = tEnd2 = 0;
            } else { // 1. b
                if (Math.abs(deltaSide) > ACCEPT_THRESHOLD) {
                    final double radius = (deltaForward * deltaForward + deltaSide * deltaSide) / (4 * deltaForward);
                    r1 = r2 = Math.abs(radius);
                    h1 = xStart - radius * angle1.sin;
                    k1 = zStart + radius * angle1.cos;
                    h2 = xEnd - radius * angle2.sin;
                    k2 = zEnd + radius * angle2.cos;
                    reverseT1 = deltaForward < 0 != deltaSide < 0;
                    reverseT2 = !reverseT1;
                    tStart1 = getTBounds(xStart, h1, zStart, k1, r1);
                    tEnd1 = getTBounds(xStart + vecDifference.x / 2, h1, zStart + vecDifference.z / 2, k1, r1, tStart1, reverseT1);
                    tStart2 = getTBounds(xStart + vecDifference.x / 2, h2, zStart + vecDifference.z / 2, k2, r2);
                    tEnd2 = getTBounds(xEnd, h2, zEnd, k2, r2, tStart2, reverseT2);
                    isStraight1 = isStraight2 = false;
                } else {
                    // Banned node perpendicular to the rail nodes direction
                    h1 = k1 = h2 = k2 = r1 = r2 = 0;
                    tStart1 = tStart2 = tEnd1 = tEnd2 = 0;
                    reverseT1 = false;
                    reverseT2 = false;
                    isStraight1 = isStraight2 = true;
                }
            }
        } else { // 3.
            // Check if it needs invert
            final Angle newAngle1 = vecDifferenceRotated.x < -ACCEPT_THRESHOLD ? angle1.getOpposite() : angle1;
            final Angle newAngle2 = angle2.cos * vecDifference.x + angle2.sin * vecDifference.z < -ACCEPT_THRESHOLD ? angle2.getOpposite() : angle2;
            final double angleForward = Math.atan2(deltaForward, deltaSide);
            final Angle railAngleDifference = newAngle2.sub(newAngle1);
            final double angleDifference = railAngleDifference.angleRadians;

            if (Math.signum(angleForward) == Math.signum(angleDifference)) {
                final double absAngleForward = Math.abs(angleForward);

                if (absAngleForward - Math.abs(angleDifference / 2) < ACCEPT_THRESHOLD) { // Segment First
                    final double offsetSide = Math.abs(deltaForward / railAngleDifference.halfTan);
                    final double remainingSide = deltaSide - offsetSide;
                    final double deltaXEnd = xStart + remainingSide * newAngle1.cos;
                    final double deltaZEnd = zStart + remainingSide * newAngle1.sin;
                    h1 = newAngle1.cos;
                    k1 = newAngle1.sin;
                    if (Math.abs(h1) >= 0.5 && Math.abs(k1) >= 0.5) {
                        r1 = (h1 * zStart - k1 * xStart) / h1 / h1;
                        tStart1 = xStart / h1;
                        tEnd1 = deltaXEnd / h1;
                    } else {
                        final double div = newAngle1.add(newAngle1).cos;
                        r1 = (h1 * zStart - k1 * xStart) / div;
                        tStart1 = (h1 * xStart - k1 * zStart) / div;
                        tEnd1 = (h1 * deltaXEnd - k1 * deltaZEnd) / div;
                    }
                    isStraight1 = true;
                    reverseT1 = tStart1 > tEnd1;
                    final double radius = deltaForward / (1 - railAngleDifference.cos);
                    r2 = Math.abs(radius);
                    h2 = deltaXEnd - radius * newAngle1.sin;
                    k2 = deltaZEnd + radius * newAngle1.cos;
                    reverseT2 = (deltaForward < 0);
                    tStart2 = getTBounds(deltaXEnd, h2, deltaZEnd, k2, r2);
                    tEnd2 = getTBounds(xEnd, h2, zEnd, k2, r2, tStart2, reverseT2);
                    isStraight2 = false;
                } else if (absAngleForward - Math.abs(angleDifference) < ACCEPT_THRESHOLD) { // Circle First
                    final double crossSide = deltaForward / railAngleDifference.tan;
                    final double remainingSide = (deltaSide - crossSide) * (1 + railAngleDifference.cos);
                    final double remainingForward = (deltaSide - crossSide) * (railAngleDifference.sin);
                    final double deltaXEnd = xStart + remainingSide * newAngle1.cos - remainingForward * newAngle1.sin;
                    final double deltaZEnd = zStart + remainingSide * newAngle1.sin + remainingForward * newAngle1.cos;
                    final double radius = (deltaSide - deltaForward / railAngleDifference.tan) / railAngleDifference.halfTan;
                    r1 = Math.abs(radius);
                    h1 = xStart - radius * newAngle1.sin;
                    k1 = zStart + radius * newAngle1.cos;
                    isStraight1 = false;
                    reverseT1 = (deltaForward < 0);
                    tStart1 = getTBounds(xStart, h1, zStart, k1, r1);
                    tEnd1 = getTBounds(deltaXEnd, h1, deltaZEnd, k1, r1, tStart1, reverseT1);
                    h2 = newAngle2.cos;
                    k2 = newAngle2.sin;
                    if (Math.abs(h2) >= 0.5 && Math.abs(k2) >= 0.5) {
                        r2 = (h2 * deltaZEnd - k2 * deltaXEnd) / h2 / h2;
                        tStart2 = deltaXEnd / h2;
                        tEnd2 = xEnd / h2;
                    } else {
                        final double div = newAngle2.add(newAngle2).cos;
                        r2 = (h2 * deltaZEnd - k2 * deltaXEnd) / div;
                        tStart2 = (h2 * deltaXEnd - k2 * deltaZEnd) / div;
                        tEnd2 = (h2 * xEnd - k2 * zEnd) / div;
                    }
                    isStraight2 = true;
                    reverseT2 = tStart2 > tEnd2;
                } else { // Out of available range
                    // TODO complex one. Normally we don't need it.
                    h1 = k1 = h2 = k2 = r1 = r2 = 0;
                    tStart1 = tStart2 = tEnd1 = tEnd2 = 0;
                    reverseT1 = false;
                    reverseT2 = false;
                    isStraight1 = isStraight2 = true;
                }
            } else {
                // TODO 3. b. If not -> r = very complex one. Normally we don't need it.
                h1 = k1 = h2 = k2 = r1 = r2 = 0;
                tStart1 = tStart2 = tEnd1 = tEnd2 = 0;
                reverseT1 = false;
                reverseT2 = false;
                isStraight1 = isStraight2 = true;
            }
        }
        yStart = position1.getY();
        yEnd = position2.getY();
        this.shape = shape;
        this.verticalRadius = Math.min(verticalRadius, getMaxVerticalRadius());
    }

    public void init() {
        if (!checkRun) {
            final int SEGMENT_LENGTH = 1;
            final double count1 = Math.abs(tEnd1 - tStart1);
            final double count2 = Math.abs(tEnd2 - tStart2);
            final double segment_count1 = Math.round(count1 / SEGMENT_LENGTH);
            final double segment_count2 = Math.round(count2 / SEGMENT_LENGTH);
            final double increment1 = count1 / Math.max(1, segment_count1);
            final double increment2 = count2 / Math.max(1, segment_count2);
            final double rawValueOffset = Math.abs(tEnd1 - tStart1);
            for (double i = 0; i < count1 - 0.1; i += increment1) {
                final double t1 = (reverseT1 ? -1 : 1) * i + tStart1;
                final double t2 = (reverseT1 ? -1 : 1) * (i + increment1) + tStart1;
                final Vector corner1 = getPositionXZ(h1, k1, r1, t1, -0.015625F, isStraight1);
                final Vector corner2 = getPositionXZ(h1, k1, r1, t1, 0.015625F, isStraight1);
                final Vector corner3 = getPositionXZ(h1, k1, r1, t2, 0.015625F, isStraight1);
                final Vector corner4 = getPositionXZ(h1, k1, r1, t2, -0.015625F, isStraight1);
                final Vector corner5 = getPositionXZ(h1, k1, r1, t1, -0.09375F, isStraight1);
                final Vector corner6 = getPositionXZ(h1, k1, r1, t1, 0.09375F, isStraight1);
                final Vector corner7 = getPositionXZ(h1, k1, r1, t2, 0.09375F, isStraight1);
                final Vector corner8 = getPositionXZ(h1, k1, r1, t2, -0.09375F, isStraight1);
                final double y1 = getPositionY(i);
                final double y2 = getPositionY(i + increment1);
                final SolidVectorLocation locationTemp1 = new SolidVectorLocation(corner1.x, corner1.z, corner2.x, corner2.z, corner3.x, corner3.z, corner4.x, corner4.z, corner5.x, corner5.z, corner6.x, corner6.z, corner7.x, corner7.z, corner8.x, corner8.z, y1, y2);
                corners1.add(locationTemp1);
            }
            for (double i = 0; i < count2 - 0.1; i += increment2) {
                final double t1 = (reverseT2 ? -1 : 1) * i + tStart2;
                final double t2 = (reverseT2 ? -1 : 1) * (i + increment2) + tStart2;
                final Vector corner1 = getPositionXZ(h2, k2, r2, t1, -0.015625F, isStraight2);
                final Vector corner2 = getPositionXZ(h2, k2, r2, t1, 0.015625F, isStraight2);
                final Vector corner3 = getPositionXZ(h2, k2, r2, t2, 0.015625F, isStraight2);
                final Vector corner4 = getPositionXZ(h2, k2, r2, t2, -0.015625F, isStraight2);
                final Vector corner5 = getPositionXZ(h2, k2, r2, t1, -0.09375F, isStraight2);
                final Vector corner6 = getPositionXZ(h2, k2, r2, t1, 0.09375F, isStraight2);
                final Vector corner7 = getPositionXZ(h2, k2, r2, t2, 0.09375F, isStraight2);
                final Vector corner8 = getPositionXZ(h2, k2, r2, t2, -0.09375F, isStraight2);
                final double y1 = getPositionY(i + rawValueOffset);
                final double y2 = getPositionY(i + increment2 + rawValueOffset);
                final SolidVectorLocation locationTemp = new SolidVectorLocation(corner1.x, corner1.z, corner2.x, corner2.z, corner3.x, corner3.z, corner4.x, corner4.z, corner5.x, corner5.z, corner6.x, corner6.z, corner7.x, corner7.z, corner8.x, corner8.z, y1, y2);
                corners2.add(locationTemp);
            }
            checkRun = !checkRun;
        }
    }

    public void render(RenderRigidCatenary callback) {
        init();
        renderSegment(corners1, callback);
        renderSegment(corners2, callback);
    }

    private void renderSegment(ObjectArrayList<SolidVectorLocation> vecLocations, RenderRigidCatenary callback) {
        for (SolidVectorLocation vecLocation : vecLocations) {
            callback.renderRigidCatenary(vecLocation.x1, vecLocation.z1, vecLocation.x2, vecLocation.z2, vecLocation.x3, vecLocation.z3, vecLocation.x4, vecLocation.z4, vecLocation.xs1, vecLocation.zs1, vecLocation.xs2, vecLocation.zs2, vecLocation.xs3, vecLocation.zs3, vecLocation.xs4, vecLocation.zs4, vecLocation.y1, vecLocation.y2);
        }

    }

    public double getLength() {
        return Math.abs(tEnd2 - tStart2) + Math.abs(tEnd1 - tStart1);
    }

    public RigidCatenary.Shape getShape() {
        return shape;
    }

    public double getVerticalRadius() {
        return verticalRadius;
    }

    public double getMaxVerticalRadius() {
        final double length = getLength();
        final double height = yEnd - yStart;
        return Math.floor((length * length + height * height) * 100 / Math.abs(4 * height)) / 100;
    }

    boolean isValid() {
        return h1 != 0 || k1 != 0 || h2 != 0 || k2 != 0 || r1 != 0 || r2 != 0 || tStart1 != 0 || tStart2 != 0 || tEnd1 != 0 || tEnd2 != 0;
    }

    private double getPositionY(double value) {
        if (yStart == yEnd) {
            return yStart;
        }

        final double length = getLength();

        switch (shape) {
            case TWO_RADII:
                // Copied from NTE
                if (verticalRadius <= 0) {
                    return (value / length) * (yEnd - yStart) + yStart;
                } else {
                    final double vTheta = getVTheta();
                    final double curveLength = Math.sin(vTheta) * verticalRadius;
                    final double curveHeight = (1 - Math.cos(vTheta)) * verticalRadius;
                    final int sign = yStart < yEnd ? 1 : -1;

                    if (value < curveLength) {
                        return sign * (verticalRadius - Math.sqrt(verticalRadius * verticalRadius - value * value)) + yStart;
                    } else if (value > length - curveLength) {
                        final double r = length - value;
                        return -sign * (verticalRadius - Math.sqrt(verticalRadius * verticalRadius - r * r)) + yEnd;
                    } else {
                        return sign * (((value - curveLength) / (length - 2 * curveLength)) * (Math.abs(yEnd - yStart) - 2 * curveHeight) + curveHeight) + yStart;
                    }
                }
            case CABLE:
                if (value < 0.5) {
                    return yStart;
                } else if (value > length - 0.5) {
                    return yEnd;
                }

                final double cableOffsetValue = value - 0.5;
                final double offsetLength = length - 1;
                final double posY = yStart + (yEnd - yStart) * cableOffsetValue / offsetLength;
                final double dip = offsetLength * offsetLength / 4 / CABLE_CURVATURE_SCALE;
                return posY + (dip > MAX_CABLE_DIP ? MAX_CABLE_DIP / dip : 1) * (cableOffsetValue - offsetLength) * cableOffsetValue / CABLE_CURVATURE_SCALE;
            default:
                final double intercept = length / 2;
                final double yChange;
                final double yInitial;
                final double offsetValue;

                if (value < intercept) {
                    yChange = (yEnd - yStart) / 2D;
                    yInitial = yStart;
                    offsetValue = value;
                } else {
                    yChange = (yStart - yEnd) / 2D;
                    yInitial = yEnd;
                    offsetValue = length - value;
                }

                return yChange * offsetValue * offsetValue / (intercept * intercept) + yInitial;
        }
    }

    private double getVTheta() {
        final double height = Math.abs(yEnd - yStart);
        final double length = getLength();
        return 2 * Math.atan2(Math.sqrt(height * height - 4 * verticalRadius * height + length * length) - length, height - 4 * verticalRadius);
    }

    private static Vector getPositionXZ(double h, double k, double r, double t, double radiusOffset, boolean isStraight) {
        if (isStraight) {
            return new Vector(h * t + k * ((Math.abs(h) >= 0.5 && Math.abs(k) >= 0.5 ? 0 : r) + radiusOffset) + 0.5, 0, k * t + h * (r - radiusOffset) + 0.5);
        } else {
            return new Vector(h + (r + radiusOffset) * Math.cos(t / r) + 0.5, 0, k + (r + radiusOffset) * Math.sin(t / r) + 0.5);
        }
    }

    private static double getTBounds(double x, double h, double z, double k, double r) {
        return Math.atan2(z - k, x - h) * r;
    }

    private static double getTBounds(double x, double h, double z, double k, double r, double tStart, boolean reverse) {
        final double t = getTBounds(x, h, z, k, r);
        if (t < tStart && !reverse) {
            return t + 2 * Math.PI * r;
        } else if (t > tStart && reverse) {
            return t - 2 * Math.PI * r;
        } else {
            return t;
        }
    }

    @FunctionalInterface
    public interface RenderRigidCatenary {
        void renderRigidCatenary(double x1, double z1, double x2, double z2, double x3, double z3, double x4, double z4, double xs1, double zs1, double xs2, double zs2, double xs3, double zs3, double xs4, double zs4, double y1, double y2);
    }
}
