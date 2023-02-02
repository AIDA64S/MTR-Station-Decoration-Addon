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
    private static final String SEGMENT_LENGTH = "rigid_catenary_segment_length";
    private static final Path CONFIG_PATH = Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("msd.json");

    public static int getRigidCatenarySegmentLength() {
        return rigidCatenarySegmentLength;
    }

    public static void refreshProperties() {
        LOGGER.info("Refreshed MSD config");
        try {
            final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_PATH))).getAsJsonObject();
            try {
                rigidCatenarySegmentLength = jsonConfig.get(SEGMENT_LENGTH).getAsInt();
                if(rigidCatenarySegmentLength < 1 || rigidCatenarySegmentLength > 4){
                    LOGGER.error("error! Rigid Catenary Segment Length must be >= 1 & <= 4");
                    rigidCatenarySegmentLength = 1;
                }
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