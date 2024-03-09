package top.mcmtr.mod.items;

import org.mtr.core.data.Position;
import org.mtr.core.data.TwoPositionsBase;
import org.mtr.core.tool.Angle;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import top.mcmtr.core.data.CatenaryType;
import top.mcmtr.core.data.RigidCatenary;
import top.mcmtr.mod.Init;
import top.mcmtr.mod.blocks.BlockNodeBase;
import top.mcmtr.mod.blocks.BlockRigidCatenaryNode;
import top.mcmtr.mod.packet.MSDPacketDeleteData;
import top.mcmtr.mod.packet.MSDPacketUpdateData;

import javax.annotation.Nullable;

public final class ItemRigidCatenaryConnector extends ItemBlockClickingBase {
    public ItemRigidCatenaryConnector(ItemSettings itemSettings, boolean isConnector) {
        super(itemSettings, isConnector, CatenaryType.RIGID_CATENARY);
    }

    @Override
    protected void onConnect(World world, ItemStack stack, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, CatenaryType catenaryType, @Nullable ServerPlayerEntity player) {
        final Position positionStart = Init.blockPosToPosition(posStart);
        final Position positionEnd = Init.blockPosToPosition(posEnd);
        if (RigidCatenary.verifyPosition(positionStart, positionEnd)) {
            final ObjectObjectImmutablePair<Angle, Angle> angles = getAngles(posStart, BlockRigidCatenaryNode.getAngle(stateStart), posEnd, BlockRigidCatenaryNode.getAngle(stateEnd));
            final RigidCatenary rigidCatenary = new RigidCatenary(positionStart, angles.left(), positionEnd, angles.right(), RigidCatenary.Shape.QUADRATIC, 0);
            world.setBlockState(posStart, stateStart.with(new Property<>(BlockNodeBase.IS_CONNECTED.data), true));
            world.setBlockState(posEnd, stateEnd.with(new Property<>(BlockNodeBase.IS_CONNECTED.data), true));
            MSDPacketUpdateData.sendDirectlyToServerRigidCatenary(ServerWorld.cast(world), rigidCatenary);
        }
    }

    @Override
    protected void onRemove(World world, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayerEntity player) {
        MSDPacketDeleteData.sendDirectlyToServerRigidCatenaryId(ServerWorld.cast(world), TwoPositionsBase.getHexId(Init.blockPosToPosition(posStart), Init.blockPosToPosition(posEnd)));
    }

    @Override
    protected boolean clickCondition(ItemUsageContext context) {
        final World world = context.getWorld();
        final Block blockStart = world.getBlockState(context.getBlockPos()).getBlock();
        return blockStart.data instanceof BlockRigidCatenaryNode;
    }

    public static ObjectObjectImmutablePair<Angle, Angle> getAngles(BlockPos posStart, float angle1, BlockPos posEnd, float angle2) {
        final float angleDifference = (float) Math.toDegrees(Math.atan2(posEnd.getZ() - posStart.getZ(), posEnd.getX() - posStart.getX()));
        return new ObjectObjectImmutablePair<>(
                Angle.fromAngle(angle1 + (Angle.similarFacing(angleDifference, angle1) ? 0 : 180)),
                Angle.fromAngle(angle2 + (Angle.similarFacing(angleDifference, angle2) ? 180 : 0))
        );
    }
}