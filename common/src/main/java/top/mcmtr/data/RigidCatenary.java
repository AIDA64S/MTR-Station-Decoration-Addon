package top.mcmtr.data;

import mtr.data.EnumHelper;
import mtr.data.MessagePackHelper;
import mtr.data.RailAngle;
import mtr.data.SerializedDataBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;
import top.mcmtr.config.Config;

import java.io.IOException;
import java.util.Map;

public class RigidCatenary extends SerializedDataBase {
    public final CatenaryType catenaryType;
    public final RailAngle facingStart;
    public final RailAngle facingEnd;
    private final double h1, k1, r1, tStart1, tEnd1;
    private final double h2, k2, r2, tStart2, tEnd2;
    private final int yStart, yEnd;
    private final boolean reverseT1, isStraight1, reverseT2, isStraight2;
    private static final double ACCEPT_THRESHOLD = 1E-4;
    private static final int MIN_RADIUS = 2;
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

    public RigidCatenary(BlockPos posStart, RailAngle facingStart, BlockPos posEnd, RailAngle facingEnd, CatenaryType catenaryType) {
        this.facingStart = facingStart;
        this.facingEnd = facingEnd;
        this.catenaryType = catenaryType;
        yStart = posStart.getY();
        yEnd = posEnd.getY();
        final int xStart = posStart.getX();
        final int zStart = posStart.getZ();
        final int xEnd = posEnd.getX();
        final int zEnd = posEnd.getZ();
        final Vec3 vecDifference = new Vec3(posEnd.getX() - posStart.getX(), 0, posEnd.getZ() - posStart.getZ());
        final Vec3 vecDifferenceRotated = vecDifference.yRot((float) facingStart.angleRadians);
        final double deltaForward = vecDifferenceRotated.z;
        final double deltaSide = vecDifferenceRotated.x;
        if (facingStart.isParallel(facingEnd)) {
            if (Math.abs(deltaForward) < ACCEPT_THRESHOLD) {
                h1 = facingStart.cos;
                k1 = facingStart.sin;
                if (Math.abs(h1) >= 0.5 && Math.abs(k1) >= 0.5) {
                    r1 = (h1 * zStart - k1 * xStart) / h1 / h1;
                    tStart1 = xStart / h1;
                    tEnd1 = xEnd / h1;
                } else {
                    final double div = facingStart.add(facingStart).cos;
                    r1 = (h1 * zStart - k1 * xStart) / div;
                    tStart1 = (h1 * xStart - k1 * zStart) / div;
                    tEnd1 = (h1 * xEnd - k1 * zEnd) / div;
                }
                h2 = k2 = r2 = 0;
                reverseT1 = tStart1 > tEnd1;
                reverseT2 = false;
                isStraight1 = isStraight2 = true;
                tStart2 = tEnd2 = 0;
            } else {
                if (Math.abs(deltaSide) > ACCEPT_THRESHOLD) {
                    final double radius = (deltaForward * deltaForward + deltaSide * deltaSide) / (4 * deltaForward);
                    r1 = r2 = Math.abs(radius);
                    h1 = xStart - radius * facingStart.sin;
                    k1 = zStart + radius * facingStart.cos;
                    h2 = xEnd - radius * facingEnd.sin;
                    k2 = zEnd + radius * facingEnd.cos;
                    reverseT1 = deltaForward < 0 != deltaSide < 0;
                    reverseT2 = !reverseT1;
                    tStart1 = getTBounds(xStart, h1, zStart, k1, r1);
                    tEnd1 = getTBounds(xStart + vecDifference.x / 2, h1, zStart + vecDifference.z / 2, k1, r1, tStart1, reverseT1);
                    tStart2 = getTBounds(xStart + vecDifference.x / 2, h2, zStart + vecDifference.z / 2, k2, r2);
                    tEnd2 = getTBounds(xEnd, h2, zEnd, k2, r2, tStart2, reverseT2);
                    isStraight1 = isStraight2 = false;
                } else {
                    h1 = k1 = h2 = k2 = r1 = r2 = 0;
                    tStart1 = tStart2 = tEnd1 = tEnd2 = 0;
                    reverseT1 = false;
                    reverseT2 = false;
                    isStraight1 = isStraight2 = true;
                }
            }
        } else {
            final RailAngle newFacingStart = vecDifferenceRotated.x < -ACCEPT_THRESHOLD ? facingStart.getOpposite() : facingStart;
            final RailAngle newFacingEnd = facingEnd.cos * vecDifference.x + facingEnd.sin * vecDifference.z < -ACCEPT_THRESHOLD ? facingEnd.getOpposite() : facingEnd;
            final double angleForward = Math.atan2(deltaForward, deltaSide);
            final RailAngle railAngleDifference = newFacingEnd.sub(newFacingStart);
            final double angleDifference = railAngleDifference.angleRadians;
            if (Math.signum(angleForward) == Math.signum(angleDifference)) {
                final double absAngleForward = Math.abs(angleForward);
                if (absAngleForward - Math.abs(angleDifference / 2) < ACCEPT_THRESHOLD) { // Segment First
                    final double offsetSide = Math.abs(deltaForward / railAngleDifference.halfTan);
                    final double remainingSide = deltaSide - offsetSide;
                    final double deltaXEnd = xStart + remainingSide * newFacingStart.cos;
                    final double deltaZEnd = zStart + remainingSide * newFacingStart.sin;
                    h1 = newFacingStart.cos;
                    k1 = newFacingStart.sin;
                    if (Math.abs(h1) >= 0.5 && Math.abs(k1) >= 0.5) {
                        r1 = (h1 * zStart - k1 * xStart) / h1 / h1;
                        tStart1 = xStart / h1;
                        tEnd1 = deltaXEnd / h1;
                    } else {
                        final double div = newFacingStart.add(newFacingStart).cos;
                        r1 = (h1 * zStart - k1 * xStart) / div;
                        tStart1 = (h1 * xStart - k1 * zStart) / div;
                        tEnd1 = (h1 * deltaXEnd - k1 * deltaZEnd) / div;
                    }
                    isStraight1 = true;
                    reverseT1 = tStart1 > tEnd1;
                    final double radius = deltaForward / (1 - railAngleDifference.cos);
                    r2 = Math.abs(radius);
                    h2 = deltaXEnd - radius * newFacingStart.sin;
                    k2 = deltaZEnd + radius * newFacingStart.cos;
                    reverseT2 = (deltaForward < 0);
                    tStart2 = getTBounds(deltaXEnd, h2, deltaZEnd, k2, r2);
                    tEnd2 = getTBounds(xEnd, h2, zEnd, k2, r2, tStart2, reverseT2);
                    isStraight2 = false;
                } else if (absAngleForward - Math.abs(angleDifference) < ACCEPT_THRESHOLD) { // Circle First
                    final double crossSide = deltaForward / railAngleDifference.tan;
                    final double remainingSide = (deltaSide - crossSide) * (1 + railAngleDifference.cos);
                    final double remainingForward = (deltaSide - crossSide) * (railAngleDifference.sin);
                    final double deltaXEnd = xStart + remainingSide * newFacingStart.cos - remainingForward * newFacingStart.sin;
                    final double deltaZEnd = zStart + remainingSide * newFacingStart.sin + remainingForward * newFacingStart.cos;
                    final double radius = (deltaSide - deltaForward / railAngleDifference.tan) / railAngleDifference.halfTan;
                    r1 = Math.abs(radius);
                    h1 = xStart - radius * newFacingStart.sin;
                    k1 = zStart + radius * newFacingStart.cos;
                    isStraight1 = false;
                    reverseT1 = (deltaForward < 0);
                    tStart1 = getTBounds(xStart, h1, zStart, k1, r1);
                    tEnd1 = getTBounds(deltaXEnd, h1, deltaZEnd, k1, r1, tStart1, reverseT1);
                    h2 = newFacingEnd.cos;
                    k2 = newFacingEnd.sin;
                    if (Math.abs(h2) >= 0.5 && Math.abs(k2) >= 0.5) {
                        r2 = (h2 * deltaZEnd - k2 * deltaXEnd) / h2 / h2;
                        tStart2 = deltaXEnd / h2;
                        tEnd2 = xEnd / h2;
                    } else {
                        final double div = newFacingEnd.add(newFacingEnd).cos;
                        r2 = (h2 * deltaZEnd - k2 * deltaXEnd) / div;
                        tStart2 = (h2 * deltaXEnd - k2 * deltaZEnd) / div;
                        tEnd2 = (h2 * xEnd - k2 * zEnd) / div;
                    }
                    isStraight2 = true;
                    reverseT2 = tStart2 > tEnd2;
                } else { // Out of available range
                    h1 = k1 = h2 = k2 = r1 = r2 = 0;
                    tStart1 = tStart2 = tEnd1 = tEnd2 = 0;
                    reverseT1 = false;
                    reverseT2 = false;
                    isStraight1 = isStraight2 = true;
                }
            } else {
                h1 = k1 = h2 = k2 = r1 = r2 = 0;
                tStart1 = tStart2 = tEnd1 = tEnd2 = 0;
                reverseT1 = false;
                reverseT2 = false;
                isStraight1 = isStraight2 = true;
            }
        }
    }

