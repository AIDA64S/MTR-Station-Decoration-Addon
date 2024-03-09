package top.mcmtr.mod;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mtr.core.data.Position;
import org.mtr.core.servlet.Webserver;
import org.mtr.core.tool.RequestHelper;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.MinecraftServerHelper;
import org.mtr.mapping.registry.EventRegistry;
import org.mtr.mapping.registry.Registry;
import top.mcmtr.core.MSDMain;
import top.mcmtr.mod.packet.*;

import javax.annotation.Nullable;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;

public class Init implements Utilities {
    private static MSDMain main;
    private static Webserver webserver;
    private static int serverPort;
    public static final String MOD_ID = "msd";
    public static final Logger MSD_LOGGER = LogManager.getLogger("MTR-Station-Decoration");
    public static final Registry REGISTRY = new Registry();
    private static final ObjectArrayList<String> WORLD_ID_LIST = new ObjectArrayList<>();
    private static final RequestHelper REQUEST_HELPER = new RequestHelper(false);

    public static void init() {
        Blocks.init();
        Items.init();
        BlockEntityTypes.init();
        CreativeModeTabs.init();

        REGISTRY.setupPackets(new Identifier(MOD_ID, "packet"));
        REGISTRY.registerPacket(MSDPacketDeleteData.class, MSDPacketDeleteData::new);
        REGISTRY.registerPacket(MSDPacketRequestData.class, MSDPacketRequestData::new);
        REGISTRY.registerPacket(MSDPacketUpdateData.class, MSDPacketUpdateData::new);
        REGISTRY.registerPacket(MSDPacketResetData.class, MSDPacketResetData::new);
        REGISTRY.registerPacket(MSDPacketOpenCatenaryScreen.class, MSDPacketOpenCatenaryScreen::new);
        REGISTRY.registerPacket(MSDPacketUpdateCatenaryNode.class, MSDPacketUpdateCatenaryNode::new);
        REGISTRY.registerPacket(MSDPacketUpdateYamanoteRailwaySignConfig.class, MSDPacketUpdateYamanoteRailwaySignConfig::new);
        REGISTRY.registerPacket(MSDPacketOpenBlockEntityScreen.class, MSDPacketOpenBlockEntityScreen::new);
        REGISTRY.registerPacket(MSDPacketOpenCustomTextConfig.class, MSDPacketOpenCustomTextConfig::new);
        REGISTRY.registerPacket(MSDPacketUpdateCustomText.class, MSDPacketUpdateCustomText::new);

        EventRegistry.registerServerStarted(minecraftServer -> {
            WORLD_ID_LIST.clear();
            MinecraftServerHelper.iterateWorlds(minecraftServer, serverWorld -> {
                WORLD_ID_LIST.add(getWorldId(new World(serverWorld.data)));
            });

            final int defaultPort = getDefaultPortFromConfig(minecraftServer);
            serverPort = findFreePort(defaultPort);
            final int port = findFreePort(serverPort + 1);
            main = new MSDMain(minecraftServer.getSavePath(WorldSavePath.getRootMapped()).resolve("msd"), serverPort, WORLD_ID_LIST.toArray(new String[0]));
            webserver = new Webserver(port);
            webserver.start();
        });

        EventRegistry.registerPlayerDisconnect((minecraftServer, serverPlayerEntity) -> {
            if (main != null) {
                main.save();
            }
        });

        EventRegistry.registerServerStopping(minecraftServer -> {
            if (main != null) {
                main.stop();
            }
            if (webserver != null) {
                webserver.stop();
            }
        });

        REGISTRY.init();
    }

    public static void sendHttpRequest(String endpoint, @Nullable World world, String content, @Nullable Consumer<String> consumer) {
        REQUEST_HELPER.sendPostRequest(String.format(
                "http://localhost:%s/msd/api/%s?%s",
                serverPort,
                endpoint,
                world == null ? "dimensions=all" : "dimension=" + WORLD_ID_LIST.indexOf(getWorldId(world))
        ), content, consumer);
    }

    public static BlockPos positionToBlockPos(Position position) {
        return new BlockPos((int) position.getX(), (int) position.getY(), (int) position.getZ());
    }

    public static Position blockPosToPosition(BlockPos blockPos) {
        return new Position(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static BlockPos newBlockPos(double x, double y, double z) {
        return new BlockPos(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
    }

    public static boolean isChunkLoaded(World world, ChunkManager chunkManager, BlockPos blockPos) {
        return chunkManager.getWorldChunk(blockPos.getX() / 16, blockPos.getZ() / 16) != null && world.isRegionLoaded(blockPos, blockPos);
    }

    private static int getDefaultPortFromConfig(MinecraftServer minecraftServer) {
        final Path filePath = minecraftServer.getRunDirectory().toPath().resolve("config/msd_webserver_port.txt");
        final int defaultPort = 8989;

        try {
            return Integer.parseInt(FileUtils.readFileToString(filePath.toFile(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            try {
                Files.write(filePath, String.valueOf(defaultPort).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (Exception notfound) {
                logException(notfound, "MSD get default server port error");
            }
        }

        return defaultPort;
    }


    private static int findFreePort(int startingPort) {
        for (int i = Math.max(1025, startingPort); i <= 65535; i++) {
            try (final ServerSocket serverSocket = new ServerSocket(i)) {
                final int port = serverSocket.getLocalPort();
                MSD_LOGGER.info("MSD Found available server port: " + port);
                return port;
            } catch (Exception ignored) {
                MSD_LOGGER.info("port: " + i + "is used, ignore.");
            }
        }
        return 0;
    }

    private static String getWorldId(World world) {
        final Identifier identifier = MinecraftServerHelper.getWorldId(world);
        return String.format("%s/%s", identifier.getNamespace(), identifier.getPath());
    }

    public static void logException(Exception e, String message) {
        MSD_LOGGER.error(message, e);
    }
}