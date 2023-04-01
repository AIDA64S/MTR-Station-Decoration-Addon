package top.mcmtr.blocks;

import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import top.mcmtr.MSDBlockEntityTypes;

public class BlockStandingSign1 extends BlockCustomTextSignBase {
    public BlockStandingSign1() {
        super(1);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Block.box(0, 2.5, 7.6, 16, 8.5, 8.4);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TileEntityBlockStandingSign1(blockPos, blockState);
    }

    public static class TileEntityBlockStandingSign1 extends TileEntityBlockCustomTextSignBase {
        public static final int MAX_ARRIVALS = 1;

        public TileEntityBlockStandingSign1(BlockPos pos, BlockState state) {
            super(MSDBlockEntityTypes.STANDING_SIGN_1_TILE_ENTITY.get(), pos, state);
        }

        @Override
        public int getMaxArrivals() {
            return MAX_ARRIVALS;
        }
    }
}