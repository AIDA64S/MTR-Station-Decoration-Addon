package top.mcmtr.data;

import mtr.data.EnumHelper;
import mtr.data.MessagePackHelper;
import mtr.data.SerializedDataBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;

import java.io.IOException;
import java.util.Map;

public class Catenary extends SerializedDataBase {
    private final int xStart, yStart, zStart, xEnd, yEnd, zEnd;
    public final CatenaryType catenaryType;
    private static final int ELECTRIC_CURVATURE_SCALE = 300;
    private static final int MAX_ELECTRIC_DIP = 8;
    private static final String KEY_X_START = "c_x_start";
    private static final String KEY_Y_START = "c_y_start";
    private static final String KEY_Z_START = "c_z_start";
    private static final String KEY_X_END = "c_x_end";
    private static final String KEY_Y_END = "c_y_end";
    private static final String KEY_Z_END = "c_z_end";
    private static final String KEY_CATENARY_TYPE = "catenary_type";

    public Catenary(BlockPos posStart, BlockPos posEnd, CatenaryType catenaryType) {
        this.xStart = posStart.getX();
        this.yStart = posStart.getY();
        this.zStart = posStart.getZ();
        this.xEnd = posEnd.getX();
        this.yEnd = posEnd.getY();
        this.zEnd = posEnd.getZ();
        this.catenaryType = catenaryType;
    }

    public Catenary(Map<String, Value> map) {
        final MessagePackHelper messagePackHelper = new MessagePackHelper(map);
        this.xStart = messagePackHelper.getInt(KEY_X_START);
        this.yStart = messagePackHelper.getInt(KEY_Y_START);
        this.zStart = messagePackHelper.getInt(KEY_Z_START);
        this.xEnd = messagePackHelper.getInt(KEY_X_END);
        this.yEnd = messagePackHelper.getInt(KEY_Y_END);
        this.zEnd = messagePackHelper.getInt(KEY_Z_END);
        this.catenaryType = EnumHelper.valueOf(CatenaryType.CATENARY, messagePackHelper.getString(KEY_CATENARY_TYPE));
    }

    @Deprecated
    public Catenary(CompoundTag compoundTag) {
        this.xStart = compoundTag.getInt(KEY_X_START);
        this.yStart = compoundTag.getInt(KEY_Y_START);
        this.zStart = compoundTag.getInt(KEY_Z_START);
        this.xEnd = compoundTag.getInt(KEY_X_END);
        this.yEnd = compoundTag.getInt(KEY_Y_END);
        this.zEnd = compoundTag.getInt(KEY_Z_END);
        this.catenaryType = EnumHelper.valueOf(CatenaryType.CATENARY, compoundTag.getString(KEY_CATENARY_TYPE));
    }

    public Catenary(FriendlyByteBuf packet) {
        this.xStart = packet.readInt();
        this.yStart = packet.readInt();
        this.zStart = packet.readInt();
        this.xEnd = packet.readInt();
        this.yEnd = packet.readInt();
        this.zEnd = packet.readInt();
        this.catenaryType = EnumHelper.valueOf(CatenaryType.CATENARY, packet.readUtf(PACKET_STRING_READ_LENGTH));
    }

    @Override
    public void toMessagePack(MessagePacker messagePacker) throws IOException {
        messagePacker.packString(KEY_X_START).packInt(xStart);
        messagePacker.packString(KEY_Y_START).packInt(yStart);
        messagePacker.packString(KEY_Z_START).packInt(zStart);
        messagePacker.packString(KEY_X_END).packInt(xEnd);
        messagePacker.packString(KEY_Y_END).packInt(yEnd);
        messagePacker.packString(KEY_Z_END).packInt(zEnd);
        messagePacker.packString(KEY_CATENARY_TYPE).packString(catenaryType.toString());
    }

    @Override
    public int messagePackLength() {
        return 7;
    }

    @Override
    public void writePacket(FriendlyByteBuf packet) {
        packet.writeInt(xStart);
        packet.writeInt(yStart);
        packet.writeInt(zStart);
        packet.writeInt(xEnd);
        packet.writeInt(yEnd);
        packet.writeInt(zEnd);
        packet.writeUtf(catenaryType.toString());
    }

    public void render(RenderCatenary callback) {
        renderSegment(xStart, yStart, zStart, xEnd, yEnd, zEnd, catenaryType, callback);
    }

