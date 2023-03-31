package top.mcmtr.blocks;

import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import top.mcmtr.MSDBlockEntityTypes;

public class BlockStandingSign extends BlockCustomTextSignBase {

    public BlockStandingSign() {
        super(1);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape shape1 = IBlock.getVoxelShapeByDirection(7, 8, 0, 9, 16, 17, IBlock.getStatePropertySafe(blockState, FACING));
        VoxelShape shape2 = IBlock.getVoxelShapeByDirection(7.5, 8, 17, 8.5, 16, 18, IBlock.getStatePropertySafe(blockState, FACING));
        return Shapes.or(shape1, shape2);
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