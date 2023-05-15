package top.mcmtr.blocks;

import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import top.mcmtr.MSDBlockEntityTypes;
import top.mcmtr.data.CatenaryData;
import top.mcmtr.packet.MSDPacketTrainDataGuiServer;

public class BlockCatenaryNode extends BlockNodeBase {
    public BlockCatenaryNode(Properties properties) {
        super(properties);
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide) {
            final CatenaryData catenaryData = CatenaryData.getInstance(world);
            if (catenaryData != null) {
                catenaryData.removeCatenaryNode(pos);
                MSDPacketTrainDataGuiServer.removeCatenaryNodeS2C(world, pos);
            }
        }
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockCatenaryNodeEntity(blockPos, blockState);
    }

    public static class BlockCatenaryNodeEntity extends BlockNodeBaseEntity {
        public BlockCatenaryNodeEntity(BlockPos pos, BlockState state) {
            super(MSDBlockEntityTypes.BLOCK_CATENARY_NODE_ENTITY.get(), pos, state);
        }
    }
}