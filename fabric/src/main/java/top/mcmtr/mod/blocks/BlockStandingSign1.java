package top.mcmtr.mod.blocks;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.IBlock;
import top.mcmtr.mod.BlockEntityTypes;

public final class BlockStandingSign1 extends BlockCustomTextBase {
    public BlockStandingSign1() {
        super(Blocks.createDefaultBlockSettings(true, blockState -> 8), 1, 2);
    }

    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(7.6, 4.5, -1, 8.4, 10.5, 17, IBlock.getStatePropertySafe(state, FACING));
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockStandingSign1Entity(blockPos, blockState, getMaxArrivals());
    }


    public static final class BlockStandingSign1Entity extends BlockCustomTextEntity {

        public BlockStandingSign1Entity(BlockPos blockPos, BlockState blockState, int maxArrivals) {
            super(BlockEntityTypes.STANDING_SIGN_1.get(), blockPos, blockState, maxArrivals);
        }
    }
}
