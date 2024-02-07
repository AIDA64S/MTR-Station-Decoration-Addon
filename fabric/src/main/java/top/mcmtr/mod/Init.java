package top.mcmtr.mod;

import org.apache.commons.io.FileUtils;
import org.mtr.core.data.Client;
import org.mtr.core.data.Position;
import org.mtr.core.generated.data.ClientGroupSchema;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.JsonWriter;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.io.socket.client.IO;
import org.mtr.libraries.io.socket.client.Socket;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.MinecraftServerHelper;
import org.mtr.mapping.mapper.WorldHelper;
import org.mtr.mapping.registry.EventRegistry;
import org.mtr.mapping.registry.Registry;
import top.mcmtr.core.Main;
import top.mcmtr.core.data.Data;
import top.mcmtr.core.integration.Integration;
import top.mcmtr.core.servlet.IntegrationServlet;
import top.mcmtr.mod.packet.PacketData;

import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static top.mcmtr.core.servlet.ServletBase.PARAMETER_DIMENSION;

public class Init implements Utilities {
    private static Main main;
    private static Socket socket;
    private static int port;
    private static Runnable sendWorldTimeUpdate;
    private static int serverTick;
    public static final String MOD_ID = "msd";
    public static final Logger MSD_LOGGER = Logger.getLogger("Station-Decoration");
    public static final Registry REGISTRY = new Registry();
    public static final int SECONDS_PER_MC_HOUR = 50;
    private static final String CHANNEL = "update";
    private static final int MILLIS_PER_MC_DAY = SECONDS_PER_MC_HOUR * MILLIS_PER_SECOND * HOURS_PER_DAY;

    private static final Object2ObjectArrayMap<Identifier, IntObjectImmutablePair<ObjectArraySet<ObjectBooleanImmutablePair<ServerPlayerEntity>>>> PLAYERS_TO_UPDATE = new Object2ObjectArrayMap<>();

    public static void init() {
        REGISTRY.setupPackets(new Identifier(MOD_ID, "packet"));
        EventRegistry.registerServerStarted(minecraftServer -> {
            final ObjectArrayList<String> worldNames = new ObjectArrayList<>();
            PLAYERS_TO_UPDATE.clear();
            final int[] index = {0};
            MinecraftServerHelper.iterateWorlds(minecraftServer, serverWorld -> {
                final Identifier identifier = MinecraftServerHelper.getWorldId(new World(serverWorld.data));
                worldNames.add(String.format("%s/%s", identifier.getNamespace(), identifier.getPath()));
                PLAYERS_TO_UPDATE.put(identifier, new IntObjectImmutablePair<>(index[0], new ObjectArraySet<>()));
                index[0]++;
            });
            setFreePort(minecraftServer);
            main = new Main(minecraftServer.getSavePath(WorldSavePath.getRootMapped()).resolve(MOD_ID), port, worldNames.toArray(new String[0]));
            try {
                socket = IO.socket("http://localhost:" + port).connect();
                socket.on(CHANNEL, args -> {
                    final JsonObject responseObject = Utilities.parseJson(args[0].toString());
                    responseObject.keySet().forEach(playerUuid -> {
                        final ServerPlayerEntity serverPlayerEntity = minecraftServer.getPlayerManager().getPlayer(UUID.fromString(playerUuid));
                        if (serverPlayerEntity != null) {
                            REGISTRY.sendPacketToClient(serverPlayerEntity, new PacketData(IntegrationServlet.Operation.LIST, new Integration(new JsonReader(responseObject.getAsJsonObject(playerUuid)), new Data()), true));
                        }
                    });
                });
            } catch (Exception e) {
                MSD_LOGGER.log(Level.WARNING, "MSD setup the socket error", e);
            }
            serverTick = 0;
            sendWorldTimeUpdate = () -> {
                final JsonObject timeObject = new JsonObject();
                timeObject.addProperty("gameMillis", (WorldHelper.getTimeOfDay(minecraftServer.getOverworld()) + 6000) * SECONDS_PER_MC_HOUR);
                timeObject.addProperty("millisPerDay", MILLIS_PER_MC_DAY);
                PacketData.sendHttpRequest("operation/set-time", timeObject, responseObject -> {
                });
            };
        });

        EventRegistry.registerServerStopping(minecraftServer -> {
            if (main != null) {
                main.stop();
            }
        });

        EventRegistry.registerStartServerTick(() -> {
            if (socket != null) {
                PLAYERS_TO_UPDATE.forEach((identifier, worldDetails) -> {
                    final ObjectArraySet<ObjectBooleanImmutablePair<ServerPlayerEntity>> playerDetails = worldDetails.right();
                    if (!playerDetails.isEmpty()) {
                        final ClientGroupNew clientGroupNew = new ClientGroupNew(worldDetails.leftInt());
                        playerDetails.forEach(clientGroupNew::addClient);
                        playerDetails.clear();
                        final JsonObject jsonObject = new JsonObject();
                        clientGroupNew.serializeDataJson(new JsonWriter(jsonObject));
                        socket.emit(CHANNEL, jsonObject.toString());
                    }
                });
            }
            if (sendWorldTimeUpdate != null && serverTick % (SECONDS_PER_MC_HOUR * 10) == 0) {
                sendWorldTimeUpdate.run();
            }
            serverTick++;
        });
        REGISTRY.init();
    }

