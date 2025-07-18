package dao;

import java.sql.SQLException;

public interface UtenteDAO {
	boolean login(String username, String password) throws SQLException;
	boolean registrazione(String username, String password) throws SQLException;
}
