package top.mcmtr.init;

import net.fabricmc.api.ClientModInitializer;
import top.mcmtr.mod.Init;
import top.mcmtr.mod.InitClient;


public class MSDClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Init.MSD_LOGGER.info("MSD-Fabric-Client启动测试");
        InitClient.init();
    }
}