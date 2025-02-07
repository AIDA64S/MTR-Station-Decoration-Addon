package top.mcmtr.mod.blocks;

import org.mtr.mapping.holder.*;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.IBlock;

public final class BlockStandingSignPole extends BlockChangeModelBase {

    public BlockStandingSignPole() {
        super(Blocks.createDefaultBlockSettings(true), 2);
    }

    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(7.375, 0, 7.375, 8.625, 16, 8.625, IBlock.getStatePropertySafe(state, FACING));
    }
}