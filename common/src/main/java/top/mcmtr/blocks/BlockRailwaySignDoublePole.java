package top.mcmtr.blocks;

import mtr.block.BlockRailwaySignPole;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockRailwaySignDoublePole extends BlockRailwaySignPole {
    public BlockRailwaySignDoublePole(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        switch (IBlock.getStatePropertySafe(state, TYPE)) {
            case 0:
                return IBlock.getVoxelShapeByDirection(14, 0, 7, 15.25, 32, 9, facing);
            case 1:
                return IBlock.getVoxelShapeByDirection(10, 0, 7, 11.25, 32, 9, facing);
            case 2:
                return IBlock.getVoxelShapeByDirection(6, 0, 7, 7.25, 32, 9, facing);
            case 3:
                return IBlock.getVoxelShapeByDirection(2, 0, 7, 3.25, 32, 9, facing);
            default:
                return Shapes.block();
        }
    }
}
