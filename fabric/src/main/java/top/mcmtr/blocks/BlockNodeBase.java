package top.mcmtr.blocks;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;

import java.util.List;

public abstract class BlockNodeBase extends BlockExtension implements DirectionHelper {
    public BlockNodeBase(BlockSettings blockSettings) {
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
}