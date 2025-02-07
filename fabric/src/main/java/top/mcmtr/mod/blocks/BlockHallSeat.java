package top.mcmtr.mod.blocks;

import org.mtr.mapping.holder.*;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.IBlock;

public final class BlockHallSeat extends BlockChangeModelBase {
    public BlockHallSeat() {
        super(Blocks.createDefaultBlockSettings(false), 3);
    }

    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape1 = IBlock.getVoxelShapeByDirection(0, 6, 0, 16, 8, 16, IBlock.getStatePropertySafe(state, FACING));
        VoxelShape shape2 = IBlock.getVoxelShapeByDirection(0, 8, 0, 16, 16, 5, IBlock.getStatePropertySafe(state, FACING));
        return VoxelShapes.union(shape1, shape2);
    }
}