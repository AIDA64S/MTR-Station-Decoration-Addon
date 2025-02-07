package top.mcmtr.mod.blocks;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.BlockPIDSHorizontalBase;
import org.mtr.mod.block.BlockPoleCheckBase;
import org.mtr.mod.block.IBlock;

import java.util.List;

public final class BlockYuuniPIDSPole extends BlockPoleCheckBase {
    public BlockYuuniPIDSPole() {
        super(Blocks.createDefaultBlockSettings(true));
    }

    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        return IBlock.getVoxelShapeByDirection(7.75, 0, 10.5, 8.25, 16, 11, facing);
    }

    @Override
    protected boolean isBlock(Block block) {
        return block.data instanceof BlockPIDSHorizontalBase || block.data instanceof BlockYuuniPIDSPole;
    }

    @Override
    protected Text getTooltipBlockText() {
        return new Text(TextHelper.translatable("block.msd.yamanote_pids").data);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
    }
}