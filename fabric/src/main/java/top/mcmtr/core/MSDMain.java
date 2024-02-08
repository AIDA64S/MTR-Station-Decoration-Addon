package top.mcmtr.core;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MSDMain {
    public static final Logger MSD_LOGGER = Logger.getLogger("msd_core_log");

    public static void logException(Exception e, String message) {
        MSD_LOGGER.log(Level.WARNING, message + "\n" + e.getMessage(), e);
    }
}