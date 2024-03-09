package top.mcmtr.mod.render;

import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.RenderTrains;
import org.mtr.mod.screen.PIDSFormatter;
import top.mcmtr.mod.InitClient;
import top.mcmtr.mod.blocks.BlockChangeModelBase;
import top.mcmtr.mod.blocks.BlockCustomTextSignBase;

public class RenderCustomText<T extends BlockCustomTextSignBase.BlockCustomTextSignBaseEntity> extends BlockEntityRenderer<T> implements IGui, Utilities {
    private final float startX;
    private final float startY;
    private final float startZ;
    private final float maxHeight;
    private final float maxWidth;
    private final boolean rotate90;
    private final float textPadding;

    public static final int SWITCH_LANGUAGE_TICKS = 60;

    public RenderCustomText(Argument argument, float startX, float startY, float startZ, float maxHeight, int maxWidth, boolean rotate90, float textPadding) {
        super(argument);
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
        this.rotate90 = rotate90;
        this.textPadding = textPadding;
    }

    @Override
    public void render(T entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
        final World world = entity.getWorld2();
        if (world == null) {
            return;
        }
        final BlockPos blockPos = entity.getPos2();
        final BlockState blockState = world.getBlockState(blockPos);
        if (!(blockState.getBlock().data instanceof BlockChangeModelBase)) {
            return;
        }
        final Direction facing = IBlock.getStatePropertySafe(world, blockPos, DirectionHelper.FACING);
        RenderTrains.scheduleRender(RenderTrains.QueuedRenderLayer.TEXT, (graphicsHolderNew, offset) -> {
            render(entity, blockPos.offset(facing), facing, graphicsHolderNew, offset, false);
            render(entity, blockPos.offset(facing), facing, graphicsHolderNew, offset, true);
        });

    }

    private void render(T entity, BlockPos blockPos, Direction facing, GraphicsHolder graphicsHolder, Vector3d offset, boolean addRotation) {
        final float scale = 160 * entity.maxArrivals / maxHeight * textPadding;
        for (int i = 0; i < entity.maxArrivals; i++) {
            final int languageTicks = (int) Math.floor(InitClient.getGameTick()) / SWITCH_LANGUAGE_TICKS;
            final String[] messageSplit = entity.getMessage(i).split("\\|");
            if (messageSplit.length == 0) {
                break;
            }
            ArrivalResponse[] arrivalResponses = {null};
            ObjectImmutableList<ArrivalResponse> arrivalResponseList = new ObjectImmutableList<>(arrivalResponses);
            graphicsHolder.push();
            graphicsHolder.translate(blockPos.getX() - offset.getXMapped() + 0.5, blockPos.getY() - offset.getYMapped(), blockPos.getZ() - offset.getZMapped() + 0.5);
            graphicsHolder.rotateYDegrees((rotate90 ? 90 : 0) + (addRotation ? 180 : 0) - facing.asRotation());
            graphicsHolder.rotateZDegrees(180);
            graphicsHolder.translate((startX - 8) / 16, -startY / 16 + i * maxHeight / entity.maxArrivals / 16, (startZ - 8) / 16 - SMALL_OFFSET * 2);
            graphicsHolder.scale(1 / scale, 1 / scale, 1 / scale);

            final ObjectImmutableList<PIDSFormatter.Column> columns = new PIDSFormatter(arrivalResponseList, messageSplit[languageTicks % messageSplit.length]).getColumns();

            columns.forEach(column -> {
                final int textWidth = column.getTextWidth();
                final float availableWidth = maxWidth * scale / 16 * (column.endPercentage - column.startPercentage) / 100;
                graphicsHolder.push();
                graphicsHolder.translate(maxWidth * scale / 16 * column.startPercentage / 100, 0, 0);
                if (availableWidth < textWidth) {
                    graphicsHolder.scale(availableWidth / textWidth, 1, 1);
                }

                final int[] x = {(int) column.horizontalAlignment.getOffset(0, -Math.max(0, availableWidth - textWidth))};
                column.textChunks.forEach(textChunk -> {
                    if (textChunk.condition.getAsBoolean()) {
                        graphicsHolder.drawText(textChunk.text, x[0], 0, textChunk.color | ARGB_BLACK, false, MAX_LIGHT_GLOWING);
                        x[0] += GraphicsHolder.getTextWidth(textChunk.text);
                    }
                });
                graphicsHolder.pop();
            });
            graphicsHolder.pop();
        }
    }
}