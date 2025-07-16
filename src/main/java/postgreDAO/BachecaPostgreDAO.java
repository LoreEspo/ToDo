package postgreDAO;

import dao.BachecaDAO;
import db.ConnessioneDatabase;
import model.Bacheca;
import model.Utente;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class BachecaPostgreDAO implements BachecaDAO {

    @Override
    public List<Bacheca> bachecheDisponibili(Utente utente) throws SQLException {
        List<Bacheca> output = new ArrayList<>();

        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();

        String query = "SELECT * FROM BACHECA WHERE autore = '" + utente.getUsername() + "'";

        ResultSet rs = conn.prepareStatement(query).executeQuery();

        while (rs.next()) {
            Bacheca bacheca = new Bacheca(
                    Bacheca.NomeBacheca.daString(rs.getString("titolo")),
                    rs.getString("descrizione"),
                    rs.getString("autore")
            );
            output.add(bacheca);
        }

        return output;
    }

    @Override
    public void setStatoBacheca(String autore, String titolo, boolean aperta) throws SQLException {
        ConnessioneDatabase conn = ConnessioneDatabase.getInstance();

        String query = "UPDATE BACHECA SET aperta = " + (aperta ? "true" : "false") + " WHERE autore = '" + autore +
                "' AND titolo = '" + titolo + "'";

        conn.prepareStatement(query).execute();
    }
}
