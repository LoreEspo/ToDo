package postgredao;

import dao.UtenteDAO;
import db.ConnessioneDatabase;
import logger.ToDoLogger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Implementazione per PostgreSQL di {@link UtenteDAO}.
 */
public class UtentePostgreDAO implements UtenteDAO {
	public boolean login(String username, String password) throws SQLException {
		ConnessioneDatabase conn = ConnessioneDatabase.getInstance();

		String query = String.format("SELECT 1 FROM UTENTE WHERE username='%s' AND password='%s';", username, password);

		PreparedStatement statement = conn.prepareStatement(query);
		ResultSet rs = statement.executeQuery();
		ToDoLogger.getInstance().logQuery(query);

		return rs.next();
	}

	public boolean registrazione(String username, String password) throws SQLException {
		ConnessioneDatabase conn = ConnessioneDatabase.getInstance();

		try {
			String query = String.format("INSERT INTO UTENTE VALUES ('%s', '%s')", username, password);
			conn.prepareStatement(
					query
			).execute();
			ToDoLogger.getInstance().logQuery(query);
		} catch (SQLException e) {
			ToDoLogger.getInstance().logError(e);
			return false;
		}

		return true;
	}

}
