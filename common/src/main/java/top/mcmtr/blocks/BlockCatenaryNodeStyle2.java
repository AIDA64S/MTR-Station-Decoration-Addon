package top.mcmtr.blocks;

import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import top.mcmtr.MSDBlockEntityTypes;
import top.mcmtr.data.CatenaryData;
import top.mcmtr.packet.MSDPacketTrainDataGuiServer;

public class BlockCatenaryNodeStyle2 extends BlockNodeBase {
    public BlockCatenaryNodeStyle2(Properties properties) {
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
        return new BlockCatenaryNodeStyle2Entity(blockPos, blockState);
    }

    public static class BlockCatenaryNodeStyle2Entity extends BlockNodeBaseEntity{
        public BlockCatenaryNodeStyle2Entity(BlockPos pos, BlockState state) {
            super(MSDBlockEntityTypes.BLOCK_CATENARY_NODE_STYLE_2_ENTITY.get(), pos, state);
        }
    }
}