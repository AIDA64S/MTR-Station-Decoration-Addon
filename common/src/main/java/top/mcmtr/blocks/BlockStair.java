package top.mcmtr.blocks;

import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockStair extends BlockChangeModelBase {
    public BlockStair() {
        super(1, Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape shape1 = IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 4, 16, IBlock.getStatePropertySafe(blockState, FACING));
        VoxelShape shape2 = IBlock.getVoxelShapeByDirection(0, 4, 0, 16, 8, 12, IBlock.getStatePropertySafe(blockState, FACING));
        VoxelShape shape3 = IBlock.getVoxelShapeByDirection(0, 8, 0, 16, 12, 8, IBlock.getStatePropertySafe(blockState, FACING));
        VoxelShape shape4 = IBlock.getVoxelShapeByDirection(0, 12, 0, 16, 16, 4, IBlock.getStatePropertySafe(blockState, FACING));
        return Shapes.or(shape1, shape2, shape3, shape4);
    }
}