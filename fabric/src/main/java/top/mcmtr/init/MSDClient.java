package top.mcmtr.init;

import net.fabricmc.api.ClientModInitializer;

public class MSDClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("MSD-Fabric-Client启动测试");
    }
}