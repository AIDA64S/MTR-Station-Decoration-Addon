package top.mcmtr.blocks;

import mtr.block.IBlock;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class BlockElectricPoleTop extends HorizontalDirectionalBlock {
    public static final BooleanProperty IS_LONG = BooleanProperty.create("is_long");

    public BlockElectricPoleTop(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(IS_LONG, false));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Text.translatable("tooltip.msd.electric_pole_top").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return IBlock.getVoxelShapeByDirection(7.5, 0, 0, 8.5, 1, 9, IBlock.getStatePropertySafe(blockState, FACING));
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.empty();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, IS_LONG);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        final Direction direction = blockPlaceContext.getHorizontalDirection().getOpposite();
        return IBlock.isReplaceable(blockPlaceContext, direction, 2) ? defaultBlockState().setValue(FACING, direction).setValue(IS_LONG, false) : null;
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (IBlock.getStatePropertySafe(blockState, FACING) == direction && !blockState2.is(this)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return blockState;
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        if (!level.isClientSide) {
            final Direction direction = IBlock.getStatePropertySafe(blockState, FACING);
            level.setBlock(blockPos.relative(direction), defaultBlockState().setValue(FACING, direction.getOpposite()).setValue(IS_LONG, true), 3);
            level.updateNeighborsAt(blockPos, Blocks.AIR);
            blockState.updateNeighbourShapes(level, blockPos, 3);
        }
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.BLOCK;
    }
}