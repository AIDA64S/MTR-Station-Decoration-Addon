package top.mcmtr.items;

import mtr.data.RailAngle;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import top.mcmtr.MSDCreativeModeTabs;
import top.mcmtr.blocks.BlockRigidCatenaryNode;
import top.mcmtr.data.RigidCatenaryData;

import java.util.List;

public abstract class ItemMSDOriginNodeModifierBase extends ItemMSDBlockClickingBase {
    protected final boolean isConnector;

    public ItemMSDOriginNodeModifierBase(boolean isConnector) {
        super(MSDCreativeModeTabs.MSD_BLOCKS, properties -> properties.stacksTo(1));
        this.isConnector = isConnector;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Text.translatable("tooltip.msd.rigid_catenary").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
    }

    @Override
    protected void onStartClick(UseOnContext context, CompoundTag compoundTag) {
    }

    @Override
    protected void onEndClick(UseOnContext context, BlockPos posEnd, CompoundTag compoundTag) {
        final Level world = context.getLevel();
        final RigidCatenaryData rigidCatenaryData = RigidCatenaryData.getInstance(world);
        final BlockPos posStart = context.getClickedPos();
        final BlockState stateStart = world.getBlockState(posStart);
        final BlockState stateEnd = world.getBlockState(posEnd);
        if (rigidCatenaryData != null && stateEnd.getBlock() instanceof BlockRigidCatenaryNode) {
            final Player player = context.getPlayer();
            if (isConnector) {
                if (!posStart.equals(posEnd)) {
                    final float angle1 = BlockRigidCatenaryNode.getAngle(stateStart);
                    final float angle2 = BlockRigidCatenaryNode.getAngle(stateEnd);
                    final float angleDifference = (float) Math.toDegrees(Math.atan2(posEnd.getZ() - posStart.getZ(), posEnd.getX() - posStart.getX()));
                    final RailAngle rigidCatenaryAngleStart = RailAngle.fromAngle(angle1 + (RailAngle.similarFacing(angleDifference, angle1) ? 0 : 180));
                    final RailAngle rigidCatenaryAngleEnd = RailAngle.fromAngle(angle2 + (RailAngle.similarFacing(angleDifference, angle2) ? 180 : 0));
                    onConnect(world, context.getItemInHand(), stateStart, stateEnd, posStart, posEnd, rigidCatenaryAngleStart, rigidCatenaryAngleEnd, rigidCatenaryData, player);
                }
            } else {
                onRemove(world, posStart, posEnd, rigidCatenaryData);
            }
        }
    }

    @Override
    protected boolean clickCondition(UseOnContext context) {
        final Level world = context.getLevel();
        final Block blockStart = world.getBlockState(context.getClickedPos()).getBlock();
        return blockStart instanceof BlockRigidCatenaryNode;
    }

    protected abstract void onConnect(Level world, ItemStack stack, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, RailAngle facingStart, RailAngle facingEnd, RigidCatenaryData rigidCatenaryData, Player player);

    protected abstract void onRemove(Level world, BlockPos posStart, BlockPos posEnd, RigidCatenaryData rigidCatenaryData);

}
