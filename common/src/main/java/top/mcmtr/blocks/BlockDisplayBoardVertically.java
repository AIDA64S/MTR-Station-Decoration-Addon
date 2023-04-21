package top.mcmtr.blocks;

import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockDisplayBoardVertically extends BlockChangeModelBase {
    public BlockDisplayBoardVertically() {
        super(6, Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F).lightLevel((state) -> 5));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape voxelShape1 = IBlock.getVoxelShapeByDirection(3, 0, 4, 13, 8, 12, IBlock.getStatePropertySafe(blockState, FACING));
        VoxelShape voxelShape2 = IBlock.getVoxelShapeByDirection(3, 8, 4, 13, 16, 10, IBlock.getStatePropertySafe(blockState, FACING));
        return Shapes.or(voxelShape1, voxelShape2);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.empty();
    }
}