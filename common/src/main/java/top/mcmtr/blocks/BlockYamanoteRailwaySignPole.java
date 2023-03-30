package top.mcmtr.blocks;

import mtr.block.BlockPoleCheckBase;
import mtr.block.IBlock;
import mtr.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockYamanoteRailwaySignPole extends BlockPoleCheckBase {
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 3);

    public BlockYamanoteRailwaySignPole(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        switch (IBlock.getStatePropertySafe(state, TYPE)) {
            case 0:
                return IBlock.getVoxelShapeByDirection(14, 0, 7.5, 15, 16, 8.5, facing);
            case 1:
                return IBlock.getVoxelShapeByDirection(10, 0, 7.5, 11, 16, 8.5, facing);
            case 2:
                return IBlock.getVoxelShapeByDirection(6, 0, 7.5, 7, 16, 8.5, facing);
            case 3:
                return IBlock.getVoxelShapeByDirection(2, 0, 7.5, 3, 16, 8.5, facing);
            default:
                return Shapes.block();
        }
    }

    @Override
    protected BlockState placeWithState(BlockState stateBelow) {
        final int type;
        final Block block = stateBelow.getBlock();
        if (block instanceof BlockYamanoteRailwaySign) {
            type = (((BlockYamanoteRailwaySign) block).length + (((BlockYamanoteRailwaySign) block).isOdd ? 2 : 0)) % 4;
        } else {
            type = IBlock.getStatePropertySafe(stateBelow, TYPE);
        }
        return super.placeWithState(stateBelow).setValue(TYPE, type);
    }

    @Override
    protected boolean isBlock(Block block) {
        return (block instanceof BlockYamanoteRailwaySign && ((BlockYamanoteRailwaySign) block).length > 0) || block instanceof BlockYamanoteRailwaySignPole;
    }

    @Override
    protected Component getTooltipBlockText() {
        return Text.translatable("block.msd.yamanote_railway_sign");
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE);
    }
}