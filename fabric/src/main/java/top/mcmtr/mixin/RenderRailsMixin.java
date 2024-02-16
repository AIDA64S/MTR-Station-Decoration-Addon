package top.mcmtr.mixin;

import org.mtr.mapping.holder.Identifier;
import org.mtr.mod.render.RenderRails;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.mcmtr.mod.Init;

@Mixin(RenderRails.class)
public class RenderRailsMixin {
    @Unique
    private static final Identifier CATENARY_TEXTURE = new Identifier(Init.MOD_ID, "textures/block/overhead_line.png");

    @Inject(method = "render", at = @At("HEAD"), remap = false)
    private static void renderCatenary(CallbackInfo ci) {

    }
}