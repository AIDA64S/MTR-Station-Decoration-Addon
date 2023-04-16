package top.mcmtr;

import mtr.RegistryClient;
import mtr.data.PIDSType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import top.mcmtr.client.MSDClientData;
import top.mcmtr.config.Config;
import top.mcmtr.items.ItemMSDBlockClickingBase;
import top.mcmtr.packet.MSDPacket;
import top.mcmtr.packet.MSDPacketTrainDataGuiClient;
import top.mcmtr.render.RenderCustomTextSign;
import top.mcmtr.render.RenderPIDS;
import top.mcmtr.render.RenderYamanoteRailwaySign;

public class MSDMainClient {
    public static void init() {
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.YUUNI_PIDS.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.YUUNI_2_PIDS.get());
        RegistryClient.registerBlockRenderType(RenderType.translucent(), MSDBlocks.SURVEILLANCE_CAMERAS.get());
        RegistryClient.registerBlockRenderType(RenderType.translucent(), MSDBlocks.SURVEILLANCE_CAMERAS_WALL.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.HALL_SEAT_SIDE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.HALL_SEAT_MIDDLE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.HALL_SEAT_SIDE_MIRROR.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_POLE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_POLE_TOP_MIDDLE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_POLE_TOP_SIDE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_RACK_POLE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_RACK_POLE_BOTH_SIDE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_RACK_SIDE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_RACK_BOTH_SIDE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_RACK_2.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_RACK_1.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_NODE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_NODE_STYLE_2.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.SHORT_CATENARY_RACK_POLE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.SHORT_CATENARY_RACK_POLE_BOTH_SIDE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.SHORT_CATENARY_RACK_SIDE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.SHORT_CATENARY_RACK_BOTH_SIDE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.SHORT_CATENARY_RACK.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.SHORT_CATENARY_NODE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.SHORT_CATENARY_NODE_STYLE_2.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.ELECTRIC_POLE_TOP_SIDE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.ELECTRIC_POLE_TOP_BOTH_SIDE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.ELECTRIC_POLE_SIDE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.ELECTRIC_POLE_ANOTHER_SIDE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.ELECTRIC_NODE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.RIGID_CATENARY_NODE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.YUUNI_TICKET.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CEILING_DOUBLE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CEILING_DOUBLE_LIGHT.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.STANDING_SIGN.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.STANDING_SIGN_POLE.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.STANDING_SIGN_1.get());

        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YUUNI_PIDS_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 2, 2.5F, 7.5F, 6, 6.5F, 27, true, false, PIDSType.PIDS, 0xFF9900, 0x33CC00, 1.25F, true));
        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YUUNI_PIDS_2_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 1, 1, 3.25F, 6F, 2.5F, 30, true, false, PIDSType.PIDS, 0xFF9900, 0xFFC0CB));
        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_4_PIDS_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 3, 0F, 15F, 7, 6F, 32, true, true, PIDSType.PIDS, 0x00FF00, 0xFF0000));
        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_5_PIDS_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 3, -4F, 15F, 7, 6F, 40, true, true, PIDSType.PIDS, 0x00FF00, 0xFF0000));
        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_6_PIDS_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 3, -8F, 15F, 7, 6F, 48, true, true, PIDSType.PIDS, 0x00FF00, 0xFF0000));
        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_7_PIDS_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 3, -12F, 15F, 7, 6F, 56, true, true, PIDSType.PIDS, 0x00FF00, 0xFF0000));
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
        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.STANDING_SIGN_TILE_ENTITY.get(), dispatcher -> new RenderCustomTextSign<>(dispatcher, 3, 8F, 14.5F, 7.01F, 15F, 11, true, 0x000000, true, 0xFFFFFF, 2F, 3.1F, 6.2F, 0.004F));
        RegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.STANDING_SIGN_1_TILE_ENTITY.get(), dispatcher -> new RenderCustomTextSign<>(dispatcher, 1, 2.5F, 7.25F, 7.65F, 4F, 11, true, 0xA78E44, false, 0xA78E44, 1.6F, 1.6F, 3.2F, 0.0625F));

        RegistryClient.registerNetworkReceiver(MSDPacket.PACKET_OPEN_YAMANOTE_RAILWAY_SIGN_SCREEN, packet -> MSDPacketTrainDataGuiClient.openYamanoteRailwaySignScreenS2C(Minecraft.getInstance(), packet));
        RegistryClient.registerNetworkReceiver(MSDPacket.PACKET_WRITE_CATENARY, packet -> MSDClientData.writeCatenaries(Minecraft.getInstance(), packet));
        RegistryClient.registerNetworkReceiver(MSDPacket.PACKET_REMOVE_CATENARY_NODE, packet -> MSDPacketTrainDataGuiClient.removeCatenaryNodeS2C(Minecraft.getInstance(), packet));
        RegistryClient.registerNetworkReceiver(MSDPacket.PACKET_CREATE_CATENARY, packet -> MSDPacketTrainDataGuiClient.createCatenaryS2C(Minecraft.getInstance(), packet));
        RegistryClient.registerNetworkReceiver(MSDPacket.PACKET_REMOVE_CATENARY, packet -> MSDPacketTrainDataGuiClient.removeCatenaryConnectionS2C(Minecraft.getInstance(), packet));
        RegistryClient.registerNetworkReceiver(MSDPacket.PACKET_WRITE_RIGID_CATENARY, packet -> MSDClientData.writeRigidCatenaries(Minecraft.getInstance(), packet));
        RegistryClient.registerNetworkReceiver(MSDPacket.PACKET_REMOVE_RIGID_CATENARY_NODE, packet -> MSDPacketTrainDataGuiClient.removeRigidCatenaryNodeS2C(Minecraft.getInstance(), packet));
        RegistryClient.registerNetworkReceiver(MSDPacket.PACKET_CREATE_RIGID_CATENARY, packet -> MSDPacketTrainDataGuiClient.createRigidCatenaryS2C(Minecraft.getInstance(), packet));
        RegistryClient.registerNetworkReceiver(MSDPacket.PACKET_REMOVE_RIGID_CATENARY, packet -> MSDPacketTrainDataGuiClient.removeRigidCatenaryConnectionS2C(Minecraft.getInstance(), packet));
        RegistryClient.registerNetworkReceiver(MSDPacket.PACKET_OPEN_CUSTOM_TEXT_SIGN_CONFIG_SCREEN, packet -> MSDPacketTrainDataGuiClient.openCustomTextSignConfigScreenS2C(Minecraft.getInstance(), packet));

        Config.refreshProperties();
    }

    public static void registerItemModelPredicates() {
        RegistryClient.registerItemModelPredicate(MSDMain.MOD_ID + ":selected", MSDItems.CATENARY_REMOVER.get(), ItemMSDBlockClickingBase.TAG_POS);
        RegistryClient.registerItemModelPredicate(MSDMain.MOD_ID + ":selected", MSDItems.ELECTRIC_CONNECTOR.get(), ItemMSDBlockClickingBase.TAG_POS);
        RegistryClient.registerItemModelPredicate(MSDMain.MOD_ID + ":selected", MSDItems.CATENARY_CONNECTOR.get(), ItemMSDBlockClickingBase.TAG_POS);
        RegistryClient.registerItemModelPredicate(MSDMain.MOD_ID + ":selected", MSDItems.RIGID_CATENARY_CONNECTOR.get(), ItemMSDBlockClickingBase.TAG_POS);
        RegistryClient.registerItemModelPredicate(MSDMain.MOD_ID + ":selected", MSDItems.RIGID_CATENARY_REMOVER.get(), ItemMSDBlockClickingBase.TAG_POS);
        RegistryClient.registerItemModelPredicate(MSDMain.MOD_ID + ":selected", MSDItems.RIGID_SOFT_CATENARY_CONNECTOR.get(), ItemMSDBlockClickingBase.TAG_POS);
    }
}