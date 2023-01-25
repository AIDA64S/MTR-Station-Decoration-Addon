package top.mcmtr;

import net.fabricmc.api.ClientModInitializer;

public class MSDMainFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MSDMainClient.init();
        MSDMainClient.registerItemModelPredicates();
    }
}
