package dao;

import java.sql.SQLException;
import java.util.Map;

public interface ToDoDAO {
    String TITOLO = "titolo";
    String SCADENZA = "scadenza";
    String LINK_ATTIVITA = "linkAttivita";
    String DESCRIZIONE = "descrizione";
    String IMMAGINE = "immagine";
    String COLORE_SFONDO = "coloreSfondo";
    String COMPLETATO = "completato";
    String LISTA_ATTIVITA = "listaAttivita";
    String AUTORE = "autore";
    String TITOLO_BACHECA = "titoloBacheca";

    Integer aggiungi(Map<String, Object> todo) throws SQLException;
    void rimuovi(Integer id) throws SQLException;
    Map<Integer, Map<String, Object>> todoBacheca(String autore, String titoloBacheca) throws SQLException;
    void aggiornaTodo(Integer indice, Map<String, Object> todo) throws SQLException;
    void aggiornaOrdine(Map<Integer, Integer> indiceAOrdine) throws SQLException;
}
