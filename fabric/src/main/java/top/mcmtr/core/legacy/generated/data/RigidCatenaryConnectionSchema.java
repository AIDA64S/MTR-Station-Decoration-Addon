package top.mcmtr.core.legacy.generated.data;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.serializer.WriterBase;

public abstract class RigidCatenaryConnectionSchema implements SerializedDataBase {
    protected long rigidCatenaryNodePos;
    protected double h1;
    protected double k1;
    protected double r1;
    protected double h2;
    protected double k2;
    protected double r2;
    protected double tStart1;
    protected double tEnd1;
    protected double tStart2;
    protected double tEnd2;
    protected int yStart;
    protected int yEnd;
    protected boolean reverseT1;
    protected boolean isStraight1;
    protected boolean reverseT2;
    protected boolean isStraight2;
    protected String catenaryType;
    private static final String KEY_RIGID_CATENARY_NODE_POS = "rigid_catenary_node_pos";
    private static final String KEY_H_1 = "rigid_h_1";
    private static final String KEY_K_1 = "rigid_k_1";
    private static final String KEY_H_2 = "rigid_h_2";
    private static final String KEY_K_2 = "rigid_k_2";
    private static final String KEY_R_1 = "rigid_r_1";
    private static final String KEY_R_2 = "rigid_r_2";
    private static final String KEY_T_START_1 = "rigid_t_start_1";
    private static final String KEY_T_END_1 = "rigid_t_end_1";
    private static final String KEY_T_START_2 = "rigid_t_start_2";
    private static final String KEY_T_END_2 = "rigid_t_end_2";
    private static final String KEY_Y_START = "rigid_y_start";
    private static final String KEY_Y_END = "rigid_y_end";
    private static final String KEY_REVERSE_T_1 = "rigid_reverse_t_1";
    private static final String KEY_IS_STRAIGHT_1 = "rigid_is_straight_1";
    private static final String KEY_REVERSE_T_2 = "rigid_reverse_t_2";
    private static final String KEY_IS_STRAIGHT_2 = "rigid_is_straight_2";
    private static final String KEY_CATENARY_TYPE = "rigid_catenary_type";

    public RigidCatenaryConnectionSchema() {
    }

    public RigidCatenaryConnectionSchema(ReaderBase readerBase) {
    }

    @Override
    public void updateData(ReaderBase readerBase) {
        readerBase.unpackLong(KEY_RIGID_CATENARY_NODE_POS, value -> rigidCatenaryNodePos = value);
        readerBase.unpackDouble(KEY_H_1, value -> h1 = value);
        readerBase.unpackDouble(KEY_K_1, value -> k1 = value);
        readerBase.unpackDouble(KEY_H_2, value -> h2 = value);
        readerBase.unpackDouble(KEY_K_2, value -> k2 = value);
        readerBase.unpackDouble(KEY_R_1, value -> r1 = value);
        readerBase.unpackDouble(KEY_R_2, value -> r2 = value);
        readerBase.unpackDouble(KEY_T_START_1, value -> tStart1 = value);
        readerBase.unpackDouble(KEY_T_END_1, value -> tEnd1 = value);
        readerBase.unpackDouble(KEY_T_START_2, value -> tStart2 = value);
        readerBase.unpackDouble(KEY_T_END_2, value -> tEnd2 = value);
        readerBase.unpackInt(KEY_Y_START, value -> yStart = value);
        readerBase.unpackInt(KEY_Y_END, value -> yEnd = value);
        readerBase.unpackBoolean(KEY_REVERSE_T_1, value -> reverseT1 = value);
        readerBase.unpackBoolean(KEY_IS_STRAIGHT_1, value -> isStraight1 = value);
        readerBase.unpackBoolean(KEY_REVERSE_T_2, value -> reverseT2 = value);
        readerBase.unpackBoolean(KEY_IS_STRAIGHT_2, value -> isStraight2 = value);
        readerBase.unpackString(KEY_CATENARY_TYPE, value -> catenaryType = value);
    }

    @Override
    public void serializeData(WriterBase writerBase) {
        writerBase.writeLong(KEY_RIGID_CATENARY_NODE_POS, rigidCatenaryNodePos);
        writerBase.writeDouble(KEY_H_1, h1);
        writerBase.writeDouble(KEY_K_1, k1);
        writerBase.writeDouble(KEY_H_2, h2);
        writerBase.writeDouble(KEY_K_2, k2);
        writerBase.writeDouble(KEY_R_1, r1);
        writerBase.writeDouble(KEY_R_2, r2);
        writerBase.writeDouble(KEY_T_START_1, tStart1);
        writerBase.writeDouble(KEY_T_END_1, tEnd1);
        writerBase.writeDouble(KEY_T_START_2, tStart2);
        writerBase.writeDouble(KEY_T_END_2, tEnd2);
        writerBase.writeInt(KEY_Y_START, yStart);
        writerBase.writeInt(KEY_Y_END, yEnd);
        writerBase.writeBoolean(KEY_REVERSE_T_1, reverseT1);
        writerBase.writeBoolean(KEY_IS_STRAIGHT_1, isStraight1);
        writerBase.writeBoolean(KEY_REVERSE_T_2, reverseT2);
        writerBase.writeBoolean(KEY_IS_STRAIGHT_2, isStraight2);
        writerBase.writeString(KEY_CATENARY_TYPE, catenaryType);
    }
}