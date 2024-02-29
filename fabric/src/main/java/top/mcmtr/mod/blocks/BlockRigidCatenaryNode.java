package top.mcmtr.mod.blocks;

import org.mtr.core.tool.Angle;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.IBlock;
import top.mcmtr.mod.BlockEntityTypes;
import top.mcmtr.mod.Init;
import top.mcmtr.mod.packet.MSDPacketDeleteData;

import java.util.List;

public final class BlockRigidCatenaryNode extends BlockNodeBase {
    public static final BooleanProperty FACING = BooleanProperty.of("facing");
    public static final BooleanProperty IS_22_5 = BooleanProperty.of("is_22_5");
    public static final BooleanProperty IS_45 = BooleanProperty.of("is_45");

    public BlockRigidCatenaryNode() {
    }

    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        final int quadrant = Angle.getQuadrant(ctx.getPlayerYaw(), true);
        return getDefaultState2().with(new Property<>(FACING.data), quadrant % 8 >= 4).with(new Property<>(IS_45.data), quadrant % 4 >= 2).with(new Property<>(IS_22_5.data), quadrant % 2 >= 1).with(new Property<>(IS_CONNECTED.data), false);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        super.addBlockProperties(properties);
        properties.add(FACING);
        properties.add(IS_22_5);
        properties.add(IS_45);
    }

    @Override
    public void onBreak3(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient()) {
            MSDPacketDeleteData.sendDirectlyToServerRigidCatenaryNodePosition(ServerWorld.cast(world), Init.blockPosToPosition(pos));
        }
    }

    public static float getAngle(BlockState state) {
        return (IBlock.getStatePropertySafe(state, FACING) ? 0 : 90) + (IBlock.getStatePropertySafe(state, IS_22_5) ? 22.5F : 0) + (IBlock.getStatePropertySafe(state, IS_45) ? 45 : 0);
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockRigidCatenaryNodeEntity(blockPos, blockState);
    }

    public static class BlockRigidCatenaryNodeEntity extends BlockNodeBaseEntity {
        public BlockRigidCatenaryNodeEntity(BlockPos blockPos, BlockState blockState) {
            super(BlockEntityTypes.RIGID_CATENARY_NODE.get(), blockPos, blockState);
        }
    }
}