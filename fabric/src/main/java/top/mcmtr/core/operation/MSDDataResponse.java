package top.mcmtr.core.operation;

import org.mtr.core.serializer.ReaderBase;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.data.MSDClientData;
import top.mcmtr.core.data.MSDData;
import top.mcmtr.core.data.RigidCatenary;
import top.mcmtr.core.generated.operation.MSDDataResponseSchema;

public final class MSDDataResponse extends MSDDataResponseSchema {
    private final MSDData data;

    public MSDDataResponse(MSDData data) {
        super();
        this.data = data;
    }

    public MSDDataResponse(ReaderBase readerBase, MSDClientData data) {
        super(readerBase);
        this.data = data;
        updateData(readerBase);
    }

    public void write() {
        boolean isSync = false;
        if (data instanceof MSDClientData && (!catenaries.isEmpty())) {
            data.catenaries.removeIf(catenary -> !catenariesToKeep.contains(catenary.getHexId()));
            data.catenaries.addAll(catenaries);
            isSync = true;
        }
        if (data instanceof MSDClientData && (!rigidCatenaries.isEmpty())) {
            data.rigidCatenaries.removeIf(rigidCatenary -> !rigidCatenariesToKeep.contains(rigidCatenary.getHexId()));
            data.rigidCatenaries.addAll(rigidCatenaries);
            isSync = true;
        }
        if (isSync) {
            data.sync();
        }
    }

    void addCatenary(Catenary catenary) {
        catenaries.add(catenary);
    }

    void addRigidCatenary(RigidCatenary rigidCatenary) {
        rigidCatenaries.add(rigidCatenary);
    }

    void addCatenary(String catenaryId) {
        catenariesToKeep.add(catenaryId);
    }

    void addRigidCatenary(String rigidCatenaryId) {
        rigidCatenariesToKeep.add(rigidCatenaryId);
    }
}