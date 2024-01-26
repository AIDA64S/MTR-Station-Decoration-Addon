package top.mcmtr.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import top.mcmtr.mod.Init;
import top.mcmtr.mod.InitClient;

@Mod(Init.MOD_ID)
public class MSD {
    public MSD() {
        Init.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> InitClient::init);
        System.out.println("MSD-Forge启动测试");
    }
}