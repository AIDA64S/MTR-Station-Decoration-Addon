package top.mcmtr.items;

import mtr.data.RailAngle;
import mtr.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import top.mcmtr.blocks.BlockRigidCatenaryNode;
import top.mcmtr.data.CatenaryType;
import top.mcmtr.data.RigidCatenary;
import top.mcmtr.data.RigidCatenaryData;
import top.mcmtr.packet.MSDPacketTrainDataGuiServer;

public class ItemRigidCatenaryModifier extends ItemMSDOriginNodeModifierBase {
    private final CatenaryType catenaryType;

    public ItemRigidCatenaryModifier() {
        super(false);
        this.catenaryType = null;
    }

    public ItemRigidCatenaryModifier(boolean isConnector, CatenaryType catenaryType) {
        super(isConnector);
        this.catenaryType = catenaryType;
    }

    @Override
    protected void onConnect(Level world, ItemStack stack, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, RailAngle facingStart, RailAngle facingEnd, RigidCatenaryData rigidCatenaryData, Player player) {
        final RigidCatenary catenary1 = new RigidCatenary(posStart, facingStart, posEnd, facingEnd, catenaryType);
        final RigidCatenary catenary2 = new RigidCatenary(posEnd, facingEnd, posStart, facingStart, catenaryType);
        final boolean goodRadius = catenary1.goodRadius() && catenary2.goodRadius();
        final boolean isValid = catenary1.isValid() && catenary2.isValid();
        if (goodRadius && isValid) {
            rigidCatenaryData.addRigidCatenary(posStart, posEnd, catenary1);
            rigidCatenaryData.addRigidCatenary(posEnd, posStart, catenary2);
            world.setBlockAndUpdate(posStart, stateStart.setValue(BlockRigidCatenaryNode.IS_CONNECTED, true));
            world.setBlockAndUpdate(posEnd, stateEnd.setValue(BlockRigidCatenaryNode.IS_CONNECTED, true));
            MSDPacketTrainDataGuiServer.createRigidCatenaryS2C(world, posStart, posEnd, catenary1, catenary2);
        } else if (player != null) {
            player.displayClientMessage(Text.translatable(goodRadius ? "gui.mtr.invalid_orientation" : "gui.mtr.radius_too_small"), true);
        }
    }

    @Override
    protected void onRemove(Level world, BlockPos posStart, BlockPos posEnd, RigidCatenaryData rigidCatenaryData) {
        rigidCatenaryData.removeRigidCatenaryConnection(posStart, posEnd);
        MSDPacketTrainDataGuiServer.removeRigidCatenaryConnectionS2C(world, posStart, posEnd);
    }
}
