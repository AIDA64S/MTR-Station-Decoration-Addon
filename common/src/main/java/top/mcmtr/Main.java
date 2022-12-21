package top.mcmtr;

import mtr.CreativeModeTabs;
import mtr.RegistryObject;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiConsumer;

public class Main {
    public static final String MOD_ID = "msd";

    public static void init(
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
    }

    @FunctionalInterface
    public interface RegisterBlockItem {
        void accept(String string, RegistryObject<Block> block, CreativeModeTabs.Wrapper tab);
    }
}
