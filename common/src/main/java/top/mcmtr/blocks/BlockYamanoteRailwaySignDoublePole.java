package top.mcmtr.blocks;

import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockYamanoteRailwaySignDoublePole extends BlockYamanoteRailwaySignPole {
    public BlockYamanoteRailwaySignDoublePole(Properties settings) {
        super(settings);
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        switch (IBlock.getStatePropertySafe(state, TYPE)) {
            case 0:
                return IBlock.getVoxelShapeByDirection(14, 0, 7.5, 15, 32, 8.5, facing);
            case 1:
                return IBlock.getVoxelShapeByDirection(10, 0, 7.5, 11, 32, 8.5, facing);
            case 2:
                return IBlock.getVoxelShapeByDirection(6, 0, 7.5, 7, 32, 8.5, facing);
            case 3:
                return IBlock.getVoxelShapeByDirection(2, 0, 7.5, 3, 32, 8.5, facing);
            default:
                return Shapes.block();
        }
    }

    @Override
    protected boolean isBlock(Block block) {
        return block instanceof BlockYamanoteRailwaySign && ((BlockYamanoteRailwaySign) block).length > 0 || block instanceof BlockYamanoteRailwaySignPole;
    }
}
