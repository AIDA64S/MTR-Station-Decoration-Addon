package top.mcmtr.blocks;

import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class BlockSurveillanceCameras extends Block {
    public BlockSurveillanceCameras(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape shape1 = IBlock.getVoxelShapeByDirection(6.5, 14, 6.5, 9.5, 16, 9.5, IBlock.getStatePropertySafe(blockState, FACING));
        VoxelShape shape2 = IBlock.getVoxelShapeByDirection(6.5, 9, 5, 9.5, 14, 13, IBlock.getStatePropertySafe(blockState, FACING));
        return Shapes.or(shape1, shape2);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
