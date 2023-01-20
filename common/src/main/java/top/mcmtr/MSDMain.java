package top.mcmtr;

import mtr.CreativeModeTabs;
import mtr.Registry;
import mtr.RegistryObject;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import top.mcmtr.data.CatenaryData;
import top.mcmtr.data.RigidCatenaryData;
import top.mcmtr.packet.MSDPacketTrainDataGuiServer;

import java.util.function.BiConsumer;

import static top.mcmtr.packet.MSDPacket.PACKET_YAMANOTE_SIGN_TYPES;

public class MSDMain {
    public static final String MOD_ID = "msd";

    public static void init(
            BiConsumer<String, RegistryObject<Item>> registerItem,
            BiConsumer<String, RegistryObject<Block>> registerBlock,
            RegisterBlockItem registerBlockItem,
            BiConsumer<String, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>>> registerBlockEntityType,
            BiConsumer<String, RegistryObject<? extends EntityType<? extends Entity>>> registerEntityType,
            BiConsumer<String, SoundEvent> registerSoundEvent
    ) {
        registerItem.accept("catenary_connector", MSDItems.CATENARY_CONNECTOR);
        registerItem.accept("electric_connector", MSDItems.ELECTRIC_CONNECTOR);
        registerItem.accept("catenary_remover", MSDItems.CATENARY_REMOVER);
        registerItem.accept("rigid_catenary_connector", MSDItems.RIGID_CATENARY_CONNECTOR);
        registerItem.accept("rigid_catenary_remover", MSDItems.RIGID_CATENARY_REMOVER);
        registerItem.accept("rigid_soft_catenary_connector", MSDItems.RIGID_SOFT_CATENARY_CONNECTOR);

        registerBlockItem.accept("railing_stair", MSDBlocks.RAILING_STAIR, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("railing_stair_end", MSDBlocks.RAILING_STAIR_END, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("railing_stair_end_mirror", MSDBlocks.RAILING_STAIR_END_MIRROR, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("railing_stair_flat", MSDBlocks.RAILING_STAIR_FLAT, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("railing_stair_mirror", MSDBlocks.RAILING_STAIR_MIRROR, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("railing_stair_start", MSDBlocks.RAILING_STAIR_START, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("railing_stair_start_mirror", MSDBlocks.RAILING_STAIR_START_MIRROR, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("yuuni_pids", MSDBlocks.YUUNI_PIDS, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_4_pids", MSDBlocks.YAMANOTE_4_PIDS, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_5_pids", MSDBlocks.YAMANOTE_5_PIDS, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_6_pids", MSDBlocks.YAMANOTE_6_PIDS, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_7_pids", MSDBlocks.YAMANOTE_7_PIDS, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_2_even", MSDBlocks.YAMANOTE_RAILWAY_SIGN_2_EVEN, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_2_odd", MSDBlocks.YAMANOTE_RAILWAY_SIGN_2_ODD, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_3_even", MSDBlocks.YAMANOTE_RAILWAY_SIGN_3_EVEN, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_3_odd", MSDBlocks.YAMANOTE_RAILWAY_SIGN_3_ODD, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_4_even", MSDBlocks.YAMANOTE_RAILWAY_SIGN_4_EVEN, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_4_odd", MSDBlocks.YAMANOTE_RAILWAY_SIGN_4_ODD, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_5_even", MSDBlocks.YAMANOTE_RAILWAY_SIGN_5_EVEN, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_5_odd", MSDBlocks.YAMANOTE_RAILWAY_SIGN_5_ODD, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_6_even", MSDBlocks.YAMANOTE_RAILWAY_SIGN_6_EVEN, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_6_odd", MSDBlocks.YAMANOTE_RAILWAY_SIGN_6_ODD, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_7_even", MSDBlocks.YAMANOTE_RAILWAY_SIGN_7_EVEN, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_7_odd", MSDBlocks.YAMANOTE_RAILWAY_SIGN_7_ODD, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_pole", MSDBlocks.YAMANOTE_RAILWAY_SIGN_POLE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_double_pole", MSDBlocks.YAMANOTE_RAILWAY_SIGN_DOUBLE_POLE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("railway_sign_double_pole", MSDBlocks.RAILWAY_SIGN_DOUBLE_POLE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("surveillance_cameras", MSDBlocks.SURVEILLANCE_CAMERAS, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("surveillance_cameras_wall", MSDBlocks.SURVEILLANCE_CAMERAS_WALL, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("hall_seat_side", MSDBlocks.HALL_SEAT_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("hall_seat_middle", MSDBlocks.HALL_SEAT_MIDDLE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("hall_seat_side_mirror", MSDBlocks.HALL_SEAT_SIDE_MIRROR, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("catenary_pole", MSDBlocks.CATENARY_POLE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("catenary_pole_top_side", MSDBlocks.CATENARY_POLE_TOP_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("catenary_pole_top_middle", MSDBlocks.CATENARY_POLE_TOP_MIDDLE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("catenary_rack_pole", MSDBlocks.CATENARY_RACK_POLE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("catenary_rack_pole_both_side", MSDBlocks.CATENARY_RACK_POLE_BOTH_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("catenary_rack_side", MSDBlocks.CATENARY_RACK_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("catenary_rack_both_side", MSDBlocks.CATENARY_RACK_BOTH_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("catenary_rack_2", MSDBlocks.CATENARY_RACK_2, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("catenary_rack_1", MSDBlocks.CATENARY_RACK_1, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("catenary_node", MSDBlocks.CATENARY_NODE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("catenary_node_style_2", MSDBlocks.CATENARY_NODE_STYLE_2, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("short_catenary_rack_pole", MSDBlocks.SHORT_CATENARY_RACK_POLE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("short_catenary_rack_pole_both_side", MSDBlocks.SHORT_CATENARY_RACK_POLE_BOTH_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("short_catenary_rack_side", MSDBlocks.SHORT_CATENARY_RACK_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("short_catenary_rack_both_side", MSDBlocks.SHORT_CATENARY_RACK_BOTH_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("short_catenary_rack", MSDBlocks.SHORT_CATENARY_RACK, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("short_catenary_node", MSDBlocks.SHORT_CATENARY_NODE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("short_catenary_node_style_2", MSDBlocks.SHORT_CATENARY_NODE_STYLE_2, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("electric_pole_top_side", MSDBlocks.ELECTRIC_POLE_TOP_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("electric_pole_top_both_side", MSDBlocks.ELECTRIC_POLE_TOP_BOTH_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("electric_pole_side", MSDBlocks.ELECTRIC_POLE_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("electric_pole_another_side", MSDBlocks.ELECTRIC_POLE_ANOTHER_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("electric_node", MSDBlocks.ELECTRIC_NODE, MSDCreativeModeTabs.MSD_BLOCKS);
        registerBlockItem.accept("rigid_catenary_node", MSDBlocks.RIGID_CATENARY_NODE, MSDCreativeModeTabs.MSD_BLOCKS);

        registerBlock.accept("yamanote_railway_sign_middle", MSDBlocks.YAMANOTE_RAILWAY_SIGN_MIDDLE);

        registerBlockEntityType.accept("yuuni_pids", MSDBlockEntityTypes.YUUNI_PIDS_TILE_ENTITY);
        registerBlockEntityType.accept("yamanote_4_pids", MSDBlockEntityTypes.YAMANOTE_4_PIDS_TILE_ENTITY);
        registerBlockEntityType.accept("yamanote_5_pids", MSDBlockEntityTypes.YAMANOTE_5_PIDS_TILE_ENTITY);
        registerBlockEntityType.accept("yamanote_6_pids", MSDBlockEntityTypes.YAMANOTE_6_PIDS_TILE_ENTITY);
        registerBlockEntityType.accept("yamanote_7_pids", MSDBlockEntityTypes.YAMANOTE_7_PIDS_TILE_ENTITY);
        registerBlockEntityType.accept("yamanote_railway_sign_2_even", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_2_EVEN_TILE_ENTITY);
        registerBlockEntityType.accept("yamanote_railway_sign_2_odd", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_2_ODD_TILE_ENTITY);
        registerBlockEntityType.accept("yamanote_railway_sign_3_even", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_3_EVEN_TILE_ENTITY);
        registerBlockEntityType.accept("yamanote_railway_sign_3_odd", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_3_ODD_TILE_ENTITY);
        registerBlockEntityType.accept("yamanote_railway_sign_4_even", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_4_EVEN_TILE_ENTITY);
        registerBlockEntityType.accept("yamanote_railway_sign_4_odd", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_4_ODD_TILE_ENTITY);
        registerBlockEntityType.accept("yamanote_railway_sign_5_even", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_5_EVEN_TILE_ENTITY);
        registerBlockEntityType.accept("yamanote_railway_sign_5_odd", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_5_ODD_TILE_ENTITY);
        registerBlockEntityType.accept("yamanote_railway_sign_6_even", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_6_EVEN_TILE_ENTITY);
        registerBlockEntityType.accept("yamanote_railway_sign_6_odd", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_6_ODD_TILE_ENTITY);
        registerBlockEntityType.accept("yamanote_railway_sign_7_even", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_7_EVEN_TILE_ENTITY);
        registerBlockEntityType.accept("yamanote_railway_sign_7_odd", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_7_ODD_TILE_ENTITY);

        Registry.registerNetworkReceiver(PACKET_YAMANOTE_SIGN_TYPES, MSDPacketTrainDataGuiServer::receiveMSDSignIdsC2S);

        Registry.registerTickEvent(minecraftServer -> {
            minecraftServer.getAllLevels().forEach(serverLevel -> {
                final CatenaryData catenaryData = CatenaryData.getInstance(serverLevel);
                final RigidCatenaryData rigidCatenaryData = RigidCatenaryData.getInstance(serverLevel);
                if (catenaryData != null) {
                    catenaryData.simulateCatenaries();
                }
                if (rigidCatenaryData != null) {
                    rigidCatenaryData.simulateRigidCatenaries();
                }
            });
        });
        Registry.registerPlayerJoinEvent(player -> {
            final CatenaryData catenaryData = CatenaryData.getInstance(player.getLevel());
            final RigidCatenaryData rigidCatenaryData = RigidCatenaryData.getInstance(player.getLevel());
        });
        Registry.registerPlayerQuitEvent(player -> {
            final CatenaryData catenaryData = CatenaryData.getInstance(player.getLevel());
            final RigidCatenaryData rigidCatenaryData = RigidCatenaryData.getInstance(player.getLevel());
            if (catenaryData != null) {
                catenaryData.disconnectPlayer(player);
            }
            if (rigidCatenaryData != null) {
                rigidCatenaryData.disconnectPlayer(player);
            }
        });
    }

    @FunctionalInterface
    public interface RegisterBlockItem {
        void accept(String string, RegistryObject<Block> block, CreativeModeTabs.Wrapper tab);
    }
}