    public RigidCatenary(Map<String, Value> map) {
        final MessagePackHelper messagePackHelper = new MessagePackHelper(map);
        h1 = messagePackHelper.getDouble(KEY_H_1);
        k1 = messagePackHelper.getDouble(KEY_K_1);
        h2 = messagePackHelper.getDouble(KEY_H_2);
        k2 = messagePackHelper.getDouble(KEY_K_2);
        r1 = messagePackHelper.getDouble(KEY_R_1);
        r2 = messagePackHelper.getDouble(KEY_R_2);
        tStart1 = messagePackHelper.getDouble(KEY_T_START_1);
        tEnd1 = messagePackHelper.getDouble(KEY_T_END_1);
        tStart2 = messagePackHelper.getDouble(KEY_T_START_2);
        tEnd2 = messagePackHelper.getDouble(KEY_T_END_2);
        yStart = messagePackHelper.getInt(KEY_Y_START);
        yEnd = messagePackHelper.getInt(KEY_Y_END);
        reverseT1 = messagePackHelper.getBoolean(KEY_REVERSE_T_1);
        isStraight1 = messagePackHelper.getBoolean(KEY_IS_STRAIGHT_1);
        reverseT2 = messagePackHelper.getBoolean(KEY_REVERSE_T_2);
        isStraight2 = messagePackHelper.getBoolean(KEY_IS_STRAIGHT_2);
        catenaryType = EnumHelper.valueOf(CatenaryType.RIGID_CATENARY, messagePackHelper.getString(KEY_CATENARY_TYPE));
        facingStart = getRigidCatenaryAngle(false);
        facingEnd = getRigidCatenaryAngle(true);
    }

