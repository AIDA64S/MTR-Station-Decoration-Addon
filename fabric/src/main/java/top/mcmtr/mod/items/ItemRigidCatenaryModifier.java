package top.mcmtr.mod.items;

import org.mtr.mapping.holder.ActionResult;
import org.mtr.mapping.holder.ItemSettings;
import org.mtr.mapping.holder.ItemUsageContext;
import org.mtr.mapping.mapper.ItemExtension;
import top.mcmtr.core.data.RigidCatenary;
import top.mcmtr.mod.client.MSDMinecraftClientData;
import top.mcmtr.mod.packet.MSDClientPacketHelper;

public class ItemRigidCatenaryModifier extends ItemExtension {
    public ItemRigidCatenaryModifier(ItemSettings itemSettings) {
        super(itemSettings);
    }

    @Override
    public ActionResult useOnBlock2(ItemUsageContext context) {
        if (context.getWorld().isClient()) {
            final RigidCatenary rigidCatenary = MSDMinecraftClientData.getInstance().getFacingRigidCatenary();
            if (rigidCatenary == null) {
                return ActionResult.FAIL;
            } else {
                MSDClientPacketHelper.openRigidCatenaryShapeModifierScreen(rigidCatenary.getHexId());
                return ActionResult.SUCCESS;
            }
        } else {
            return ActionResult.FAIL;
        }
    }
}