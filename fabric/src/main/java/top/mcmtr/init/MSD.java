package top.mcmtr.init;

import net.fabricmc.api.ModInitializer;
import top.mcmtr.mod.Init;


public class MSD implements ModInitializer {
    @Override
    public void onInitialize() {
        Init.init();
    }
}