package top.mcmtr.mod.blocks;

import org.jetbrains.annotations.NotNull;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.IBlock;
import top.mcmtr.mod.BlockEntityTypes;
import top.mcmtr.mod.Blocks;
import top.mcmtr.mod.Init;
import top.mcmtr.mod.packet.MSDPacketOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class BlockYamanoteRailwaySign extends BlockExtension implements IBlock, DirectionHelper, BlockWithEntity {
    public final int length;
    public final boolean isOdd;
    public static final float SMALL_SIGN_PERCENTAGE = 0.75F;

    public BlockYamanoteRailwaySign(int length, boolean isOdd) {
        super(BlockHelper.createBlockSettings(true, blockState -> 15));
        this.length = length;
        this.isOdd = isOdd;
    }

    @NotNull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlock.checkHoldingBrush(world, player, () -> {
            final Direction facing = IBlock.getStatePropertySafe(state, FACING);
            final Direction hitSide = hit.getSide();
            if (hitSide == facing || hitSide == facing.getOpposite()) {
                final BlockPos checkPos = findEndWithDirection(world, pos, hitSide.getOpposite(), false);
                if (checkPos != null) {
                    Init.REGISTRY.sendPacketToClient(ServerPlayerEntity.cast(player), new MSDPacketOpenBlockEntityScreen(checkPos));
                }
            }
        });
    }

    @NotNull
    @Override
    public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final boolean isNext = direction == facing.rotateYClockwise() || state.isOf(Blocks.YAMANOTE_RAILWAY_SIGN_MIDDLE.get()) && direction == facing.rotateYCounterclockwise();
        if (isNext && !(neighborState.getBlock().data instanceof BlockYamanoteRailwaySign)) {
            return org.mtr.mapping.holder.Blocks.getAirMapped().getDefaultState();
        } else {
            return state;
        }
    }

    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        final Direction facing = ctx.getPlayerFacing();
        return IBlock.isReplaceable(ctx, facing.rotateYClockwise(), getMiddleLength() + 2) ? getDefaultState2().with(new Property<>(FACING.data), facing.data) : null;
    }

    @Override
    public void onBreak3(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final BlockPos checkPos = findEndWithDirection(world, pos, facing, true);
        if (checkPos != null) {
            IBlock.onBreakCreative(world, player, checkPos);
        }
        super.onBreak3(world, pos, state, player);
    }

    @Override
    public void onPlaced2(World world, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient()) {
            final Direction facing = IBlock.getStatePropertySafe(state, FACING);
            for (int i = 1; i <= getMiddleLength(); i++) {
                world.setBlockState(pos.offset(facing.rotateYClockwise(), i), Blocks.YAMANOTE_RAILWAY_SIGN_MIDDLE.get().getDefaultState().with(new Property<>(FACING.data), facing.data), 3);
            }
            world.setBlockState(pos.offset(facing.rotateYClockwise(), getMiddleLength() + 1), getDefaultState2().with(new Property<>(FACING.data), facing.getOpposite().data), 3);
            world.updateNeighbors(pos, org.mtr.mapping.holder.Blocks.getAirMapped());
            state.updateNeighbors(new WorldAccess(world.data), pos, 3);
        }
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        if (state.isOf(Blocks.YAMANOTE_RAILWAY_SIGN_MIDDLE.get())) {
            return IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 12, 9, facing);
        } else {
            final int xStart = getXStart();
            final VoxelShape main = IBlock.getVoxelShapeByDirection(xStart - 0.75, 0, 7, 16, 12, 9, facing);
            final VoxelShape pole = IBlock.getVoxelShapeByDirection(xStart - 2, 0, 7, xStart - 0.75, 16, 9, facing);
            return VoxelShapes.union(main, pole);
        }
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
    }

    @Nonnull
    @Override
    public String getTranslationKey2() {
        return "block.msd.yamanote_railway_sign";
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
        tooltip.add(TextHelper.translatable("tooltip.msd.yamanote_railway_sign_length", length).formatted(TextFormatting.GRAY));
        tooltip.add(TextHelper.translatable(isOdd ? "tooltip.msd.yamanote_railway_sign_odd" : "tooltip.msd.yamanote_railway_sign_even").formatted(TextFormatting.GRAY));
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (this == Blocks.YAMANOTE_RAILWAY_SIGN_MIDDLE.get().data) {
            return null;
        }
        return new BlockYamanoteRailwaySignEntity(length, isOdd, blockPos, blockState);
    }

    public int getXStart() {
        switch (length % 4) {
            default:
                return isOdd ? 8 : 16;
            case 1:
                return isOdd ? 4 : 12;
            case 2:
                return isOdd ? 16 : 8;
            case 3:
                return isOdd ? 12 : 4;
        }
    }

    private int getMiddleLength() {
        return (length - (4 - getXStart() / 4)) / 2;
    }

    private BlockPos findEndWithDirection(World world, BlockPos startPos, Direction direction, boolean allowOpposite) {
        int i = 0;
        while (true) {
            final BlockPos checkPos = startPos.offset(direction.rotateYCounterclockwise(), i);
            final BlockState checkState = world.getBlockState(checkPos);
            if (checkState.getBlock().data instanceof BlockYamanoteRailwaySign) {
                final Direction facing = IBlock.getStatePropertySafe(checkState, FACING);
                if (!checkState.isOf(Blocks.YAMANOTE_RAILWAY_SIGN_MIDDLE.get()) && (facing == direction || allowOpposite && facing == direction.getOpposite())) {
                    return checkPos;
                }
            } else {
                return null;
            }
            i++;
        }
    }

    public static class BlockYamanoteRailwaySignEntity extends BlockEntityExtension {
        private final LongAVLTreeSet selectedIds;
        private final String[] signIds;
        private static final String KEY_SELECTED_IDS = "yamanote_selected_ids";
        private static final String KEY_SIGN_LENGTH = "yamanote_sign_length";

        public BlockYamanoteRailwaySignEntity(int length, boolean isOdd, BlockPos pos, BlockState state) {
            super(getType(length, isOdd), pos, state);
            signIds = new String[length];
            selectedIds = new LongAVLTreeSet();
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            selectedIds.clear();
            Arrays.stream(compoundTag.getLongArray(KEY_SELECTED_IDS)).forEach(selectedIds::add);
            for (int i = 0; i < signIds.length; i++) {
                final String signId = compoundTag.getString(KEY_SIGN_LENGTH + i);
                signIds[i] = signId.isEmpty() ? null : signId.toLowerCase(Locale.ENGLISH);
            }
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putLongArray(KEY_SELECTED_IDS, new ArrayList<>(selectedIds));
            for (int i = 0; i < signIds.length; i++) {
                compoundTag.putString(KEY_SIGN_LENGTH + i, signIds[i] == null ? "" : signIds[i]);
            }
        }

        public void setData(LongAVLTreeSet selectedIds, String[] signTypes) {
            this.selectedIds.clear();
            this.selectedIds.addAll(selectedIds);
            if (signIds.length == signTypes.length) {
                System.arraycopy(signTypes, 0, signIds, 0, signTypes.length);
            }
            markDirty2();
        }

        public LongAVLTreeSet getSelectedIds() {
            return selectedIds;
        }

        public String[] getSignIds() {
            return signIds;
        }

        private static BlockEntityType<? extends BlockEntityExtension> getType(int length, boolean isOdd) {
            switch (length) {
                case 2:
                    return isOdd ? BlockEntityTypes.YAMANOTE_RAILWAY_SIGN_ENTITY_2_ODD.get() : BlockEntityTypes.YAMANOTE_RAILWAY_SIGN_ENTITY_2_EVEN.get();
                case 3:
                    return isOdd ? BlockEntityTypes.YAMANOTE_RAILWAY_SIGN_ENTITY_3_ODD.get() : BlockEntityTypes.YAMANOTE_RAILWAY_SIGN_ENTITY_3_EVEN.get();
                case 4:
                    return isOdd ? BlockEntityTypes.YAMANOTE_RAILWAY_SIGN_ENTITY_4_ODD.get() : BlockEntityTypes.YAMANOTE_RAILWAY_SIGN_ENTITY_4_EVEN.get();
                case 5:
                    return isOdd ? BlockEntityTypes.YAMANOTE_RAILWAY_SIGN_ENTITY_5_ODD.get() : BlockEntityTypes.YAMANOTE_RAILWAY_SIGN_ENTITY_5_EVEN.get();
                case 6:
                    return isOdd ? BlockEntityTypes.YAMANOTE_RAILWAY_SIGN_ENTITY_6_ODD.get() : BlockEntityTypes.YAMANOTE_RAILWAY_SIGN_ENTITY_6_EVEN.get();
                case 7:
                    return isOdd ? BlockEntityTypes.YAMANOTE_RAILWAY_SIGN_ENTITY_7_ODD.get() : BlockEntityTypes.YAMANOTE_RAILWAY_SIGN_ENTITY_7_EVEN.get();
                default:
                    return BlockEntityTypes.YAMANOTE_RAILWAY_SIGN_ENTITY_2_EVEN.get();
            }
        }
    }
}