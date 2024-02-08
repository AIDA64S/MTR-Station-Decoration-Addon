package top.mcmtr.mixin;

import org.mtr.mod.render.RenderRails;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderRails.class)
public class RenderRailsMixin {
    @Inject(method = "render", at = @At("HEAD"), remap = false)
    private static void renderCatenary(CallbackInfo ci) {

    }
}