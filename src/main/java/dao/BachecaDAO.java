package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface BachecaDAO {
    String TITOLO = "titolo";
    String DESCRIZIONE = "descrizione";
    String AUTORE = "autore";

    List<Map<String, Object>> bachecheDisponibili(String autore) throws SQLException;
    void setStatoBacheca(String autore, String titolo, boolean aperta) throws SQLException;
    void setDescrizione(String autore, String titolo, String descrizione) throws SQLException;

}
