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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class BlockCatenaryRack extends Block {
    private final CatenaryRackType catenaryRackType;

    public BlockCatenaryRack(CatenaryRackType catenaryRackType, Properties properties) {
        super(properties);
        this.catenaryRackType = catenaryRackType;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        switch (catenaryRackType) {
            case RACK:
                return IBlock.getVoxelShapeByDirection(6.5, 0, 0, 9.5, 16, 16, IBlock.getStatePropertySafe(blockState, FACING));
            case POLE:
                return IBlock.getVoxelShapeByDirection(6, 0, 5, 10, 16, 11, IBlock.getStatePropertySafe(blockState, FACING));
            case UNDER_GANTRY:
                return IBlock.getVoxelShapeByDirection(6, 0, 6.5, 10, 16, 9.5, IBlock.getStatePropertySafe(blockState, FACING));
            default:
                return IBlock.getVoxelShapeByDirection(4, 0, 0, 12, 9, 16, IBlock.getStatePropertySafe(blockState, FACING));
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> list, TooltipFlag tooltipFlag) {
        switch (catenaryRackType) {
            case GANTRY_SIDE:
                list.add(Text.translatable("tooltip.msd.catenary_pole_top_side").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
                break;
            case GANTRY_MIDDLE:
                list.add(Text.translatable("tooltip.msd.catenary_pole_top_middle").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
                break;
            case UNDER_GANTRY:
                list.add(Text.translatable("tooltip.msd.catenary_rack_top").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
                break;
        }
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection());
    }

    public enum CatenaryRackType {
        POLE,
        RACK,
        UNDER_GANTRY,
        GANTRY_SIDE,
        GANTRY_MIDDLE
    }
}