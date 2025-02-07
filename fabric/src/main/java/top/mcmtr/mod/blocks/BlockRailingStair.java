package top.mcmtr.mod.blocks;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.IBlock;

import javax.annotation.Nullable;
import java.util.List;

public final class BlockRailingStair extends BlockChangeModelBase {
    private final boolean isMirror;

    public BlockRailingStair(boolean isMirror) {
        super(Blocks.createDefaultBlockSettings(true).nonOpaque(), 5);
        this.isMirror = isMirror;
    }

    @Override
    public String getTranslationKey2() {
        return "block.msd.railing_stair";
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
        tooltip.add(TextHelper.translatable("tooltip.msd.model_count", this.getCount()).formatted(TextFormatting.GOLD));
        if (this.isMirror) {
            tooltip.add(TextHelper.translatable("tooltip.msd.mirror").formatted(TextFormatting.GOLD));
        }
    }

    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final int index = state.get(new Property<>(TYPE.data));
        if (index == 4) {
            return this.isMirror ?
                    IBlock.getVoxelShapeByDirection(13, 0, 0, 16, 24, 3, IBlock.getStatePropertySafe(state, FACING)) :
                    IBlock.getVoxelShapeByDirection(0, 0, 0, 3, 24, 3, IBlock.getStatePropertySafe(state, FACING));
        }
        if (index == 5) {
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