package top.mcmtr.mod.blocks;

import org.mtr.mapping.holder.*;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.IBlock;

public final class BlockDecorationPC extends BlockChangeModelBase {

    public BlockDecorationPC() {
        super(Blocks.createDefaultBlockSettings(true), 1);
    }

    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 16, IBlock.getStatePropertySafe(state, FACING));
    }

    @Override
    public VoxelShape getCollisionShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }
}