package top.mcmtr.init;

import net.fabricmc.api.ModInitializer;

import static top.mcmtr.mod.Init.MSD_LOGGER;

public class MSD implements ModInitializer {
    @Override
    public void onInitialize() {
        MSD_LOGGER.info("MSD-Fabric-Server启动测试");
    }
}