package top.mcmtr.core.data;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.SerializedDataBaseWithId;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import javax.annotation.Nullable;

public abstract class TwoPositionsBase implements SerializedDataBaseWithId {
    public TwoPositionsBase() {
    }

    public final String getHexId() {
        boolean reversePositions = this.getPosition1().compareTo(this.getPosition2()) > 0;
        return String.format("%s-%s-%s-%s-%s-%s", Utilities.numberToPaddedHexString((reversePositions ? this.getPosition2() : this.getPosition1()).getX()), Utilities.numberToPaddedHexString((reversePositions ? this.getPosition2() : this.getPosition1()).getY()), Utilities.numberToPaddedHexString((reversePositions ? this.getPosition2() : this.getPosition1()).getZ()), Utilities.numberToPaddedHexString((reversePositions ? this.getPosition1() : this.getPosition2()).getX()), Utilities.numberToPaddedHexString((reversePositions ? this.getPosition1() : this.getPosition2()).getY()), Utilities.numberToPaddedHexString((reversePositions ? this.getPosition1() : this.getPosition2()).getZ()));
    }

    @Nullable
    public final Catenary getCatenaryFromData(Data data, ObjectOpenHashSet<Position> positionsToUpdate) {
        final Catenary catenary = data.catenaryIdMap.get(this.getHexId());
        if (catenary != null) {
            positionsToUpdate.add(this.getPosition1());
            positionsToUpdate.add(this.getPosition2());
        }
        return catenary;
    }

    protected final boolean matchesPositions(TwoPositionsBase twoPositionsBase) {
        return this.getPosition1().equals(twoPositionsBase.getPosition1()) && this.getPosition2().equals(twoPositionsBase.getPosition2()) || this.getPosition2().equals(twoPositionsBase.getPosition1()) && this.getPosition1().equals(twoPositionsBase.getPosition2());
    }

    protected abstract Position getPosition1();

    protected abstract Position getPosition2();
}