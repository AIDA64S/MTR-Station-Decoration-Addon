package top.mcmtr.mod;


import org.mtr.mapping.holder.Block;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.registry.BlockRegistryObject;
import top.mcmtr.mod.blocks.BlockCatenaryNode;

public class Blocks {
    static {
        CATENARY_NODE = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "catenary_node"), () -> new Block(new BlockCatenaryNode()), CreativeModeTabs.EXTERNAL);
    }

    public static final BlockRegistryObject CATENARY_NODE;

    public static void init() {
        Init.MSD_LOGGER.info("Registering MTR Station Decoration blocks");
    }
}