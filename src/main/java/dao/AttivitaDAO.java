package dao;

import java.sql.SQLException;
import java.util.Map;

/**
 * Interfaccia che gestisce le query al database
 * inerenti alle attività.
 * Fa uso delle mappe generate dall'interfaccia {@link model.Mappabile}
 */
public interface AttivitaDAO {
    String TITOLO = "titolo";
    String COMPLETATO = "completato";

    /**
     * Aggiunge un attività
     *
     * @param idTodo   l'id del promemoria al quale appartiene l'attività
     * @param attivita la mappa dell'attività
     * @return l'id dell'attività
     * @throws SQLException errori durante l'aggiunta dell'attività
     */
    int aggiungi(int idTodo, Map<String, Object> attivita) throws SQLException;

    /**
     * Rimuove un'attività.
     *
     * @param id id dell'attività
     * @throws SQLException errori durante la rimozione
     */
    void rimuovi(int id) throws SQLException;

    /**
     * Aggiorna le proprietà di un'attività.
     *
     * @param id       l'id dell'attività
     * @param attivita la mappa dell'attività
     * @throws SQLException errori durante l'aggiornamento
     */
    void aggiorna(int id, Map<String, Object> attivita) throws SQLException;

    /**
     * Restituisce la lista di attività appartenenti ad un promemoria.
     * La lista è una mappa: le chiavi sono l'id dell'attività e i valori sono
     * la mappa dell'attività
     *
     * @param idTodo l'id del promemoria
     * @return la lista di attività
     * @throws SQLException errori durante la richiesta
     */
    Map<Integer, Map<String, Object>> listaTodo(int idTodo) throws SQLException;
}
