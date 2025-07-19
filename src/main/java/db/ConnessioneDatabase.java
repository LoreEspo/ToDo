package db;

import logger.ToDoLogger;

import java.sql.*;


/**
 * Il singleton che effettua le query al database.
 */
public class ConnessioneDatabase {
	/**
	 * L'istanza del singleton.
	 */
	static ConnessioneDatabase instance;
	private Connection connection = null;

	/**
	 * L'url al database
	 */
	String url = "jdbc:postgresql://localhost:5432/ToDo?currentSchema=public";
	/**
	 * Il nome utente per l'accesso al database.
	 */
	String nome = "todo";
	/**
	 * La password per l'accesso al database.
	 */
	String password = "todopassword";

	private ConnessioneDatabase() throws SQLException {
		try {

			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(
					url, nome, password
			);
			ToDoLogger.getInstance().logInfo("Connessione avvenuta con successo");
		} catch (ClassNotFoundException ex) {
			ToDoLogger.getInstance().logInfo("\nErrore nella connessione al database:");
			ToDoLogger.getInstance().logError(ex);
		}
	}

	/**
	 * @return l'istanza
	 * @throws SQLException errore nella connessione al database
	 */
	public static ConnessioneDatabase getInstance() throws SQLException {
		if (instance == null || instance.connection.isClosed()) {
			instance = new ConnessioneDatabase();
		}
		return instance;
	}

	/**
	 * Chiude la connessione
	 *
	 * @throws SQLException errore nella chiusura
	 */
	public void close() throws SQLException {
		connection.close();
	}

	/**
	 * Prepara una query
	 *
	 * @param sql la query
	 * @return la query preparata
	 * @throws SQLException errore nella preparazione
	 */
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return connection.prepareStatement(sql);
	}

}
