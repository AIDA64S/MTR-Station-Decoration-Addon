package top.mcmtr.mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.registry.Registry;

public class Init implements Utilities {
    public static final String MOD_ID = "msd";
    public static final Logger MSD_LOGGER = LogManager.getLogger("MTR-Station-Decoration");
    public static final Registry REGISTRY = new Registry();

    public static void init() {
        Blocks.init();
        Items.init();
        BlockEntityTypes.init();
        CreativeModeTabs.init();

        REGISTRY.init();
    }
}