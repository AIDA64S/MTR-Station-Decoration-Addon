package org.mtr.mod;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jonafanho.apitools.ModId;
import com.jonafanho.apitools.ModLoader;
import com.jonafanho.apitools.ModProvider;
import org.apache.commons.io.IOUtils;
import org.gradle.api.Project;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuildTools {
    public final String minecraftVersion;
    public final String loader;
    public final int javaLanguageVersion;
    private final Path path;
    private final String version;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public BuildTools(String minecraftVersion, String loader, Project project) throws IOException {
        this.minecraftVersion = minecraftVersion;
        this.loader = loader;
        path = project.getProjectDir().toPath();
        version = project.getVersion().toString();
        int majorVersion = Integer.parseInt(minecraftVersion.split("\\.")[1]);
        javaLanguageVersion = majorVersion <= 16 ? 8 : majorVersion == 17 ? 16 : 17;

        final Path accessWidenerPath = path.resolve("src/main/resources").resolve(loader.equals("fabric") ? "" : "META-INF");
        Files.createDirectories(accessWidenerPath);
        create(minecraftVersion, loader, accessWidenerPath.resolve(loader.equals("fabric") ? "msd.accesswidener" : "accesstransformer.cfg"));

        final Path mixinPath = path.resolve("src/main/java/top/mcmtr/mixin");
        Files.createDirectories(mixinPath);
    }

    public String getFabricVersion() {
        String fabricVersion = getJson("https://meta.fabricmc.net/v2/versions/loader/" + minecraftVersion).getAsJsonArray().get(0).getAsJsonObject().getAsJsonObject("loader").get("version").getAsString();
        System.out.println("Fabric loader version: "+ fabricVersion);
        return fabricVersion;
    }

    public String getYarnVersion() {
        String yarnVersion =  getJson("https://meta.fabricmc.net/v2/versions/yarn/" + minecraftVersion).getAsJsonArray().get(0).getAsJsonObject().get("version").getAsString();
        System.out.println("Yarn version: " + yarnVersion);
        return yarnVersion;
    }

    public String getFabricApiVersion() {
        final String modIdString = "fabric-api";
        String fabricApiVersion = new ModId(modIdString, ModProvider.MODRINTH).getModFiles(minecraftVersion, ModLoader.FABRIC, "").get(0).fileName.split(".jar")[0].replace(modIdString + "-", "");
        System.out.println("Fabric API version: "+ fabricApiVersion);
        return fabricApiVersion;
    }

    public String getModMenuVersion() {
        final String modIdString = "modmenu";
        String modMenuVersion = new ModId(modIdString, ModProvider.MODRINTH).getModFiles(minecraftVersion, ModLoader.FABRIC, "").get(0).fileName.split(".jar")[0].replace(modIdString + "-", "");
        System.out.println("ModMenu version: " + modMenuVersion);
        return modMenuVersion;
    }

    public String getForgeVersion() {
        String forgeVersion = getJson("https://files.minecraftforge.net/net/minecraftforge/forge/promotions_slim.json").getAsJsonObject().getAsJsonObject("promos").get(minecraftVersion + "-latest").getAsString();
        System.out.println("Forge version: " + forgeVersion);
        return forgeVersion;
    }

    public void copyBuildFile() throws IOException {
        final Path directory = path.getParent().resolve("build/release");
        Files.createDirectories(directory);
        Files.copy(path.resolve(String.format("build/libs/%s-%s%s.jar", loader, version, loader.equals("fabric") ? "" : "-all")), directory.resolve(String.format("MSD-%s-%s-%s.jar", loader, minecraftVersion, version)), StandardCopyOption.REPLACE_EXISTING);
    }

    private static JsonElement getJson(String url) {
        for (int i = 0; i < 5; i++) {
            try {
                return JsonParser.parseString(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
            } catch (Exception e) {
                logException(e);
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                logException(e);
            }
        }

        return new JsonObject();
    }

    private static void logException(Exception e) {
        LOGGER.log(Level.INFO, e.getMessage(), e);
    }

    public static void create(String minecraftVersion, String loader, Path path) throws IOException {
        String content = null;
        switch (String.format("%s-%s", minecraftVersion, loader)) {
            case "1.16.5-fabric":
            case "1.17.1-fabric":
            case "1.18.2-fabric":
            case "1.19.4-fabric":
            case "1.20.4-fabric":
                content = "accessWidener v2 named";
                break;
            case "1.16.5-forge":
            case "1.17.1-forge":
            case "1.18.2-forge":
            case "1.19.4-forge":
            case "1.20.4-forge":
                content = "";
                break;
            default:
                break;
        }
        if (content != null) {
            Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
    }
}