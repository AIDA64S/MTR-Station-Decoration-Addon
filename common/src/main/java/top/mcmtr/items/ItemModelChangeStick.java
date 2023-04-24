package top.mcmtr.items;

import mtr.item.ItemWithCreativeTabBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import top.mcmtr.MSDCreativeModeTabs;
import top.mcmtr.blocks.BlockChangeModelBase;

import static top.mcmtr.blocks.BlockChangeModelBase.TEXTURE_TYPE;

public class ItemModelChangeStick extends ItemWithCreativeTabBase {
    public ItemModelChangeStick() {
        super(MSDCreativeModeTabs.MSD_Station_Decoration, properties -> properties.stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        final Level world = useOnContext.getLevel();
        if (!world.isClientSide) {
            if (clickCondition(useOnContext)) {
                final BlockPos blockPos = useOnContext.getClickedPos();
                final BlockState blockState = world.getBlockState(blockPos);
                final BlockChangeModelBase block = (BlockChangeModelBase) blockState.getBlock();
                int blockTextureType = blockState.getValue(TEXTURE_TYPE);
                if (blockTextureType == block.getCount() - 1) {
                    world.setBlockAndUpdate(blockPos, blockState.setValue(TEXTURE_TYPE, 0));
                } else {
                    world.setBlockAndUpdate(blockPos, blockState.setValue(TEXTURE_TYPE, blockTextureType + 1));
                }
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            return super.useOn(useOnContext);
        }
    }

    private boolean clickCondition(UseOnContext context) {
        final Level world = context.getLevel();
        final Block block = world.getBlockState(context.getClickedPos()).getBlock();
        return block instanceof BlockChangeModelBase;
    }
}