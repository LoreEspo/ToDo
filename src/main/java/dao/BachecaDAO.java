package dao;

import model.Bacheca;
import model.Utente;

import java.sql.SQLException;
import java.util.List;

public interface BachecaDAO {
    public List<Bacheca> bachecheDisponibili(Utente utente) throws SQLException;

    public void setStatoBacheca(String autore, String titolo, boolean aperta) throws SQLException;

}
