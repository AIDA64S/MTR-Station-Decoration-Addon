package top.mcmtr.mod.blocks;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.tool.HolderBase;
import top.mcmtr.mod.data.OffsetPosition;

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

    @Nullable
    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        return getDefaultState2().with(new Property<>(IS_CONNECTED.data), false);
    }

    @NotNull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }

    @NotNull
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
            this.offsetPosition.x = offsetPosition.x;
            this.offsetPosition.y = offsetPosition.y;
            this.offsetPosition.z = offsetPosition.z;
        }

        public OffsetPosition getOffsetPosition() {
            return this.offsetPosition;
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            this.offsetPosition.x = compoundTag.getDouble(KEY_OFFSET_POSITION + "x");
            this.offsetPosition.y = compoundTag.getDouble(KEY_OFFSET_POSITION + "y");
            this.offsetPosition.z = compoundTag.getDouble(KEY_OFFSET_POSITION + "z");
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putDouble(KEY_OFFSET_POSITION + "x", this.offsetPosition.x);
            compoundTag.putDouble(KEY_OFFSET_POSITION + "y", this.offsetPosition.y);
            compoundTag.putDouble(KEY_OFFSET_POSITION + "z", this.offsetPosition.z);
        }
    }
}