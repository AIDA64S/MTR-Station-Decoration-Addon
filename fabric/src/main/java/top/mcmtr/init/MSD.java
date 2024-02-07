package top.mcmtr.init;

import net.fabricmc.api.ModInitializer;
import top.mcmtr.mod.Init;


public class MSD implements ModInitializer {
    @Override
    public void onInitialize() {
        Init.MSD_LOGGER.info("MSD-Fabric-Server启动测试");
        Init.init();
    }
}