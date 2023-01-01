package top.mcmtr.blocks;

import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import top.mcmtr.data.CatenaryData;
import top.mcmtr.packet.MSDPacketTrainDataGuiServer;

public class BlockCatenaryNode extends Block {
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.NORTH, Direction.EAST, Direction.WEST, Direction.SOUTH);
    public static final BooleanProperty IS_CONNECTED = BooleanProperty.create("is_connected");

    public BlockCatenaryNode(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection()).setValue(IS_CONNECTED, false);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return IBlock.getVoxelShapeByDirection(7.5,6, 7.5, 8.5, 15 ,8.5,IBlock.getStatePropertySafe(blockState, FACING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, IS_CONNECTED);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.BLOCK;
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide) {
            final CatenaryData catenaryData = CatenaryData.getInstance(world);
            if (catenaryData != null) {
                catenaryData.removeCatenaryNode(pos);
                MSDPacketTrainDataGuiServer.removeCatenaryNodeS2C(world, pos);
            }
        }
    }

    public static void resetCatenaryNode(Level world, BlockPos pos) {
        final BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockCatenaryNode) {
            world.setBlockAndUpdate(pos, state.setValue(BlockCatenaryNode.IS_CONNECTED, false));
        }
    }
}