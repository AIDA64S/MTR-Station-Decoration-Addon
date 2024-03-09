package top.mcmtr.mod;

import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.registry.BlockEntityTypeRegistryObject;
import top.mcmtr.mod.blocks.*;

public class BlockEntityTypes {
    static {
        CATENARY_NODE = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "catenary_node"), BlockCatenaryNode.BlockCatenaryNodeEntity::new, Blocks.CATENARY_NODE::get);
        RIGID_CATENARY_NODE = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "rigid_catenary_node"), BlockRigidCatenaryNode.BlockRigidCatenaryNodeEntity::new, Blocks.RIGID_CATENARY_NODE::get);
        ELECTRIC_NODE = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "electric_node"), BlockCatenaryNode.BlockCatenaryNodeEntity::new, Blocks.ELECTRIC_NODE::get);
        YAMANOTE_RAILWAY_SIGN_ENTITY_2_EVEN = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "yamanote_railway_sign_2_even"), (pos, state) -> new BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity(2, false, pos, state), Blocks.YAMANOTE_RAILWAY_SIGN_2_EVEN::get);
        YAMANOTE_RAILWAY_SIGN_ENTITY_2_ODD = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "yamanote_railway_sign_2_odd"), (pos, state) -> new BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity(2, true, pos, state), Blocks.YAMANOTE_RAILWAY_SIGN_2_ODD::get);
        YAMANOTE_RAILWAY_SIGN_ENTITY_3_EVEN = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "yamanote_railway_sign_3_even"), (pos, state) -> new BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity(3, false, pos, state), Blocks.YAMANOTE_RAILWAY_SIGN_3_EVEN::get);
        YAMANOTE_RAILWAY_SIGN_ENTITY_3_ODD = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "yamanote_railway_sign_3_odd"), (pos, state) -> new BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity(3, true, pos, state), Blocks.YAMANOTE_RAILWAY_SIGN_3_ODD::get);
        YAMANOTE_RAILWAY_SIGN_ENTITY_4_EVEN = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "yamanote_railway_sign_4_even"), (pos, state) -> new BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity(4, false, pos, state), Blocks.YAMANOTE_RAILWAY_SIGN_4_EVEN::get);
        YAMANOTE_RAILWAY_SIGN_ENTITY_4_ODD = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "yamanote_railway_sign_4_odd"), (pos, state) -> new BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity(4, true, pos, state), Blocks.YAMANOTE_RAILWAY_SIGN_4_ODD::get);
        YAMANOTE_RAILWAY_SIGN_ENTITY_5_EVEN = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "yamanote_railway_sign_5_even"), (pos, state) -> new BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity(5, false, pos, state), Blocks.YAMANOTE_RAILWAY_SIGN_5_EVEN::get);
        YAMANOTE_RAILWAY_SIGN_ENTITY_5_ODD = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "yamanote_railway_sign_5_odd"), (pos, state) -> new BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity(5, true, pos, state), Blocks.YAMANOTE_RAILWAY_SIGN_5_ODD::get);
        YAMANOTE_RAILWAY_SIGN_ENTITY_6_EVEN = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "yamanote_railway_sign_6_even"), (pos, state) -> new BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity(6, false, pos, state), Blocks.YAMANOTE_RAILWAY_SIGN_6_EVEN::get);
        YAMANOTE_RAILWAY_SIGN_ENTITY_6_ODD = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "yamanote_railway_sign_6_odd"), (pos, state) -> new BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity(6, true, pos, state), Blocks.YAMANOTE_RAILWAY_SIGN_6_ODD::get);
        YAMANOTE_RAILWAY_SIGN_ENTITY_7_EVEN = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "yamanote_railway_sign_7_even"), (pos, state) -> new BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity(7, false, pos, state), Blocks.YAMANOTE_RAILWAY_SIGN_7_EVEN::get);
        YAMANOTE_RAILWAY_SIGN_ENTITY_7_ODD = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "yamanote_railway_sign_7_odd"), (pos, state) -> new BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity(7, true, pos, state), Blocks.YAMANOTE_RAILWAY_SIGN_7_ODD::get);

        YUUNI_PIDS = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "yuuni_pids"), (pos, state) -> new BlockYuuniPIDS.BlockYuuniPIDSEntity(2, pos, state), Blocks.YUUNI_PIDS::get);
        YUUNI_2_PIDS = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "yuuni_2_pids"), (pos, state) -> new BlockYuuniPIDS.BlockYuuniPIDSEntity(1, pos, state), Blocks.YUUNI_2_PIDS::get);
        YAMANOTE_4_PIDS = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "yamanote_4_pids"), (pos, state) -> new BlockYamanotePIDS.BlockYamanotePIDSEntity(4, pos, state), Blocks.YAMANOTE_4_PIDS::get);
        YAMANOTE_5_PIDS = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "yamanote_5_pids"), (pos, state) -> new BlockYamanotePIDS.BlockYamanotePIDSEntity(5, pos, state), Blocks.YAMANOTE_5_PIDS::get);
        YAMANOTE_6_PIDS = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "yamanote_6_pids"), (pos, state) -> new BlockYamanotePIDS.BlockYamanotePIDSEntity(6, pos, state), Blocks.YAMANOTE_6_PIDS::get);
        YAMANOTE_7_PIDS = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "yamanote_7_pids"), (pos, state) -> new BlockYamanotePIDS.BlockYamanotePIDSEntity(7, pos, state), Blocks.YAMANOTE_7_PIDS::get);
        STANDING_SIGN = Init.REGISTRY.registerBlockEntityType(new Identifier(Init.MOD_ID, "yuuni_standing_sign"), (pos, state) -> new BlockStandingSign.BlockStandingSignEntity(pos, state, 3), Blocks.STANDING_SIGN::get);
    }

    public static final BlockEntityTypeRegistryObject<BlockCatenaryNode.BlockCatenaryNodeEntity> CATENARY_NODE;
    public static final BlockEntityTypeRegistryObject<BlockRigidCatenaryNode.BlockRigidCatenaryNodeEntity> RIGID_CATENARY_NODE;
    public static final BlockEntityTypeRegistryObject<BlockCatenaryNode.BlockCatenaryNodeEntity> ELECTRIC_NODE;
    public static final BlockEntityTypeRegistryObject<BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity> YAMANOTE_RAILWAY_SIGN_ENTITY_2_EVEN;
    public static final BlockEntityTypeRegistryObject<BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity> YAMANOTE_RAILWAY_SIGN_ENTITY_2_ODD;
    public static final BlockEntityTypeRegistryObject<BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity> YAMANOTE_RAILWAY_SIGN_ENTITY_3_EVEN;
    public static final BlockEntityTypeRegistryObject<BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity> YAMANOTE_RAILWAY_SIGN_ENTITY_3_ODD;
    public static final BlockEntityTypeRegistryObject<BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity> YAMANOTE_RAILWAY_SIGN_ENTITY_4_EVEN;
    public static final BlockEntityTypeRegistryObject<BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity> YAMANOTE_RAILWAY_SIGN_ENTITY_4_ODD;
    public static final BlockEntityTypeRegistryObject<BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity> YAMANOTE_RAILWAY_SIGN_ENTITY_5_EVEN;
    public static final BlockEntityTypeRegistryObject<BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity> YAMANOTE_RAILWAY_SIGN_ENTITY_5_ODD;
    public static final BlockEntityTypeRegistryObject<BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity> YAMANOTE_RAILWAY_SIGN_ENTITY_6_EVEN;
    public static final BlockEntityTypeRegistryObject<BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity> YAMANOTE_RAILWAY_SIGN_ENTITY_6_ODD;
    public static final BlockEntityTypeRegistryObject<BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity> YAMANOTE_RAILWAY_SIGN_ENTITY_7_EVEN;
    public static final BlockEntityTypeRegistryObject<BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity> YAMANOTE_RAILWAY_SIGN_ENTITY_7_ODD;
    public static final BlockEntityTypeRegistryObject<BlockYuuniPIDS.BlockYuuniPIDSEntity> YUUNI_PIDS;
    public static final BlockEntityTypeRegistryObject<BlockYuuniPIDS.BlockYuuniPIDSEntity> YUUNI_2_PIDS;
    public static final BlockEntityTypeRegistryObject<BlockYamanotePIDS.BlockYamanotePIDSEntity> YAMANOTE_4_PIDS;
    public static final BlockEntityTypeRegistryObject<BlockYamanotePIDS.BlockYamanotePIDSEntity> YAMANOTE_5_PIDS;
    public static final BlockEntityTypeRegistryObject<BlockYamanotePIDS.BlockYamanotePIDSEntity> YAMANOTE_6_PIDS;
    public static final BlockEntityTypeRegistryObject<BlockYamanotePIDS.BlockYamanotePIDSEntity> YAMANOTE_7_PIDS;
    public static final BlockEntityTypeRegistryObject<BlockStandingSign.BlockStandingSignEntity> STANDING_SIGN;

    public static void init() {
        Init.MSD_LOGGER.info("Registering MTR Station Decoration block entity types");
    }
}