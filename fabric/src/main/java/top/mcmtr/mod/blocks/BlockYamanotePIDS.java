package top.mcmtr.mod.blocks;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.block.BlockPIDSHorizontalBase;
import org.mtr.mod.block.IBlock;
import top.mcmtr.mod.BlockEntityTypes;

public class BlockYamanotePIDS extends BlockPIDSHorizontalBase {
    private static final int MAX_ARRIVALS = 3;
    private final int length;

    public BlockYamanotePIDS(int length) {
        super(MAX_ARRIVALS);
        this.length = length;
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockYamanotePIDSEntity(length, blockPos, blockState);
    }

    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final VoxelShape shape1;
        final VoxelShape shape2;
        switch (length) {
            case 5:
                shape1 = IBlock.getVoxelShapeByDirection(7, 8, 0, 9, 16, 21, IBlock.getStatePropertySafe(state, FACING));
                shape2 = IBlock.getVoxelShapeByDirection(7.5, 8, 21, 8.5, 16, 22, IBlock.getStatePropertySafe(state, FACING));
                return VoxelShapes.union(shape1, shape2);
            case 6:
                shape1 = IBlock.getVoxelShapeByDirection(7, 8, 0, 9, 16, 25, IBlock.getStatePropertySafe(state, FACING));
                shape2 = IBlock.getVoxelShapeByDirection(7.5, 8, 25, 8.5, 16, 26, IBlock.getStatePropertySafe(state, FACING));
                return VoxelShapes.union(shape1, shape2);
            case 7:
                shape1 = IBlock.getVoxelShapeByDirection(7, 8, 0, 9, 16, 29, IBlock.getStatePropertySafe(state, FACING));
                shape2 = IBlock.getVoxelShapeByDirection(7.5, 8, 29, 8.5, 16, 30, IBlock.getStatePropertySafe(state, FACING));
                return VoxelShapes.union(shape1, shape2);
            default:
                shape1 = IBlock.getVoxelShapeByDirection(7, 8, 0, 9, 16, 17, IBlock.getStatePropertySafe(state, FACING));
                shape2 = IBlock.getVoxelShapeByDirection(7.5, 8, 17, 8.5, 16, 18, IBlock.getStatePropertySafe(state, FACING));
                return VoxelShapes.union(shape1, shape2);
        }
    }

    @Override
    public String getTranslationKey2() {
        return "block.msd.yamanote_pids";
    }

    public static class BlockYamanotePIDSEntity extends BlockEntityHorizontalBase {

        public BlockYamanotePIDSEntity(int length, BlockPos pos, BlockState state) {
            super(MAX_ARRIVALS, getEntity(length), pos, state);
        }

        @Override
        public String defaultFormat(int line) {
            return "@0-60L@$#FF9900$%destination*%@60-100R@%RAH*%:%RA0m*%:%RA0s*%".replace("*", String.valueOf(line + 1));
        }

        public static BlockEntityType<? extends BlockEntityExtension> getEntity(int length) {
            switch (length) {
                case 5:
                    return BlockEntityTypes.YAMANOTE_5_PIDS.get();
                case 6:
                    return BlockEntityTypes.YAMANOTE_6_PIDS.get();
                case 7:
                    return BlockEntityTypes.YAMANOTE_7_PIDS.get();
                default:
                    return BlockEntityTypes.YAMANOTE_4_PIDS.get();
            }
        }
    }
}