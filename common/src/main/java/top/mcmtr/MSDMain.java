package top.mcmtr;

import mtr.CreativeModeTabs;
import mtr.Registry;
import mtr.RegistryObject;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
            BiConsumer<String, SoundEvent> registerSoundEvent
    ) {
        registerBlockItem.accept("railing_stair", MSDBlocks.RAILING_STAIR, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("railing_stair_end", MSDBlocks.RAILING_STAIR_END, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("railing_stair_end_mirror", MSDBlocks.RAILING_STAIR_END_MIRROR, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("railing_stair_flat", MSDBlocks.RAILING_STAIR_FLAT, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("railing_stair_mirror", MSDBlocks.RAILING_STAIR_MIRROR, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("railing_stair_start", MSDBlocks.RAILING_STAIR_START, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("railing_stair_start_mirror", MSDBlocks.RAILING_STAIR_START_MIRROR, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_pids", MSDBlocks.YAMANOTE_PIDS, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_4_pids", MSDBlocks.YAMANOTE_4_PIDS, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_5_pids", MSDBlocks.YAMANOTE_5_PIDS, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_6_pids", MSDBlocks.YAMANOTE_6_PIDS, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_7_pids", MSDBlocks.YAMANOTE_7_PIDS, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_2_even", MSDBlocks.YAMANOTE_RAILWAY_SIGN_2_EVEN, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_2_odd", MSDBlocks.YAMANOTE_RAILWAY_SIGN_2_ODD, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_3_even", MSDBlocks.YAMANOTE_RAILWAY_SIGN_3_EVEN, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_3_odd", MSDBlocks.YAMANOTE_RAILWAY_SIGN_3_ODD, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_4_even", MSDBlocks.YAMANOTE_RAILWAY_SIGN_4_EVEN, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_4_odd", MSDBlocks.YAMANOTE_RAILWAY_SIGN_4_ODD, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_5_even", MSDBlocks.YAMANOTE_RAILWAY_SIGN_5_EVEN, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_5_odd", MSDBlocks.YAMANOTE_RAILWAY_SIGN_5_ODD, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_6_even", MSDBlocks.YAMANOTE_RAILWAY_SIGN_6_EVEN, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_6_odd", MSDBlocks.YAMANOTE_RAILWAY_SIGN_6_ODD, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_7_even", MSDBlocks.YAMANOTE_RAILWAY_SIGN_7_EVEN, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_7_odd", MSDBlocks.YAMANOTE_RAILWAY_SIGN_7_ODD, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_pole", MSDBlocks.YAMANOTE_RAILWAY_SIGN_POLE, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("yamanote_railway_sign_double_pole", MSDBlocks.YAMANOTE_RAILWAY_SIGN_DOUBLE_POLE, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("railway_sign_double_pole", MSDBlocks.RAILWAY_SIGN_DOUBLE_POLE, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("electric_pole", MSDBlocks.ELECTRIC_POLE, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("surveillance_cameras", MSDBlocks.SURVEILLANCE_CAMERAS, MSDItems.MSD_BLOCKS);
        registerBlockItem.accept("surveillance_cameras_wall", MSDBlocks.SURVEILLANCE_CAMERAS_WALL, MSDItems.MSD_BLOCKS);

        registerBlock.accept("yamanote_railway_sign_middle", MSDBlocks.YAMANOTE_RAILWAY_SIGN_MIDDLE);

        registerBlockEntityType.accept("yamanote_pids", MSDBlockEntityTypes.YAMANOTE_PIDS_TILE_ENTITY);
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

        Registry.registerNetworkReceiver(PACKET_YAMANOTE_SIGN_TYPES, MSDPacketTrainDataGuiServer::receiveSignIdsC2S);
    }

    @FunctionalInterface
    public interface RegisterBlockItem {
        void accept(String string, RegistryObject<Block> block, CreativeModeTabs.Wrapper tab);
    }
}
