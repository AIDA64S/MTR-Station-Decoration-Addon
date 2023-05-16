package top.mcmtr.blocks;

import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import top.mcmtr.MSDBlockEntityTypes;
import top.mcmtr.data.CatenaryData;
import top.mcmtr.data.TransCatenaryData;
import top.mcmtr.packet.MSDPacketTrainDataGuiServer;

public class BlockTransCatenaryNode extends BlockNodeBase {
    public BlockTransCatenaryNode() {
        super(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F));
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide) {
            final TransCatenaryData catenaryData = TransCatenaryData.getInstance(world);
            final CatenaryData catenaryData1 = CatenaryData.getInstance(world);
            if (catenaryData != null) {
                catenaryData.removeTransCatenaryNode(pos);
                MSDPacketTrainDataGuiServer.removeTransCatenaryNodeS2C(world, pos);
            }
            if (catenaryData1 != null) {
                catenaryData1.removeCatenaryNode(pos);
                MSDPacketTrainDataGuiServer.removeCatenaryNodeS2C(world, pos);
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        if (blockState.getValue(IS_CONNECTED)) {
            return RenderShape.INVISIBLE;
        }
        return super.getRenderShape(blockState);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockTransCatenaryNodeEntity(blockPos, blockState);
    }

    public static class BlockTransCatenaryNodeEntity extends BlockNodeBaseEntity {
        public BlockTransCatenaryNodeEntity(BlockPos pos, BlockState state) {
            super(MSDBlockEntityTypes.BLOCK_TRANS_CATENARY_NODE_ENTITY.get(), pos, state);
        }
    }
}