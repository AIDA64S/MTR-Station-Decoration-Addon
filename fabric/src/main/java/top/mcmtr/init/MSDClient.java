package top.mcmtr.init;

import net.fabricmc.api.ClientModInitializer;

import static top.mcmtr.mod.Init.MSD_LOGGER;

public class MSDClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MSD_LOGGER.info("MSD-Fabric-Client启动测试");
    }
}