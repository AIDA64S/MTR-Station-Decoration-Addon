package top.mcmtr.mod.blocks.old;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.IBlock;

import java.util.List;

public class BlockOldRailingStair extends BlockExtension implements DirectionHelper {
    public static final IntegerProperty TYPE = IntegerProperty.of("type", 0, 5);
    private final boolean isMirror;
    private final int index;
    private final int metal;

    public BlockOldRailingStair(boolean isMirror, int index, int metal) {
        super(Blocks.createDefaultBlockSettings(true).nonOpaque());
        this.isMirror = isMirror;
        this.index = index;
        this.metal = metal;
        this.setDefaultState2(this.getDefaultState2().with(new Property<>(TYPE.data), this.index));
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(TYPE);
    }

    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        return this.getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data).with(new Property<>(TYPE.data), this.index);
    }

    @Override
    public String getTranslationKey2() {
        return "block.msd.railing_stair";
    }

    @Override
    public void addTooltips(ItemStack stack, BlockView world, List<MutableText> tooltip, TooltipContext options) {
        if (this.isMirror) {
            tooltip.add(TextHelper.translatable("tooltip.msd.mirror").formatted(TextFormatting.GOLD));
        }
        switch (this.metal) {
            case 0:
                tooltip.add(TextHelper.translatable("tooltip.msd.metal_iron").formatted(TextFormatting.GRAY));
                break;
            case 1:
                tooltip.add(TextHelper.translatable("tooltip.msd.metal_glass").formatted(TextFormatting.GRAY));
                break;
            default:
                break;
        }
        switch (this.index) {
            case 1:
                tooltip.add(TextHelper.translatable("tooltip.msd.part_start").formatted(TextFormatting.GRAY));
                break;
            case 2:
                tooltip.add(TextHelper.translatable("tooltip.msd.part_middle").formatted(TextFormatting.GRAY));
                break;
            case 3:
                tooltip.add(TextHelper.translatable("tooltip.msd.part_end").formatted(TextFormatting.GRAY));
                break;
            case 4:
                tooltip.add(TextHelper.translatable("tooltip.msd.part_corner_1").formatted(TextFormatting.GRAY));
                break;
            case 5:
                tooltip.add(TextHelper.translatable("tooltip.msd.part_corner_2").formatted(TextFormatting.GRAY));
                break;
            default:
                break;
        }
    }

    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (this.index == 4) {
            return this.isMirror ?
                    IBlock.getVoxelShapeByDirection(13, 0, 0, 16, 24, 3, IBlock.getStatePropertySafe(state, FACING)) :
                    IBlock.getVoxelShapeByDirection(0, 0, 0, 3, 24, 3, IBlock.getStatePropertySafe(state, FACING));
        }
        if (this.index == 5) {
            VoxelShape voxelShape1;
            VoxelShape voxelShape2;
            if (this.isMirror) {
                voxelShape1 = IBlock.getVoxelShapeByDirection(1, 0, 1, 16, 24, 3, IBlock.getStatePropertySafe(state, FACING));
                voxelShape2 = IBlock.getVoxelShapeByDirection(1, 0, 1, 3, 24, 16, IBlock.getStatePropertySafe(state, FACING));
            } else {
                voxelShape1 = IBlock.getVoxelShapeByDirection(0, 0, 1, 15, 24, 3, IBlock.getStatePropertySafe(state, FACING));
                voxelShape2 = IBlock.getVoxelShapeByDirection(13, 0, 1, 15, 24, 16, IBlock.getStatePropertySafe(state, FACING));
            }
            return VoxelShapes.union(voxelShape1, voxelShape2);
        }
        return IBlock.getVoxelShapeByDirection(0, 0, 1, 16, 24, 3, IBlock.getStatePropertySafe(state, FACING));
    }
}