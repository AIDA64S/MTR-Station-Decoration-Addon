package top.mcmtr.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mtr.core.servlet.WebServlet;
import org.mtr.core.servlet.Webserver;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.org.eclipse.jetty.servlet.ServletHolder;
import top.mcmtr.core.servlet.MSDOperationServlet;
import top.mcmtr.core.simulation.MSDSimulator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MSDMain {
    private final ObjectImmutableList<MSDSimulator> simulators;
    private final Webserver webserver;
    private final ScheduledExecutorService scheduledExecutorService;
    public static final int MILLISECONDS_PER_TICK = 10;
    public static final Logger MSD_CORE_LOG = LogManager.getLogger("MSD_SERVER_LOGGER");

    public MSDMain(Path rootPath, int webserverPort, String... dimensions) {
        final ObjectArrayList<MSDSimulator> tempSimulators = new ObjectArrayList<>();

        MSD_CORE_LOG.info("MSD server Loading files...");
        for (final String dimension : dimensions) {
            tempSimulators.add(new MSDSimulator(dimension, rootPath));
        }

        simulators = new ObjectImmutableList<>(tempSimulators);
        webserver = new Webserver(webserverPort);
        webserver.addServlet(new ServletHolder(new WebServlet()), "/");
        webserver.addServlet(new ServletHolder(new MSDOperationServlet(simulators)), "/msd/api/operation/*");
        webserver.start();

        scheduledExecutorService = Executors.newScheduledThreadPool(simulators.size());
        simulators.forEach(simulator -> scheduledExecutorService.scheduleAtFixedRate(simulator::tick, 0, MILLISECONDS_PER_TICK, TimeUnit.MILLISECONDS));
        MSD_CORE_LOG.info("MSD server started with dimensions " + Arrays.toString(dimensions));
    }

    public void save() {
        MSD_CORE_LOG.info("MSD server Starting quick save...");
        simulators.forEach(MSDSimulator::save);
    }

    public void stop() {
        MSD_CORE_LOG.info("MSD server Stopping...");
        webserver.stop();
        scheduledExecutorService.shutdown();
        Utilities.awaitTermination(scheduledExecutorService);
        MSD_CORE_LOG.info("MSD server Starting full save...");
        simulators.forEach(MSDSimulator::stop);
        MSD_CORE_LOG.info("MSD server Stopped");
    }
}