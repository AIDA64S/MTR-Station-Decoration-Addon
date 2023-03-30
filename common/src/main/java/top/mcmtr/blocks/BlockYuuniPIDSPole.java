package top.mcmtr.blocks;

import mtr.block.BlockPIDSBase;
import mtr.block.BlockPoleCheckBase;
import mtr.block.IBlock;
import mtr.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockYuuniPIDSPole extends BlockPoleCheckBase {
    public BlockYuuniPIDSPole(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return IBlock.getVoxelShapeByDirection(7.75, 0, 10.5, 8.25, 16, 11, IBlock.getStatePropertySafe(blockState, FACING));
    }

    @Override
    protected boolean isBlock(Block block) {
        return block instanceof BlockPIDSBase || block instanceof BlockYuuniPIDSPole;
    }

    @Override
    protected Component getTooltipBlockText() {
        return Text.translatable("block.msd.yuuni_pids");
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}