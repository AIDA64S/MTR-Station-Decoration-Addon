package top.mcmtr.mixin;

import org.mtr.libraries.com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import org.mtr.libraries.com.logisticscraft.occlusionculling.util.Vec3d;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.render.OcclusionCullingThread;
import org.mtr.mod.render.RenderRails;
import org.mtr.mod.render.RenderTrains;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.mod.Init;
import top.mcmtr.mod.client.MSDMinecraftClientData;

import java.util.function.Function;

@Mixin(RenderRails.class)
public final class RenderRailsMixin {
    @Unique
    private static final Identifier CATENARY_TEXTURE = new Identifier(Init.MOD_ID, "textures/block/overhead_line.png");

    @Final
    @Shadow(remap = false)
    private static final OcclusionCullingThread OCCLUSION_CULLING_THREAD = new OcclusionCullingThread();

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lorg/mtr/mod/render/OcclusionCullingThread;start()V"), remap = false)
    private static void renderCatenary(CallbackInfo ci) {
        final MinecraftClient minecraftClientMSD = MinecraftClient.getInstance();
        final ClientWorld clientWorldMSD = minecraftClientMSD.getWorldMapped();
        final ClientPlayerEntity clientPlayerEntityMSD = minecraftClientMSD.getPlayerMapped();

        if (clientWorldMSD == null || clientPlayerEntityMSD == null) {
            return;
        }

        final ObjectArrayList<Function<OcclusionCullingInstance, Runnable>> cullingTasksMSD = new ObjectArrayList<>();
        final Vector3d cameraPositionMSD = minecraftClientMSD.getGameRendererMapped().getCamera().getPos();
        final Vec3d cameraMSD = new Vec3d(cameraPositionMSD.getXMapped(), cameraPositionMSD.getYMapped(), cameraPositionMSD.getZMapped());
        final ObjectArrayList<Catenary> catenariesToRender = new ObjectArrayList<>();
        MSDMinecraftClientData.getInstance().catenaryWrapperList.values().forEach(catenaryWrapper -> {
            cullingTasksMSD.add(occlusionCullingInstance -> {
                final boolean shouldRender = occlusionCullingInstance.isAABBVisible(catenaryWrapper.startVector, catenaryWrapper.endVector, cameraMSD);
                return () -> catenaryWrapper.shouldRender = shouldRender;
            });
            if (catenaryWrapper.shouldRender) {
                catenariesToRender.add(catenaryWrapper.getCatenary());
            }
        });

        catenariesToRender.forEach(catenary -> {
            switch (catenary.getCatenaryType()) {
                case CATENARY:
                    renderCatenaryStandard(clientWorldMSD, catenary);
                    break;
                case ELECTRIC:
                    RenderTrains.scheduleRender(RenderTrains.QueuedRenderLayer.LINES, (graphicsHolder, offset) ->
                            catenary.catenaryMath.render((x1, y1, z1, x2, y2, z2, count, i, base, sinX, sinZ, increment) -> {
                                graphicsHolder.drawLineInWorld(
                                        (float) (x1 - offset.getXMapped()),
                                        (float) (y1 - offset.getYMapped() + 0.5F),
                                        (float) (z1 - offset.getZMapped()),
                                        (float) (x2 - offset.getXMapped()),
                                        (float) (y2 - offset.getYMapped() + 0.5F),
                                        (float) (z2 - offset.getZMapped()),
                                        0xFF000000
                                );
                            }));
                    break;
            }
        });

        OCCLUSION_CULLING_THREAD.schedule(occlusionCullingInstance -> {
            final ObjectArrayList<Runnable> tasks = new ObjectArrayList<>();
            cullingTasksMSD.forEach(occlusionCullingInstanceRunnableFunction -> tasks.add(occlusionCullingInstanceRunnableFunction.apply(occlusionCullingInstance)));
            minecraftClientMSD.execute(() -> tasks.forEach(Runnable::run));
        });
    }

