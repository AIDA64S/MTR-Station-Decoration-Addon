package top.mcmtr.packet;

import net.minecraft.resources.ResourceLocation;
import top.mcmtr.MSDMain;

public interface MSDPacket {
    ResourceLocation PACKET_OPEN_YAMANOTE_RAILWAY_SIGN_SCREEN = new ResourceLocation(MSDMain.MOD_ID, "packet_open_yamanote_railway_sign_screen");
    ResourceLocation PACKET_YAMANOTE_SIGN_TYPES = new ResourceLocation(MSDMain.MOD_ID, "packet_yamanote_sign_types");
    ResourceLocation PACKET_REMOVE_CATENARY_NODE = new ResourceLocation(MSDMain.MOD_ID, "packet_remove_catenary_node");
    ResourceLocation PACKET_REMOVE_CATENARY = new ResourceLocation(MSDMain.MOD_ID, "packet_remove_catenary");
    ResourceLocation PACKET_CREATE_CATENARY = new ResourceLocation(MSDMain.MOD_ID, "packet_create_catenary");
    ResourceLocation PACKET_WRITE_CATENARY = new ResourceLocation(MSDMain.MOD_ID, "write_catenary");
    ResourceLocation PACKET_REMOVE_RIGID_CATENARY_NODE = new ResourceLocation(MSDMain.MOD_ID, "packet_remove_rigid_catenary_node");
    ResourceLocation PACKET_REMOVE_RIGID_CATENARY = new ResourceLocation(MSDMain.MOD_ID, "packet_remove_rigid_catenary");
    ResourceLocation PACKET_CREATE_RIGID_CATENARY = new ResourceLocation(MSDMain.MOD_ID, "packet_create_rigid_catenary");
    ResourceLocation PACKET_WRITE_RIGID_CATENARY = new ResourceLocation(MSDMain.MOD_ID, "packet_write_rigid_catenary");
}