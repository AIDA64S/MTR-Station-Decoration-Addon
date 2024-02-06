package top.mcmtr.core;

import org.mtr.core.generated.WebserverResources;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.webserver.Webserver;
import top.mcmtr.core.servlet.IntegrationServlet;
import top.mcmtr.core.servlet.SocketHandler;
import top.mcmtr.core.simulation.Simulator;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static top.mcmtr.mod.Init.MSD_LOGGER;

@ParametersAreNonnullByDefault
public class Main {
    private final ObjectImmutableList<Simulator> simulators;
    private final Webserver webserver;
    private final ScheduledExecutorService scheduledExecutorService;
    public static final int MILLISECONDS_PER_TICK = 10;

    public Main(Path rootPath, int webServerPort, String... dimensions) {
        final ObjectArrayList<Simulator> tempSimulators = new ObjectArrayList<>();
        MSD_LOGGER.info("MSD loading files...");
        for (final String dimension : dimensions) {
            tempSimulators.add(new Simulator(dimension, rootPath));
        }
        simulators = new ObjectImmutableList<>(tempSimulators);
        webserver = new Webserver(Main.class, WebserverResources::get, Utilities.clamp(webServerPort, 1025, 65535), StandardCharsets.UTF_8, jsonObject -> 0);
        new IntegrationServlet(webserver, "/msd/api/data/*", simulators);
        SocketHandler.register(webserver, simulators);
        webserver.start();
        scheduledExecutorService = Executors.newScheduledThreadPool(simulators.size());
        simulators.forEach(simulator -> scheduledExecutorService.scheduleAtFixedRate(simulator::tick, 0, MILLISECONDS_PER_TICK, TimeUnit.MILLISECONDS));
        MSD_LOGGER.info("MSD Server started with dimensions " + Arrays.toString(dimensions));
    }

    public void save() {
        MSD_LOGGER.info("MSD Starting quick save...");
        simulators.forEach(Simulator::save);
    }

    public void stop() {
        MSD_LOGGER.info("MSD Server Stopping...");
        webserver.start();
        scheduledExecutorService.shutdown();
        Utilities.awaitTermination(scheduledExecutorService);
        MSD_LOGGER.info("MSD Server Starting full save...");
        simulators.forEach(Simulator::stop);
        MSD_LOGGER.info("MSD Server Stopped");
    }
}
