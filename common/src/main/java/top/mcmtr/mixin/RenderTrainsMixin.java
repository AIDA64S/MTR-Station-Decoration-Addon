package top.mcmtr.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.IDrawing;
import mtr.data.RailwayData;
import mtr.entity.EntitySeat;
import mtr.mappings.UtilitiesClient;
import mtr.path.PathData;
import mtr.render.RenderTrains;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.mcmtr.client.MSDClientData;
import top.mcmtr.data.Catenary;
import top.mcmtr.data.CatenaryType;
import top.mcmtr.data.RigidCatenary;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(RenderTrains.class)
public class RenderTrainsMixin {
    @Inject(method = "render(Lmtr/entity/EntitySeat;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V", ordinal = 0))
    private static void renderCatenary(EntitySeat entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, CallbackInfo ci) {
        final Minecraft client2 = Minecraft.getInstance();
        final LocalPlayer player2 = client2.player;
        final Level world = client2.level;
        final int renderDistanceChunks2 = UtilitiesClient.getRenderDistance();
        final int maxCatenaryDistance2 = renderDistanceChunks2 * 16;
        final Map<UUID, Boolean> renderedCatenaryMap = new HashMap<>();
        matrices.pushPose();
        MSDClientData.CATENARIES.forEach((startPos, catenaryMap) -> catenaryMap.forEach((endPos, catenary) -> {
            if (!RailwayData.isBetween(player2.getX(), startPos.getX(), endPos.getX(), maxCatenaryDistance2) || !RailwayData.isBetween(player2.getZ(), startPos.getZ(), endPos.getZ(), maxCatenaryDistance2)) {
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
                catenary.render((x1, y1, z1, x2, y2, z2, count, i, base, sinX, sinZ, increment) ->
                        IDrawing.drawLine(matrices, vertexConsumers, (float) x1, (float) y1 + 0.5F, (float) z1, (float) x2, (float) y2 + 0.5F, (float) z2, 0, 0, 0)
                );
            } else if (catenary.catenaryType == CatenaryType.RIGID_SOFT_CATENARY) {
                renderRigidSoftCatenaryStandard(catenary);
            } else {
                renderCatenaryStandard(catenary);
            }
        }));
        matrices.popPose();
        matrices.pushPose();
        final Map<UUID, Boolean> renderedRigidCatenaryMap = new HashMap<>();
        MSDClientData.RIGID_CATENARIES.forEach((startPos, rigidCatenaryMap) -> rigidCatenaryMap.forEach((endPos, rigidCatenary) -> {
            if (!RailwayData.isBetween(player2.getX(), startPos.getX(), endPos.getX(), maxCatenaryDistance2) || !RailwayData.isBetween(player2.getZ(), startPos.getZ(), endPos.getZ(), maxCatenaryDistance2)) {
                return;
            }
            final UUID rigidCatenaryProduct = PathData.getRailProduct(startPos, endPos);
            if (renderedRigidCatenaryMap.containsKey(rigidCatenaryProduct)) {
                if (renderedRigidCatenaryMap.get(rigidCatenaryProduct)) {
                    return;
                }
            } else {
                renderedRigidCatenaryMap.put(rigidCatenaryProduct, true);
            }
            if (rigidCatenary.catenaryType == CatenaryType.RIGID_CATENARY) {
                renderRigidCatenaryStandard(world, rigidCatenary);
            }
        }));
        matrices.popPose();
    }

    private static void renderCatenaryStandard(Catenary catenary) {
        renderCatenaryStandard(catenary, "msd:textures/block/overhead_line.png");
    }

    private static void renderRigidCatenaryStandard(Level world, RigidCatenary rigidCatenary) {
        renderRigidCatenaryStandard(world, rigidCatenary, "msd:textures/block/rigid_overhead_line.png");
    }

    private static void renderRigidSoftCatenaryStandard(Catenary catenary) {
        renderRigidSoftCatenaryStandard(catenary, "msd:textures/block/overhead_line.png");
    }

    private static void renderCatenaryStandard(Catenary catenary, String texture) {
        final int maxCatenaryDistance = UtilitiesClient.getRenderDistance() * 16;
        catenary.render((x1, y1, z1, x2, y2, z2, count, i, base, sinX, sinZ, increment) -> {
            final BlockPos pos3 = RailwayData.newBlockPos(x1, y1, z1);
            if (RenderTrains.shouldNotRender(pos3, maxCatenaryDistance, null)) {
                return;
            }
            RenderTrains.scheduleRender(new ResourceLocation(texture), false, RenderTrains.QueuedRenderLayer.EXTERIOR, (matrices, vertexConsumer) -> {
                if (count < 8) {
                    IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x2, (float) y2 + 0.65F + (float) base, (float) z2, (float) x2, (float) y2, (float) z2, (float) x1, (float) y1, (float) z1, 0.0F, 0.0F, 1.0F, 1.0F, Direction.UP, -1, 0);
                    IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y2 + 0.65F + (float) base, (float) z2, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2, 0.0F, 1.0F, 1.0F, 0.0F, Direction.UP, -1, 0);
                } else {
                    if (i < (count / 2 - increment)) {
                        IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x2, (float) y2 + 0.65F + (float) (base * 0.5), (float) z2, (float) x2, (float) y2, (float) z2, (float) x1, (float) y1, (float) z1, 0.0F, 0.0F, 1.0F, 1.0F, Direction.UP, -1, 0);
                        IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y2 + 0.65F + (float) (base * 0.5), (float) z2, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2, 0.0F, 1.0F, 1.0F, 0.0F, Direction.UP, -1, 0);
                    } else if (i >= (count / 2)) {
                        IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x2, (float) y2 + 0.65F + (float) (base / 0.5), (float) z2, (float) x2, (float) y2, (float) z2, (float) x1, (float) y1, (float) z1, 0.0F, 0.0F, 1.0F, 1.0F, Direction.UP, -1, 0);
                        IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y2 + 0.65F + (float) (base / 0.5), (float) z2, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2, 0.0F, 1.0F, 1.0F, 0.0F, Direction.UP, -1, 0);
                    } else {
                        IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x2, (float) y2 + 0.65F + (float) base, (float) z2, (float) x2, (float) y2, (float) z2, (float) x1, (float) y1, (float) z1, 0.0F, 0.0F, 1.0F, 1.0F, Direction.UP, -1, 0);
                        IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y2 + 0.65F + (float) base, (float) z2, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2, 0.0F, 1.0F, 1.0F, 0.0F, Direction.UP, -1, 0);
                    }
                }
                IDrawing.drawTexture(matrices, vertexConsumer, (float) (x1 - sinX), (float) y1, (float) (z1 + sinZ), (float) (x2 - sinX), (float) y2, (float) (z2 + sinZ), (float) (x2 + sinX), (float) y2, (float) (z2 - sinZ), (float) (x1 + sinX), (float) y1, (float) (z1 - sinZ), 0.0F, 0.0F, 1.0F, 0.03125F, Direction.UP, -1, 0);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) (x2 - sinX), (float) y2, (float) (z2 + sinZ), (float) (x1 - sinX), (float) y1, (float) (z1 + sinZ), (float) (x1 + sinX), (float) y1, (float) (z1 - sinZ), (float) (x2 + sinX), (float) y2, (float) (z2 - sinZ), 0.0F, 0.03125F, 1.0F, 0.0F, Direction.UP, -1, 0);
            });
        });
    }

    private static void renderRigidCatenaryStandard(Level world, RigidCatenary rigidCatenary, String texture) {
        final int maxCatenaryDistance = UtilitiesClient.getRenderDistance() * 16;
        rigidCatenary.render((x1, z1, x2, z2, x3, z3, x4, z4, x5, z5, x6, z6, x7, z7, x8, z8, y1, y2) -> {
            final BlockPos pos3 = RailwayData.newBlockPos(x1, y1, z1);
            if (RenderTrains.shouldNotRender(pos3, maxCatenaryDistance, null)) {
                return;
            }
            final int light2 = LightTexture.pack(world.getBrightness(LightLayer.BLOCK, pos3), world.getBrightness(LightLayer.SKY, pos3));
            RenderTrains.scheduleRender(new ResourceLocation(texture), false, RenderTrains.QueuedRenderLayer.EXTERIOR, (matrices, vertexConsumer) -> {
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1, (float) z1, (float) x2, (float) y1, (float) z2, (float) x3, (float) y2, (float) z3, (float) x4, (float) y2, (float) z4, 0.0F, 0.0F, 1.0F, 0.03125F, Direction.UP, -1, 0);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x3, (float) y2, (float) z3, (float) x2, (float) y1, (float) z2, (float) x1, (float) y1, (float) z1, (float) x4, (float) y2, (float) z4, 0.0F, 0.03125F, 1.0F, 0.0F, Direction.UP, -1, 0);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1, (float) z1, (float) x5, (float) y1 + 0.125F, (float) z5, (float) x8, (float) y2 + 0.125F, (float) z8, (float) x4, (float) y2, (float) z4, 0.0F, 0.5F, 1.0F, 1.0F, Direction.UP, -1, light2);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x8, (float) y2 + 0.125F, (float) z8, (float) x5, (float) y1 + 0.125F, (float) z5, (float) x1, (float) y1, (float) z1, (float) x4, (float) y2, (float) z4, 0.0F, 1.0F, 1.0F, 0.5F, Direction.UP, -1, light2);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y1, (float) z2, (float) x6, (float) y1 + 0.125F, (float) z6, (float) x7, (float) y2 + 0.125F, (float) z7, (float) x3, (float) y2, (float) z3, 0.0F, 0.5F, 1.0F, 1.0F, Direction.UP, -1, light2);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x7, (float) y2 + 0.125F, (float) z7, (float) x6, (float) y1 + 0.125F, (float) z6, (float) x2, (float) y1, (float) z2, (float) x3, (float) y2, (float) z3, 0.0F, 1.0F, 1.0F, 0.5F, Direction.UP, -1, light2);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x5, (float) y1 + 0.125F, (float) z5, (float) x6, (float) y1 + 0.125F, (float) z6, (float) x7, (float) y2 + 0.125F, (float) z7, (float) x8, (float) y2 + 0.125F, (float) z8, 0.0F, 0.5F, 1.0F, 1.0F, Direction.UP, -1, light2);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x7, (float) y2 + 0.125F, (float) z7, (float) x6, (float) y1 + 0.125F, (float) z6, (float) x5, (float) y1 + 0.125F, (float) z5, (float) x8, (float) y2 + 0.125F, (float) z8, 0.0F, 1.0F, 1.0F, 0.5F, Direction.UP, -1, light2);

            });
        });
    }

    private static void renderRigidSoftCatenaryStandard(Catenary catenary, String texture) {
        final int maxCatenaryDistance = UtilitiesClient.getRenderDistance() * 16;
        catenary.render((x1, y1, z1, x2, y2, z2, count, i, base, sinX, sinZ, increment) -> {
            final BlockPos pos3 = RailwayData.newBlockPos(x1, y1, z1);
            if (RenderTrains.shouldNotRender(pos3, maxCatenaryDistance, null)) {
                return;
            }
            RenderTrains.scheduleRender(new ResourceLocation(texture), false, RenderTrains.QueuedRenderLayer.EXTERIOR, (matrices, vertexConsumer) -> {
                IDrawing.drawTexture(matrices, vertexConsumer, (float) (x1 - sinX), (float) y1, (float) (z1 + sinZ), (float) (x2 - sinX), (float) y2, (float) (z2 + sinZ), (float) (x2 + sinX), (float) y2, (float) (z2 - sinZ), (float) (x1 + sinX), (float) y1, (float) (z1 - sinZ), 0.0F, 0.0F, 1.0F, 0.03125F, Direction.UP, -1, 0);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) (x2 - sinX), (float) y2, (float) (z2 + sinZ), (float) (x1 - sinX), (float) y1, (float) (z1 + sinZ), (float) (x1 + sinX), (float) y1, (float) (z1 - sinZ), (float) (x2 + sinX), (float) y2, (float) (z2 - sinZ), 0.0F, 0.03125F, 1.0F, 0.0F, Direction.UP, -1, 0);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1 + 0.03125F, (float) z1, (float) x2, (float) y2 + 0.03125F, (float) z2, (float) x2, (float) y2, (float) z2, (float) x1, (float) y1, (float) z1, 0.0F, 0.0F, 1.0F, 0.03125F, Direction.UP, -1, 0);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y2 + 0.03125F, (float) z2, (float) x1, (float) y1 + 0.03125F, (float) z1, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2, 0.0F, 0.03125F, 1.0F, 0.0F, Direction.UP, -1, 0);
            });
        });
    }
}