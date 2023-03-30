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
import top.mcmtr.data.CatenaryType;
import top.mcmtr.data.RigidCatenary;
import top.mcmtr.data.RigidCatenaryData;
import top.mcmtr.packet.MSDPacketTrainDataGuiServer;

import java.util.List;

public class ItemRigidCatenaryModifier extends ItemMSDBlockClickingBase {
    private final CatenaryType catenaryType;
    private final boolean isConnector;

    public ItemRigidCatenaryModifier() {
        super(MSDCreativeModeTabs.MSD_BLOCKS, properties -> properties.stacksTo(1));
        this.catenaryType = null;
        this.isConnector = false;
    }

    public ItemRigidCatenaryModifier(boolean isConnector, CatenaryType catenaryType) {
        super(MSDCreativeModeTabs.MSD_BLOCKS, properties -> properties.stacksTo(1));
        this.catenaryType = catenaryType;
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
                    onConnect(world, stateStart, stateEnd, posStart, posEnd, rigidCatenaryAngleStart, rigidCatenaryAngleEnd, rigidCatenaryData, player);
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

    private void onConnect(Level world, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, RailAngle facingStart, RailAngle facingEnd, RigidCatenaryData rigidCatenaryData, Player player) {
        final RigidCatenary catenary1 = new RigidCatenary(posStart, facingStart, posEnd, facingEnd, catenaryType);
        final RigidCatenary catenary2 = new RigidCatenary(posEnd, facingEnd, posStart, facingStart, catenaryType);
        final boolean goodRadius = catenary1.goodRadius() && catenary2.goodRadius();
        final boolean isValid = catenary1.isValid() && catenary2.isValid();
        if (goodRadius && isValid) {
            if (!rigidCatenaryData.addRigidCatenary(posStart, posEnd, catenary1)) {
                return;
            }
            rigidCatenaryData.addRigidCatenary(posEnd, posStart, catenary2);
            world.setBlockAndUpdate(posStart, stateStart.setValue(BlockRigidCatenaryNode.IS_CONNECTED, true));
            world.setBlockAndUpdate(posEnd, stateEnd.setValue(BlockRigidCatenaryNode.IS_CONNECTED, true));
            MSDPacketTrainDataGuiServer.createRigidCatenaryS2C(world, posStart, posEnd, catenary1, catenary2);
        } else if (player != null) {
            player.displayClientMessage(Text.translatable(goodRadius ? "gui.msd.invalid_orientation" : "gui.msd.radius_too_small"), true);
        }
    }

    private void onRemove(Level world, BlockPos posStart, BlockPos posEnd, RigidCatenaryData rigidCatenaryData) {
        rigidCatenaryData.removeRigidCatenaryConnection(posStart, posEnd);
        MSDPacketTrainDataGuiServer.removeRigidCatenaryConnectionS2C(world, posStart, posEnd);
    }
}