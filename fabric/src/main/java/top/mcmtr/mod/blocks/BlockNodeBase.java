package top.mcmtr.mod.blocks;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.tool.HolderBase;
import top.mcmtr.core.data.OffsetPosition;

import java.util.List;

public abstract class BlockNodeBase extends BlockExtension implements BlockWithEntity {
    public BlockNodeBase() {
        super(BlockHelper.createBlockSettings(true).nonOpaque());
    }

    public static final BooleanProperty IS_CONNECTED = BooleanProperty.of("is_connected");

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(IS_CONNECTED);
    }

    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        return getDefaultState2().with(new Property<>(IS_CONNECTED.data), false);
    }

    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }

    @Override
    public VoxelShape getCollisionShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    public abstract static class BlockNodeBaseEntity extends BlockEntityExtension {
        private static final String KEY_OFFSET_POSITION = "msd_offset_position_";
        private final OffsetPosition offsetPosition;

        public BlockNodeBaseEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
            super(type, blockPos, blockState);
            this.offsetPosition = new OffsetPosition(0, 0, 0);
        }

        public void setOffsetPosition(OffsetPosition offsetPosition) {
            this.offsetPosition.setX(offsetPosition.getX());
            this.offsetPosition.setY(offsetPosition.getY());
            this.offsetPosition.setZ(offsetPosition.getZ());
        }

        public OffsetPosition getOffsetPosition() {
            return this.offsetPosition;
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            this.offsetPosition.setX(compoundTag.getDouble(KEY_OFFSET_POSITION + "x"));
            this.offsetPosition.setY(compoundTag.getDouble(KEY_OFFSET_POSITION + "y"));
            this.offsetPosition.setZ(compoundTag.getDouble(KEY_OFFSET_POSITION + "z"));
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putDouble(KEY_OFFSET_POSITION + "x", this.offsetPosition.getX());
            compoundTag.putDouble(KEY_OFFSET_POSITION + "y", this.offsetPosition.getY());
            compoundTag.putDouble(KEY_OFFSET_POSITION + "z", this.offsetPosition.getZ());
        }
    }
}