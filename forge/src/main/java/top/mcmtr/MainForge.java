package top.mcmtr;

import mtr.CreativeModeTabs;
import mtr.Registry;
import mtr.RegistryObject;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.DeferredRegisterHolder;
import mtr.mappings.RegistryUtilities;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.mcmtr.mappings.ForgeUtilities;

public class MainForge {
    private static final DeferredRegisterHolder<Item> ITEMS = new DeferredRegisterHolder<>(Main.MOD_ID, ForgeUtilities.registryGetItem());
    private static final DeferredRegisterHolder<Block> BLOCKS = new DeferredRegisterHolder<>(Main.MOD_ID, ForgeUtilities.registryGetBlock());
    private static final DeferredRegisterHolder<BlockEntityType<?>> BLOCK_ENTITY_TYPES = new DeferredRegisterHolder<>(Main.MOD_ID, ForgeUtilities.registryGetBlockEntityType());
    private static final DeferredRegisterHolder<SoundEvent> SOUND_EVENTS = new DeferredRegisterHolder<>(Main.MOD_ID, ForgeUtilities.registryGetSoundEvent());
    static {
        Main.init(MainForge::registerBlock, MainForge::registerBlockEntityType, MainForge::registerSoundEvent);
    }

    public MainForge() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ForgeUtilities.registerModEventBus(Main.MOD_ID, eventBus);

        ITEMS.register();
        BLOCKS.register();
        BLOCK_ENTITY_TYPES.register();
        SOUND_EVENTS.register();

        eventBus.register(MTRForgeRegistry.class);
    }

    private static void registerBlock(String path, RegistryObject<Block> block) {
        BLOCKS.register(path, block::get);
    }

    private static void registerBlock(String path, RegistryObject<Block> block, CreativeModeTabs.Wrapper creativeModeTabWrapper) {
        registerBlock(path, block);
        ITEMS.register(path, () -> {
            final BlockItem blockItem = new BlockItem(block.get(), RegistryUtilities.createItemProperties(creativeModeTabWrapper::get));
            Registry.registerCreativeModeTab(creativeModeTabWrapper.resourceLocation, blockItem);
            return blockItem;
        });
    }

    private static void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>> blockEntityType) {
        BLOCK_ENTITY_TYPES.register(path, blockEntityType::get);
    }

    private static void registerSoundEvent(String path, SoundEvent soundEvent) {
        SOUND_EVENTS.register(path, () -> soundEvent);
    }

    private static class MTRForgeRegistry {
        @SubscribeEvent
        public static void onClientSetupEvent(FMLClientSetupEvent event) {
            MainClient.init();
        }
    }
}
