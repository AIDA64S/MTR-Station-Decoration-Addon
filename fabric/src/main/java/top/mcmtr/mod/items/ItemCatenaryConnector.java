package top.mcmtr.mod.items;

import org.mtr.core.data.Position;
import org.mtr.core.data.TwoPositionsBase;
import org.mtr.mapping.holder.*;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.data.CatenaryType;
import top.mcmtr.core.data.OffsetPosition;
import top.mcmtr.mod.Init;
import top.mcmtr.mod.blocks.BlockNodeBase;
import top.mcmtr.mod.packet.MSDPacketDeleteData;
import top.mcmtr.mod.packet.MSDPacketUpdateData;

import javax.annotation.Nullable;

public final class ItemCatenaryConnector extends ItemBlockClickingBase {

    public ItemCatenaryConnector(ItemSettings itemSettings, boolean isConnector, CatenaryType catenaryType) {
        super(itemSettings, isConnector, catenaryType);
    }

    @Override
    protected void onConnect(World world, ItemStack stack, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, CatenaryType catenaryType, @Nullable ServerPlayerEntity player) {
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

    @Override
    protected void onRemove(World world, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayerEntity player) {
        MSDPacketDeleteData.sendDirectlyToServerCatenaryId(ServerWorld.cast(world), TwoPositionsBase.getHexId(Init.blockPosToPosition(posStart), Init.blockPosToPosition(posEnd)));
    }
}