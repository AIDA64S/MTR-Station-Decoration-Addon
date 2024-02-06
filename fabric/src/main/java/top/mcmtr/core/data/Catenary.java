package top.mcmtr.core.data;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBaseWithId;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import top.mcmtr.core.generated.data.CatenarySchema;
import top.mcmtr.mod.data.OffsetPosition;

public class Catenary extends CatenarySchema implements SerializedDataBaseWithId {
    public final CatenaryMath catenaryMath;
    private final ObjectOpenHashSet<Catenary> connectedCatenaries1 = new ObjectOpenHashSet<>();
    private final ObjectOpenHashSet<Catenary> connectedCatenaries2 = new ObjectOpenHashSet<>();

    public Catenary(Position position1, Position position2, OffsetPosition offsetPosition1, OffsetPosition offsetPosition2) {
        super(position1, position2, offsetPosition1, offsetPosition2);
        this.catenaryMath = new CatenaryMath(position1, position2, offsetPosition1, offsetPosition2);
    }

    public Catenary(ReaderBase readerBase) {
        super(readerBase);
        this.catenaryMath = new CatenaryMath(this.position1, this.position2, this.offsetPosition1, this.offsetPosition2);
    }

    public boolean closeTo(Position position, double radius) {
        return Utilities.isBetween(position, position1, position2, radius);
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
        return catenaryMath.verify();
    }

    void writePositionsToCatenaryCache(Object2ObjectOpenHashMap<Position, Object2ObjectOpenHashMap<Position, Catenary>> positionsToCatenary) {
        Data.put(positionsToCatenary, position1, position2, oldValue -> this, Object2ObjectOpenHashMap::new);
        Data.put(positionsToCatenary, position2, position1, oldValue -> this, Object2ObjectOpenHashMap::new);
    }

    void writeConnectedCatenariesCacheFromMap(Object2ObjectOpenHashMap<Position, Object2ObjectOpenHashMap<Position, Catenary>> positionsToCatenary) {
        writeConnectedCatenariesCacheFromMap(positionsToCatenary, position1, connectedCatenaries1);
        writeConnectedCatenariesCacheFromMap(positionsToCatenary, position2, connectedCatenaries2);
    }

    private void writeConnectedCatenariesCacheFromMap(Object2ObjectOpenHashMap<Position, Object2ObjectOpenHashMap<Position, Catenary>> positionsToCatenary, Position position, ObjectOpenHashSet<Catenary> connectedCatenaries) {
        connectedCatenaries.clear();
        positionsToCatenary.getOrDefault(position, new Object2ObjectOpenHashMap<>()).forEach((connectedCatenary, catenary) -> {
            if (!equals(catenary)) {
                connectedCatenaries.add(catenary);
            }
        });
    }
}