    @Deprecated
    public RigidCatenary(CompoundTag compoundTag) {
        h1 = compoundTag.getDouble(KEY_H_1);
        k1 = compoundTag.getDouble(KEY_K_1);
        h2 = compoundTag.getDouble(KEY_H_2);
        k2 = compoundTag.getDouble(KEY_K_2);
        r1 = compoundTag.getDouble(KEY_R_1);
        r2 = compoundTag.getDouble(KEY_R_2);
        tStart1 = compoundTag.getDouble(KEY_T_START_1);
        tEnd1 = compoundTag.getDouble(KEY_T_END_1);
        tStart2 = compoundTag.getDouble(KEY_T_START_2);
        tEnd2 = compoundTag.getDouble(KEY_T_END_2);
        yStart = compoundTag.getInt(KEY_Y_START);
        yEnd = compoundTag.getInt(KEY_Y_END);
        reverseT1 = compoundTag.getBoolean(KEY_REVERSE_T_1);
        isStraight1 = compoundTag.getBoolean(KEY_IS_STRAIGHT_1);
        reverseT2 = compoundTag.getBoolean(KEY_REVERSE_T_2);
        isStraight2 = compoundTag.getBoolean(KEY_IS_STRAIGHT_2);
        catenaryType = EnumHelper.valueOf(CatenaryType.RIGID_CATENARY, compoundTag.getString(KEY_CATENARY_TYPE));
        facingStart = getRigidCatenaryAngle(false);
        facingEnd = getRigidCatenaryAngle(true);
    }

    public RigidCatenary(FriendlyByteBuf packet) {
        h1 = packet.readDouble();
        k1 = packet.readDouble();
        h2 = packet.readDouble();
        k2 = packet.readDouble();
        r1 = packet.readDouble();
        r2 = packet.readDouble();
        tStart1 = packet.readDouble();
        tEnd1 = packet.readDouble();
        tStart2 = packet.readDouble();
        tEnd2 = packet.readDouble();
        yStart = packet.readInt();
        yEnd = packet.readInt();
        reverseT1 = packet.readBoolean();
        isStraight1 = packet.readBoolean();
        reverseT2 = packet.readBoolean();
        isStraight2 = packet.readBoolean();
        catenaryType = EnumHelper.valueOf(CatenaryType.RIGID_CATENARY, packet.readUtf(PACKET_STRING_READ_LENGTH));
        facingStart = getRigidCatenaryAngle(false);
        facingEnd = getRigidCatenaryAngle(true);
    }

