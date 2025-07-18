package dao;

import model.Bacheca;
import model.ToDo;
import model.Utente;

import java.sql.SQLException;
import java.util.Map;

public interface ToDoDAO {
    Integer aggiungi(ToDo todo) throws SQLException;
    void rimuovi(Integer id) throws SQLException;
    Map<Integer, ToDo> todoBacheca(Utente utente, Bacheca bacheca) throws SQLException;
    void aggiornaTodo(Integer indice, ToDo todo) throws SQLException;
}
