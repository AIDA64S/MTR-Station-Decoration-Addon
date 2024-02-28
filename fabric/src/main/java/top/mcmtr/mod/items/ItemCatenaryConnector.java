package top.mcmtr.mod.items;

import org.mtr.core.data.Position;
import org.mtr.mapping.holder.*;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.data.CatenaryType;
import top.mcmtr.core.data.OffsetPosition;
import top.mcmtr.core.data.TwoPositionsBase;
import top.mcmtr.mod.Init;
import top.mcmtr.mod.blocks.BlockCatenaryNode;
import top.mcmtr.mod.blocks.BlockNodeBase;
import top.mcmtr.mod.packet.MSDPacketDeleteData;
import top.mcmtr.mod.packet.MSDPacketUpdateData;

import javax.annotation.Nullable;

public final class ItemCatenaryConnector extends ItemBlockClickingBase {
    private final boolean isConnector;
    private final CatenaryType catenaryType;

    public ItemCatenaryConnector(ItemSettings itemSettings, boolean isConnector, CatenaryType catenaryType) {
        super(itemSettings);
        this.isConnector = isConnector;
        this.catenaryType = catenaryType;
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

        if (ServerPlayerEntity.isInstance(player) && stateEnd.getBlock().data instanceof BlockNodeBase) {
            if (isConnector) {
                if (!posStart.equals(posEnd)) {
                    onConnect(world, context.getStack(), stateStart, stateEnd, posStart, posEnd, catenaryType, ServerPlayerEntity.cast(player));
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

    private void onConnect(World world, ItemStack stack, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, CatenaryType catenaryType, @Nullable ServerPlayerEntity player) {
        if (catenaryType != null) {
            final BlockNodeBase.BlockNodeBaseEntity startBlockEntity = (BlockNodeBase.BlockNodeBaseEntity) world.getBlockEntity(posStart).data;
            final BlockNodeBase.BlockNodeBaseEntity endBlockEntity = (BlockNodeBase.BlockNodeBaseEntity) world.getBlockEntity(posEnd).data;
            final OffsetPosition offsetPositionStart = startBlockEntity.getOffsetPosition();
            final OffsetPosition offsetPositionEnd = endBlockEntity.getOffsetPosition();
            final Position positionStart = Init.blockPosToPosition(posStart);
            final Position positionEnd = Init.blockPosToPosition(posEnd);
            if (Catenary.verifyPosition(positionStart, positionEnd, offsetPositionStart, offsetPositionEnd)) {
                final Catenary catenary = new Catenary(positionStart, positionEnd, offsetPositionStart, offsetPositionEnd, catenaryType);
                world.setBlockState(posStart, stateStart.with(new Property<>(BlockNodeBase.IS_CONNECTED.data), true));
                world.setBlockState(posEnd, stateEnd.with(new Property<>(BlockNodeBase.IS_CONNECTED.data), true));
                MSDPacketUpdateData.sendDirectlyToServerCatenary(ServerWorld.cast(world), catenary);
            }
        }
    }

    private void onRemove(World world, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayerEntity player) {
        MSDPacketDeleteData.sendDirectlyToServerCatenaryId(ServerWorld.cast(world), TwoPositionsBase.getHexId(Init.blockPosToPosition(posStart), Init.blockPosToPosition(posEnd)));
    }
}