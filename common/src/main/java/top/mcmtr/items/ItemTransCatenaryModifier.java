package top.mcmtr.items;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import top.mcmtr.MSDCreativeModeTabs;
import top.mcmtr.blocks.BlockNodeBase;
import top.mcmtr.blocks.BlockRigidCatenaryNode;
import top.mcmtr.data.CatenaryType;
import top.mcmtr.data.TransCatenary;
import top.mcmtr.data.TransCatenaryData;
import top.mcmtr.packet.MSDPacketTrainDataGuiServer;

public class ItemTransCatenaryModifier extends ItemMSDBlockClickingBase {
    private final CatenaryType catenaryType;
    private final boolean isConnector;

    public ItemTransCatenaryModifier() {
        super(MSDCreativeModeTabs.MSD_BLOCKS, properties -> properties.stacksTo(1));
        this.catenaryType = null;
        this.isConnector = false;
    }

    public ItemTransCatenaryModifier(boolean isConnector, CatenaryType catenaryType) {
        super(MSDCreativeModeTabs.MSD_BLOCKS, properties -> properties.stacksTo(1));
        this.catenaryType = catenaryType;
        this.isConnector = isConnector;
    }


    @Override
    protected void onStartClick(UseOnContext context, CompoundTag compoundTag) {
    }

    @Override
    protected void onEndClick(UseOnContext context, BlockPos posEnd, CompoundTag compoundTag) {
        final Level world = context.getLevel();
        final TransCatenaryData catenaryData = TransCatenaryData.getInstance(world);
        final BlockPos posStart = context.getClickedPos();
        final BlockState stateStart = world.getBlockState(posStart);
        final BlockState stateEnd = world.getBlockState(posEnd);
        if ((stateStart.getBlock() instanceof BlockNodeBase) && (stateEnd.getBlock() instanceof BlockNodeBase) && (stateStart.getBlock() instanceof BlockRigidCatenaryNode) && (stateEnd.getBlock() instanceof BlockRigidCatenaryNode)) {
            return;
        }
        if (catenaryData != null && stateEnd.getBlock() instanceof BlockNodeBase) {
            if (isConnector) {
                if (!posStart.equals(posEnd)) {
                    onConnect(world, stateStart, stateEnd, posStart, posEnd, catenaryData);
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
        return blockStart instanceof BlockNodeBase;
    }

    private void onConnect(Level world, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, TransCatenaryData catenaryData) {
        final BlockNodeBase.BlockNodeBaseEntity entityStart = (BlockNodeBase.BlockNodeBaseEntity) world.getBlockEntity(posStart);
        final BlockNodeBase.BlockNodeBaseEntity entityEnd = (BlockNodeBase.BlockNodeBaseEntity) world.getBlockEntity(posEnd);
        final TransCatenary catenary1 = new TransCatenary(posStart, posEnd, entityStart.getPointLocation(), entityEnd.getPointLocation(), catenaryType);
        final TransCatenary catenary2 = new TransCatenary(posEnd, posStart, entityEnd.getPointLocation(), entityStart.getPointLocation(), catenaryType);
        if (!catenaryData.addTransCatenary(posStart, posEnd, catenary1)) {
            return;
        }
        catenaryData.addTransCatenary(posEnd, posStart, catenary2);
        if (stateStart.getBlock() instanceof BlockNodeBase) {
            world.setBlockAndUpdate(posStart, stateStart.setValue(BlockNodeBase.IS_CONNECTED, true));
        } else {
            world.setBlockAndUpdate(posStart, stateStart.setValue(BlockRigidCatenaryNode.IS_CONNECTED, true));
        }
        if (stateEnd.getBlock() instanceof BlockNodeBase) {
            world.setBlockAndUpdate(posEnd, stateEnd.setValue(BlockNodeBase.IS_CONNECTED, true));
        } else {
            world.setBlockAndUpdate(posEnd, stateEnd.setValue(BlockRigidCatenaryNode.IS_CONNECTED, true));
        }
        MSDPacketTrainDataGuiServer.createTransCatenaryS2C(world, posStart, posEnd, catenary1, catenary2);
    }

    private void onRemove(Level world, BlockPos posStart, BlockPos posEnd, TransCatenaryData catenaryData) {
        catenaryData.removeTransCatenaryConnection(posStart, posEnd);
        MSDPacketTrainDataGuiServer.removeTransCatenaryConnectionS2C(world, posStart, posEnd);
    }
}