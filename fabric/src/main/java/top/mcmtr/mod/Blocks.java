package top.mcmtr.mod;

import org.mtr.mapping.holder.Block;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.registry.BlockRegistryObject;
import top.mcmtr.mod.blocks.*;

public class Blocks {
    static {
        CATENARY_NODE = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "catenary_node"), () -> new Block(new BlockCatenaryNode()), CreativeModeTabs.EXTERNAL);
        RIGID_CATENARY_NODE = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "rigid_catenary_node"), () -> new Block(new BlockRigidCatenaryNode()), CreativeModeTabs.EXTERNAL);
        YAMANOTE_RAILWAY_SIGN_2_EVEN = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "yamanote_railway_sign_2_even"), () -> new Block(new BlockYamanoteRailwaySign(2, false)), CreativeModeTabs.STATION);
        YAMANOTE_RAILWAY_SIGN_2_ODD = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "yamanote_railway_sign_2_odd"), () -> new Block(new BlockYamanoteRailwaySign(2, true)), CreativeModeTabs.STATION);
        YAMANOTE_RAILWAY_SIGN_3_EVEN = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "yamanote_railway_sign_3_even"), () -> new Block(new BlockYamanoteRailwaySign(3, false)), CreativeModeTabs.STATION);
        YAMANOTE_RAILWAY_SIGN_3_ODD = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "yamanote_railway_sign_3_odd"), () -> new Block(new BlockYamanoteRailwaySign(3, true)), CreativeModeTabs.STATION);
        YAMANOTE_RAILWAY_SIGN_4_EVEN = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "yamanote_railway_sign_4_even"), () -> new Block(new BlockYamanoteRailwaySign(4, false)), CreativeModeTabs.STATION);
        YAMANOTE_RAILWAY_SIGN_4_ODD = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "yamanote_railway_sign_4_odd"), () -> new Block(new BlockYamanoteRailwaySign(4, true)), CreativeModeTabs.STATION);
        YAMANOTE_RAILWAY_SIGN_5_EVEN = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "yamanote_railway_sign_5_even"), () -> new Block(new BlockYamanoteRailwaySign(5, false)), CreativeModeTabs.STATION);
        YAMANOTE_RAILWAY_SIGN_5_ODD = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "yamanote_railway_sign_5_odd"), () -> new Block(new BlockYamanoteRailwaySign(5, true)), CreativeModeTabs.STATION);
        YAMANOTE_RAILWAY_SIGN_6_EVEN = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "yamanote_railway_sign_6_even"), () -> new Block(new BlockYamanoteRailwaySign(6, false)), CreativeModeTabs.STATION);
        YAMANOTE_RAILWAY_SIGN_6_ODD = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "yamanote_railway_sign_6_odd"), () -> new Block(new BlockYamanoteRailwaySign(6, true)), CreativeModeTabs.STATION);
        YAMANOTE_RAILWAY_SIGN_7_EVEN = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "yamanote_railway_sign_7_even"), () -> new Block(new BlockYamanoteRailwaySign(7, false)), CreativeModeTabs.STATION);
        YAMANOTE_RAILWAY_SIGN_7_ODD = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "yamanote_railway_sign_7_odd"), () -> new Block(new BlockYamanoteRailwaySign(7, true)), CreativeModeTabs.STATION);

        YUUNI_PIDS = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "yuuni_pids"), () -> new Block(new BlockYuuniPIDS(2)), CreativeModeTabs.STATION);
        YUUNI_2_PIDS = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "yuuni_2_pids"), () -> new Block(new BlockYuuniPIDS(1)), CreativeModeTabs.STATION);
        YAMANOTE_4_PIDS = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "yamanote_4_pids"), () -> new Block(new BlockYamanotePIDS(4)), CreativeModeTabs.STATION);
        YAMANOTE_5_PIDS = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "yamanote_5_pids"), () -> new Block(new BlockYamanotePIDS(5)), CreativeModeTabs.STATION);
        YAMANOTE_6_PIDS = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "yamanote_6_pids"), () -> new Block(new BlockYamanotePIDS(6)), CreativeModeTabs.STATION);
        YAMANOTE_7_PIDS = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "yamanote_7_pids"), () -> new Block(new BlockYamanotePIDS(7)), CreativeModeTabs.STATION);
        STANDING_SIGN = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "yuuni_standing_sign"), () -> new Block(new BlockStandingSign(3)), CreativeModeTabs.EXTERNAL);

        SURVEILLANCE_CAMERAS = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "surveillance_cameras"), () -> new Block(new BlockSurveillanceCameras()), CreativeModeTabs.STATION);
        ELECTRIC_NODE = Init.REGISTRY.registerBlock(new Identifier(Init.MOD_ID, "electric_node"), () -> new Block(new BlockCatenaryNode()));
        YAMANOTE_RAILWAY_SIGN_MIDDLE = Init.REGISTRY.registerBlock(new Identifier(Init.MOD_ID, "yamanote_railway_sign_middle"), () -> new Block(new BlockYamanoteRailwaySign(0, false)));
    }

    public static final BlockRegistryObject CATENARY_NODE;
    public static final BlockRegistryObject RIGID_CATENARY_NODE;
    public static final BlockRegistryObject ELECTRIC_NODE;
    public static final BlockRegistryObject YAMANOTE_RAILWAY_SIGN_2_EVEN;
    public static final BlockRegistryObject YAMANOTE_RAILWAY_SIGN_2_ODD;
    public static final BlockRegistryObject YAMANOTE_RAILWAY_SIGN_3_EVEN;
    public static final BlockRegistryObject YAMANOTE_RAILWAY_SIGN_3_ODD;
    public static final BlockRegistryObject YAMANOTE_RAILWAY_SIGN_4_EVEN;
    public static final BlockRegistryObject YAMANOTE_RAILWAY_SIGN_4_ODD;
    public static final BlockRegistryObject YAMANOTE_RAILWAY_SIGN_5_EVEN;
    public static final BlockRegistryObject YAMANOTE_RAILWAY_SIGN_5_ODD;
    public static final BlockRegistryObject YAMANOTE_RAILWAY_SIGN_6_EVEN;
    public static final BlockRegistryObject YAMANOTE_RAILWAY_SIGN_6_ODD;
    public static final BlockRegistryObject YAMANOTE_RAILWAY_SIGN_7_EVEN;
    public static final BlockRegistryObject YAMANOTE_RAILWAY_SIGN_7_ODD;
    public static final BlockRegistryObject YAMANOTE_RAILWAY_SIGN_MIDDLE;
    public static final BlockRegistryObject YUUNI_PIDS;
    public static final BlockRegistryObject YUUNI_2_PIDS;
    public static final BlockRegistryObject YAMANOTE_4_PIDS;
    public static final BlockRegistryObject YAMANOTE_5_PIDS;
    public static final BlockRegistryObject YAMANOTE_6_PIDS;
    public static final BlockRegistryObject YAMANOTE_7_PIDS;
    public static final BlockRegistryObject SURVEILLANCE_CAMERAS;
    public static final BlockRegistryObject STANDING_SIGN;

    public static void init() {
        Init.MSD_LOGGER.info("Registering MTR Station Decoration blocks");
    }
}