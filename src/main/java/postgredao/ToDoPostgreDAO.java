package postgredao;

import dao.ToDoDAO;
import db.ConnessioneDatabase;
import logger.ToDoLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


/**
 * Implementazione per PostgreSQL di {@link ToDoDAO}.
 */
public class ToDoPostgreDAO implements ToDoDAO {

    @Override
    public int aggiungi(Map<String, Object> mappaTodo) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();

        String query = "SELECT MAX(idTodo) FROM TODO";
        ToDoLogger.getInstance().logQuery(query);
        ResultSet rs = conn.prepareStatement(query).executeQuery();

        int id;

        if (rs.next()) {
            id = rs.getInt(1) + 1;
        } else {
            id = 0;
        }

        query = String.format(
                "SELECT MAX(ordine) FROM TODO WHERE autore = '%s' AND titoloBacheca = '%s'",
                mappaTodo.get(AUTORE), mappaTodo.get(TITOLO_BACHECA));
        ToDoLogger.getInstance().logQuery(query);
        rs = conn.prepareStatement(query).executeQuery();

        int ordine;

        if (rs.next()) {
            ordine = rs.getInt(1) + 1;
        } else {
            ordine = 0;
        }

        query = "INSERT INTO TODO VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, '" + mappaTodo.get(TITOLO_BACHECA) + "')";
        // Titolo della bacheca aggiunto separatamente poiché PreparedStatement.setString() ignora il dominio
        // della colonna e la considera VARCHAR
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setInt(1, id);
        statement.setString(2, (String) mappaTodo.get(TITOLO));
        Date scadenza = (Date) mappaTodo.get(SCADENZA);
        if (scadenza != null) {
            statement.setDate(3, new java.sql.Date((scadenza).getTime()));
        } else {
            statement.setDate(3, null);
        }
        statement.setString(4, (String) mappaTodo.get(LINK_ATTIVITA));
        statement.setString(5, (String) mappaTodo.get(DESCRIZIONE));
        statement.setBytes(6, (byte[]) mappaTodo.get(IMMAGINE));
        statement.setString(7, (String) mappaTodo.get(COLORE_SFONDO));
        statement.setBoolean(8, (boolean) mappaTodo.get(COMPLETATO));
        statement.setInt(9, ordine);
        statement.setString(10, (String) mappaTodo.get(AUTORE));
        ToDoLogger.getInstance().logQuery(statement.toString());
        statement.execute();

