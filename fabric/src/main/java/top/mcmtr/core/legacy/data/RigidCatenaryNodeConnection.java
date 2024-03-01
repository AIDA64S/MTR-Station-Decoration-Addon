package top.mcmtr.core.legacy.data;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Angle;
import org.mtr.core.tool.Utilities;
import org.mtr.core.tool.Vector;
import org.mtr.legacy.data.DataFixer;
import top.mcmtr.core.legacy.generated.data.RigidCatenaryConnectionSchema;

public final class RigidCatenaryNodeConnection extends RigidCatenaryConnectionSchema {
    public RigidCatenaryNodeConnection(ReaderBase readerBase) {
        super(readerBase);
        updateData(readerBase);
    }

    public Position getEndPosition() {
        return DataFixer.fromLong(rigidCatenaryNodePos);
    }

    public long getEndPositionLong() {
        return rigidCatenaryNodePos;
    }

    public Angle getStartAngle() {
        return getAngle(false);
    }

    public Angle getEndAngle() {
        return getAngle(true);
    }

    private Angle getAngle(boolean reverse) {
        final Vector vector1 = getPosition(0, reverse);
        final Vector vector2 = getPosition(0.1, reverse);
        return Angle.fromAngle((float) Math.toDegrees(Math.atan2(vector2.z - vector1.z, vector2.x - vector1.x)));
    }

    private Vector getPosition(double rawValue, boolean reverse) {
        final double count1 = Math.abs(tEnd1 - tStart1);
        final double count2 = Math.abs(tEnd2 - tStart2);
        final double clampedValue = Utilities.clamp(rawValue, 0, count1 + count2);
        final double value = reverse ? count1 + count2 - clampedValue : clampedValue;

        if (value <= count1) {
            return getPositionXZ(h1, k1, r1, (reverseT1 ? -1 : 1) * value + tStart1, isStraight1);
        } else {
            return getPositionXZ(h2, k2, r2, (reverseT2 ? -1 : 1) * (value - count1) + tStart2, isStraight2);
        }
    }

    private static Vector getPositionXZ(double h, double k, double r, double t, boolean isStraight) {
        if (isStraight) {
            return new Vector(h * t + k * ((Math.abs(h) >= 0.5 && Math.abs(k) >= 0.5 ? 0 : r)) + 0.5, 0, k * t + h * r + 0.5);
        } else {
            return new Vector(h + r * Math.cos(t / r) + 0.5, 0, k + r * Math.sin(t / r) + 0.5);
        }
    }
}