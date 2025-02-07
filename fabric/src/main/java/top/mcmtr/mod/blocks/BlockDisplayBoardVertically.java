package top.mcmtr.mod.blocks;

import org.mtr.mapping.holder.*;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.IBlock;

public final class BlockDisplayBoardVertically extends BlockChangeModelBase {

    public BlockDisplayBoardVertically() {
        super(Blocks.createDefaultBlockSettings(true, blockState -> 5), 6);
    }

    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape1 = IBlock.getVoxelShapeByDirection(3, 0, 4, 13, 8, 12, IBlock.getStatePropertySafe(state, FACING));
        VoxelShape shape2 = IBlock.getVoxelShapeByDirection(3, 8, 4, 13, 16, 10, IBlock.getStatePropertySafe(state, FACING));
        return VoxelShapes.union(shape1, shape2);
    }
}