    private void renderSegment(int xStart, int yStart, int zStart, int xEnd, int yEnd, int zEnd, CatenaryType catenaryType, RenderCatenary callback) {
        final double count = getLength();
        final double increment = count / Math.round(count / 2);
        final double increment2 = increment - 0.1F;
        double base = 0.6;
        final double sinX = getSin(zStart, zEnd, 0.015625, count);
        final double sinZ = getSin(xStart, xEnd, 0.015625, count);
        if (count < 8) {
            for (double i = 0; i < count - 0.1; i += increment) {
                final Vec3 corner1 = new Vec3(getPositionXZ(i, xStart, xEnd) + 0.5F, getPositionY(i, yStart, yEnd, catenaryType) - 0.3125, getPositionXZ(i, zStart, zEnd) + 0.5F);
                final Vec3 corner2 = new Vec3(getPositionXZ(i + increment, xStart, xEnd) + 0.5F, getPositionY(i + increment, yStart, yEnd, catenaryType) - 0.3125, getPositionXZ(i + increment, zStart, zEnd) + 0.5F);
                callback.renderCatenary(corner1.x, corner1.y, corner1.z, corner2.x, corner2.y, corner2.z, count, i, base, sinX, sinZ, increment2);
            }
        } else {
            for (double i = 0; i < count - 0.1; i += increment) {
                final Vec3 corner1 = new Vec3(getPositionXZ(i, xStart, xEnd) + 0.5F, getPositionY(i, yStart, yEnd, catenaryType) - 0.3125, getPositionXZ(i, zStart, zEnd) + 0.5F);
                final Vec3 corner2 = new Vec3(getPositionXZ(i + increment, xStart, xEnd) + 0.5F, getPositionY(i + increment, yStart, yEnd, catenaryType) - 0.3125, getPositionXZ(i + increment, zStart, zEnd) + 0.5F);
                callback.renderCatenary(corner1.x, corner1.y, corner1.z, corner2.x, corner2.y, corner2.z, count, i, base, sinX, sinZ, increment2);
                if (i < (count / 2 - increment2)) {
                    base *= 0.5;
                } else if (i >= (count / 2)) {
                    base /= 0.5;
                }
            }
        }
    }

    private double getPositionXZ(double value, int start, int end) {
        final double length = getLength();
        final double intercept = length / 2;
        final double change;
        final double initial;
        final double offsetValue;
        if (value < intercept) {
            change = (end - start) / 2D;
            initial = start;
            offsetValue = value;
        } else {
            change = (start - end) / 2D;
            initial = end;
            offsetValue = length - value;
        }
        return change / intercept * offsetValue + initial;
    }

    private double getPositionY(double value, int start, int end, CatenaryType catenaryType) {
        final double length = getLength();
        if (catenaryType == CatenaryType.ELECTRIC) {
            if (value < 0.5) {
                return start;
            } else if (value > length - 0.5) {
                return end;
            }
            final double offsetValue = value - 0.5;
            final double offsetLength = length - 1;
            final double posY = start + (end - start) * offsetValue / offsetLength;
            final double dip = offsetLength * offsetLength / 4 / ELECTRIC_CURVATURE_SCALE;
            return posY + (dip > MAX_ELECTRIC_DIP ? MAX_ELECTRIC_DIP / dip : 1) * (offsetValue - offsetLength) * offsetValue / ELECTRIC_CURVATURE_SCALE;
        } else {
            final double intercept = length / 2;
            final double change;
            final double initial;
            final double offsetValue;
            if (value < intercept) {
                change = (end - start) / 2D;
                initial = start;
                offsetValue = value;
            } else {
                change = (start - end) / 2D;
                initial = end;
                offsetValue = length - value;
            }
            return change / intercept * offsetValue + initial;
        }
    }

    public double getLength() {
        return Math.sqrt(((xEnd - xStart) * (xEnd - xStart)) + ((yEnd - yStart) * (yEnd - yStart)) + ((zEnd - zStart) * (zEnd - zStart)));
    }

    private double getSin(double start, double end, double offset, double length) {
        return ((end - start) / length) * offset;
    }

    @FunctionalInterface
    public interface RenderCatenary {
        void renderCatenary(double x1, double y1, double z1, double x2, double y2, double z2, double count, double i, double base, double sinX, double sinY, double incre);
    }
}