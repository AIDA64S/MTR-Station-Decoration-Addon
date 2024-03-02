package top.mcmtr.mod;


import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.registry.BlockEntityTypeRegistryObject;
import top.mcmtr.mod.blocks.BlockCatenaryNode;
import top.mcmtr.mod.blocks.BlockRigidCatenaryNode;

public class BlockEntityTypes {
    static {
        CATENARY_NODE = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "catenary_node"), BlockCatenaryNode.BlockCatenaryNodeEntity::new, Blocks.CATENARY_NODE::get);
        RIGID_CATENARY_NODE = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "rigid_catenary_node"), BlockRigidCatenaryNode.BlockRigidCatenaryNodeEntity::new, Blocks.RIGID_CATENARY_NODE::get);
        ELECTRIC_NODE = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "electric_node"), BlockCatenaryNode.BlockCatenaryNodeEntity::new, Blocks.ELECTRIC_NODE::get);
    }

    public static final BlockEntityTypeRegistryObject<BlockCatenaryNode.BlockCatenaryNodeEntity> CATENARY_NODE;
    public static final BlockEntityTypeRegistryObject<BlockRigidCatenaryNode.BlockRigidCatenaryNodeEntity> RIGID_CATENARY_NODE;
    public static final BlockEntityTypeRegistryObject<BlockCatenaryNode.BlockCatenaryNodeEntity> ELECTRIC_NODE;

    public static void init() {
        Init.MSD_LOGGER.info("Registering MTR Station Decoration block entity types");
    }
}