        return id;
    }

    @Override
    public void rimuovi(int id) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();
        conn.prepareStatement("DELETE FROM TODO WHERE idTodo = " + id).execute();
    }

    @Override
    public Map<Integer, Map<String, Object>> todoBacheca(String autore, String titoloBacheca) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();
        Map<Integer, Map<String, Object>> out = new LinkedHashMap<>();

        String query = String.format("SELECT * FROM TODO WHERE autore = '%s' AND titoloBacheca = '%s' ORDER BY ordine", autore, titoloBacheca);
        ToDoLogger.getInstance().logQuery(query);
        ResultSet rs = conn.prepareStatement(query).executeQuery();

        while (rs.next()) {
            Map<String, Object> todo = creaTodoMap(autore, titoloBacheca, rs);
            out.put(rs.getInt("idTodo"), todo);
        }

        query = String.format(
                "SELECT * FROM TODO NATURAL JOIN CONDIVISI WHERE destinatario = '%s' AND " +
                        "titoloBacheca = '%s' ORDER BY ordine", autore, titoloBacheca);
        ToDoLogger.getInstance().logQuery(query);
        rs = conn.prepareStatement(query).executeQuery();

        while (rs.next()) {
            Map<String, Object> todo = creaTodoMap(autore, titoloBacheca, rs);
            todo.put(CONDIVISO, true);
            out.put(rs.getInt("idTodo"), todo);
        }

        return out;
    }

    private static Map<String, Object> creaTodoMap(String autore, String titoloBacheca, ResultSet rs) throws SQLException {
        Map<String, Object> todo = new HashMap<>();
        todo.put(
                TITOLO, rs.getString(TITOLO));
        todo.put(
                SCADENZA, rs.getDate(SCADENZA));
        todo.put(
                LINK_ATTIVITA, rs.getString(LINK_ATTIVITA));
        todo.put(
                DESCRIZIONE, rs.getString(DESCRIZIONE));
        todo.put(
                IMMAGINE, rs.getBytes(IMMAGINE));
        todo.put(
                COLORE_SFONDO, rs.getString(COLORE_SFONDO));
        todo.put(
                COMPLETATO, rs.getBoolean(COMPLETATO));
        todo.put(
                AUTORE, autore);
        todo.put(
                TITOLO_BACHECA, titoloBacheca);
        return todo;
    }

    @Override
    public void aggiornaTodo(int id, Map<String, Object> mappaTodo) throws SQLException {
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
        statement.setString(1,
                (String) mappaTodo.get(TITOLO));
        Date scadenza = (Date) mappaTodo.get(SCADENZA);
        if (scadenza != null) {
            statement.setDate(2, new java.sql.Date(
                    scadenza.getTime()));
        } else {
            statement.setDate(2, null);
        }
        statement.setString(3,
                (String) mappaTodo.get(LINK_ATTIVITA));
        statement.setString(4,
                (String) mappaTodo.get(DESCRIZIONE));
        statement.setBytes(5,
                (byte[]) mappaTodo.get(IMMAGINE));
        statement.setString(6,
                (String) mappaTodo.get(COLORE_SFONDO));
        statement.setBoolean(7,
                (boolean) mappaTodo.get(COMPLETATO));
        statement.setInt(8, id);
        ToDoLogger.getInstance().logQuery(statement.toString());
        statement.execute();
    }

    @Override
    public void aggiornaOrdine(Map<Integer, Integer> ordineAId) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();
        StringBuilder queryBuilder = new StringBuilder();

        for (Map.Entry<Integer, Integer> todo : ordineAId.entrySet()) {
            queryBuilder.append(
                    String.format("UPDATE TODO SET ordine = %d WHERE idTodo = %d;", todo.getKey(), todo.getValue())
            );
        }

        String query = queryBuilder.toString();
        ToDoLogger.getInstance().logQuery(query);
        conn.prepareStatement(query).execute();

    }

    @Override
    public void condividi(int id, String destinatario) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();
        PreparedStatement statement = conn.prepareStatement("INSERT INTO CONDIVISI VALUES (?, ?)");
        statement.setInt(1, id);
        statement.setString(2, destinatario);
        ToDoLogger.getInstance().logQuery(statement.toString());
        statement.execute();
    }

    @Override
    public void rimuoviCondivisione(int id, String destinatario) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();
        PreparedStatement statement = conn.prepareStatement("DELETE FROM CONDIVISI WHERE idTodo = ? AND destinatario = ?");
        statement.setInt(1, id);
        statement.setString(2, destinatario);
        ToDoLogger.getInstance().logQuery(statement.toString());
        statement.execute();
    }

    @Override
    public List<String> condivisiDi(int id) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();
        List<String> out = new ArrayList<>();

        PreparedStatement statement = conn.prepareStatement("SELECT destinatario FROM CONDIVISI WHERE idTodo = ?");
        statement.setInt(1, id);
        ToDoLogger.getInstance().logQuery(statement.toString());
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            out.add(rs.getString(1));
        }
        return out;
    }

    @Override
    public void sposta(int id, String nuovaBacheca) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();

        String query = String.format("UPDATE TODO SET titoloBacheca = '%s' WHERE idTodo = '%d'", nuovaBacheca, id);
        ToDoLogger.getInstance().logQuery(query);
        conn.prepareStatement(query).execute();
    }

    @Override
    public List<Map<String, String>> ricerca(String username, String contenuto, boolean inTitolo, boolean inDescrizione) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();

        StringBuilder queryBuilder = new StringBuilder(
                "SELECT * FROM (SELECT titolo, titoloBacheca, descrizione FROM todo WHERE autore = '" + username +
                        "' UNION SELECT titolo, titoloBacheca, descrizione FROM todo NATURAL JOIN condivisi " +
                        "WHERE destinatario = '" + username + "') WHERE ("
                );
        if (inTitolo) {
            queryBuilder.append("titolo LIKE '%").append(contenuto).append("%'");
        }
        if (inTitolo && inDescrizione) {
            queryBuilder.append(" OR ");
        }
        if (inDescrizione) {
            queryBuilder.append("descrizione LIKE '%").append(contenuto).append("%'");
        }
        queryBuilder.append(");");

        String query = queryBuilder.toString();
        ToDoLogger.getInstance().logQuery(query);
        ResultSet rs = conn.prepareStatement(query).executeQuery();

        List<Map<String, String>> out = new ArrayList<>();

        while (rs.next()) {
            Map<String, String> todo = new HashMap<>();
            todo.put("titolo", rs.getString("titolo"));
            todo.put("titoloBacheca", rs.getString("titoloBacheca"));
            out.add(todo);
        }
        return out;
    }
}
