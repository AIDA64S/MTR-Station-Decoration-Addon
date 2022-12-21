package top.mcmtr;

import mtr.RegistryClient;
import net.minecraft.client.renderer.RenderType;

public class MainClient {
    public static void init() {
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.RAILING_STAIR.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.RAILING_STAIR_END.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.RAILING_STAIR_END_MIRROR.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.RAILING_STAIR_FLAT.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.RAILING_STAIR_MIRROR.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.RAILING_STAIR_START.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.RAILING_STAIR_START_MIRROR.get());
    }
}
