package top.mcmtr.mod.blocks.old;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;

import java.util.List;

public final class BlockOldDecorationRack extends BlockExtension implements DirectionHelper {
    public BlockOldDecorationRack() {
        super(Blocks.createDefaultBlockSettings(true).nonOpaque());
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
        properties.add(FACING_NORMAL);
    }

    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        final Direction facing = ctx.getPlayerFacing();
        return getDefaultState2().with(new Property<>(FACING_NORMAL.data), facing.data);
    }
}