package top.mcmtr;

import mtr.RegistryClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import top.mcmtr.packet.MSDPacket;
import top.mcmtr.packet.MSDPacketTrainDataGuiClient;
import top.mcmtr.render.RenderPIDS;
import top.mcmtr.render.RenderYamanoteRailwaySign;

public class MainClient {
    public static void init() {
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.RAILING_STAIR.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.RAILING_STAIR_END.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.RAILING_STAIR_END_MIRROR.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.RAILING_STAIR_FLAT.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.RAILING_STAIR_MIRROR.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.RAILING_STAIR_START.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.RAILING_STAIR_START_MIRROR.get());

        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_PIDS_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 3, 1F, 15F, 7, 6F, 30, true, true, 0x00FF00, 0xFF0000));
        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_2_EVEN_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_2_ODD_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_3_EVEN_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_3_ODD_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_4_EVEN_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_4_ODD_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_5_EVEN_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_5_ODD_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_6_EVEN_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_6_ODD_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_7_EVEN_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_7_ODD_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);

        RegistryClient.registerNetworkReceiver(MSDPacket.PACKET_OPEN_YAMANOTE_RAILWAY_SIGN_SCREEN, packet -> MSDPacketTrainDataGuiClient.openYamanoteRailwaySignScreenS2C(Minecraft.getInstance(), packet));
    }
}
