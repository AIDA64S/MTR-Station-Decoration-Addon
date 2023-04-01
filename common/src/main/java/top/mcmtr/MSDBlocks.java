package top.mcmtr;

import mtr.RegistryObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import top.mcmtr.blocks.*;

public interface MSDBlocks {
    RegistryObject<Block> RAILING_STAIR = new RegistryObject<>(() -> new BlockRailingStair(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> RAILING_STAIR_END = new RegistryObject<>(() -> new BlockRailingStair(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> RAILING_STAIR_END_MIRROR = new RegistryObject<>(() -> new BlockRailingStair(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> RAILING_STAIR_FLAT = new RegistryObject<>(() -> new BlockRailingStair(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> RAILING_STAIR_MIRROR = new RegistryObject<>(() -> new BlockRailingStair(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> RAILING_STAIR_START = new RegistryObject<>(() -> new BlockRailingStair(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> RAILING_STAIR_START_MIRROR = new RegistryObject<>(() -> new BlockRailingStair(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> YUUNI_PIDS = new RegistryObject<>(BlockYuuniPIDS::new);
    RegistryObject<Block> YUUNI_PIDS_POLE = new RegistryObject<>(() -> new BlockYuuniPIDSPole(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> YUUNI_2_PIDS = new RegistryObject<>(BlockYuuniPIDS_2::new);
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
    RegistryObject<Block> SURVEILLANCE_CAMERAS = new RegistryObject<>(() -> new BlockSurveillanceCameras(BlockSurveillanceCameras.CameraType.CEILING, BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F).lightLevel(state -> 1)));
    RegistryObject<Block> SURVEILLANCE_CAMERAS_WALL = new RegistryObject<>(() -> new BlockSurveillanceCameras(BlockSurveillanceCameras.CameraType.WALL, BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F).lightLevel(state -> 1)));
    RegistryObject<Block> HALL_SEAT_SIDE = new RegistryObject<>(() -> new BlockHallSeat(BlockHallSeat.SeatLocation.SIDE));
    RegistryObject<Block> HALL_SEAT_MIDDLE = new RegistryObject<>(() -> new BlockHallSeat(BlockHallSeat.SeatLocation.MIDDLE));
    RegistryObject<Block> HALL_SEAT_SIDE_MIRROR = new RegistryObject<>(() -> new BlockHallSeat(BlockHallSeat.SeatLocation.SIDE_MIRROR));
    RegistryObject<Block> CATENARY_POLE = new RegistryObject<>(() -> new BlockCatenaryRack(BlockCatenaryRack.CatenaryRackType.POLE, BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> CATENARY_POLE_TOP_MIDDLE = new RegistryObject<>(() -> new BlockCatenaryRack(BlockCatenaryRack.CatenaryRackType.GANTRY_MIDDLE, BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> CATENARY_POLE_TOP_SIDE = new RegistryObject<>(() -> new BlockCatenaryRack(BlockCatenaryRack.CatenaryRackType.GANTRY_SIDE, BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> CATENARY_RACK_POLE = new RegistryObject<>(() -> new BlockCatenaryRack(BlockCatenaryRack.CatenaryRackType.POLE, BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> CATENARY_RACK_POLE_BOTH_SIDE = new RegistryObject<>(() -> new BlockCatenaryRack(BlockCatenaryRack.CatenaryRackType.POLE, BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> CATENARY_RACK_SIDE = new RegistryObject<>(() -> new BlockCatenaryRack(BlockCatenaryRack.CatenaryRackType.UNDER_GANTRY, BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> CATENARY_RACK_BOTH_SIDE = new RegistryObject<>(() -> new BlockCatenaryRack(BlockCatenaryRack.CatenaryRackType.UNDER_GANTRY, BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> CATENARY_RACK_2 = new RegistryObject<>(() -> new BlockCatenaryRack(BlockCatenaryRack.CatenaryRackType.RACK, BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> CATENARY_RACK_1 = new RegistryObject<>(() -> new BlockCatenaryRack(BlockCatenaryRack.CatenaryRackType.RACK, BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> CATENARY_NODE = new RegistryObject<>(() -> new BlockCatenaryNode(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> CATENARY_NODE_STYLE_2 = new RegistryObject<>(() -> new BlockCatenaryNode(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> SHORT_CATENARY_RACK_POLE = new RegistryObject<>(() -> new BlockCatenaryRack(BlockCatenaryRack.CatenaryRackType.POLE, BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> SHORT_CATENARY_RACK_POLE_BOTH_SIDE = new RegistryObject<>(() -> new BlockCatenaryRack(BlockCatenaryRack.CatenaryRackType.POLE, BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> SHORT_CATENARY_RACK_SIDE = new RegistryObject<>(() -> new BlockCatenaryRack(BlockCatenaryRack.CatenaryRackType.UNDER_GANTRY, BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> SHORT_CATENARY_RACK_BOTH_SIDE = new RegistryObject<>(() -> new BlockCatenaryRack(BlockCatenaryRack.CatenaryRackType.UNDER_GANTRY, BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> SHORT_CATENARY_RACK = new RegistryObject<>(() -> new BlockCatenaryRack(BlockCatenaryRack.CatenaryRackType.RACK, BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> SHORT_CATENARY_NODE = new RegistryObject<>(() -> new BlockCatenaryNode(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> SHORT_CATENARY_NODE_STYLE_2 = new RegistryObject<>(() -> new BlockCatenaryNode(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> ELECTRIC_POLE_TOP_SIDE = new RegistryObject<>(() -> new BlockElectricPoleTop(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> ELECTRIC_POLE_TOP_BOTH_SIDE = new RegistryObject<>(() -> new BlockElectricPoleTopBothSide(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> ELECTRIC_POLE_SIDE = new RegistryObject<>(() -> new BlockElectricPoleSide(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> ELECTRIC_POLE_ANOTHER_SIDE = new RegistryObject<>(() -> new BlockElectricPoleSide(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> ELECTRIC_NODE = new RegistryObject<>(() -> new BlockCatenaryNode(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> RIGID_CATENARY_NODE = new RegistryObject<>(() -> new BlockRigidCatenaryNode(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F)));
    RegistryObject<Block> LAPTOP = new RegistryObject<>(() -> new BlockLaptop(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F).lightLevel(state -> 2)));
    RegistryObject<Block> DECORATION_BOOK = new RegistryObject<>(BlockDecorationBook::new);
    RegistryObject<Block> DISPLAY_BOARD_HORIZONTAL = new RegistryObject<>(BlockDisplayBoardHorizontal::new);
    RegistryObject<Block> DISPLAY_BOARD_VERTICALLY = new RegistryObject<>(BlockDisplayBoardVertically::new);
    RegistryObject<Block> YUUNI_TICKET = new RegistryObject<>(() -> new BlockYuuniTicket(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F).lightLevel(state -> 5)));
    RegistryObject<Block> CEILING_DOUBLE = new RegistryObject<>(() -> new BlockCeilingDouble(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1.0F)));
    RegistryObject<Block> CEILING_DOUBLE_LIGHT = new RegistryObject<>(() -> new BlockCeilingDouble(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1.0F).lightLevel(state -> 15)));
    RegistryObject<Block> STANDING_SIGN = new RegistryObject<>(BlockStandingSign::new);
    RegistryObject<Block> STANDING_SIGN_POLE = new RegistryObject<>(BlockStandingSignPole::new);
    RegistryObject<Block> STANDING_SIGN_1 = new RegistryObject<>(BlockStandingSign1::new);
}