    @Unique
    private static void renderCatenaryStandard(ClientWorld clientWorld, Catenary catenary) {
        catenary.catenaryMath.render((x1, y1, z1, x2, y2, z2, count, i, base, sinX, sinZ, increment) -> {
            final BlockPos blockPos = Init.newBlockPos(x1, y1, z1);
            final int light = LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.getBlockMapped(), blockPos), clientWorld.getLightLevel(LightType.getSkyMapped(), blockPos));
            RenderTrains.scheduleRender(CATENARY_TEXTURE, false, RenderTrains.QueuedRenderLayer.EXTERIOR, ((graphicsHolder, offset) -> {
                if (count < 8) {
                    IDrawing.drawTexture(graphicsHolder, x1, y1 + 0.65F + base, z1, x2, y2 + 0.65F + base, z2, x2, y2, z2, x1, y1, z1, offset, 0.0F, 0.0F, 1.0F, 1.0F, Direction.UP, -1, light);
                    IDrawing.drawTexture(graphicsHolder, x2, y2 + 0.65F + base, z2, x1, y1 + 0.65F + base, z1, x1, y1, z1, x2, y2, z2, offset, 0.0F, 1.0F, 1.0F, 0.0F, Direction.UP, -1, light);
                } else {
                    if (i < (count / 2 - increment)) {
                        IDrawing.drawTexture(graphicsHolder, x1, y1 + 0.65F + base, z1, x2, y2 + 0.65F + (base * 0.5), z2, x2, y2, z2, x1, y1, z1, offset, 0.0F, 0.0F, 1.0F, 1.0F, Direction.UP, -1, light);
                        IDrawing.drawTexture(graphicsHolder, x2, y2 + 0.65F + (base * 0.5), z2, x1, y1 + 0.65F + base, z1, x1, y1, z1, x2, y2, z2, offset, 0.0F, 1.0F, 1.0F, 0.0F, Direction.UP, -1, light);
                    } else if (i >= (count / 2)) {
                        IDrawing.drawTexture(graphicsHolder, x1, y1 + 0.65F + base, z1, x2, y2 + 0.65F + (base / 0.5), z2, x2, y2, z2, x1, y1, z1, offset, 0.0F, 0.0F, 1.0F, 1.0F, Direction.UP, -1, light);
                        IDrawing.drawTexture(graphicsHolder, x2, y2 + 0.65F + (base / 0.5), z2, x1, y1 + 0.65F + base, z1, x1, y1, z1, x2, y2, z2, offset, 0.0F, 1.0F, 1.0F, 0.0F, Direction.UP, -1, light);
                    } else {
                        IDrawing.drawTexture(graphicsHolder, x1, y1 + 0.65F + base, z1, x2, y2 + 0.65F + base, z2, x2, y2, z2, x1, y1, z1, offset, 0.0F, 0.0F, 1.0F, 1.0F, Direction.UP, -1, light);
                        IDrawing.drawTexture(graphicsHolder, x2, y2 + 0.65F + base, z2, x1, y1 + 0.65F + base, z1, x1, y1, z1, x2, y2, z2, offset, 0.0F, 1.0F, 1.0F, 0.0F, Direction.UP, -1, light);
                    }
                }
                IDrawing.drawTexture(graphicsHolder, (x1 - sinX), y1, (z1 + sinZ), (x2 - sinX), y2, (z2 + sinZ), (x2 + sinX), y2, (z2 - sinZ), (x1 + sinX), y1, (z1 - sinZ), offset, 0.0F, 0.0F, 1.0F, 0.03125F, Direction.UP, -1, light);
                IDrawing.drawTexture(graphicsHolder, (x2 - sinX), y2, (z2 + sinZ), (x1 - sinX), y1, (z1 + sinZ), (x1 + sinX), y1, (z1 - sinZ), (x2 + sinX), y2, (z2 - sinZ), offset, 0.0F, 0.03125F, 1.0F, 0.0F, Direction.UP, -1, light);
            }));
        });
    }
}