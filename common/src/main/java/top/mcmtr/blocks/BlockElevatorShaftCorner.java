package top.mcmtr.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class BlockElevatorShaftCorner extends Block {
    public static final VoxelShape DIRECTION_NORTH = Block.box(0, 0, 0, 4, 16, 4);
    public static final VoxelShape DIRECTION_EAST = Block.box(12, 0, 0, 16, 16, 4);
    public static final VoxelShape DIRECTION_SOUTH = Block.box(12, 0, 12, 16, 16, 16);
    public static final VoxelShape DIRECTION_WEST = Block.box(0, 0, 12, 4, 16, 16);

    public BlockElevatorShaftCorner(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(FACING) == Direction.NORTH) {
            return DIRECTION_NORTH;
        } else if (blockState.getValue(FACING) == Direction.EAST) {
            return DIRECTION_EAST;
        } else if (blockState.getValue(FACING) == Direction.SOUTH) {
            return DIRECTION_SOUTH;
        } else {
            return DIRECTION_WEST;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection());
    }
}
