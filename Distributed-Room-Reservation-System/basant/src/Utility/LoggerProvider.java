package Utility;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import Configuration.Constants;

public class LoggerProvider {
    /**Returns Logger object for a given location
     * @param location
     * @return
     * @throws IOException
     */
    public static Logger getLogger(String location, String replicaName) throws IOException {
        Logger logger = Logger.getLogger(location.toString() + replicaName);
        Path path = Paths.get(Constants.PROJECT_DIR, Constants.LOG_DIR_NAME, location.toString(), replicaName +
                "_logger.log");
        FileHandler fHandler = new FileHandler(path.toString(), true);
        fHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fHandler);
        logger.setLevel(Level.INFO);
        return logger;
    }
}