    @Override
    public void toMessagePack(MessagePacker messagePacker) throws IOException {
        messagePacker.packString(KEY_H_1).packDouble(h1);
        messagePacker.packString(KEY_K_1).packDouble(k1);
        messagePacker.packString(KEY_H_2).packDouble(h2);
        messagePacker.packString(KEY_K_2).packDouble(k2);
        messagePacker.packString(KEY_R_1).packDouble(r1);
        messagePacker.packString(KEY_R_2).packDouble(r2);
        messagePacker.packString(KEY_T_START_1).packDouble(tStart1);
        messagePacker.packString(KEY_T_END_1).packDouble(tEnd1);
        messagePacker.packString(KEY_T_START_2).packDouble(tStart2);
        messagePacker.packString(KEY_T_END_2).packDouble(tEnd2);
        messagePacker.packString(KEY_Y_START).packInt(yStart);
        messagePacker.packString(KEY_Y_END).packInt(yEnd);
        messagePacker.packString(KEY_REVERSE_T_1).packBoolean(reverseT1);
        messagePacker.packString(KEY_IS_STRAIGHT_1).packBoolean(isStraight1);
        messagePacker.packString(KEY_REVERSE_T_2).packBoolean(reverseT2);
        messagePacker.packString(KEY_IS_STRAIGHT_2).packBoolean(isStraight2);
        messagePacker.packString(KEY_CATENARY_TYPE).packString(catenaryType.toString());
    }

    @Override
    public int messagePackLength() {
        return 17;
    }

    @Override
    public void writePacket(FriendlyByteBuf packet) {
        packet.writeDouble(h1);
        packet.writeDouble(k1);
        packet.writeDouble(h2);
        packet.writeDouble(k2);
        packet.writeDouble(r1);
        packet.writeDouble(r2);
        packet.writeDouble(tStart1);
        packet.writeDouble(tEnd1);
        packet.writeDouble(tStart2);
        packet.writeDouble(tEnd2);
        packet.writeInt(yStart);
        packet.writeInt(yEnd);
        packet.writeBoolean(reverseT1);
        packet.writeBoolean(isStraight1);
        packet.writeBoolean(reverseT2);
        packet.writeBoolean(isStraight2);
        packet.writeUtf(catenaryType.toString());
    }

    public Vec3 getPosition(double rawValue) {
        final double count1 = Math.abs(tEnd1 - tStart1);
        final double count2 = Math.abs(tEnd2 - tStart2);
        final double value = Mth.clamp(rawValue, 0, count1 + count2);
        final double y = getPositionY(value);

        if (value <= count1) {
            return getPositionXZ(h1, k1, r1, (reverseT1 ? -1 : 1) * value + tStart1, 0, isStraight1).add(0, y, 0);
        } else {
            return getPositionXZ(h2, k2, r2, (reverseT2 ? -1 : 1) * (value - count1) + tStart2, 0, isStraight2).add(0, y, 0);
        }
    }

    public double getLength() {
        return Math.abs(tEnd2 - tStart2) + Math.abs(tEnd1 - tStart1);
    }

    public void render(RenderRigidCatenary callback) {
        renderSegment(h1, k1, r1, tStart1, tEnd1, 0, reverseT1, isStraight1, callback);
        renderSegment(h2, k2, r2, tStart2, tEnd2, Math.abs(tEnd1 - tStart1), reverseT2, isStraight2, callback);
    }

