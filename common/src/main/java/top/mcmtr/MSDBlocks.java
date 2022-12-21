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

}
