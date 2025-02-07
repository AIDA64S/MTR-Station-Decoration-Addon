package top.mcmtr.mod.blocks;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockDirectionalDoubleBlockBase;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.data.TicketSystem;
import org.mtr.mod.packet.PacketOpenTicketMachineScreen;

import java.util.List;

public final class BlockYuuniTicket extends BlockDirectionalDoubleBlockBase {

    public BlockYuuniTicket() {
        super(Blocks.createDefaultBlockSettings(true, blockState -> 5));
    }

    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient()) {
            Init.REGISTRY.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenTicketMachineScreen(TicketSystem.getBalance(world, player)));
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final int height = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 15 : 16;
        return IBlock.getVoxelShapeByDirection(0, 0, 2, 16, height, 14, facing);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(HALF);
    }
}