    public static void schedulePlayerUpdate(ServerPlayerEntity serverPlayerEntity, boolean forceUpdate) {
        final IntObjectImmutablePair<ObjectArraySet<ObjectBooleanImmutablePair<ServerPlayerEntity>>> worldDetails = PLAYERS_TO_UPDATE.get(MinecraftServerHelper.getWorldId(new World(serverPlayerEntity.getServerWorld().data)));
        if (worldDetails != null) {
            worldDetails.right().add(new ObjectBooleanImmutablePair<>(serverPlayerEntity, forceUpdate));
        }
    }

    public static int getPort() {
        return port;
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

    private static void setFreePort(MinecraftServer minecraftServer) {
        final Path filePath = minecraftServer.getRunDirectory().toPath().resolve("config/msd_webserver_port.txt");
        int startingPort = 8888;
        try {
            startingPort = Integer.parseInt(FileUtils.readFileToString(filePath.toFile(), StandardCharsets.UTF_8));
        } catch (Exception ignored) {
            try {
                Files.write(filePath, String.valueOf(startingPort).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (Exception e) {
                MSD_LOGGER.log(Level.WARNING, "MSD write port file error", e);
            }
        }
        for (int i = startingPort; i <= 65535; i++) {
            try (final ServerSocket serverSocket = new ServerSocket(i)) {
                port = serverSocket.getLocalPort();
                MSD_LOGGER.log(Level.INFO, "MSD Found available port: " + port);
                return;
            } catch (Exception e) {
                MSD_LOGGER.log(Level.WARNING, "port: " + i + " is used");
            }
        }
    }

    private static class ClientGroupNew extends ClientGroupSchema {
        private final int dimensionIndex;

        private ClientGroupNew(int dimensionIndex) {
            this.dimensionIndex = dimensionIndex;
            updateRadius = 16 * 16;
        }

        private void serializeDataJson(JsonWriter jsonWriter) {
            super.serializeData(jsonWriter);
            jsonWriter.writeInt(PARAMETER_DIMENSION, dimensionIndex);
        }

        private void addClient(ObjectBooleanImmutablePair<ServerPlayerEntity> player) {
            clients.add(new ClientNew(player.left(), player.rightBoolean()));
        }
    }

    private static class ClientNew extends Client {
        protected ClientNew(ServerPlayerEntity serverPlayerEntity, boolean forceUpdate) {
            super(serverPlayerEntity.getUuid());
            final BlockPos blockPos = serverPlayerEntity.getBlockPos();
            position = new Position(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            this.forceUpdate = forceUpdate;
        }
    }
}