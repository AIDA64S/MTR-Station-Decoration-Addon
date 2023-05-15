package top.mcmtr.data;

import mtr.data.EnumHelper;
import mtr.data.MessagePackHelper;
import mtr.data.SerializedDataBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransCatenary extends SerializedDataBase {
    private final double xStart, yStart, zStart, xEnd, yEnd, zEnd;
    private final double count, increment, increment2, sinX, sinZ;
    private final List<VecLocation> corner = new ArrayList<>();
    private static final double ELECTRIC_CURVATURE_SCALE = 300;
    private static final double MAX_ELECTRIC_DIP = 8;
    public final CatenaryType catenaryType;
    private static final String KEY_X_START = "t_c_x_start";
    private static final String KEY_Y_START = "t_c_y_start";
    private static final String KEY_Z_START = "t_c_z_start";
    private static final String KEY_X_END = "t_c_x_end";
    private static final String KEY_Y_END = "t_c_y_end";
    private static final String KEY_Z_END = "t_c_z_end";
    private static final String KEY_CATENARY_TYPE = "t_catenary_type";

    public TransCatenary(BlockPos posStart, BlockPos posEnd, BlockLocation startLocation, BlockLocation endLocation, CatenaryType catenaryType) {
        this.xStart = posStart.getX() + startLocation.getX();
        this.yStart = posStart.getY() + startLocation.getY();
        this.zStart = posStart.getZ() + startLocation.getZ();
        this.xEnd = posEnd.getX() + endLocation.getX();
        this.yEnd = posEnd.getY() + endLocation.getY();
        this.zEnd = posEnd.getZ() + endLocation.getZ();
        this.catenaryType = catenaryType;
        this.count = getLength();
        this.increment = count / Math.round(count / 2);
        this.increment2 = increment - 0.5;
        this.sinX = getSin(zStart, zEnd, 0.015625, count);
        this.sinZ = getSin(xStart, xEnd, 0.015625, count);
        for (double i = 0; i < count - 0.1; i += increment) {
            final VecLocation cornerTemp = new VecLocation(getPositionXYZ(i, xStart, xEnd) + 0.5, getPositionY(i, yStart, yEnd, catenaryType), getPositionXYZ(i, zStart, zEnd) + 0.5,
                    getPositionXYZ(i + increment, xStart, xEnd) + 0.5, getPositionY(i + increment, yStart, yEnd, catenaryType), getPositionXYZ(i + increment, zStart, zEnd) + 0.5, i);
            corner.add(cornerTemp);
        }
    }

    public TransCatenary(Map<String, Value> map) {
        final MessagePackHelper messagePackHelper = new MessagePackHelper(map);
        this.xStart = messagePackHelper.getDouble(KEY_X_START);
        this.yStart = messagePackHelper.getDouble(KEY_Y_START);
        this.zStart = messagePackHelper.getDouble(KEY_Z_START);
        this.xEnd = messagePackHelper.getDouble(KEY_X_END);
        this.yEnd = messagePackHelper.getDouble(KEY_Y_END);
        this.zEnd = messagePackHelper.getDouble(KEY_Z_END);
        this.catenaryType = EnumHelper.valueOf(CatenaryType.CATENARY, messagePackHelper.getString(KEY_CATENARY_TYPE));
        this.count = getLength();
        this.increment = count / Math.round(count / 2);
        this.increment2 = increment - 0.5;
        this.sinX = getSin(zStart, zEnd, 0.015625, count);
        this.sinZ = getSin(xStart, xEnd, 0.015625, count);
        for (double i = 0; i < count - 0.1; i += increment) {
            final VecLocation cornerTemp = new VecLocation(getPositionXYZ(i, xStart, xEnd) + 0.5, getPositionXYZ(i, yStart, yEnd), getPositionXYZ(i, zStart, zEnd) + 0.5,
                    getPositionXYZ(i + increment, xStart, xEnd) + 0.5, getPositionXYZ(i + increment, yStart, yEnd), getPositionXYZ(i + increment, zStart, zEnd) + 0.5, i);
            corner.add(cornerTemp);
        }
    }

    public TransCatenary(FriendlyByteBuf packet) {
        this.xStart = packet.readDouble();
        this.yStart = packet.readDouble();
        this.zStart = packet.readDouble();
        this.xEnd = packet.readDouble();
        this.yEnd = packet.readDouble();
        this.zEnd = packet.readDouble();
        this.catenaryType = EnumHelper.valueOf(CatenaryType.CATENARY, packet.readUtf(PACKET_STRING_READ_LENGTH));
        this.count = getLength();
        this.increment = count / Math.round(count / 2);
        this.increment2 = increment - 0.5;
        this.sinX = getSin(zStart, zEnd, 0.015625, count);
        this.sinZ = getSin(xStart, xEnd, 0.015625, count);
        for (double i = 0; i < count - 0.1; i += increment) {
            final VecLocation cornerTemp = new VecLocation(getPositionXYZ(i, xStart, xEnd) + 0.5, getPositionXYZ(i, yStart, yEnd), getPositionXYZ(i, zStart, zEnd) + 0.5,
                    getPositionXYZ(i + increment, xStart, xEnd) + 0.5, getPositionXYZ(i + increment, yStart, yEnd), getPositionXYZ(i + increment, zStart, zEnd) + 0.5, i);
            corner.add(cornerTemp);
        }
    }

    @Override
    public void toMessagePack(MessagePacker messagePacker) throws IOException {
        messagePacker.packString(KEY_X_START).packDouble(xStart);
        messagePacker.packString(KEY_Y_START).packDouble(yStart);
        messagePacker.packString(KEY_Z_START).packDouble(zStart);
        messagePacker.packString(KEY_X_END).packDouble(xEnd);
        messagePacker.packString(KEY_Y_END).packDouble(yEnd);
        messagePacker.packString(KEY_Z_END).packDouble(zEnd);
        messagePacker.packString(KEY_CATENARY_TYPE).packString(catenaryType.toString());
    }

    @Override
    public int messagePackLength() {
        return 7;
    }

    @Override
    public void writePacket(FriendlyByteBuf packet) {
        packet.writeDouble(xStart);
        packet.writeDouble(yStart);
        packet.writeDouble(zStart);
        packet.writeDouble(xEnd);
        packet.writeDouble(yEnd);
        packet.writeDouble(zEnd);
        packet.writeUtf(catenaryType.toString());
    }

    public void render(RenderTransCatenary callback) {
        renderSegment(callback);
    }

    private void renderSegment(RenderTransCatenary callback) {
        double base = 0.6;
        if (count < 8) {
            for (VecLocation cornerResult : corner) {
                callback.renderTransCatenary(cornerResult.x1, cornerResult.y1, cornerResult.z1, cornerResult.x2, cornerResult.y2, cornerResult.z2, count, cornerResult.i, base, sinX, sinZ, increment2);
            }
        } else {
            for (VecLocation cornerResult : corner) {
                callback.renderTransCatenary(cornerResult.x1, cornerResult.y1, cornerResult.z1, cornerResult.x2, cornerResult.y2, cornerResult.z2, count, cornerResult.i, base, sinX, sinZ, increment2);
                if (cornerResult.i < (count / 2 - increment2)) {
                    base *= 0.5;
                } else if (cornerResult.i >= (count / 2)) {
                    base /= 0.5;
                }
            }
        }
    }

    private double getPositionXYZ(double value, double start, double end) {
        final double length = count;
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

    private double getPositionY(double value, double start, double end, CatenaryType catenaryType) {
        final double length = getLength();
        if (catenaryType == CatenaryType.TRANS_ELECTRIC) {
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

    private double getLength() {
        return Math.sqrt(((xEnd - xStart) * (xEnd - xStart)) + ((yEnd - yStart) * (yEnd - yStart)) + ((zEnd - zStart) * (zEnd - zStart)));
    }

    private double getSin(double start, double end, double offset, double length) {
        return ((end - start) / length) * offset;
    }

    @FunctionalInterface
    public interface RenderTransCatenary {
        void renderTransCatenary(double x1, double y1, double z1, double x2, double y2, double z2, double count, double i, double base, double sinX, double sinY, double increment);
    }
}