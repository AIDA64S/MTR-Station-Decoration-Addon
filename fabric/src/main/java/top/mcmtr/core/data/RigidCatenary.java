package top.mcmtr.core.data;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Angle;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import top.mcmtr.core.generated.data.RigidCatenarySchema;

public final class RigidCatenary extends RigidCatenarySchema {
    public final RigidCatenaryMath rigidCatenaryMath;
    private final boolean reversePositions;

    public static RigidCatenary copy(RigidCatenary rigidCatenary, RigidCatenary.Shape newShape, double newVerticalRadius) {
        return new RigidCatenary(rigidCatenary.position1, rigidCatenary.angle1, rigidCatenary.position2, rigidCatenary.angle2, newShape, newVerticalRadius);
    }

    public RigidCatenary(Position position1, Angle angle1, Position position2, Angle angle2, RigidCatenary.Shape shape, double verticalRadius) {
        super(position1, angle1, position2, angle2, shape, verticalRadius);
        this.reversePositions = position1.compareTo(position2) > 0;
        this.rigidCatenaryMath = reversePositions ? new RigidCatenaryMath(position2, angle2, position1, angle1, shape, verticalRadius) : new RigidCatenaryMath(position1, angle1, position2, angle2, shape, verticalRadius);
    }

    public RigidCatenary(ReaderBase readerBase) {
        super(readerBase);
        this.reversePositions = position1.compareTo(position2) > 0;
        this.rigidCatenaryMath = reversePositions ? new RigidCatenaryMath(position2, angle2, position1, angle1, shape, verticalRadius) : new RigidCatenaryMath(position1, angle1, position2, angle2, shape, verticalRadius);
    }

    @Override
    protected Position getPosition1() {
        return this.position1;
    }

    @Override
    protected Position getPosition2() {
        return this.position2;
    }

    @Override
    public boolean isValid() {
        return this.rigidCatenaryMath.isValid();
    }

    public boolean closeTo(Position position, double radius) {
        return Utilities.isBetween(position, position1, position2, radius);
    }

    void writePositionsToRigidCatenaryCache(Object2ObjectOpenHashMap<Position, Object2ObjectOpenHashMap<Position, RigidCatenary>> positionsToCatenary) {
        MSDData.put(positionsToCatenary, position1, position2, oldValue -> this, Object2ObjectOpenHashMap::new);
        MSDData.put(positionsToCatenary, position2, position1, oldValue -> this, Object2ObjectOpenHashMap::new);
    }

    public static boolean verifyPosition(Position positionStart, Position positionEnd) {
        double xStart = positionStart.getX();
        double yStart = positionStart.getY();
        double zStart = positionStart.getZ();
        double xEnd = positionEnd.getX();
        double yEnd = positionEnd.getY();
        double zEnd = positionEnd.getZ();
        return Math.sqrt(((xEnd - xStart) * (xEnd - xStart)) + ((yEnd - yStart) * (yEnd - yStart)) + ((zEnd - zStart) * (zEnd - zStart))) > 0;
    }

    public enum Shape {
        QUADRATIC, TWO_RADII;

        Shape() {
        }
    }
}