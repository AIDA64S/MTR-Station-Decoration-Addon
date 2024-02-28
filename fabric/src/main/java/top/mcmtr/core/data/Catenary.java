package top.mcmtr.core.data;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import top.mcmtr.core.generated.data.CatenarySchema;

public final class Catenary extends CatenarySchema {
    public final CatenaryMath catenaryMath;
    private final boolean reversePositions;

    public Catenary(Position positionStart, Position positionEnd, OffsetPosition offsetPositionStart, OffsetPosition offsetPositionEnd, CatenaryType catenaryType) {
        super(positionStart, positionEnd, offsetPositionStart, offsetPositionEnd, catenaryType);
        this.reversePositions = positionStart.compareTo(positionEnd) > 0;
        this.catenaryMath = reversePositions ? new CatenaryMath(positionEnd, positionStart, offsetPositionEnd, offsetPositionStart, catenaryType) : new CatenaryMath(positionStart, positionEnd, offsetPositionStart, offsetPositionEnd, catenaryType);
    }

    public Catenary(ReaderBase readerBase) {
        super(readerBase);
        this.reversePositions = positionStart.compareTo(positionEnd) > 0;
        this.catenaryMath = reversePositions ? new CatenaryMath(positionEnd, positionStart, offsetPositionEnd, offsetPositionStart, catenaryType) : new CatenaryMath(positionStart, positionEnd, offsetPositionStart, offsetPositionEnd, catenaryType);
    }

    @Override
    public Position getPositionStart() {
        return this.positionStart;
    }

    @Override
    public Position getPositionEnd() {
        return this.positionEnd;
    }

    public OffsetPosition getOffsetPositionStart() {
        return this.offsetPositionStart;
    }

    public OffsetPosition getOffsetPositionEnd() {
        return this.offsetPositionEnd;
    }

    public CatenaryType getCatenaryType() {
        return this.catenaryType;
    }

    @Override
    public boolean isValid() {
        return this.catenaryMath.verify();
    }

    public boolean closeTo(Position position, double radius) {
        return Utilities.isBetween(position, positionStart, positionEnd, radius);
    }

    void writePositionsToCatenaryCache(Object2ObjectOpenHashMap<Position, Object2ObjectOpenHashMap<Position, Catenary>> positionsToCatenary) {
        MSDData.put(positionsToCatenary, positionStart, positionEnd, oldValue -> this, Object2ObjectOpenHashMap::new);
        MSDData.put(positionsToCatenary, positionEnd, positionStart, oldValue -> this, Object2ObjectOpenHashMap::new);
    }

    public static boolean verifyPosition(Position positionStart, Position positionEnd, OffsetPosition offsetStart, OffsetPosition offsetEnd) {
        double xStart = positionStart.getX() + offsetStart.getX();
        double yStart = positionStart.getY() + offsetStart.getY();
        double zStart = positionStart.getZ() + offsetStart.getZ();
        double xEnd = positionEnd.getX() + offsetEnd.getX();
        double yEnd = positionEnd.getY() + offsetEnd.getY();
        double zEnd = positionEnd.getZ() + offsetEnd.getZ();
        return Math.sqrt(((xEnd - xStart) * (xEnd - xStart)) + ((yEnd - yStart) * (yEnd - yStart)) + ((zEnd - zStart) * (zEnd - zStart))) > 0;
    }
}