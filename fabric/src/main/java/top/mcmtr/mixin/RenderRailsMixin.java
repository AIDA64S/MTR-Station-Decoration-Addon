package top.mcmtr.mixin;

import org.mtr.mapping.holder.*;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.render.RenderRails;
import org.mtr.mod.render.RenderTrains;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.mod.Init;
import top.mcmtr.mod.client.ClientData;

@Mixin(RenderRails.class)
public class RenderRailsMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lorg/mtr/libraries/it/unimi/dsi/fastutil/objects/ObjectOpenHashBigSet;forEach(Ljava/util/function/Consumer;)V", ordinal = 0))
    private static void renderCatenary(CallbackInfo ci) {
        final MinecraftClient minecraftClient = MinecraftClient.getInstance();
        final ClientWorld clientWorld = minecraftClient.getWorldMapped();
        final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
        if (clientWorld == null || clientPlayerEntity == null) {
            return;
        }
        ClientData.getInstance().catenaries.forEach(catenary -> {
            if (ClientData.getInstance().catenaryCulling.getOrDefault(catenary.getHexId(), false)) {
                renderCatenaryStandard(clientWorld, catenary);
            }
        });
    }

    @Unique
    private static void renderCatenaryStandard(ClientWorld clientWorld, Catenary catenary) {
        Identifier texture = new Identifier(Init.MOD_ID, "textures/block/overhead_line.png");
        catenary.catenaryMath.render((x1, y1, z1, x2, y2, z2, count, i, base, sinX, sinZ, increment) -> {
            final BlockPos blockPos = Init.newBlockPos(x1, y1, z1);
            final int light = LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.getBlockMapped(), blockPos), clientWorld.getLightLevel(LightType.getSkyMapped(), blockPos));
            RenderTrains.scheduleRender(texture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, ((graphicsHolder, offset) -> {
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