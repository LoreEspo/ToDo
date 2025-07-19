package logger;

import java.io.IOException;
import java.util.logging.*;


/**
 * Singleton del logger proprio dell'applicazione.
 */
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

    /**
     * Logga un semplice messaggio.
     *
     * @param info il messaggio
     */
    public void logInfo(String info) {
        logger.info(info);
    }

    /**
     * Logga una query SQL.
     *
     * @param query la query
     */
    public void logQuery(String query) {
        logger.log(Level.INFO, "SQL QUERY: {0}", query);
    }

    /**
     * Logga un errore e stampa lo stacktrace.
     *
     * @param error l'errore
     */
    public void logError(Exception error) {
        logger.log(Level.SEVERE, "ERROR: {0}", error.toString());
        error.printStackTrace();
    }

    /**
     * @return l'istanza
     */
    public static ToDoLogger getInstance() {
        if (instance == null) {
            instance = new ToDoLogger();
        }
        return instance;
    }
}
