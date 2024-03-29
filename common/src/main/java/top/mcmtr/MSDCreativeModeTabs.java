package top.mcmtr;

import mtr.CreativeModeTabs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static top.mcmtr.MSDMain.MOD_ID;

public interface MSDCreativeModeTabs {
    CreativeModeTabs.Wrapper MSD_BLOCKS = new CreativeModeTabs.Wrapper(new ResourceLocation(MOD_ID, "msd_blocks"), () -> new ItemStack(MSDBlocks.RAILING_STAIR_FLAT.get()));
    CreativeModeTabs.Wrapper MSD_Station_Decoration = new CreativeModeTabs.Wrapper(new ResourceLocation(MOD_ID, "msd_station_blocks"), () -> new ItemStack(MSDBlocks.DECORATION_STAIR.get()));
}