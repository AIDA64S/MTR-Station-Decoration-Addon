package top.mcmtr.data;

import mtr.data.IGui;
import mtr.data.TransportMode;
import net.minecraft.world.level.material.MaterialColor;

import static mtr.data.RailType.CABLE_CAR_STATION;
import static mtr.data.RailType.WOODEN;

public enum MSDRailType implements IGui {
    VIADUCT(60, MaterialColor.COLOR_BROWN, false, true, true, RailSlopeStyle.CURVE);
    public final int speedLimit;
    public final float maxBlocksPerTick;
    public final int color;
    public final boolean hasSavedRail;
    public final boolean canAccelerate;
    public final boolean hasSignal;
    public final RailSlopeStyle railSlopeStyle;

    MSDRailType(int speedLimit, MaterialColor MaterialColor, boolean hasSavedRail, boolean canAccelerate, boolean hasSignal, RailSlopeStyle railSlopeStyle) {
        this.speedLimit = speedLimit;
        maxBlocksPerTick = speedLimit / 3.6F / 20;
        color = MaterialColor.col | ARGB_BLACK;
        this.hasSavedRail = hasSavedRail;
        this.canAccelerate = canAccelerate;
        this.hasSignal = hasSignal;
        this.railSlopeStyle = railSlopeStyle;
    }

    public static float getDefaultMaxBlocksPerTick(TransportMode transportMode) {
        return (transportMode.continuousMovement ? CABLE_CAR_STATION : WOODEN).maxBlocksPerTick;
    }

    public enum RailSlopeStyle {CURVE, CABLE}
}
