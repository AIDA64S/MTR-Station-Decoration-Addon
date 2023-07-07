package top.mcmtr.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class Config {
    private static final Logger LOGGER = LogManager.getLogger();
    private static int rigidCatenarySegmentLength = 1;
    private static int yuuniPIDSMaxViewDistance = 128;
    private static int yamanoteRailwaySignMaxViewDistance = 128;
    private static int customTextSignMaxViewDistance = 128;
    private static final String SEGMENT_LENGTH = "rigid_catenary_segment_length";
    private static final String PIDS_MAX_VIEW_DISTANCE = "yuuni_pids_max_view_distance";
    private static final String RAILWAY_SIGN_MAX_VIEW_DISTANCE = "railway_sign_max_view_distance";
    private static final String TEXT_SIGN_MAX_VIEW_DISTANCE = "custom_text_sign_max_view_distance";
    private static final Path CONFIG_PATH = Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("msd.json");
    public static final int MAX_VIEW_DISTANCE = 256;

    public static void setRigidCatenarySegmentLength(int rigidCatenarySegmentLength) {
        Config.rigidCatenarySegmentLength = rigidCatenarySegmentLength;
        writeToFile();
    }

    public static void setYuuniPIDSMaxViewDistance(int yuuniPIDSMaxViewDistance) {
        Config.yuuniPIDSMaxViewDistance = yuuniPIDSMaxViewDistance;
        writeToFile();
    }

    public static void setYamanoteRailwaySignMaxViewDistance(int yamanoteRailwaySignMaxViewDistance) {
        Config.yamanoteRailwaySignMaxViewDistance = yamanoteRailwaySignMaxViewDistance;
        writeToFile();
    }

    public static void setCustomTextSignMaxViewDistance(int customTextSignMaxViewDistance) {
        Config.customTextSignMaxViewDistance = customTextSignMaxViewDistance;
        writeToFile();
    }

    public static int getRigidCatenarySegmentLength() {
        return rigidCatenarySegmentLength;
    }

    public static int getYuuniPIDSMaxViewDistance() {
        return yuuniPIDSMaxViewDistance;
    }

    public static int getYamanoteRailwaySignMaxViewDistance() {
        return yamanoteRailwaySignMaxViewDistance;
    }

    public static int getCustomTextSignMaxViewDistance() {
        return customTextSignMaxViewDistance;
    }

    public static void refreshProperties() {
        LOGGER.info("Refreshed MSD config");
        try {
            final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_PATH))).getAsJsonObject();
            try {
                rigidCatenarySegmentLength = jsonConfig.get(SEGMENT_LENGTH).getAsInt();
                if (rigidCatenarySegmentLength < 1 || rigidCatenarySegmentLength > 4) {
                    LOGGER.error("error! Rigid Catenary Segment Length must be >= 1 & <= 4");
                    rigidCatenarySegmentLength = 1;
                }
            } catch (Exception ignored) {
            }
            try {
                yuuniPIDSMaxViewDistance = jsonConfig.get(PIDS_MAX_VIEW_DISTANCE).getAsInt();
            } catch (Exception ignored) {
            }
            try {
                yamanoteRailwaySignMaxViewDistance = jsonConfig.get(RAILWAY_SIGN_MAX_VIEW_DISTANCE).getAsInt();
            } catch (Exception ignored) {
            }
            try {
                customTextSignMaxViewDistance = jsonConfig.get(TEXT_SIGN_MAX_VIEW_DISTANCE).getAsInt();
            } catch (Exception ignored) {
            }
        } catch (Exception e) {
            writeToFile();
            refreshProperties();
        }
    }

    private static void writeToFile() {
        LOGGER.info("Wrote MSD config to file");
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty(SEGMENT_LENGTH, rigidCatenarySegmentLength);
        jsonConfig.addProperty(PIDS_MAX_VIEW_DISTANCE, yuuniPIDSMaxViewDistance);
        jsonConfig.addProperty(RAILWAY_SIGN_MAX_VIEW_DISTANCE, yamanoteRailwaySignMaxViewDistance);
        jsonConfig.addProperty(TEXT_SIGN_MAX_VIEW_DISTANCE, customTextSignMaxViewDistance);
        try {
            if (!Files.exists(CONFIG_PATH.getParent())) {
                Files.createDirectories(CONFIG_PATH.getParent());
            }
            Files.write(CONFIG_PATH, Collections.singleton(prettyPrint(jsonConfig)));
        } catch (IOException e) {
            LOGGER.error("Configuration file write exception");
            e.printStackTrace();
        }
    }

    private static String prettyPrint(JsonElement jsonElement) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
    }
}