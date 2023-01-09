package top.mcmtr.blocks;

import mtr.block.IBlock;
import mtr.mappings.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import top.mcmtr.MSDBlockEntityTypes;
import top.mcmtr.MSDBlocks;
import top.mcmtr.packet.MSDPacketTrainDataGuiServer;

import java.util.*;

public class BlockYamanoteRailwaySign extends BlockDirectionalMapper implements EntityBlockMapper, IBlock {
    public final int length;
    public final boolean isOdd;

    public static final float SMALL_SIGN_PERCENTAGE = 0.75F;

    public BlockYamanoteRailwaySign(int length, boolean isOdd) {
        super(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2).lightLevel(state -> 15));
        this.length = length;
        this.isOdd = isOdd;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return IBlock.checkHoldingBrush(level, player, () -> {
            final Direction facing = IBlock.getStatePropertySafe(blockState, FACING);
            final Direction hitSide = blockHitResult.getDirection();
            if (hitSide == facing || hitSide == facing.getOpposite()) {
                final BlockPos checkPos = findEndWithDirection(level, blockPos, hitSide.getOpposite(), false);
                if (checkPos != null) {
                    MSDPacketTrainDataGuiServer.openYamanoteRailwaySignScreenS2C((ServerPlayer) player, checkPos);
                }
            }
        });
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final boolean isNext = direction == facing.getClockWise() || state.is(MSDBlocks.YAMANOTE_RAILWAY_SIGN_MIDDLE.get()) && direction == facing.getCounterClockWise();
        if (isNext && !(newState.getBlock() instanceof BlockYamanoteRailwaySign)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return state;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        final Direction facing = blockPlaceContext.getHorizontalDirection();
        return IBlock.isReplaceable(blockPlaceContext, facing.getClockWise(), getMiddleLength() + 2) ? defaultBlockState().setValue(FACING, facing) : null;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        final Direction facing = IBlock.getStatePropertySafe(blockState, FACING);
        final BlockPos checkPos = findEndWithDirection(level, blockPos, facing, true);
        if (checkPos != null) {
            IBlock.onBreakCreative(level, player, checkPos);
        }
        super.playerWillDestroy(level, blockPos, blockState, player);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        if (!level.isClientSide) {
            final Direction facing = IBlock.getStatePropertySafe(blockState, FACING);
            for (int i = 1; i <= getMiddleLength(); i++) {
                level.setBlock(blockPos.relative(facing.getClockWise(), i), MSDBlocks.YAMANOTE_RAILWAY_SIGN_MIDDLE.get().defaultBlockState().setValue(FACING, facing), 3);
            }
            level.setBlock(blockPos.relative(facing.getClockWise(), getMiddleLength() + 1), defaultBlockState().setValue(FACING, facing.getOpposite()), 3);
            level.updateNeighborsAt(blockPos, Blocks.AIR);
            blockState.updateNeighbourShapes(level, blockPos, 3);
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        final Direction facing = IBlock.getStatePropertySafe(blockState, FACING);
        if (blockState.is(MSDBlocks.YAMANOTE_RAILWAY_SIGN_MIDDLE.get())) {
            return IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 12, 9, facing);
        } else {
            final int xStart = getXStart();
            final VoxelShape main = IBlock.getVoxelShapeByDirection(xStart - 1, 0, 7, 16, 12, 9, facing);
            final VoxelShape pole = IBlock.getVoxelShapeByDirection(xStart - 2, 0, 7.5, xStart - 1, 16, 8.5, facing);
            return Shapes.or(main, pole);
        }
    }

    @Override
    public String getDescriptionId() {
        return "block.msd.yamanote_railway_sign";
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Text.translatable("tooltip.msd.yamanote_railway_sign_length", length).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        tooltip.add(Text.translatable(isOdd ? "tooltip.msd.yamanote_railway_sign_odd" : "tooltip.msd.yamanote_railway_sign_even").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (this == MSDBlocks.YAMANOTE_RAILWAY_SIGN_MIDDLE.get()) {
            return null;
        } else {
            return new TileEntityRailwaySign(length, isOdd, blockPos, blockState);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
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

    private BlockPos findEndWithDirection(Level world, BlockPos startPos, Direction direction, boolean allowOpposite) {
        int i = 0;
        while (true) {
            final BlockPos checkPos = startPos.relative(direction.getCounterClockWise(), i);
            final BlockState checkState = world.getBlockState(checkPos);
            if (checkState.getBlock() instanceof BlockYamanoteRailwaySign) {
                final Direction facing = IBlock.getStatePropertySafe(checkState, FACING);
                if (!checkState.is(MSDBlocks.YAMANOTE_RAILWAY_SIGN_MIDDLE.get()) && (facing == direction || allowOpposite && facing == direction.getOpposite())) {
                    return checkPos;
                }
            } else {
                return null;
            }
            i++;
        }
    }

    public static class TileEntityRailwaySign extends BlockEntityClientSerializableMapper {
        private final Set<Long> selectedIds;
        private final String[] signIds;
        private static final String KEY_SELECTED_IDS = "yamanote_selected_ids";
        private static final String KEY_SIGN_LENGTH = "yamanote_sign_length";

        public TileEntityRailwaySign(int length, boolean isOdd, BlockPos pos, BlockState state) {
            super(getType(length, isOdd), pos, state);
            this.signIds = new String[length];
            this.selectedIds = new HashSet<>();
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            selectedIds.clear();
            Arrays.stream(compoundTag.getLongArray(KEY_SELECTED_IDS)).forEach(selectedIds::add);
            for (int i = 0; i < signIds.length; i++) {
                final String signId = compoundTag.getString(KEY_SIGN_LENGTH + i);
                signIds[i] = signId.isEmpty() ? null : signId;
            }
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putLongArray(KEY_SELECTED_IDS, new ArrayList<>(selectedIds));
            for (int i = 0; i < signIds.length; i++) {
                compoundTag.putString(KEY_SIGN_LENGTH + i, signIds[i] == null ? "" : signIds[i]);
            }
        }

        public void setData(Set<Long> selectedIds, String[] signTypes) {
            this.selectedIds.clear();
            this.selectedIds.addAll(selectedIds);
            if (signIds.length == signTypes.length) {
                System.arraycopy(signTypes, 0, signIds, 0, signTypes.length);
            }
            setChanged();
            syncData();
        }

        public Set<Long> getSelectedIds() {
            return selectedIds;
        }

        public String[] getSignIds() {
            return signIds;
        }

        private static BlockEntityType<?> getType(int length, boolean isOdd) {
            switch (length) {
                case 2:
                    return isOdd ? MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_2_ODD_TILE_ENTITY.get() : MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_2_EVEN_TILE_ENTITY.get();
                case 3:
                    return isOdd ? MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_3_ODD_TILE_ENTITY.get() : MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_3_EVEN_TILE_ENTITY.get();
                case 4:
                    return isOdd ? MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_4_ODD_TILE_ENTITY.get() : MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_4_EVEN_TILE_ENTITY.get();
                case 5:
                    return isOdd ? MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_5_ODD_TILE_ENTITY.get() : MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_5_EVEN_TILE_ENTITY.get();
                case 6:
                    return isOdd ? MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_6_ODD_TILE_ENTITY.get() : MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_6_EVEN_TILE_ENTITY.get();
                case 7:
                    return isOdd ? MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_7_ODD_TILE_ENTITY.get() : MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_7_EVEN_TILE_ENTITY.get();
                default:
                    return null;
            }
        }
    }
}