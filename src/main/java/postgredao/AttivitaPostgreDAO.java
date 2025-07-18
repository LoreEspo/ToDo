package postgredao;

import dao.AttivitaDAO;
import db.ConnessioneDatabase;
import logger.ToDoLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AttivitaPostgreDAO implements AttivitaDAO {
    @Override
    public int aggiungi(Integer indiceTodo, Map<String, Object> attivita) throws SQLException {
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
        statement.setInt(4, indiceTodo);
        ToDoLogger.getInstance().logQuery(statement.toString());
        statement.execute();

        return id;
    }

    public void rimuovi(int indice) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();

        String query = "DELETE FROM ATTIVITA WHERE idAttivita = " + indice;
        ToDoLogger.getInstance().logQuery(query);
        conn.prepareStatement(query).execute();
    }

    @Override
    public void aggiorna(int indice, Map<String, Object> attivita) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();
        PreparedStatement statement = conn.prepareStatement("UPDATE ATTIVITA SET " +
                "titolo = ?, completato = ? WHERE idTodo = ?");
        statement.setString(1, (String) attivita.get(TITOLO));
        statement.setBoolean(2, (boolean) attivita.get(COMPLETATO));
        statement.setInt(3, indice);
        statement.execute();
    }

    @Override
    public Map<Integer, Map<String, Object>> listaTodo(int indiceTodo) throws SQLException {
        Map<Integer, Map<String, Object>> out = new HashMap<>();
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();

        PreparedStatement statement = conn.prepareStatement(
                "SELECT idAttivita, titolo, completato FROM ATTIVITA WHERE idTodo = ?"
        );
        statement.setInt(1, indiceTodo);
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
