package top.mcmtr.items;


import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import top.mcmtr.MSDCreativeModeTabs;
import top.mcmtr.blocks.BlockCatenaryNode;
import top.mcmtr.blocks.BlockRigidCatenaryNode;
import top.mcmtr.data.CatenaryData;

import java.util.List;

public abstract class ItemMSDNodeModifierBase extends ItemMSDBlockClickingBase {
    protected final boolean isConnector;

    public ItemMSDNodeModifierBase(boolean isConnector) {
        super(MSDCreativeModeTabs.MSD_BLOCKS, properties -> properties.stacksTo(1));
        this.isConnector = isConnector;
    }

    @Override
    protected void onStartClick(UseOnContext context, CompoundTag compoundTag) {
    }

    @Override
    protected void onEndClick(UseOnContext context, BlockPos posEnd, CompoundTag compoundTag) {
        final Level world = context.getLevel();
        final CatenaryData catenaryData = CatenaryData.getInstance(world);
        final BlockPos posStart = context.getClickedPos();
        final BlockState stateStart = world.getBlockState(posStart);
        final BlockState stateEnd = world.getBlockState(posEnd);
        if (catenaryData != null && (stateEnd.getBlock() instanceof BlockCatenaryNode || stateEnd.getBlock() instanceof BlockRigidCatenaryNode)) {
            if (isConnector) {
                if (!posStart.equals(posEnd)) {
                    onConnect(world, context.getItemInHand(), stateStart, stateEnd, posStart, posEnd, catenaryData);
                }
            } else {
                onRemove(world, posStart, posEnd, catenaryData);
            }
        }
    }

    @Override
    protected boolean clickCondition(UseOnContext context) {
        final Level world = context.getLevel();
        final Block blockStart = world.getBlockState(context.getClickedPos()).getBlock();
        return blockStart instanceof BlockCatenaryNode || blockStart instanceof BlockRigidCatenaryNode;
    }

    protected abstract void onConnect(Level world, ItemStack stack, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, CatenaryData catenaryData);

    protected abstract void onRemove(Level world, BlockPos posStart, BlockPos posEnd, CatenaryData catenaryData);
}