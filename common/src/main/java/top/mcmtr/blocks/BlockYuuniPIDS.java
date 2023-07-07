package top.mcmtr.blocks;

import mtr.block.BlockPIDSBaseHorizontal;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import top.mcmtr.MSDBlockEntityTypes;
import top.mcmtr.config.Config;

public class BlockYuuniPIDS extends BlockPIDSBaseHorizontal {
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
        VoxelShape shape1 = IBlock.getVoxelShapeByDirection(5.75, 0.3, 0, 10.25, 11.6, 15.7, IBlock.getStatePropertySafe(state, FACING));
        VoxelShape shape2 = IBlock.getVoxelShapeByDirection(7.75, 11.6, 10.5, 8.25, 16, 11, IBlock.getStatePropertySafe(state, FACING));
        return Shapes.or(shape1, shape2);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityPIDS(pos, state);
    }

    public static class TileEntityPIDS extends TileEntityBlockPIDSBaseHorizontal {
        public TileEntityPIDS(BlockPos pos, BlockState state) {
            super(MSDBlockEntityTypes.YUUNI_PIDS_TILE_ENTITY.get(), pos, state);
        }

        @Environment(EnvType.CLIENT)
        public double getViewDistance() {
            return Config.getYuuniPIDSMaxViewDistance();
        }

        @Override
        public int getMaxArrivals() {
            return 2;
        }
    }
}