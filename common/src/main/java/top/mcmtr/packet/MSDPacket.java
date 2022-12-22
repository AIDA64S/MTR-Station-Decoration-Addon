package top.mcmtr.packet;

import mtr.MTR;
import net.minecraft.resources.ResourceLocation;
import top.mcmtr.Main;

public interface MSDPacket {
    ResourceLocation PACKET_OPEN_YAMANOTE_RAILWAY_SIGN_SCREEN = new ResourceLocation(Main.MOD_ID, "packet_open_yamanote_railway_sign_screen");
    ResourceLocation PACKET_YAMANOTE_SIGN_TYPES = new ResourceLocation(MTR.MOD_ID, "packet_sign_types");
}
