package top.mcmtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.block.IBlock;
import mtr.data.IGui;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.UtilitiesClient;
import mtr.render.RenderTrains;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import top.mcmtr.blocks.BlockCustomTextSignBase;
import top.mcmtr.config.Config;

public class RenderCustomTextSign<T extends BlockEntityMapper> extends BlockEntityRendererMapper<T> implements IGui {
    private final float scale;
    private final float fRowScale;
    private final float sRowScale;
    private final float fRowTotalScaledWidth;
    private final float sRowTotalScaledWidth;
    private final float rowSpacing;
    private final float totalScaledWidth;
    private final int maxArrivals;
    private final float maxHeight;
    private final float startX;
    private final float startY;
    private final float startZ;
    private final boolean rotate90;
    private final int textColor;
    private final boolean enableFirstTextColor;
    private final int firstTextColor;

    /**
     * (总行高x0.8)/ (行数x缩放倍数)= 文字高度
     */
    public RenderCustomTextSign(BlockEntityRenderDispatcher dispatcher, int maxArrivals, float startX, float startY, float startZ, float maxHeight, int maxWidth, boolean rotate90, int textColor, boolean enableFirstTextColor, int firstTextColor, float textPadding, float fRowTextPadding, float sRowTextPadding, float rowSpacing) {
        super(dispatcher);
        this.scale = 160 * maxArrivals / maxHeight * textPadding;
        this.totalScaledWidth = scale * maxWidth / 16;
        this.fRowScale = 160 * maxArrivals / maxHeight * fRowTextPadding;
        this.fRowTotalScaledWidth = fRowScale * maxWidth / 16;
        this.sRowScale = 160 * maxArrivals / maxHeight * sRowTextPadding;
        this.sRowTotalScaledWidth = sRowScale * maxWidth / 16;
        this.maxArrivals = maxArrivals;
        this.maxHeight = maxHeight;
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.rotate90 = rotate90;
        this.textColor = textColor;
        this.enableFirstTextColor = enableFirstTextColor;
        this.firstTextColor = firstTextColor;
        this.rowSpacing = rowSpacing;
    }

    @Environment(EnvType.CLIENT)
    public int getViewDistance() {
        return Config.getCustomTextSignMaxViewDistance();
    }

