package dao;

import java.sql.SQLException;

/**
 * Interfaccia che gestisce le query al database
 * inerenti agli utenti.
 * Fa uso delle mappe generate dall'interfaccia {@link model.Mappabile}
 */
public interface UtenteDAO {
	/**
	 * Effettua un accesso.
	 *
	 * @param username l'username
	 * @param password la password
	 * @return se l'accesso ha avuto successo
	 * @throws SQLException errore durante l'accesso. Probabilmente i dati non rispettano
	 * i domini dei campi sul database
	 */
	boolean login(String username, String password) throws SQLException;

	/**
	 * Effettua una registrazione.
	 *
	 * @param username l'username
	 * @param password la password
	 * @return se la registrazione ha avuto successo
	 * @throws SQLException errore durante l'accesso. Probabilmente i dati non rispettano
	 * i domini dei campi sul database
	 */
	boolean registrazione(String username, String password) throws SQLException;
}
