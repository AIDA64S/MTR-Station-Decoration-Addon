package top.mcmtr.core.operation;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import top.mcmtr.core.data.MSDData;
import top.mcmtr.core.generated.operation.MSDDeleteDataResponseSchema;

import java.util.function.Consumer;

public final class MSDDeleteDataResponse extends MSDDeleteDataResponseSchema {
    public MSDDeleteDataResponse() {
        super();
    }

    public MSDDeleteDataResponse(ReaderBase readerBase) {
        super(readerBase);
        updateData(readerBase);
    }

    public void write(MSDData data) {
        data.catenaries.removeIf(catenary -> catenaryIds.contains(catenary.getHexId()));
        data.rigidCatenaries.removeIf(rigidCatenary -> rigidCatenaryIds.contains(rigidCatenary.getHexId()));
        data.sync();
    }

    public void iterateCatenaryNodePosition(Consumer<Position> consumer) {
        catenaryNodePositions.forEach(consumer);
    }

    ObjectArrayList<String> getCatenaryIds() {
        return catenaryIds;
    }

    ObjectArrayList<Position> getCatenaryNodePositions() {
        return catenaryNodePositions;
    }

    ObjectArrayList<String> getRigidCatenaryIds() {
        return rigidCatenaryIds;
    }
}