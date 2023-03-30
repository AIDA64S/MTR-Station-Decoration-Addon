package top.mcmtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.MTRClient;
import mtr.block.IBlock;
import mtr.data.IGui;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.UtilitiesClient;
import mtr.render.RenderTrains;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import top.mcmtr.blocks.BlockCustomTextSignBase;

public class RenderCustomTextSign<T extends BlockEntityMapper> extends BlockEntityRendererMapper<T> implements IGui {
    private final float scale;
    private final float totalScaledWidth;
    private final int maxArrivals;
    private final float maxHeight;
    private final float startX;
    private final float startY;
    private final float startZ;
    private final boolean rotate90;
    private final int textColor;
    public static final int MAX_VIEW_DISTANCE = 128;
    private static final int SWITCH_LANGUAGE_TICKS = 60;
    private int startIndex;

    public RenderCustomTextSign(BlockEntityRenderDispatcher dispatcher, int maxArrivals, float startX, float startY, float startZ, float maxHeight, int maxWidth, boolean rotate90, int textColor, float textPadding) {
        super(dispatcher);
        scale = 160 * maxArrivals / maxHeight * textPadding;
        totalScaledWidth = scale * maxWidth / 16;
        this.maxArrivals = maxArrivals;
        this.maxHeight = maxHeight;
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.rotate90 = rotate90;
        this.textColor = textColor;
    }

    @Override
    public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        final BlockGetter world = entity.getLevel();
        if (world == null) {
            return;
        }
        final BlockPos pos = entity.getBlockPos();
        final Direction facing = IBlock.getStatePropertySafe(world, pos, HorizontalDirectionalBlock.FACING);
        if (RenderTrains.shouldNotRender(pos, Math.min(MAX_VIEW_DISTANCE, RenderTrains.maxTrainRenderDistance), rotate90 ? null : facing)) {
            return;
        }
        final String[] customMessages = new String[maxArrivals];
        if (!((BlockCustomTextSignBase.TileEntityBlockCustomTextSignBase) entity).isDirectionFlap()) {
            startIndex = ((BlockCustomTextSignBase.TileEntityBlockCustomTextSignBase) entity).getMaxArrivals() / 2;
        } else {
            startIndex = 0;
        }
        for (int i = startIndex; i < maxArrivals + startIndex; i++) {
            if (entity instanceof BlockCustomTextSignBase.TileEntityBlockCustomTextSignBase) {
                customMessages[i] = ((BlockCustomTextSignBase.TileEntityBlockCustomTextSignBase) entity).getMessage(i);
            } else {
                customMessages[i] = "";
            }
        }
        try {
            for (int i = startIndex; i < maxArrivals + startIndex; i++) {
                final int languageTicks = (int) Math.floor(MTRClient.getGameTick()) / SWITCH_LANGUAGE_TICKS;
                final String destinationString;
                final String[] destinationSplit = customMessages[i].split("\\|");
                destinationString = destinationSplit[languageTicks % destinationSplit.length];
                matrices.pushPose();
                matrices.translate(0.5, 0, 0.5);
                UtilitiesClient.rotateYDegrees(matrices, (rotate90 ? 90 : 0) - facing.toYRot());
                UtilitiesClient.rotateZDegrees(matrices, 180);
                matrices.translate((startX - 8) / 16, -startY / 16 + i * maxHeight / maxArrivals / 16, (startZ - 8) / 16 - SMALL_OFFSET * 2);
                matrices.scale(1F / scale, 1F / scale, 1F / scale);
                final Font textRenderer = Minecraft.getInstance().font;
                final int destinationWidth = textRenderer.width(destinationString);
                final float leftLength;
                if (!((BlockCustomTextSignBase.TileEntityBlockCustomTextSignBase) entity).isDirectionFlap()) {
                    if (destinationWidth > totalScaledWidth) {
                        matrices.scale(totalScaledWidth / destinationWidth, 1, 1);
                        leftLength = 0;
                    } else {
                        leftLength = totalScaledWidth - destinationWidth;
                    }
                } else {
                    leftLength = 0;
                }

                textRenderer.draw(matrices, destinationString, leftLength, 0, textColor);
                matrices.popPose();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}