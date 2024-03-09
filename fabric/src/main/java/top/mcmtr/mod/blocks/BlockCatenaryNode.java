package top.mcmtr.mod.blocks;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.block.IBlock;
import top.mcmtr.mod.BlockEntityTypes;
import top.mcmtr.mod.Init;
import top.mcmtr.mod.packet.MSDPacketDeleteData;
import top.mcmtr.mod.packet.MSDPacketOpenCatenaryScreen;

public final class BlockCatenaryNode extends BlockNodeBase {
    public BlockCatenaryNode() {
        super();
    }

    @Override
    public BlockRenderType getRenderType2(BlockState state) {
        if (state.get(new Property<>(IS_CONNECTED.data))) {
            return BlockRenderType.INVISIBLE;
        }
        return super.getRenderType2(state);
    }

    @Override
    public void onBreak3(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient()) {
            MSDPacketDeleteData.sendDirectlyToServerCatenaryNodePosition(ServerWorld.cast(world), Init.blockPosToPosition(pos));
        }
    }

    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlock.checkHoldingBrush(world, player, () -> {
            final BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity != null && blockEntity.data instanceof BlockCatenaryNode.BlockCatenaryNodeEntity) {
                Init.REGISTRY.sendPacketToClient(ServerPlayerEntity.cast(player), new MSDPacketOpenCatenaryScreen(pos, state.get(new Property<>(IS_CONNECTED.data)), ((BlockCatenaryNodeEntity) blockEntity.data).getOffsetPosition()));
            }
        });
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockCatenaryNodeEntity(blockPos, blockState);
    }

    public static class BlockCatenaryNodeEntity extends BlockNodeBaseEntity {
        public BlockCatenaryNodeEntity(BlockPos blockPos, BlockState blockState) {
            super(BlockEntityTypes.CATENARY_NODE.get(), blockPos, blockState);
        }
    }
}