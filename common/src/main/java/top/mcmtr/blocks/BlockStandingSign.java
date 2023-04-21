package top.mcmtr.blocks;

import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import top.mcmtr.MSDBlockEntityTypes;

public class BlockStandingSign extends BlockCustomTextSignBase {

    public BlockStandingSign() {
        super(2);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return IBlock.getVoxelShapeByDirection(6.9, 1, 0, 9.1, 16, 11, IBlock.getStatePropertySafe(blockState, FACING));
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TileEntityBlockStandingSign(blockPos, blockState);
    }

    public static class TileEntityBlockStandingSign extends TileEntityBlockCustomTextSignBase {
        public static final int MAX_ARRIVALS = 3;

        public TileEntityBlockStandingSign(BlockPos pos, BlockState state) {
            super(MSDBlockEntityTypes.STANDING_SIGN_TILE_ENTITY.get(), pos, state);
        }

        @Override
        public int getMaxArrivals() {
            return MAX_ARRIVALS;
        }
    }
}