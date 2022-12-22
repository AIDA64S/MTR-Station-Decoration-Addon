package top.mcmtr;

import mtr.RegistryObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import top.mcmtr.blocks.*;

public interface MSDBlocks {
    RegistryObject<Block> RAILING_STAIR = new RegistryObject<>(() -> new BlockRailingStair(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> RAILING_STAIR_END = new RegistryObject<>(() -> new BlockRailingStairEnd(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> RAILING_STAIR_END_MIRROR = new RegistryObject<>(() -> new BlockRailingStairEndMirror(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> RAILING_STAIR_FLAT = new RegistryObject<>(() -> new BlockRailingStairFlat(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> RAILING_STAIR_MIRROR = new RegistryObject<>(() -> new BlockRailingStairMirror(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> RAILING_STAIR_START = new RegistryObject<>(() -> new BlockRailingStairStart(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> RAILING_STAIR_START_MIRROR = new RegistryObject<>(() -> new BlockRailingStairStartMirror(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> YAMANOTE_PIDS =new RegistryObject<>(BlockYamanotePIDS::new);
    RegistryObject<Block> YAMANOTE_RAILWAY_SIGN_2_EVEN = new RegistryObject<>(() -> new BlockYamanoteRailwaySign(2, false));
    RegistryObject<Block> YAMANOTE_RAILWAY_SIGN_2_ODD = new RegistryObject<>(() -> new BlockYamanoteRailwaySign(2, true));
    RegistryObject<Block> YAMANOTE_RAILWAY_SIGN_3_EVEN = new RegistryObject<>(() -> new BlockYamanoteRailwaySign(3, false));
    RegistryObject<Block> YAMANOTE_RAILWAY_SIGN_3_ODD = new RegistryObject<>(() -> new BlockYamanoteRailwaySign(3, true));
    RegistryObject<Block> YAMANOTE_RAILWAY_SIGN_4_EVEN = new RegistryObject<>(() -> new BlockYamanoteRailwaySign(4, false));
    RegistryObject<Block> YAMANOTE_RAILWAY_SIGN_4_ODD = new RegistryObject<>(() -> new BlockYamanoteRailwaySign(4, true));
    RegistryObject<Block> YAMANOTE_RAILWAY_SIGN_5_EVEN = new RegistryObject<>(() -> new BlockYamanoteRailwaySign(5, false));
    RegistryObject<Block> YAMANOTE_RAILWAY_SIGN_5_ODD = new RegistryObject<>(() -> new BlockYamanoteRailwaySign(5, true));
    RegistryObject<Block> YAMANOTE_RAILWAY_SIGN_6_EVEN = new RegistryObject<>(() -> new BlockYamanoteRailwaySign(6, false));
    RegistryObject<Block> YAMANOTE_RAILWAY_SIGN_6_ODD = new RegistryObject<>(() -> new BlockYamanoteRailwaySign(6, true));
    RegistryObject<Block> YAMANOTE_RAILWAY_SIGN_7_EVEN = new RegistryObject<>(() -> new BlockYamanoteRailwaySign(7, false));
    RegistryObject<Block> YAMANOTE_RAILWAY_SIGN_7_ODD = new RegistryObject<>(() -> new BlockYamanoteRailwaySign(7, true));
    RegistryObject<Block> YAMANOTE_RAILWAY_SIGN_MIDDLE = new RegistryObject<>(() -> new BlockYamanoteRailwaySign(0, false));
}
