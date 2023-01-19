package top.mcmtr.blocks;

import mtr.block.BlockNode;
import mtr.block.IBlock;
import mtr.data.RailAngle;
import mtr.mappings.BlockDirectionalMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import top.mcmtr.data.CatenaryData;
import top.mcmtr.data.RigidCatenaryData;
import top.mcmtr.packet.MSDPacketTrainDataGuiServer;

public class BlockRigidCatenaryNode extends BlockDirectionalMapper {
    public static final BooleanProperty FACING = BooleanProperty.create("facing");
    public static final BooleanProperty IS_22_5 = BooleanProperty.create("is_22_5");
    public static final BooleanProperty IS_45 = BooleanProperty.create("is_45");
    public static final BooleanProperty IS_CONNECTED = BooleanProperty.create("is_connected");

    public BlockRigidCatenaryNode(Properties properties) {
        super(properties);
        this.registerDefaultState(defaultBlockState().setValue(FACING, false).setValue(IS_22_5, false).setValue(IS_45, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        final int quadrant = RailAngle.getQuadrant(blockPlaceContext.getRotation(), true);
        return defaultBlockState().setValue(FACING, quadrant % 8 >= 4).setValue(IS_45, quadrant % 4 >= 2).setValue(IS_22_5, quadrant % 2 >= 1).setValue(IS_CONNECTED, false);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (!level.isClientSide) {
            final RigidCatenaryData rigidCatenaryData = RigidCatenaryData.getInstance(level);
            final CatenaryData catenaryData = CatenaryData.getInstance(level);
            if (rigidCatenaryData != null) {
                rigidCatenaryData.removeRigidCatenaryNode(blockPos);
                MSDPacketTrainDataGuiServer.removeRigidCatenaryNodeS2C(level, blockPos);
            }
            if (catenaryData != null) {
                catenaryData.removeCatenaryNode(blockPos);
                MSDPacketTrainDataGuiServer.removeCatenaryNodeS2C(level, blockPos);
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
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
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.BLOCK;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, IS_22_5, IS_45, IS_CONNECTED);
    }

    public static void resetRigidCatenaryNode(Level world, BlockPos pos) {
        final BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockRigidCatenaryNode) {
            world.setBlockAndUpdate(pos, state.setValue(BlockRigidCatenaryNode.IS_CONNECTED, false));
        }
    }

    public static float getAngle(BlockState state) {
        return (IBlock.getStatePropertySafe(state, BlockRigidCatenaryNode.FACING) ? 0 : 90) + (IBlock.getStatePropertySafe(state, BlockRigidCatenaryNode.IS_22_5) ? 22.5F : 0) + (IBlock.getStatePropertySafe(state, BlockRigidCatenaryNode.IS_45) ? 45 : 0);
    }
}
