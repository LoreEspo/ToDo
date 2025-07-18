package dao;

import model.Bacheca;
import model.Utente;

import java.sql.SQLException;
import java.util.List;

public interface BachecaDAO {
    List<Bacheca> bachecheDisponibili(Utente utente) throws SQLException;

    void setStatoBacheca(String autore, String titolo, boolean aperta) throws SQLException;
    void setDescrizione(String autore, String titolo, String descrizione) throws SQLException;

}
