package top.mcmtr.blocks;

import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;

import java.util.List;

public abstract class BlockChangeModelBase extends HorizontalDirectionalBlock {
    public static final IntegerProperty TEXTURE_TYPE = IntegerProperty.create("type", 0, 5);
    private final int count;

    public BlockChangeModelBase(int count) {
        super(Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F));
        this.count = count;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Text.translatable("tooltip.msd.model_count", count).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TEXTURE_TYPE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection()).setValue(TEXTURE_TYPE, 0);
    }

    public int getCount() {
        return count;
    }
}