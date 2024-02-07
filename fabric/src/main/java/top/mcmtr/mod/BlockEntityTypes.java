package top.mcmtr.mod;


import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.registry.BlockEntityTypeRegistryObject;
import top.mcmtr.mod.blocks.BlockCatenaryNode;

public class BlockEntityTypes {
    static {
        CATENARY_NODE = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "catenary_node"), BlockCatenaryNode.BlockCatenaryNodeEntity::new, Blocks.CATENARY_NODE::get);
    }

    public static final BlockEntityTypeRegistryObject<BlockCatenaryNode.BlockCatenaryNodeEntity> CATENARY_NODE;

    public static void init() {
        Init.MSD_LOGGER.info("Registering MTR Station Decoration block entity types");
    }
}