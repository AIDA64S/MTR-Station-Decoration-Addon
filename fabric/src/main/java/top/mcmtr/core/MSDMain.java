package top.mcmtr.core;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MSDMain {
    public static final Logger MSD_CORE_LOG = Logger.getLogger("MSD_SERVER_LOGGER");

    public static void logException(Exception e, String exceptionMessage) {
        MSD_CORE_LOG.log(Level.WARNING, exceptionMessage, e);
    }
}