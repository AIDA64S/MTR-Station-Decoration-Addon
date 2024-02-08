package top.mcmtr.core.data;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.SerializedDataBaseWithId;
import org.mtr.core.tool.Utilities;

public abstract class TwoPositionsBase implements SerializedDataBaseWithId {
    @Override
    public final String getHexId() {
        final boolean reversePositions = getPositionStart().compareTo(getPositionEnd()) > 0;
        return String.format(
                "%s-%s-%s-%s-%s-%s",
                Utilities.numberToPaddedHexString((reversePositions ? getPositionEnd() : getPositionStart()).getX()),
                Utilities.numberToPaddedHexString((reversePositions ? getPositionEnd() : getPositionStart()).getY()),
                Utilities.numberToPaddedHexString((reversePositions ? getPositionEnd() : getPositionStart()).getZ()),
                Utilities.numberToPaddedHexString((reversePositions ? getPositionStart() : getPositionEnd()).getX()),
                Utilities.numberToPaddedHexString((reversePositions ? getPositionStart() : getPositionEnd()).getY()),
                Utilities.numberToPaddedHexString((reversePositions ? getPositionStart() : getPositionEnd()).getZ())
        );
    }

    protected abstract Position getPositionStart();

    protected abstract Position getPositionEnd();
}