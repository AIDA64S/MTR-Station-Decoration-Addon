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
import top.mcmtr.data.Catenary;
import top.mcmtr.data.CatenaryData;
import top.mcmtr.data.CatenaryType;
import top.mcmtr.packet.MSDPacketTrainDataGuiServer;

public class ItemCatenaryModifier extends ItemMSDBlockClickingBase {
    private final CatenaryType catenaryType;
    private final boolean isConnector;

    public ItemCatenaryModifier() {
        super(MSDCreativeModeTabs.MSD_BLOCKS, properties -> properties.stacksTo(1));
        this.catenaryType = null;
        this.isConnector = false;
    }

    public ItemCatenaryModifier(boolean isConnector, CatenaryType catenaryType) {
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
        final CatenaryData catenaryData = CatenaryData.getInstance(world);
        final BlockPos posStart = context.getClickedPos();
        final BlockState stateStart = world.getBlockState(posStart);
        final BlockState stateEnd = world.getBlockState(posEnd);
        if (catenaryData != null && (stateEnd.getBlock() instanceof BlockNodeBase || stateEnd.getBlock() instanceof BlockRigidCatenaryNode)) {
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
        return blockStart instanceof BlockNodeBase || blockStart instanceof BlockRigidCatenaryNode;
    }

    private void onConnect(Level world, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, CatenaryData catenaryData) {
        final Catenary catenary1 = new Catenary(posStart, posEnd, catenaryType);
        final Catenary catenary2 = new Catenary(posEnd, posStart, catenaryType);
        if (!catenaryData.addCatenary(posStart, posEnd, catenary1)) {
            return;
        }
        catenaryData.addCatenary(posEnd, posStart, catenary2);
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
        MSDPacketTrainDataGuiServer.createCatenaryS2C(world, posStart, posEnd, catenary1, catenary2);
    }

    private void onRemove(Level world, BlockPos posStart, BlockPos posEnd, CatenaryData catenaryData) {
        catenaryData.removeCatenaryConnection(posStart, posEnd);
        MSDPacketTrainDataGuiServer.removeCatenaryConnectionS2C(world, posStart, posEnd);
    }
}