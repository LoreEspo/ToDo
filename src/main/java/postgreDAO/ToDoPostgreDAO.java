package postgreDAO;

import dao.ToDoDAO;
import db.ConnessioneDatabase;
import model.Utente;
import model.Bacheca;
import model.ToDo;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ToDoPostgreDAO implements ToDoDAO {

    @Override
    public Integer aggiungi(ToDo todo) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();

        String query = "SELECT MAX(idTodo) FROM TODO";
        ResultSet rs = conn.prepareStatement(query).executeQuery();

        int id;

        if (rs.next()) {
            id = rs.getInt(1) + 1;
        } else {
            id = 0;
        }

        query = "INSERT INTO TODO VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, '" + todo.getBacheca().getTitolo().valore + "')";
        // Titolo della bacheca aggiunto separatamente poiché PreparedStatement.setString() ignora il dominio
        // della colonna e la considera VARCHAR
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setInt(1, id);
        statement.setString(2, todo.getTitolo());
        statement.setDate(3, (Date) todo.getScadenza());
        statement.setString(4, todo.getLinkAttivita());
        statement.setString(5, todo.getDescrizione());
        statement.setBytes(6, todo.getImmagine());
        statement.setString(7, todo.getColoreSfondo());
        statement.setBoolean(8, todo.getCompletato());
        statement.setString(9, todo.getUtente().getUsername());

        statement.execute();

        return id;
    }

    @Override
    public void rimuovi(Integer id) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();
        conn.prepareStatement("DELETE FROM TODO WHERE idTodo = " + id).execute();
    }

    @Override
    public Map<Integer, ToDo> todoBacheca(Utente utente, Bacheca bacheca) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();
        Map<Integer, ToDo> out = new HashMap<>();

        String query = String.format("SELECT * FROM TODO WHERE autore = '%s' AND titoloBacheca = '%s'", utente.getUsername(), bacheca.getTitolo().valore);
        ResultSet rs = conn.prepareStatement(query).executeQuery();

        while (rs.next()) {
            ToDo todo = new ToDo(utente, bacheca);
            todo.setTitolo(rs.getString("titolo"));
            todo.setScadenza(rs.getDate("scadenza"));
            todo.setLinkAttivita(rs.getString("linkAttivita"));
            todo.setDescrizione(rs.getString("descrizione"));
            todo.setImmagine(rs.getBytes("immagine"));
            todo.setColoreSfondo(rs.getString("coloreSfondo"));
            todo.setCompletato(rs.getBoolean("completato"));
            out.put(rs.getInt("idTodo"), todo);
        }

        return out;
    }

    @Override
    public void aggiornaTodo(Integer indice, ToDo todo) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();

        String query = "UPDATE TODO SET " +
                "titolo = ?, " +
                "scadenza = ?, " +
                "linkAttivita = ?, " +
                "descrizione = ?, " +
                "immagine = ?, " +
                "coloreSfondo = ?, " +
                "completato = ? " +
                "WHERE idTodo = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, todo.getTitolo());
        statement.setDate(2, new Date(todo.getScadenza().getTime()));
        statement.setString(3, todo.getLinkAttivita());
        statement.setString(4, todo.getDescrizione());
        statement.setBytes(5, todo.getImmagine());
        statement.setString(6, todo.getColoreSfondo());
        statement.setBoolean(7, todo.getCompletato());
        statement.setInt(8, indice);
        statement.execute();
    }
}
