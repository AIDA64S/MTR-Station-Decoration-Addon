package top.mcmtr.core.data;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.SerializedDataBaseWithId;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;

public abstract class TwoPositionsBase implements SerializedDataBaseWithId {
    private String hexId;

    @Override
    public String getHexId() {
        if (hexId == null) {
            hexId = getHexId(getPositionStart(), getPositionEnd());
        }
        return hexId;
    }

    public final void writePositions(ObjectArraySet<Position> positionsToUpdate) {
        positionsToUpdate.add(getPositionStart());
        positionsToUpdate.add(getPositionEnd());
    }

    protected abstract Position getPositionStart();

    protected abstract Position getPositionEnd();

    public static String getHexId(Position position1, Position position2) {
        final boolean reversePositions = position1.compareTo(position2) > 0;
        return String.format(
                "%s-%s-%s-%s-%s-%s",
                Utilities.numberToPaddedHexString((reversePositions ? position2 : position1).getX()),
                Utilities.numberToPaddedHexString((reversePositions ? position2 : position1).getY()),
                Utilities.numberToPaddedHexString((reversePositions ? position2 : position1).getZ()),
                Utilities.numberToPaddedHexString((reversePositions ? position1 : position2).getX()),
                Utilities.numberToPaddedHexString((reversePositions ? position1 : position2).getY()),
                Utilities.numberToPaddedHexString((reversePositions ? position1 : position2).getZ())
        );
    }
}