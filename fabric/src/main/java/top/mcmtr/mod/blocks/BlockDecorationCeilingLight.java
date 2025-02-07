package top.mcmtr.mod.blocks;

import org.mtr.mapping.holder.*;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.IBlock;

public final class BlockDecorationCeilingLight extends BlockChangeModelBase {
    public BlockDecorationCeilingLight() {
        super(Blocks.createDefaultBlockSettings(true, blockState -> 15).nonOpaque(), 1);
    }

    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(0.0, 7.0, 0.0, 16.0, 10.0, 16.0, IBlock.getStatePropertySafe(state, FACING));
    }
}