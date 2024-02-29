package top.mcmtr.mod.items;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ItemExtension;
import top.mcmtr.core.data.CatenaryType;
import top.mcmtr.mod.blocks.BlockNodeBase;

import javax.annotation.Nullable;

public abstract class ItemBlockClickingBase extends ItemExtension {
    protected final boolean isConnector;
    protected final CatenaryType catenaryType;
    public static final String TAG_POS = "catenary_pos";

    public ItemBlockClickingBase(ItemSettings itemSettings, boolean isConnector, CatenaryType catenaryType) {
        super(itemSettings);
        this.isConnector = isConnector;
        this.catenaryType = catenaryType;
    }

    @Override
    public ActionResult useOnBlock2(ItemUsageContext context) {
        if (!context.getWorld().isClient()) {
            if (clickCondition(context)) {
                final CompoundTag compoundTag = context.getStack().getOrCreateTag();
                if (compoundTag.contains(TAG_POS)) {
                    final BlockPos posEnd = BlockPos.fromLong(compoundTag.getLong(TAG_POS));
                    onEndClick(context, posEnd, compoundTag);
                    compoundTag.remove(TAG_POS);
                } else {
                    compoundTag.putLong(TAG_POS, context.getBlockPos().asLong());
                }
                return ActionResult.SUCCESS;
            } else {
                return ActionResult.FAIL;
            }
        } else {
            return super.useOnBlock2(context);
        }
    }

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

    protected abstract void onConnect(World world, ItemStack stack, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, CatenaryType catenaryType, @Nullable ServerPlayerEntity player);

    protected abstract void onRemove(World world, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayerEntity player);

    protected boolean clickCondition(ItemUsageContext context) {
        final World world = context.getWorld();
        final Block blockStart = world.getBlockState(context.getBlockPos()).getBlock();
        return blockStart.data instanceof BlockNodeBase;
    }
}