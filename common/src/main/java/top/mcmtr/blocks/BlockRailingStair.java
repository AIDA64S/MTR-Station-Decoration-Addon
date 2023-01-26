package top.mcmtr.blocks;

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

public class BlockRailingStair extends Block {
    public BlockRailingStair(Properties properties) {
        super(properties);
    }

    public static final VoxelShape DIRECTION_NORTH = Block.box(0, 0, 1, 16, 16, 3);
    public static final VoxelShape DIRECTION_EAST = Block.box(13, 0, 0, 15, 16, 16);
    public static final VoxelShape DIRECTION_SOUTH = Block.box(0, 0, 13, 16, 16, 15);
    public static final VoxelShape DIRECTION_WEST = Block.box(1, 0, 0, 3, 16, 16);
    public static final VoxelShape DIRECTION_NORTH_COLLISION = Block.box(0, 0, 1, 16, 24, 3);
    public static final VoxelShape DIRECTION_EAST_COLLISION = Block.box(13, 0, 0, 15, 24, 16);
    public static final VoxelShape DIRECTION_SOUTH_COLLISION = Block.box(0, 0, 13, 16, 24, 15);
    public static final VoxelShape DIRECTION_WEST_COLLISION = Block.box(1, 0, 0, 3, 24, 16);

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        switch (blockState.getValue(FACING)) {
            case NORTH:
                return DIRECTION_NORTH;
            case EAST:
                return DIRECTION_EAST;
            case SOUTH:
                return DIRECTION_SOUTH;
            default:
                return DIRECTION_WEST;
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        switch (blockState.getValue(FACING)) {
            case NORTH:
                return DIRECTION_NORTH_COLLISION;
            case EAST:
                return DIRECTION_EAST_COLLISION;
            case SOUTH:
                return DIRECTION_SOUTH_COLLISION;
            default:
                return DIRECTION_WEST_COLLISION;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection());
    }
}