    @Override
    public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        final BlockGetter world = entity.getLevel();
        if (world == null) {
            return;
        }
        final BlockPos pos = entity.getBlockPos();
        final Direction facing = IBlock.getStatePropertySafe(world, pos, HorizontalDirectionalBlock.FACING);
        if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, rotate90 ? null : facing)) {
            return;
        }
        final String[] customMessages = new String[maxArrivals];
        for (int i = 0; i < maxArrivals; i++) {
            if (entity instanceof BlockCustomTextSignBase.TileEntityBlockCustomTextSignBase) {
                customMessages[i] = ((BlockCustomTextSignBase.TileEntityBlockCustomTextSignBase) entity).getMessage(i);
            } else {
                customMessages[i] = "";
            }
        }
        try {
            final Font textRenderer = Minecraft.getInstance().font;
            for (int i = 0; i < maxArrivals; i++) {
                final String destinationString;
                final String destinationString2;
                final float trueFRowScale;
                final float trueFRowTotalScaledWidth;
                final int trueColor;
                final String[] destinationSplit = customMessages[i].split("\\|");
                destinationString = destinationSplit[0];
                if (destinationSplit.length > 1) {
                    destinationString2 = destinationSplit[1];
                    trueFRowScale = fRowScale;
                    trueFRowTotalScaledWidth = fRowTotalScaledWidth;
                } else {
                    destinationString2 = "";
                    trueFRowScale = scale;
                    trueFRowTotalScaledWidth = totalScaledWidth;
                }
                if (enableFirstTextColor && (i == 0)) {
                    trueColor = firstTextColor;
                } else {
                    trueColor = textColor;
                }
                final int destinationWidth = textRenderer.width(destinationString);
                final int destinationWidth2 = textRenderer.width(destinationString2);
                matrices.pushPose();
                matrices.translate(0.5, 0, 0.5);
                UtilitiesClient.rotateYDegrees(matrices, (rotate90 ? 90 : 0) - facing.toYRot());
                UtilitiesClient.rotateZDegrees(matrices, 180);
                matrices.translate((startX - 8) / 16, -startY / 16 + i * maxHeight / maxArrivals / 16, (startZ - 8) / 16 - SMALL_OFFSET * 2);
                matrices.scale(1F / trueFRowScale, 1F / trueFRowScale, 1F / trueFRowScale);
                if (destinationWidth > trueFRowTotalScaledWidth) {
                    matrices.scale(trueFRowTotalScaledWidth / destinationWidth, 1, 1);
                }
                textRenderer.draw(matrices, destinationString, 0, 0, trueColor);
                matrices.popPose();
                matrices.pushPose();
                matrices.translate(0.5, 0, 0.5);
                UtilitiesClient.rotateYDegrees(matrices, (rotate90 ? -90 : -180) - facing.toYRot());
                UtilitiesClient.rotateZDegrees(matrices, 180);
                matrices.translate((-startX + 8 - (trueFRowTotalScaledWidth * 16 / trueFRowScale)) / 16, -startY / 16 + i * maxHeight / maxArrivals / 16, (startZ - 8) / 16 - SMALL_OFFSET * 2);
                matrices.scale(1F / trueFRowScale, 1F / trueFRowScale, 1F / trueFRowScale);
                final float leftLength;
                if (destinationWidth > trueFRowTotalScaledWidth) {
                    matrices.scale(trueFRowTotalScaledWidth / destinationWidth, 1, 1);
                    leftLength = 0;
                } else {
                    leftLength = trueFRowTotalScaledWidth - destinationWidth;
                }
                textRenderer.draw(matrices, destinationString, leftLength, 0, trueColor);
                matrices.popPose();
                matrices.pushPose();
                matrices.translate(0.5, 0, 0.5);
                UtilitiesClient.rotateYDegrees(matrices, (rotate90 ? 90 : 0) - facing.toYRot());
                UtilitiesClient.rotateZDegrees(matrices, 180);
                matrices.translate((startX - 8) / 16, (-startY / 16 + i * maxHeight / maxArrivals / 16) + (8 / fRowScale) + rowSpacing, (startZ - 8) / 16 - SMALL_OFFSET * 2);
                matrices.scale(1F / sRowScale, 1F / sRowScale, 1F / sRowScale);
                if (destinationWidth2 > sRowTotalScaledWidth) {
                    matrices.scale(sRowTotalScaledWidth / destinationWidth2, 1, 1);
                }
                textRenderer.draw(matrices, destinationString2, 0, 0, trueColor);
                matrices.popPose();
                matrices.pushPose();
                matrices.translate(0.5, 0, 0.5);
                UtilitiesClient.rotateYDegrees(matrices, (rotate90 ? -90 : -180) - facing.toYRot());
                UtilitiesClient.rotateZDegrees(matrices, 180);
                matrices.translate((-startX + 8 - (sRowTotalScaledWidth * 16 / sRowScale)) / 16, (-startY / 16 + i * maxHeight / maxArrivals / 16) + (8 / fRowScale) + rowSpacing, (startZ - 8) / 16 - SMALL_OFFSET * 2);
                matrices.scale(1F / sRowScale, 1F / sRowScale, 1F / sRowScale);
                final float leftLength2;
                if (destinationWidth2 > sRowTotalScaledWidth) {
                    matrices.scale(sRowTotalScaledWidth / destinationWidth2, 1, 1);
                    leftLength2 = 0;
                } else {
                    leftLength2 = sRowTotalScaledWidth - destinationWidth2;
                }
                textRenderer.draw(matrices, destinationString2, leftLength2, 0, trueColor);
                matrices.popPose();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}