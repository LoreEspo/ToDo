package postgreDAO;

import dao.UtenteDAO;
import db.ConnessioneDatabase;
import org.postgresql.util.PSQLException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtentePostgreDAO implements UtenteDAO {
	public boolean login(String username, String password) throws SQLException {
		ConnessioneDatabase conn = ConnessioneDatabase.getInstance();

		String query = String.format("SELECT * FROM UTENTE WHERE username='%s' AND password='%s';", username, password);

		PreparedStatement statement = conn.prepareStatement(query);
		ResultSet rs = statement.executeQuery();

		return rs.next();
	}

	public boolean registrazione(String username, String password) throws SQLException {
		ConnessioneDatabase conn = ConnessioneDatabase.getInstance();

		try {
			conn.prepareStatement(
					String.format("INSERT INTO UTENTE VALUES ('%s', '%s')", username, password)
			).execute();
		} catch (SQLException e) {
			System.out.println("Errore nella registrazione: " + e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
