package top.mcmtr.core.generated.operation;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.serializer.WriterBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

public abstract class MSDDataRequestSchema implements SerializedDataBase {
    protected final String clientId;
    protected final Position clientPosition;
    protected final long requestRadius;
    protected final ObjectArrayList<String> existingCatenaryIds = new ObjectArrayList<>();
    protected final ObjectArrayList<String> existingRigidCatenaryIds = new ObjectArrayList<>();
    private static final String KEY_CLIENT_ID = "client_id";
    private static final String KEY_CLIENT_POSITION = "client_position";
    private static final String KEY_REQUEST_RADIUS = "request_radius";
    private static final String KEY_EXISTING_CATENARY_IDS = "existing_catenary_ids";
    private static final String KEY_EXISTING_RIGID_CATENARY_IDS = "existing_rigid_catenary_ids";

    protected MSDDataRequestSchema(String clientId, Position clientPosition, long requestRadius) {
        this.clientId = clientId;
        this.clientPosition = clientPosition;
        this.requestRadius = requestRadius;
    }

    protected MSDDataRequestSchema(ReaderBase readerBase) {
        this.clientId = readerBase.getString(KEY_CLIENT_ID, "");
        this.clientPosition = new Position(readerBase.getChild(KEY_CLIENT_POSITION));
        this.requestRadius = readerBase.getLong(KEY_REQUEST_RADIUS, 0);
    }

    @Override
    public void updateData(ReaderBase readerBase) {
        readerBase.iterateStringArray(KEY_EXISTING_CATENARY_IDS, existingCatenaryIds::clear, existingCatenaryIds::add);
        readerBase.iterateStringArray(KEY_EXISTING_RIGID_CATENARY_IDS, existingRigidCatenaryIds::clear, existingRigidCatenaryIds::add);
    }

    @Override
    public void serializeData(WriterBase writerBase) {
        writerBase.writeString(KEY_CLIENT_ID, clientId);
        if (clientPosition != null) {
            clientPosition.serializeData(writerBase.writeChild(KEY_CLIENT_POSITION));
        }
        writerBase.writeLong(KEY_REQUEST_RADIUS, requestRadius);
        serializeExistingCatenaryIds(writerBase);
        serializeExistingRigidCatenaryIds(writerBase);
    }

    protected void serializeExistingCatenaryIds(WriterBase writerBase) {
        WriterBase.Array existingCatenaryIdsWriterBaseArray = writerBase.writeArray(KEY_EXISTING_CATENARY_IDS);
        existingCatenaryIds.forEach(existingCatenaryIdsWriterBaseArray::writeString);
    }

    protected void serializeExistingRigidCatenaryIds(WriterBase writerBase) {
        WriterBase.Array existingRigidCatenaryIdsWriterBaseArray = writerBase.writeArray(KEY_EXISTING_RIGID_CATENARY_IDS);
        existingRigidCatenaryIds.forEach(existingRigidCatenaryIdsWriterBaseArray::writeString);
    }
}