package top.mcmtr.blocks;

import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import top.mcmtr.MSDBlockEntityTypes;

public class BlockStandingSign extends BlockCustomTextSignBase {

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TileEntityBlockStandingSign(blockPos, blockState);
    }

    public static class TileEntityBlockStandingSign extends TileEntityBlockCustomTextSignBase {
        public static final int MAX_ARRIVALS = 6;

        public TileEntityBlockStandingSign(BlockPos pos, BlockState state) {
            super(MSDBlockEntityTypes.STANDING_SIGN_TILE_ENTITY.get(), pos, state);
        }

        @Override
        public int getMaxArrivals() {
            return MAX_ARRIVALS;
        }
    }
}