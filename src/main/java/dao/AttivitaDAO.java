package dao;

import java.sql.SQLException;
import java.util.Map;

public interface AttivitaDAO {
    String TITOLO = "titolo";
    String COMPLETATO = "completato";

    int aggiungi(Integer indiceTodo, Map<String, Object> attivita) throws SQLException;
    void rimuovi(int indice) throws SQLException;
    void aggiorna(int indice, Map<String, Object> attivita) throws SQLException;
    Map<Integer, Map<String, Object>> listaTodo(int indiceTodo) throws SQLException;
}
