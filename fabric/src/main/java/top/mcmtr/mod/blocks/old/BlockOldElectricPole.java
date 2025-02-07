package top.mcmtr.mod.blocks.old;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.IBlock;

import java.util.List;

public class BlockOldElectricPole extends BlockExtension implements DirectionHelper {
    public static final BooleanProperty IS_LONG = BooleanProperty.of("is_long");

    private final boolean hasAnother;

    public BlockOldElectricPole(boolean hasAnother) {
        super(org.mtr.mod.Blocks.createDefaultBlockSettings(false).nonOpaque());
        this.hasAnother = hasAnother;
        getDefaultState2().with(new Property<>(IS_LONG.data), false);
    }

    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }

    @Override
    public VoxelShape getCollisionShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public void addTooltips(ItemStack stack, BlockView world, List<MutableText> tooltip, TooltipContext options) {
        tooltip.add(TextHelper.translatable("tooltip.msd.deprecated").formatted(TextFormatting.RED));
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(IS_LONG);
    }

    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        final Direction facing = ctx.getPlayerFacing();
        return IBlock.isReplaceable(ctx, facing, 2) ? getDefaultState2().with(new Property<>(FACING.data), facing.data).with(new Property<>(IS_LONG.data), false) : null;
    }

    @Override
    public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (IBlock.getStatePropertySafe(state, FACING) == direction && !neighborState.isOf(this.asBlock2())) {
            return Blocks.getAirMapped().getDefaultState();
        } else {
            return state;
        }
    }

    @Override
    public void onPlaced2(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient() && this.hasAnother) {
            final Direction direction = IBlock.getStatePropertySafe(state, FACING);
            world.setBlockState(pos.offset(direction), getDefaultState2().with(new Property<>(FACING.data), direction.getOpposite().data).with(new Property<>(IS_LONG.data), true), 3);
            if (this.hasAnother) {
                world.setBlockState(pos.offset(direction.getOpposite()), getDefaultState2().with(new Property<>(FACING.data), direction.data).with(new Property<>(IS_LONG.data), true), 3);
            }
            world.updateNeighbors(pos, Blocks.getAirMapped());
            state.updateNeighbors(WorldAccess.cast(world), pos, 3);
        }
    }
}
