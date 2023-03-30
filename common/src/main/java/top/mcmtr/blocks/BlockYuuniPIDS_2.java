package top.mcmtr.blocks;

import mtr.block.BlockPIDSBase;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import top.mcmtr.MSDBlockEntityTypes;

public class BlockYuuniPIDS_2 extends BlockPIDSBase {
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape shape1 = IBlock.getVoxelShapeByDirection(5.75, 4.95, 0, 10.25, 9.6, 13.7, IBlock.getStatePropertySafe(blockState, FACING));
        VoxelShape shape2 = IBlock.getVoxelShapeByDirection(7.75, 9.6, 8.5, 8.25, 13, 9, IBlock.getStatePropertySafe(blockState, FACING));
        VoxelShape shape3 = IBlock.getVoxelShapeByDirection(7.5, 13, 8.25, 8.25, 16, 9.25, IBlock.getStatePropertySafe(blockState, FACING));
        return Shapes.or(shape1, shape2, shape3);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TileEntityPIDS(blockPos, blockState);
    }

    public static class TileEntityPIDS extends TileEntityBlockPIDSBase {
        public TileEntityPIDS(BlockPos pos, BlockState state) {
            super(MSDBlockEntityTypes.YUUNI_PIDS_2_TILE_ENTITY.get(), pos, state);
        }

        @Override
        protected int getMaxArrivals() {
            return 1;
        }
    }
}