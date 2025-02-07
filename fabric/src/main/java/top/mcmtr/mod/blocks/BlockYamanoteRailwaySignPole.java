package top.mcmtr.mod.blocks;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.BlockPoleCheckBase;
import org.mtr.mod.block.IBlock;

import javax.annotation.Nonnull;
import java.util.List;

public final class BlockYamanoteRailwaySignPole extends BlockPoleCheckBase {
    public static final IntegerProperty TYPE = IntegerProperty.of("type", 0, 3);

    public BlockYamanoteRailwaySignPole() {
        super(Blocks.createDefaultBlockSettings(true));
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
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
                return VoxelShapes.fullCube();
        }
    }

    @Override
    protected BlockState placeWithState(BlockState stateBelow) {
        final int type;
        final Block block = stateBelow.getBlock();
        if (block.data instanceof BlockYamanoteRailwaySign) {
            type = (((BlockYamanoteRailwaySign) block.data).length + (((BlockYamanoteRailwaySign) block.data).isOdd ? 2 : 0)) % 4;
        } else {
            type = IBlock.getStatePropertySafe(stateBelow, TYPE);
        }
        return super.placeWithState(stateBelow).with(new Property<>(TYPE.data), type);
    }

    @Override
    protected boolean isBlock(Block block) {
        return (block.data instanceof BlockYamanoteRailwaySign && ((BlockYamanoteRailwaySign) block.data).length > 0) || block.data instanceof BlockYamanoteRailwaySignPole;
    }

    @Override
    protected Text getTooltipBlockText() {
        return new Text(TextHelper.translatable("block.msd.yamanote_railway_sign").data);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(TYPE);
    }
}
