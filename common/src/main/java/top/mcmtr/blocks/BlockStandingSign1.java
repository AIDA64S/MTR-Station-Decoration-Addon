package top.mcmtr.blocks;

import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import top.mcmtr.MSDBlockEntityTypes;

public class BlockStandingSign1 extends BlockCustomTextSignBase {
    public BlockStandingSign1() {
        super(2);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape shape0 = Block.box(7.375, 0, 7.375, 8.625, 4.2, 8.625);
        VoxelShape shape1 = IBlock.getVoxelShapeByDirection(7.6, 4.5, -1, 8.4, 10.5, 17, IBlock.getStatePropertySafe(blockState, FACING));
        return Shapes.or(shape0, shape1);
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