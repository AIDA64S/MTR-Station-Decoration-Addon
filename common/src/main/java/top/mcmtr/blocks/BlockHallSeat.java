package top.mcmtr.blocks;

import mtr.block.IBlock;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class BlockHallSeat extends Block {
    private final SeatLocation location;

    public BlockHallSeat(SeatLocation location) {
        super(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F));
        this.location = location;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape shape1 = IBlock.getVoxelShapeByDirection(0, 6, 0, 16, 8, 16, IBlock.getStatePropertySafe(blockState, FACING));
        VoxelShape shape2 = IBlock.getVoxelShapeByDirection(0, 8, 0, 16, 16, 5, IBlock.getStatePropertySafe(blockState, FACING));
        return Shapes.or(shape1, shape2);
    }

    @Override
    public String getDescriptionId() {
        return "block.msd.hall_seat";
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> list, TooltipFlag tooltipFlag) {
        if (location == SeatLocation.SIDE) {
            list.add(Text.translatable("tooltip.msd.hall_seat_location_side").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        } else if (location == SeatLocation.MIDDLE) {
            list.add(Text.translatable("tooltip.msd.hall_seat_location_middle").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        } else {
            list.add(Text.translatable("tooltip.msd.hall_seat_location_side_mirror").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection());
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.empty();
    }

    public enum SeatLocation {
        SIDE,
        MIDDLE,
        SIDE_MIRROR
    }
}