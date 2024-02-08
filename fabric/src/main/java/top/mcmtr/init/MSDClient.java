package top.mcmtr.init;

import net.fabricmc.api.ClientModInitializer;
import top.mcmtr.mod.InitClient;


public class MSDClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        InitClient.init();
    }
}