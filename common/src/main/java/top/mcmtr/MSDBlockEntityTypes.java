package top.mcmtr;

import mtr.RegistryObject;
import mtr.mappings.RegistryUtilities;
import net.minecraft.world.level.block.entity.BlockEntityType;
import top.mcmtr.blocks.BlockYamanotePIDS;

public interface MSDBlockEntityTypes {
    RegistryObject<BlockEntityType<BlockYamanotePIDS.TileEntityNorthernLinePIDS>> YAMANOTE_PIDS_TILE_ENTITY = new RegistryObject<>(()-> RegistryUtilities.getBlockEntityType(BlockYamanotePIDS.TileEntityNorthernLinePIDS::new, MSDBlocks.YAMANOTE_PIDS.get()));
}
