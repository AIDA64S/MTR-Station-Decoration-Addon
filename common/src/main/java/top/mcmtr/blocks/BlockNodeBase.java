package top.mcmtr.blocks;

import mtr.block.IBlock;
import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.EntityBlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import top.mcmtr.data.BlockLocation;
import top.mcmtr.packet.MSDPacketTrainDataGuiServer;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public abstract class BlockNodeBase extends Block implements EntityBlockMapper {
    public static final BooleanProperty IS_CONNECTED = BooleanProperty.create("is_connected");

    public BlockNodeBase(Properties properties) {
        super(properties);
        this.registerDefaultState((this.stateDefinition.any()).setValue(FACING, Direction.UP).setValue(IS_CONNECTED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Direction direction = blockPlaceContext.getClickedFace();
        BlockState blockState = blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos().relative(direction.getOpposite()));
        return blockState.is(this) && blockState.getValue(FACING) == direction ? this.defaultBlockState().setValue(FACING, direction.getOpposite()) : this.defaultBlockState().setValue(FACING, direction);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    public BlockState rotate(BlockState arg, Rotation arg2) {
        return arg.setValue(FACING, arg2.rotate(arg.getValue(FACING)));
    }

    public BlockState mirror(BlockState arg, Mirror arg2) {
        return arg.setValue(FACING, arg2.mirror(arg.getValue(FACING)));
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Block.box(0, 0, 0, 16, 16, 16);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, IS_CONNECTED);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.BLOCK;
    }

    public static void resetNode(Level world, BlockPos pos) {
        final BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockNodeBase) {
            world.setBlockAndUpdate(pos, state.setValue(IS_CONNECTED, false));
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return IBlock.checkHoldingBrush(level, player, () -> {
            final BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if ((blockState.getBlock() instanceof BlockNodeBase) && (!blockState.getValue(IS_CONNECTED)) && (blockEntity instanceof BlockNodeBaseEntity)) {
                ((BlockNodeBaseEntity) blockEntity).syncData();
                MSDPacketTrainDataGuiServer.openBlockNodeScreenC2S((ServerPlayer) player, ((BlockNodeBaseEntity) blockEntity).getPointLocation(), blockPos);
            }
        });
    }

    public abstract static class BlockNodeBaseEntity extends BlockEntityClientSerializableMapper {
        private static final String KEY_LOCATION = "key_msd_location";
        private final BlockLocation pointLocation = new BlockLocation(0, 0, 0);

        public BlockNodeBaseEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state);
        }

        public void setPointLocation(BlockLocation pointLocation) {
            this.pointLocation.setX(pointLocation.getX());
            this.pointLocation.setY(pointLocation.getY());
            this.pointLocation.setZ(pointLocation.getZ());
        }

        public BlockLocation getPointLocation() {
            return this.pointLocation;
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            pointLocation.setX(compoundTag.getDouble(KEY_LOCATION + "x"));
            pointLocation.setY(compoundTag.getDouble(KEY_LOCATION + "y"));
            pointLocation.setZ(compoundTag.getDouble(KEY_LOCATION + "z"));
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putDouble(KEY_LOCATION + "x", pointLocation.getX());
            compoundTag.putDouble(KEY_LOCATION + "y", pointLocation.getY());
            compoundTag.putDouble(KEY_LOCATION + "z", pointLocation.getZ());
        }

        public void setData(BlockLocation location) {
            setPointLocation(location);
            setChanged();
            syncData();
        }
    }
}