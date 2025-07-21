package postgredao;

import dao.AttivitaDAO;
import db.ConnessioneDatabase;
import gui.ToDo;
import logger.ToDoLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementazione per PostgreSQL di {@link AttivitaDAO}.
 */
public class AttivitaPostgreDAO implements AttivitaDAO {
    @Override
    public int aggiungi(int idTodo, Map<String, Object> attivita) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();

        String query = "SELECT MAX(idAttivita) FROM ATTIVITA";
        ToDoLogger.getInstance().logQuery(query);
        ResultSet rs = conn.prepareStatement(query).executeQuery();

        int id;

        if (rs.next()) {
            id = rs.getInt(1) + 1;
        } else {
            id = 0;
        }

        PreparedStatement statement = conn.prepareStatement("INSERT INTO ATTIVITA VALUES (?, ?, ?, ?)");
        statement.setInt(1, id);
        statement.setString(2, (String) attivita.get(TITOLO));
        statement.setBoolean(3, (boolean) attivita.get(COMPLETATO));
        statement.setInt(4, idTodo);
        ToDoLogger.getInstance().logQuery(statement.toString());
        statement.execute();

        return id;
    }

    public void rimuovi(int id) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();

        String query = "DELETE FROM ATTIVITA WHERE idAttivita = " + id;
        ToDoLogger.getInstance().logQuery(query);
        conn.prepareStatement(query).execute();
    }

    @Override
    public void aggiorna(int id, Map<String, Object> attivita) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();
        PreparedStatement statement = conn.prepareStatement("UPDATE ATTIVITA SET " +
                "titolo = ?, completato = ? WHERE idAttivita = ?");
        statement.setString(1, (String) attivita.get(TITOLO));
        statement.setBoolean(2, (boolean) attivita.get(COMPLETATO));
        statement.setInt(3, id);
        ToDoLogger.getInstance().logQuery(statement.toString());
        statement.execute();
    }

    @Override
    public Map<Integer, Map<String, Object>> listaTodo(int idTodo) throws SQLException {
        Map<Integer, Map<String, Object>> out = new HashMap<>();
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();

        PreparedStatement statement = conn.prepareStatement(
                "SELECT idAttivita, titolo, completato FROM ATTIVITA WHERE idTodo = ?"
        );
        statement.setInt(1, idTodo);
        ToDoLogger.getInstance().logQuery(statement.toString());
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            Map<String, Object> attivita = new HashMap<>();
            attivita.put(TITOLO, rs.getString(TITOLO));
            attivita.put(COMPLETATO, rs.getBoolean(COMPLETATO));
            out.put(rs.getInt("idAttivita"), attivita);
        }
        return out;
    }
}
