package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Interfaccia che gestisce le query al database
 * inerenti alle bacheche.
 * Fa uso delle mappe generate dall'interfaccia {@link model.Mappabile}
 */
public interface BachecaDAO {
    String TITOLO = "titolo";
    String DESCRIZIONE = "descrizione";
    String AUTORE = "autore";

    /**
     * Restituisce la lista di mappe delle bacheche
     * dell'utente.
     *
     * @param autore l'utente autore delle bacheche
     * @return la lista di bacheche
     * @throws SQLException errori durante la richiesta
     */
    List<Map<String, Object>> bachecheDisponibili(String autore) throws SQLException;

    /**
     * Imposta la descrizione di una bacheca.
     *
     * @param autore      l'autore della bacheca
     * @param titolo      il titolo della bacheca
     * @param descrizione la descrizione da impostare
     * @throws SQLException errore durante la query
     */
    void setDescrizione(String autore, String titolo, String descrizione) throws SQLException;

}
