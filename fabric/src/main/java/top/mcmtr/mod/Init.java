package top.mcmtr.mod;

import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.EventRegistry;
import org.mtr.mapping.registry.Registry;

import java.util.logging.Logger;

public class Init implements Utilities {
    public static final String MOD_ID = "msd";
    public static final Logger MSD_LOGGER = Logger.getLogger("Station-Decoration");
    public static final Registry REGISTRY = new Registry();
    public static final String CHANNEL = "msd_update";

    private static final Object2ObjectArrayMap<Identifier, IntObjectImmutablePair<ObjectArraySet<ObjectBooleanImmutablePair<ServerPlayerEntity>>>> PLAYERS_TO_UPDATE = new Object2ObjectArrayMap<>();

    public static void init() {
        Blocks.init();
        Items.init();
        BlockEntityTypes.init();
        CreativeModeTabs.init();

        REGISTRY.setupPackets(new Identifier(MOD_ID, "packet"));

        EventRegistry.registerServerStarted(minecraftServer -> {

        });

        EventRegistry.registerServerStopping(minecraftServer -> {

        });

        EventRegistry.registerStartServerTick(() -> {

        });
        REGISTRY.init();
    }
}