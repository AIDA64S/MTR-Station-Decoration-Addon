package top.mcmtr.mod.items;

import org.mtr.core.data.Position;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Init;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.mod.blocks.BlockCatenaryNode;
import top.mcmtr.mod.packet.PacketData;

import javax.annotation.Nullable;

public final class ItemCatenaryConnector extends ItemBlockClickingBase {
    private final boolean isConnector;

    public ItemCatenaryConnector(ItemSettings itemSettings, boolean isConnector) {
        super(itemSettings);
        this.isConnector = isConnector;
    }

    @Override
    protected void onStartClick(ItemUsageContext context, CompoundTag compoundTag) {
    }

    @Override
    protected void onEndClick(ItemUsageContext context, BlockPos posEnd, CompoundTag compoundTag) {
        final World world = context.getWorld();
        final BlockPos posStart = context.getBlockPos();
        final BlockState stateStart = world.getBlockState(posStart);
        final BlockState stateEnd = world.getBlockState(posEnd);
        final PlayerEntity player = context.getPlayer();
        if (ServerPlayerEntity.isInstance(player) && stateEnd.getBlock().data instanceof BlockCatenaryNode) {
            if (isConnector) {
                if (!posStart.equals(posEnd)) {
                    onConnect(world, context.getStack(), stateStart, stateEnd, posStart, posEnd, ServerPlayerEntity.cast(player));
                }
            } else {
                onRemove(world, posStart, posEnd, ServerPlayerEntity.cast(player));
            }
        }
    }

    @Override
    protected boolean clickCondition(ItemUsageContext context) {
        final World world = context.getWorld();
        final Block blockStart = world.getBlockState(context.getBlockPos()).getBlock();
        return blockStart.data instanceof BlockCatenaryNode;
    }

    private void onConnect(World world, ItemStack stack, BlockState blockStart, BlockState blockEnd, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayerEntity player) {
        final Position positionStart = Init.blockPosToPosition(posStart);
        final Position positionEnd = Init.blockPosToPosition(posEnd);
        final BlockCatenaryNode.BlockCatenaryNodeEntity catenaryNodeEntityStart = (BlockCatenaryNode.BlockCatenaryNodeEntity) world.getBlockEntity(posStart).data;
        final BlockCatenaryNode.BlockCatenaryNodeEntity catenaryNodeEntityEnd = (BlockCatenaryNode.BlockCatenaryNodeEntity) world.getBlockEntity(posEnd).data;
        final Catenary catenary = new Catenary(positionStart, positionEnd, catenaryNodeEntityStart.getOffsetPosition(), catenaryNodeEntityEnd.getOffsetPosition());
        if (catenary.isValid()) {
            world.setBlockState(posStart, blockStart.with(new Property<>(BlockCatenaryNode.IS_CONNECTED.data), true));
            world.setBlockState(posEnd, blockEnd.with(new Property<>(BlockCatenaryNode.IS_CONNECTED.data), true));
            PacketData.updateCatenary(ServerWorld.cast(world), catenary);
        } else if (player != null) {
            player.sendMessage(new Text(TextHelper.translatable("gui.msd.not_allow_connect").data), true);
        }
    }

    private void onRemove(World world, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayerEntity player) {
        final BlockCatenaryNode.BlockCatenaryNodeEntity catenaryNodeEntityStart = (BlockCatenaryNode.BlockCatenaryNodeEntity) world.getBlockEntity(posStart).data;
        final BlockCatenaryNode.BlockCatenaryNodeEntity catenaryNodeEntityEnd = (BlockCatenaryNode.BlockCatenaryNodeEntity) world.getBlockEntity(posEnd).data;
        PacketData.deleteCatenary(ServerWorld.cast(world), new Catenary(Init.blockPosToPosition(posStart), Init.blockPosToPosition(posEnd), catenaryNodeEntityStart.getOffsetPosition(), catenaryNodeEntityEnd.getOffsetPosition()));
    }
}