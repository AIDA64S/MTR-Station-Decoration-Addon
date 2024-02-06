package top.mcmtr.mod.items;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.CompoundTag;
import org.mtr.mapping.holder.ItemSettings;
import org.mtr.mapping.holder.ItemUsageContext;

public final class ItemCatenaryConnector extends ItemBlockClickingBase{
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

    }

    @Override
    protected boolean clickCondition(ItemUsageContext context) {
        return false;
    }
}