    private void renderSegment(double h, double k, double r, double tStart, double tEnd, double rawValueOffset, boolean reverseT, boolean isStraight, RenderRigidCatenary callback) {
        final int SEGMENT_LENGTH = Config.getRigidCatenarySegmentLength();
        final double count = Math.abs(tEnd - tStart);
        final double segment_count = Math.round(count / SEGMENT_LENGTH);
        final double increment = count / Math.max(1, segment_count);
        for (double i = 0; i < count - 0.1; i += increment) {
            final double t1 = (reverseT ? -1 : 1) * i + tStart;
            final double t2 = (reverseT ? -1 : 1) * (i + increment) + tStart;
            final Vec3 corner1 = getPositionXZ(h, k, r, t1, -0.015625F, isStraight);
            final Vec3 corner2 = getPositionXZ(h, k, r, t1, 0.015625F, isStraight);
            final Vec3 corner3 = getPositionXZ(h, k, r, t2, 0.015625F, isStraight);
            final Vec3 corner4 = getPositionXZ(h, k, r, t2, -0.015625F, isStraight);
            final Vec3 corner5 = getPositionXZ(h, k, r, t1, -0.09375F, isStraight);
            final Vec3 corner6 = getPositionXZ(h, k, r, t1, 0.09375F, isStraight);
            final Vec3 corner7 = getPositionXZ(h, k, r, t2, 0.09375F, isStraight);
            final Vec3 corner8 = getPositionXZ(h, k, r, t2, -0.09375F, isStraight);
            final double y1 = getPositionY(i + rawValueOffset);
            final double y2 = getPositionY(i + increment + rawValueOffset);
            callback.renderRigidCatenary(corner1.x, corner1.z, corner2.x, corner2.z, corner3.x, corner3.z, corner4.x, corner4.z, corner5.x, corner5.z, corner6.x, corner6.z, corner7.x, corner7.z, corner8.x, corner8.z, y1, y2);
        }
    }

    public boolean goodRadius() {
        return (isStraight1 || r1 > MIN_RADIUS - ACCEPT_THRESHOLD) && (isStraight2 || r2 > MIN_RADIUS - ACCEPT_THRESHOLD);
    }

    public boolean isValid() {
        return (h1 != 0 || k1 != 0 || h2 != 0 || k2 != 0 || r1 != 0 || r2 != 0 || tStart1 != 0 || tStart2 != 0 || tEnd1 != 0 || tEnd2 != 0) && facingStart == getRigidCatenaryAngle(false) && facingEnd == getRigidCatenaryAngle(true);
    }

    private static Vec3 getPositionXZ(double h, double k, double r, double t, double radiusOffset, boolean isStraight) {
        if (isStraight) {
            return new Vec3(h * t + k * ((Math.abs(h) >= 0.5 && Math.abs(k) >= 0.5 ? 0 : r) + radiusOffset) + 0.5, 0, k * t + h * (r - radiusOffset) + 0.5);
        } else {
            return new Vec3(h + (r + radiusOffset) * Math.cos(t / r) + 0.5, 0, k + (r + radiusOffset) * Math.sin(t / r) + 0.5);
        }
    }

    private double getPositionY(double value) {
        final double length = getLength();
        final double intercept = length / 2;
        final double yChange;
        final double yInitial;
        final double offsetValue;
        if (value < intercept) {
            yChange = (yEnd - yStart) / 2D;
            yInitial = yStart;
            offsetValue = value;
        } else {
            yChange = (yStart - yEnd) / 2D;
            yInitial = yEnd;
            offsetValue = length - value;
        }
        return yChange * offsetValue * offsetValue / (intercept * intercept) + yInitial;
    }


    private RailAngle getRigidCatenaryAngle(boolean getEnd) {
        final double start;
        final double end;
        if (getEnd) {
            start = getLength();
            end = start - ACCEPT_THRESHOLD;
        } else {
            start = 0;
            end = ACCEPT_THRESHOLD;
        }
        final Vec3 pos1 = getPosition(start);
        final Vec3 pos2 = getPosition(end);
        return RailAngle.fromAngle((float) Math.toDegrees(Math.atan2(pos2.z - pos1.z, pos2.x - pos1.x)));
    }


    private static double getTBounds(double x, double h, double z, double k, double r) {
        return Mth.atan2(z - k, x - h) * r;
    }

    private static double getTBounds(double x, double h, double z, double k, double r, double tStart, boolean reverse) {
        final double t = getTBounds(x, h, z, k, r);
        if (t < tStart && !reverse) {
            return t + 2 * Math.PI * r;
        } else if (t > tStart && reverse) {
            return t - 2 * Math.PI * r;
        } else {
            return t;
        }
    }

    @FunctionalInterface
    public interface RenderRigidCatenary {
        void renderRigidCatenary(double x1, double z1, double x2, double z2, double x3, double z3, double x4, double z4, double xs1, double zs1, double xs2, double zs2, double xs3, double zs3, double xs4, double zs4, double y1, double y2);
    }
}