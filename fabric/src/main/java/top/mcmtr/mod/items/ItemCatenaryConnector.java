package top.mcmtr.mod.items;

import org.mtr.mapping.holder.*;
import top.mcmtr.core.data.CatenaryType;
import top.mcmtr.mod.blocks.BlockNodeBase;

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
        return blockStart.data instanceof BlockNodeBase;
    }

    private void onConnect(World world, ItemStack stack, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, CatenaryType catenaryType, @Nullable ServerPlayerEntity player) {

    }

    private void onRemove(World world, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayerEntity player) {
        final BlockNodeBase.BlockNodeBaseEntity startBlockEntity = (BlockNodeBase.BlockNodeBaseEntity) world.getBlockEntity(posStart).data;
        final BlockNodeBase.BlockNodeBaseEntity endBlockEntity = (BlockNodeBase.BlockNodeBaseEntity) world.getBlockEntity(posEnd).data;
    }
}