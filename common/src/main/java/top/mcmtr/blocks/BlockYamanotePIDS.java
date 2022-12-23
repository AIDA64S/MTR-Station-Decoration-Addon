package top.mcmtr.blocks;

import mtr.block.BlockPIDSBase;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import top.mcmtr.MSDBlockEntityTypes;

public class BlockYamanotePIDS extends BlockPIDSBase {
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
        VoxelShape shape1 = IBlock.getVoxelShapeByDirection(7, 8, 0, 9, 16, 16, IBlock.getStatePropertySafe(state, FACING));
        return shape1;
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityPIDS(pos, state);
    }

    public static class TileEntityPIDS extends TileEntityBlockPIDSBase {

        public TileEntityPIDS(BlockPos pos, BlockState state) {
            super(MSDBlockEntityTypes.YAMANOTE_PIDS_TILE_ENTITY.get(), pos, state);
        }

        @Override
        protected int getMaxArrivals() {
            return 3;
        }
    }

}
