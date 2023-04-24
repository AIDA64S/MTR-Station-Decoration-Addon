package top.mcmtr.data;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Map;

public abstract class CatenaryDataModuleBase {
    protected final CatenaryData catenaryData;
    protected final Level world;
    protected final Map<BlockPos, Map<BlockPos, Catenary>> catenaries;

    public CatenaryDataModuleBase(CatenaryData catenaryData, Level world, Map<BlockPos, Map<BlockPos, Catenary>> catenaries) {
        this.catenaryData = catenaryData;
        this.world = world;
        this.catenaries = catenaries;
    }
}