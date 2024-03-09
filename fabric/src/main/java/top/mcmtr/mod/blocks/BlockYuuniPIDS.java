package top.mcmtr.mod.blocks;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.block.BlockPIDSHorizontalBase;
import org.mtr.mod.block.IBlock;
import top.mcmtr.mod.BlockEntityTypes;

public class BlockYuuniPIDS extends BlockPIDSHorizontalBase {
    public BlockYuuniPIDS(int maxArrivals) {
        super(maxArrivals);
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockYuuniPIDSEntity(maxArrivals, blockPos, blockState);
    }

    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final VoxelShape shape1;
        final VoxelShape shape2;
        if (maxArrivals == 2) {
            shape1 = IBlock.getVoxelShapeByDirection(5.75, 4.95, 0, 10.25, 9.6, 13.7, IBlock.getStatePropertySafe(state, FACING));
            shape2 = IBlock.getVoxelShapeByDirection(7.75, 9.6, 8.5, 8.25, 13, 9, IBlock.getStatePropertySafe(state, FACING));
            return VoxelShapes.union(shape1, shape2);
        }
        shape1 = IBlock.getVoxelShapeByDirection(5.75, 0.3, 0, 10.25, 11.6, 15.7, IBlock.getStatePropertySafe(state, FACING));
        shape2 = IBlock.getVoxelShapeByDirection(7.75, 11.6, 10.5, 8.25, 16, 11, IBlock.getStatePropertySafe(state, FACING));
        return VoxelShapes.union(shape1, shape2);
    }

    @Override
    public String getTranslationKey2() {
        return "block.msd.yuuni_pids";
    }

    public static class BlockYuuniPIDSEntity extends BlockEntityHorizontalBase {
        public BlockYuuniPIDSEntity(int maxArrivals, BlockPos pos, BlockState state) {
            super(maxArrivals, getEntity(maxArrivals), pos, state);
        }

        @Override
        public String defaultFormat(int line) {
            return "@0-60L@$#FF9900$%destination*%@60-100R@%RAH*%:%RA0m*%:%RA0s*%".replace("*", String.valueOf(line + 1));
        }

        public static BlockEntityType<? extends BlockEntityExtension> getEntity(int maxArrays) {
            if (maxArrays == 2) {
                return BlockEntityTypes.YUUNI_PIDS.get();
            }
            return BlockEntityTypes.YUUNI_2_PIDS.get();
        }
    }
}
