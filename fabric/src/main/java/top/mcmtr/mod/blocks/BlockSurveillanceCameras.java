package top.mcmtr.mod.blocks;

import org.mtr.mapping.holder.*;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.IBlock;

public final class BlockSurveillanceCameras extends BlockChangeModelBase {
    public BlockSurveillanceCameras() {
        super(Blocks.createDefaultBlockSettings(true, blockState -> 5), 2);
    }

    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(6.5, 6.5, 0, 9.5, 16, 13, IBlock.getStatePropertySafe(state, FACING));
    }
}