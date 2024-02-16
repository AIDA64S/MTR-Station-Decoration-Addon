package top.mcmtr.mod.blocks;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockRenderType;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.Property;
import org.mtr.mapping.mapper.BlockEntityExtension;
import top.mcmtr.mod.BlockEntityTypes;

public final class BlockCatenaryNode extends BlockNodeBase {
    public BlockCatenaryNode() {
        super();
    }

    @Override
    public BlockRenderType getRenderType2(BlockState state) {
        if (state.get(new Property<>(IS_CONNECTED.data))) {
            return BlockRenderType.INVISIBLE;
        }
        return super.getRenderType2(state);
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockCatenaryNodeEntity(blockPos, blockState);
    }

    public static class BlockCatenaryNodeEntity extends BlockNodeBaseEntity {
        public BlockCatenaryNodeEntity(BlockPos blockPos, BlockState blockState) {
            super(BlockEntityTypes.CATENARY_NODE.get(), blockPos, blockState);
        }
    }
}