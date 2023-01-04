package top.mcmtr.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import top.mcmtr.blocks.BlockCatenaryNode;
import top.mcmtr.data.Catenary;
import top.mcmtr.data.CatenaryData;
import top.mcmtr.data.CatenaryType;
import top.mcmtr.packet.MSDPacketTrainDataGuiServer;

public class ItemCatenaryModifier extends ItemMSDNodeModifierBase {
    private final CatenaryType catenaryType;

    public ItemCatenaryModifier() {
        super(false);
        this.catenaryType = null;
    }

    public ItemCatenaryModifier(boolean isConnector, CatenaryType catenaryType) {
        super(isConnector);
        this.catenaryType = catenaryType;
    }

    @Override
    protected void onConnect(Level world, ItemStack stack, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, CatenaryData catenaryData) {
        final Catenary catenary1 = new Catenary(posStart, posEnd, catenaryType);
        final Catenary catenary2 = new Catenary(posEnd, posStart, catenaryType);
        if(!catenaryData.addCatenary(posStart, posEnd, catenary1)){
            return;
        }
        catenaryData.addCatenary(posEnd, posStart, catenary2);
        world.setBlockAndUpdate(posStart, stateStart.setValue(BlockCatenaryNode.IS_CONNECTED, true));
        world.setBlockAndUpdate(posEnd, stateEnd.setValue(BlockCatenaryNode.IS_CONNECTED, true));
        MSDPacketTrainDataGuiServer.createCatenaryS2C(world, posStart, posEnd, catenary1, catenary2);
    }

    @Override
    protected void onRemove(Level world, BlockPos posStart, BlockPos posEnd, CatenaryData catenaryData) {
        catenaryData.removeCatenaryConnection(posStart, posEnd);
        MSDPacketTrainDataGuiServer.removeCatenaryConnectionS2C(world, posStart, posEnd);
    }
}