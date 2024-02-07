package top.mcmtr.mod;

import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.Item;
import org.mtr.mapping.registry.ItemRegistryObject;
import top.mcmtr.mod.items.ItemCatenaryConnector;

public class Items {
    static {
        CATENARY_CONNECTOR = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "catenary_connector"), itemSettings -> new Item(new ItemCatenaryConnector(itemSettings.maxCount(1), true)), CreativeModeTabs.EXTERNAL);
        CONNECTOR_REMOVER = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "catenary_remover"), itemSettings -> new Item(new ItemCatenaryConnector(itemSettings.maxCount(1), false)), CreativeModeTabs.EXTERNAL);

    }

    public static final ItemRegistryObject CATENARY_CONNECTOR;
    public static final ItemRegistryObject CONNECTOR_REMOVER;

    public static void init() {
        Init.MSD_LOGGER.info("Registering MTR Station Decoration items");
    }

}