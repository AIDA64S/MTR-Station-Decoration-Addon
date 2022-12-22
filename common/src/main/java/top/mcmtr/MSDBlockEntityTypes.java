package top.mcmtr;

import mtr.RegistryObject;
import mtr.mappings.RegistryUtilities;
import net.minecraft.world.level.block.entity.BlockEntityType;
import top.mcmtr.blocks.BlockYamanotePIDS;
import top.mcmtr.blocks.BlockYamanoteRailwaySign;

public interface MSDBlockEntityTypes {
    RegistryObject<BlockEntityType<BlockYamanotePIDS.TileEntityNorthernLinePIDS>> YAMANOTE_PIDS_TILE_ENTITY = new RegistryObject<>(()-> RegistryUtilities.getBlockEntityType(BlockYamanotePIDS.TileEntityNorthernLinePIDS::new, MSDBlocks.YAMANOTE_PIDS.get()));
    RegistryObject<BlockEntityType<BlockYamanoteRailwaySign.TileEntityRailwaySign>> YAMANOTE_RAILWAY_SIGN_2_EVEN_TILE_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType((pos, state) -> new BlockYamanoteRailwaySign.TileEntityRailwaySign(2, false, pos, state), MSDBlocks.YAMANOTE_RAILWAY_SIGN_2_EVEN.get()));
    RegistryObject<BlockEntityType<BlockYamanoteRailwaySign.TileEntityRailwaySign>> YAMANOTE_RAILWAY_SIGN_2_ODD_TILE_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType((pos, state) -> new BlockYamanoteRailwaySign.TileEntityRailwaySign(2, true, pos, state), MSDBlocks.YAMANOTE_RAILWAY_SIGN_2_ODD.get()));
    RegistryObject<BlockEntityType<BlockYamanoteRailwaySign.TileEntityRailwaySign>> YAMANOTE_RAILWAY_SIGN_3_EVEN_TILE_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType((pos, state) -> new BlockYamanoteRailwaySign.TileEntityRailwaySign(3, false, pos, state), MSDBlocks.YAMANOTE_RAILWAY_SIGN_3_EVEN.get()));
    RegistryObject<BlockEntityType<BlockYamanoteRailwaySign.TileEntityRailwaySign>> YAMANOTE_RAILWAY_SIGN_3_ODD_TILE_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType((pos, state) -> new BlockYamanoteRailwaySign.TileEntityRailwaySign(3, true, pos, state), MSDBlocks.YAMANOTE_RAILWAY_SIGN_3_ODD.get()));
    RegistryObject<BlockEntityType<BlockYamanoteRailwaySign.TileEntityRailwaySign>> YAMANOTE_RAILWAY_SIGN_4_EVEN_TILE_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType((pos, state) -> new BlockYamanoteRailwaySign.TileEntityRailwaySign(4, false, pos, state), MSDBlocks.YAMANOTE_RAILWAY_SIGN_4_EVEN.get()));
    RegistryObject<BlockEntityType<BlockYamanoteRailwaySign.TileEntityRailwaySign>> YAMANOTE_RAILWAY_SIGN_4_ODD_TILE_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType((pos, state) -> new BlockYamanoteRailwaySign.TileEntityRailwaySign(4, true, pos, state), MSDBlocks.YAMANOTE_RAILWAY_SIGN_4_ODD.get()));
    RegistryObject<BlockEntityType<BlockYamanoteRailwaySign.TileEntityRailwaySign>> YAMANOTE_RAILWAY_SIGN_5_EVEN_TILE_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType((pos, state) -> new BlockYamanoteRailwaySign.TileEntityRailwaySign(5, false, pos, state), MSDBlocks.YAMANOTE_RAILWAY_SIGN_5_EVEN.get()));
    RegistryObject<BlockEntityType<BlockYamanoteRailwaySign.TileEntityRailwaySign>> YAMANOTE_RAILWAY_SIGN_5_ODD_TILE_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType((pos, state) -> new BlockYamanoteRailwaySign.TileEntityRailwaySign(5, true, pos, state), MSDBlocks.YAMANOTE_RAILWAY_SIGN_5_ODD.get()));
    RegistryObject<BlockEntityType<BlockYamanoteRailwaySign.TileEntityRailwaySign>> YAMANOTE_RAILWAY_SIGN_6_EVEN_TILE_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType((pos, state) -> new BlockYamanoteRailwaySign.TileEntityRailwaySign(6, false, pos, state), MSDBlocks.YAMANOTE_RAILWAY_SIGN_6_EVEN.get()));
    RegistryObject<BlockEntityType<BlockYamanoteRailwaySign.TileEntityRailwaySign>> YAMANOTE_RAILWAY_SIGN_6_ODD_TILE_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType((pos, state) -> new BlockYamanoteRailwaySign.TileEntityRailwaySign(6, true, pos, state), MSDBlocks.YAMANOTE_RAILWAY_SIGN_6_ODD.get()));
    RegistryObject<BlockEntityType<BlockYamanoteRailwaySign.TileEntityRailwaySign>> YAMANOTE_RAILWAY_SIGN_7_EVEN_TILE_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType((pos, state) -> new BlockYamanoteRailwaySign.TileEntityRailwaySign(7, false, pos, state), MSDBlocks.YAMANOTE_RAILWAY_SIGN_7_EVEN.get()));
    RegistryObject<BlockEntityType<BlockYamanoteRailwaySign.TileEntityRailwaySign>> YAMANOTE_RAILWAY_SIGN_7_ODD_TILE_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType((pos, state) -> new BlockYamanoteRailwaySign.TileEntityRailwaySign(7, true, pos, state), MSDBlocks.YAMANOTE_RAILWAY_SIGN_7_ODD.get()));
}
