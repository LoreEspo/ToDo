package logger;

import java.io.IOException;
import java.util.logging.*;


public class ToDoLogger {
    private static ToDoLogger instance = new ToDoLogger();

    private final Logger logger;

    private ToDoLogger() {
        logger = Logger.getLogger("ToDoLogger");
        try {
            FileHandler fileHandler = new FileHandler("todoapp.log", true);
            fileHandler.setFormatter(new SimpleFormatter());

            logger.setUseParentHandlers(false);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logInfo(String info) {
        logger.info(info);
    }

    public void logQuery(String query) {
        logger.log(Level.INFO, "SQL QUERY: {0}", query);
    }

    public void logError(Exception error) {
        logger.log(Level.SEVERE, "ERROR: {0}", error.toString());
        error.printStackTrace();
    }

    public static ToDoLogger getInstance() {
        if (instance == null) {
            instance = new ToDoLogger();
        }
        return instance;
    }
}
