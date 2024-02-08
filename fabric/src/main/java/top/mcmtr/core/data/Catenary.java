package top.mcmtr.core.data;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBaseWithId;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import top.mcmtr.core.generated.data.CatenarySchema;

public final class Catenary extends CatenarySchema implements SerializedDataBaseWithId {
    public final CatenaryMath catenaryMath;
    private final ObjectOpenHashSet<Catenary> connectedCatenaries1 = new ObjectOpenHashSet<>();
    private final ObjectOpenHashSet<Catenary> connectedCatenaries2 = new ObjectOpenHashSet<>();
    private final boolean reversePositions;

    public Catenary(Position positionStart, Position positionEnd, OffsetPosition offsetStart, OffsetPosition offsetEnd, CatenaryType catenaryType) {
        super(positionStart, positionEnd, offsetStart, offsetEnd, catenaryType);
        this.reversePositions = positionStart.compareTo(positionEnd) > 0;
        this.catenaryMath = reversePositions ? new CatenaryMath(positionEnd, positionStart, offsetEnd, offsetStart, catenaryType) : new CatenaryMath(positionStart, positionEnd, offsetStart, offsetEnd, catenaryType);
    }

    public Catenary(ReaderBase readerBase) {
        super(readerBase);
        this.reversePositions = positionStart.compareTo(positionEnd) > 0;
        this.catenaryMath = reversePositions ? new CatenaryMath(positionEnd, positionStart, offsetEnd, offsetStart, catenaryType) : new CatenaryMath(positionStart, positionEnd, offsetStart, offsetEnd, catenaryType);
        updateData(readerBase);
    }

    @Override
    protected Position getPositionStart() {
        return this.positionStart;
    }

    @Override
    protected Position getPositionEnd() {
        return this.positionEnd;
    }

    @Override
    public boolean isValid() {
        return catenaryMath.verify();
    }

    public boolean closeTo(Position position, double radius) {
        return Utilities.isBetween(position, positionStart, positionEnd, radius);
    }

    void writePositionsToCatenaryCache(Object2ObjectOpenHashMap<Position, Object2ObjectOpenHashMap<Position, Catenary>> positionsToCatenary) {
        MSDData.put(positionsToCatenary, positionStart, positionEnd, oldValue -> this, Object2ObjectOpenHashMap::new);
        MSDData.put(positionsToCatenary, positionEnd, positionStart, oldValue -> this, Object2ObjectOpenHashMap::new);
    }

    void writeConnectedCatenariesCacheFromMap(Object2ObjectOpenHashMap<Position, Object2ObjectOpenHashMap<Position, Catenary>> positionsToCatenary) {
        writeConnectedCatenariesCacheFromMap(positionsToCatenary, positionStart, connectedCatenaries1);
        writeConnectedCatenariesCacheFromMap(positionsToCatenary, positionEnd, connectedCatenaries2);
    }

    private void writeConnectedCatenariesCacheFromMap(Object2ObjectOpenHashMap<Position, Object2ObjectOpenHashMap<Position, Catenary>> positionsToCatenary, Position position, ObjectOpenHashSet<Catenary> connectedCatenaries) {
        connectedCatenaries.clear();
        positionsToCatenary.getOrDefault(position, new Object2ObjectOpenHashMap<>()).forEach((connectedPosition, rail) -> {
            if (!equals(rail)) {
                connectedCatenaries.add(rail);
            }
        });
    }
}