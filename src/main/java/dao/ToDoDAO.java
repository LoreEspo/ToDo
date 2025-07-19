package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface ToDoDAO {
    String TITOLO = "titolo";
    String SCADENZA = "scadenza";
    String LINK_ATTIVITA = "linkAttivita";
    String DESCRIZIONE = "descrizione";
    String IMMAGINE = "immagine";
    String COLORE_SFONDO = "coloreSfondo";
    String COMPLETATO = "completato";
    String AUTORE = "autore";
    String TITOLO_BACHECA = "titoloBacheca";
    String CONDIVISO = "condiviso";

    Integer aggiungi(Map<String, Object> todo) throws SQLException;
    void rimuovi(Integer id) throws SQLException;
    Map<Integer, Map<String, Object>> todoBacheca(String autore, String titoloBacheca) throws SQLException;
    void aggiornaTodo(Integer indice, Map<String, Object> todo) throws SQLException;
    void aggiornaOrdine(Map<Integer, Integer> indiceAOrdine) throws SQLException;
    void condividi(Integer indice, String destinatario) throws SQLException;
    void rimuoviCondivisione(Integer indice, String destinatario) throws SQLException;
    List<String> condivisiDi(Integer indice) throws SQLException;
    void sposta(Integer indice, String nuovaBacheca) throws SQLException;
    List<Map<String, String>> ricerca(String username, String contenuto, boolean inTitolo, boolean inDescrizione) throws SQLException;
}
