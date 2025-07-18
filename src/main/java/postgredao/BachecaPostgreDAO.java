package postgredao;

import dao.BachecaDAO;
import db.ConnessioneDatabase;
import logger.ToDoLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class BachecaPostgreDAO implements BachecaDAO {

    @Override
    public List<Map<String, Object>> bachecheDisponibili(String autore) throws SQLException {
        List<Map<String, Object>> output = new ArrayList<>();

        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();

        String query = "SELECT * FROM BACHECA WHERE autore = '" + autore + "' ORDER BY titolo";
        ToDoLogger.getInstance().logQuery(query);

        ResultSet rs = conn.prepareStatement(query).executeQuery();

        while (rs.next()) {
            Map<String, Object> bacheca = new HashMap<>();
            bacheca.put(TITOLO, rs.getString(TITOLO));
            bacheca.put(DESCRIZIONE, rs.getString(DESCRIZIONE));
            bacheca.put(AUTORE, rs.getString(AUTORE));
            output.add(bacheca);
        }

        return output;
    }

    @Override
    public void setStatoBacheca(String autore, String titolo, boolean aperta) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();

        String query = "UPDATE BACHECA SET aperta = " + (aperta ? "true" : "false") + " WHERE autore = '" + autore +
                "' AND titolo = '" + titolo + "'";
        ToDoLogger.getInstance().logQuery(query);

        conn.prepareStatement(query).execute();
    }

    @Override
    public void setDescrizione(String autore, String titolo, String descrizione) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();

        String query = "UPDATE BACHECA SET aperta = ? WHERE autore = '" + autore +
                "' AND titolo = '" + titolo + "'";
        ToDoLogger.getInstance().logQuery(query);

        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, descrizione);

        statement.execute();
    }
}
