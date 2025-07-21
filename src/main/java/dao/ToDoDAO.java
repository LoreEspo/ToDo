package dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Interfaccia che gestisce le query al database
 * inerenti ai promemoria.
 * Fa uso delle mappe generate dall'interfaccia {@link model.Mappabile}
 */
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

    /**
     * Aggiungi un promemoria al database.
     *
     * @param mappaTodo la mappa del promemoria
     * @return l'id del promemoria
     * @throws SQLException errore durante l'aggiunta
     */
    int aggiungi(Map<String, Object> mappaTodo) throws SQLException;

    /**
     * Rimuove un promemoria dal database.
     *
     * @param id l'id del promemoria
     * @throws SQLException errori durante la rimozione
     */
    void rimuovi(int id) throws SQLException;

    /**
     * Restituisce la lista di promemoria di una bacheca
     * sotto forma di mappa che ha come chiave l'id del promemoria
     * e come valore la mappa del promemoria.
     *
     * @param autore        l'autore della bacheca
     * @param titoloBacheca il titolo della bacheca
     * @return la mappa di promemoria
     * @throws SQLException errori durante la richiesta
     */
    Map<Integer, Map<String, Object>> todoBacheca(String autore, String titoloBacheca) throws SQLException;

    /**
     * Aggiorna le proprietà di un promemoria sul database.
     *
     * @param id        l'id del promemoria
     * @param mappaTodo la mappa del promemoria
     * @throws SQLException errori durante l'aggiornamento
     */
    void aggiornaTodo(int id, Map<String, Object> mappaTodo) throws SQLException;

    /**
     * Aggiorna ordine dei promemoria di una bacheca tramite
     * una mappa con l'ordine come chiave e l'id del
     * promemoria come valore.
     *
     * @param ordineAId la mappa degli ordini
     * @throws SQLException errori durante l'aggiornamento
     */
    void aggiornaOrdine(Map<Integer, Integer> ordineAId) throws SQLException;

    /**
     * Condivide un promemoria.
     *
     * @param id           id del promemoria
     * @param destinatario il destinatario
     * @throws SQLException errore durante la condivisione
     */
    void condividi(int id, String destinatario) throws SQLException;

    /**
     * Revoca una condivisione.
     *
     * @param id           l'id del promemoria
     * @param destinatario l'utente al quale revocare la condivisione
     * @throws SQLException errore durante la rimozione
     */
    void rimuoviCondivisione(int id, String destinatario) throws SQLException;

    /**
     * Restituisce la lista di utenti
     * al quale è stato condiviso il promemoria
     *
     * @param id id del promemoria
     * @return la lista di utenti
     * @throws SQLException errore durante la richiesta
     */
    List<String> condivisiDi(int id) throws SQLException;

    /**
     * Sposta un promemoria in un'altra bacheca.
     *
     * @param id           id del promemoria
     * @param nuovaBacheca la nuova bacheca
     * @throws SQLException errore durante la query
     */
    void sposta(int id, String nuovaBacheca) throws SQLException;

    /**
     * Effettua una ricerca di un contenuto testuale
     * all'interno di un promemoria.
     * I promemoria sono restituiti sotto forma di mappa
     * con le informazioni essenziali per accedervi, quali
     * titolo e titolo della bacheca al quale appartiene.
     *
     * @param username      l'utente che ricerca
     * @param contenuto     il contenuto
     * @param inTitolo      se cercare nel titolo
     * @param inDescrizione se cercare nella descrizione
     * @return la lista delle mappe dei promemoria
     * @throws SQLException errore durante la ricerca
     */
    List<Map<String, String>> ricerca(String username, String contenuto, boolean inTitolo, boolean inDescrizione) throws SQLException;

    /**
     * Restituisce la lista di promemoria
     * che scadono oltre la data data
     * in input.
     *
     * @param username l'autore dei promemoria
     * @param scadenza la scadenza
     * @return the list
     * @throws SQLException errori durante la ricerca
     */
    List<Map<String, String>> inScadenza(String username, Date scadenza) throws SQLException;
}
