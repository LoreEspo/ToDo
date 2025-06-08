package dao;

import java.sql.SQLException;

public interface UtenteDAO {
	public boolean login(String username, String password) throws SQLException;
	public boolean registrazione(String username, String password) throws SQLException;
}
