package top.mcmtr.data;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Map;

public abstract class RigidCatenaryModuleBase {
    protected final RigidCatenaryData rigidCatenaryData;
    protected final Level world;
    protected final Map<BlockPos, Map<BlockPos, RigidCatenary>> rigidCatenaries;

    public RigidCatenaryModuleBase(RigidCatenaryData rigidCatenaryData, Level world, Map<BlockPos, Map<BlockPos, RigidCatenary>> rigidCatenaries) {
        this.rigidCatenaryData = rigidCatenaryData;
        this.world = world;
        this.rigidCatenaries = rigidCatenaries;
    }
}
