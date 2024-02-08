package top.mcmtr.mod.items;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ItemExtension;

public abstract class ItemBlockClickingBase extends ItemExtension {
    public static final String TAG_POS = "catenary_pos";

    public ItemBlockClickingBase(ItemSettings itemSettings) {
        super(itemSettings);
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
                    onStartClick(context, compoundTag);
                }
                return ActionResult.SUCCESS;
            } else {
                return ActionResult.FAIL;
            }
        } else {
            return super.useOnBlock2(context);
        }
    }

    protected abstract void onStartClick(ItemUsageContext context, CompoundTag compoundTag);

    protected abstract void onEndClick(ItemUsageContext context, BlockPos posEnd, CompoundTag compoundTag);

    protected abstract boolean clickCondition(ItemUsageContext context);
}