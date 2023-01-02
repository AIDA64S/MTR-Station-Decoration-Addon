package top.mcmtr.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.IDrawing;
import mtr.entity.EntitySeat;
import mtr.mappings.UtilitiesClient;
import mtr.path.PathData;
import mtr.render.RenderTrains;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.mcmtr.client.MSDClientData;
import top.mcmtr.data.Catenary;
import top.mcmtr.data.CatenaryData;
import top.mcmtr.data.CatenaryType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(RenderTrains.class)
public class RenderTrainsMixin {
    @Inject(method = "render(Lmtr/entity/EntitySeat;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V", ordinal = 1))
    private static void renderCatenary(EntitySeat entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, CallbackInfo ci) {
        final Minecraft client2 = Minecraft.getInstance();
        final LocalPlayer player2 = client2.player;
        final int renderDistanceChunks2 = UtilitiesClient.getRenderDistance();
        final int maxCatenaryDistance2 = renderDistanceChunks2 * 16;
        final Map<UUID, Boolean> renderedCatenaryMap = new HashMap<>();
        matrices.pushPose();
        MSDClientData.CATENARIES.forEach((startPos, catenaryMap) -> catenaryMap.forEach((endPos, catenary) -> {
            if (!CatenaryData.isBetween(player2.getX(), startPos.getX(), endPos.getX(), maxCatenaryDistance2) || !CatenaryData.isBetween(player2.getZ(), startPos.getZ(), endPos.getZ(), maxCatenaryDistance2)) {
                return;
            }
            final UUID catenaryProduct = PathData.getRailProduct(startPos, endPos);
            if (renderedCatenaryMap.containsKey(catenaryProduct)) {
                if (renderedCatenaryMap.get(catenaryProduct)) {
                    return;
                }
            } else {
                renderedCatenaryMap.put(catenaryProduct, true);
            }
            if (catenary.catenaryType == CatenaryType.ELECTRIC) {
                catenary.render((x1, y1, z1, x2, y2, z2, count, i, base, sinX, sinZ) ->
                        IDrawing.drawLine(matrices, vertexConsumers, (float) x1, (float) y1 + 0.5F, (float) z1, (float) x2, (float) y2 + 0.5F, (float) z2, 0, 0, 0)
                );
            } else {
                renderCatenaryStandard(catenary);

            }
        }));
        matrices.popPose();
    }

    private static void renderCatenaryStandard(Catenary catenary) {
        renderCatenaryStandard(catenary, "msd:textures/block/overhead_line.png");
    }

    private static void renderCatenaryStandard(Catenary catenary, String texture) {
        final int maxCatenaryDistance = UtilitiesClient.getRenderDistance() * 16;
        catenary.render((x1, y1, z1, x2, y2, z2, count, i, base, sinX, sinZ) -> {
            final BlockPos pos3 = new BlockPos(x1, y1, z1);
            if (RenderTrains.shouldNotRender(pos3, maxCatenaryDistance, null)) {
                return;
            }
            RenderTrains.scheduleRender(new ResourceLocation(texture), false, RenderTrains.QueuedRenderLayer.EXTERIOR, (matrices, vertexConsumer) -> {
                if (i < 3) {
                    IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1 + 0.4375F + (float) base, (float) z1, (float) x2, (float) y2 + 0.4375F + (float) (base * 0.6), (float) z2, (float) x2, (float) y2, (float) z2, (float) x1, (float) y1, (float) z1, 0.0F, 0.0F, 1.0F, 1.0F, Direction.UP, -1, 0);
                    IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y2 + 0.4375F + (float) (base * 0.6), (float) z2, (float) x1, (float) y1 + 0.4375F + (float) base, (float) z1, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2, 0.0F, 1.0F, 1.0F, 0.0F, Direction.UP, -1, 0);
                } else if (i > count - 4) {
                    IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1 + 0.4375F + (float) base, (float) z1, (float) x2, (float) y2 + 0.4375F + (float) (base / 0.6), (float) z2, (float) x2, (float) y2, (float) z2, (float) x1, (float) y1, (float) z1, 0.0F, 0.0F, 1.0F, 1.0F, Direction.UP, -1, 0);
                    IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y2 + 0.4375F + (float) (base / 0.6), (float) z2, (float) x1, (float) y1 + 0.4375F + (float) base, (float) z1, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2, 0.0F, 1.0F, 1.0F, 0.0F, Direction.UP, -1, 0);
                } else {
                    IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1 + 0.4375F + (float) base, (float) z1, (float) x2, (float) y2 + 0.4375F + (float) base, (float) z2, (float) x2, (float) y2, (float) z2, (float) x1, (float) y1, (float) z1, 0.0F, 0.0F, 1.0F, 1.0F, Direction.UP, -1, 0);
                    IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y2 + 0.4375F + (float) base, (float) z2, (float) x1, (float) y1 + 0.4375F + (float) base, (float) z1, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2, 0.0F, 1.0F, 1.0F, 0.0F, Direction.UP, -1, 0);
                }
                IDrawing.drawTexture(matrices, vertexConsumer, (float) (x1 - sinX), (float) y1, (float) (z1 + sinZ), (float) (x2 - sinX), (float) y2, (float) (z2 + sinZ), (float) (x2 + sinX), (float) y2, (float) (z2 - sinZ), (float) (x1 + sinX), (float) y1, (float) (z1 - sinZ), 0.0F, 0.0F, 1.0F, 0.03125F, Direction.UP, -1, 0);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) (x2 - sinX), (float) y2, (float) (z2 + sinZ), (float) (x1 - sinX), (float) y1, (float) (z1 + sinZ), (float) (x1 + sinX), (float) y1, (float) (z1 - sinZ), (float) (x2 + sinX), (float) y2, (float) (z2 - sinZ), 0.0F, 0.03125F, 1.0F, 0.0F, Direction.UP, -1, 0);
            });
        });
    }
}
