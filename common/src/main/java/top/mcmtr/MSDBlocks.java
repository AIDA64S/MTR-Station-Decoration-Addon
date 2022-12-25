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
    RegistryObject<Block> YAMANOTE_4_PIDS = new RegistryObject<>(BlockYamanote4PIDS::new);
    RegistryObject<Block> YAMANOTE_5_PIDS = new RegistryObject<>(BlockYamanote5PIDS::new);
    RegistryObject<Block> YAMANOTE_6_PIDS = new RegistryObject<>(BlockYamanote6PIDS::new);
    RegistryObject<Block> YAMANOTE_7_PIDS = new RegistryObject<>(BlockYamanote7PIDS::new);
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
    RegistryObject<Block> YAMANOTE_RAILWAY_SIGN_POLE = new RegistryObject<>(() -> new BlockYamanoteRailwaySignPole(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> YAMANOTE_RAILWAY_SIGN_DOUBLE_POLE = new RegistryObject<>(() -> new BlockYamanoteRailwaySignDoublePole(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> RAILWAY_SIGN_DOUBLE_POLE = new RegistryObject<>(() -> new BlockRailwaySignDoublePole(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1.0F)));
    RegistryObject<Block> ELECTRIC_POLE = new RegistryObject<>(() -> new BlockElectricPole(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> ELEVATOR_SHAFT_CORNER = new RegistryObject<>(() -> new BlockElevatorShaftCorner(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> ELEVATOR_SHAFT_PANEL = new RegistryObject<>(() -> new BlockElevatorShaftSide(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> ELEVATOR_SHAFT_SIDE = new RegistryObject<>(() -> new BlockElevatorShaftSide(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> ELEVATOR_SHAFT_TRANSPARENT = new RegistryObject<>(() -> new BlockElevatorShaftSide(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> SURVEILLANCE_CAMERAS = new RegistryObject<>(() -> new BlockSurveillanceCameras(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> SURVEILLANCE_CAMERAS_WALL = new RegistryObject<>(() -> new BlockSurveillanceCamerasWall(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
}
