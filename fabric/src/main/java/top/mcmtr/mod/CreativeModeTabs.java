package top.mcmtr.mod;

import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.ItemConvertible;
import org.mtr.mapping.holder.ItemStack;
import org.mtr.mapping.holder.Items;
import org.mtr.mapping.registry.CreativeModeTabHolder;

public class CreativeModeTabs {
    static {
        STATION = Init.REGISTRY.createCreativeModeTabHolder(new Identifier(Init.MOD_ID, "station"), () -> new ItemStack(new ItemConvertible(Items.getBeefMapped().data)));
        EXTERNAL = Init.REGISTRY.createCreativeModeTabHolder(new Identifier(Init.MOD_ID, "external"), () -> new ItemStack(new ItemConvertible(Items.getPigSpawnEggMapped().data)));
    }

    public static final CreativeModeTabHolder STATION;

    public static final CreativeModeTabHolder EXTERNAL;

    public static void init() {
        Init.MSD_LOGGER.info("Registering MTR Station Decoration creative mode tabs");
    }
}