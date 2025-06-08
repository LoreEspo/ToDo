package db;

import java.sql.*;


public class ConnessioneDatabase {
	static ConnessioneDatabase instance;
	public Connection connection = null;

	String url = "jdbc:postgresql://localhost:5432/ToDo?currentSchema=public";
	String nome = "todo";
	String password = "todopassword";

	private ConnessioneDatabase() throws SQLException {
		try {

			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(
					url, nome, password
			);
			System.out.println("Connessione avvenuta con successo");
		} catch (ClassNotFoundException ex) {
			System.out.println("\nDatabase Connection Creation Failed : " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	static public ConnessioneDatabase getInstance() throws SQLException {
		if (instance == null || instance.connection.isClosed()) {
			instance = new ConnessioneDatabase();
		}
		return instance;
	}

	public void close() throws SQLException {
		connection.close();
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return connection.prepareStatement(sql);
	}

}
