package top.mcmtr.mod.items;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.CompoundTag;
import org.mtr.mapping.holder.ItemSettings;
import org.mtr.mapping.holder.ItemUsageContext;
import top.mcmtr.core.data.CatenaryType;

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

    }

    @Override
    protected boolean clickCondition(ItemUsageContext context) {
        return false;
    }
}