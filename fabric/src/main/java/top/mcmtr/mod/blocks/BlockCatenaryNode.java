package top.mcmtr.mod.blocks;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import top.mcmtr.mod.Init;
import top.mcmtr.mod.packet.PacketData;

public final class BlockCatenaryNode extends BlockNodeBase {

    public BlockCatenaryNode(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Override
    public void onBreak3(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient()) {
            PacketData.deleteCatenaryNode(ServerWorld.cast(world), Init.blockPosToPosition(pos));
        }
    }

    public static void resetCatenaryNode(ServerWorld serverWorld, BlockPos blockPos) {
        final BlockState state = serverWorld.getBlockState(blockPos);
        if (state.getBlock().data instanceof BlockCatenaryNode) {
            serverWorld.setBlockState(blockPos, state.with(new Property<>(BlockCatenaryNode.IS_CONNECTED.data), false));
        }
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }
}
