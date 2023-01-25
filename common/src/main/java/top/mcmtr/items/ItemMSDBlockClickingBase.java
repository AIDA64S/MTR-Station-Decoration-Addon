package top.mcmtr.items;

import mtr.CreativeModeTabs;
import mtr.item.ItemWithCreativeTabBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

import java.util.function.Function;

public abstract class ItemMSDBlockClickingBase extends ItemWithCreativeTabBase {
    public static final String TAG_POS = "catenary_pos";

    public ItemMSDBlockClickingBase(CreativeModeTabs.Wrapper creativeModeTab, Function<Properties, Properties> propertiesConsumer) {
        super(creativeModeTab, propertiesConsumer);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide) {
            if (clickCondition(context)) {
                final CompoundTag compoundTag = context.getItemInHand().getOrCreateTag();
                if (compoundTag.contains(TAG_POS)) {
                    final BlockPos posEnd = BlockPos.of(compoundTag.getLong(TAG_POS));
                    onEndClick(context, posEnd, compoundTag);
                    compoundTag.remove(TAG_POS);
                } else {
                    compoundTag.putLong(TAG_POS, context.getClickedPos().asLong());
                    onStartClick(context, compoundTag);
                }
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            return super.useOn(context);
        }
    }

    protected abstract void onStartClick(UseOnContext context, CompoundTag compoundTag);

    protected abstract void onEndClick(UseOnContext context, BlockPos posEnd, CompoundTag compoundTag);

    protected abstract boolean clickCondition(UseOnContext context);
}