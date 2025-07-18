package controller;

import dao.AttivitaDAO;
import dao.BachecaDAO;
import dao.ToDoDAO;
import dao.UtenteDAO;
import model.*;
import postgredao.AttivitaPostgreDAO;
import postgredao.BachecaPostgreDAO;
import postgredao.ToDoPostgreDAO;
import postgredao.UtentePostgreDAO;

import java.sql.SQLException;
import java.util.*;


public class Controller {
    private static Controller instance = null;

    private Bacheca bachecaAperta = null;
    private Utente utente = null;
    private final List<Integer> todoModificati = new ArrayList<>();
    private final List<Integer> attivitaModificate = new ArrayList<>();

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public boolean login(String username, String password) throws SQLException {
        UtenteDAO udao = new UtentePostgreDAO();

        if (!udao.login(username, password)) {
            return false;
        }

        utente = new Utente(username, password);
        return true;
    }

    public boolean registrazione(String username, String password) throws SQLException {
        UtenteDAO udao = new UtentePostgreDAO();

        if (!udao.registrazione(username, password)) {
            return false;
        }

        utente = new Utente(username, password);
        return true;
    }

    public void logout() { utente = null; }

    public boolean isLogged() { return utente != null; }

    public String getLoggedUsername() {
        if (isLogged()) {
            return utente.getUsername();
        }
        return "";
    }

    public boolean isBachecaChiusa() {
        return bachecaAperta == null;
    }

    public boolean modificheEffettuate() {
        return !todoModificati.isEmpty() || !attivitaModificate.isEmpty();
    }

    // Funzioni bacheche

    public void apriBacheca(int indice) throws SQLException {
        BachecaDAO bachecaDao = new BachecaPostgreDAO();
        ToDoDAO todoDao = new ToDoPostgreDAO();
        AttivitaDAO attivitaDao = new AttivitaPostgreDAO();

        bachecaDao.setStatoBacheca(getAutoreBacheca(indice), getTitoloBacheca(indice), true);
        bachecaAperta = utente.getBacheche().get(indice);

        Map<Integer, Map<String, Object>> hashmap = todoDao.todoBacheca(utente.getUsername(), bachecaAperta.getTitolo().valore);
        Map<Integer, ToDo> toDoMap = new HashMap<>();
        for (Map.Entry<Integer, Map<String, Object>> entry : hashmap.entrySet()) {
            toDoMap.put(entry.getKey(), ToDo.daMap(entry.getValue()));
        }
        utente.setTodoMap(toDoMap);
        for (Map.Entry<Integer, ToDo> entryTodo : toDoMap.entrySet()) {
            for (Map.Entry<Integer, Map<String, Object>> entryAttivita : attivitaDao.listaTodo(entryTodo.getKey()).entrySet()) {
                entryTodo.getValue().aggiungiAttivita(entryAttivita.getKey(), Attivita.daMap(entryAttivita.getValue()));
            }
        }

        todoModificati.clear();
        attivitaModificate.clear();
    }

    public void chiudiBacheca() throws SQLException {
        if (!isLogged()) return;
        BachecaDAO dao = new BachecaPostgreDAO();
        dao.setStatoBacheca(bachecaAperta.getAutore(), bachecaAperta.getTitolo().valore, false);
        bachecaAperta = null;
        utente.getTodoMap().clear();
    }

    public int richiediBacheche() throws SQLException {
        if (!isLogged()) return 0;

        BachecaDAO bachecheDao = new BachecaPostgreDAO();

        List<Map<String, Object>> bacheche = bachecheDao.bachecheDisponibili(utente.getUsername());
        List<Bacheca> lista = new ArrayList<>();
        for (Map<String, Object> map : bacheche) {
            lista.add(Bacheca.daMap(map));
        }
        utente.setBacheche(lista);

        return utente.getBacheche().size();
    }

    public String getAutoreBacheca(int indice) {
        Bacheca bacheca = utente.getBacheche().get(indice);
        return bacheca.getAutore();
    }

    public String getAutoreBacheca() {
        if (isBachecaChiusa()) {
            return "";
        }
        return bachecaAperta.getAutore();
    }

    public String getTitoloBacheca(int indice) {
        Bacheca bacheca = utente.getBacheche().get(indice);
        return bacheca.getTitolo().valore;
    }

    public String getTitoloBacheca() {
        if (isBachecaChiusa()) {
            return null;
        }
        return bachecaAperta.getTitolo().valore;
    }

    public String getDescrizioneBacheca(int indice) {
        Bacheca bacheca = utente.getBacheche().get(indice);
        return bacheca.getDescrizione();
    }

    // Funzioni promemoria

    private void setToDoModificato(Integer indice) {
        if (todoModificati.contains(indice)) {
            return;
        }
        todoModificati.add(indice);
    }

    public void salvaToDo() throws SQLException {
        ToDoDAO dao = new ToDoPostgreDAO();
        for (int indice: todoModificati) {
            dao.aggiornaTodo(indice, utente.getToDo(indice).aMap());
        }
        todoModificati.clear();
    }

    public Integer aggiungiToDo() throws SQLException {
        if (isBachecaChiusa()) {
            return -1;
        }
        ToDoDAO dao = new ToDoPostgreDAO();

        ToDo todo = new ToDo(utente.getUsername(), bachecaAperta.getTitolo());
        Integer indice = dao.aggiungi(todo.aMap());
        utente.getTodoMap().put(indice, todo);

        return indice;
    }

    public void rimuoviToDo(Integer indice) throws SQLException {
        if (isBachecaChiusa()) {
            return;
        }
        todoModificati.remove(indice);

        ToDoDAO dao = new ToDoPostgreDAO();

        dao.rimuovi(indice);

        utente.eliminaToDo(indice);
    }

    public Set<Integer> listaToDo() { return utente.getTodoMap().keySet(); }

    public String getTitoloToDo(Integer indice) {
        return utente.getToDo(indice).getTitolo();
    }

    public void setTitoloToDo(Integer indice, String titolo) {
        utente.getToDo(indice).setTitolo(titolo);
        setToDoModificato(indice);
    }

    public String getDescrizioneToDo(Integer indice) {
        return utente.getToDo(indice).getDescrizione();
    }

    public void setDescrizioneToDo(Integer indice, String descrizione) {
        utente.getToDo(indice).setDescrizione(descrizione);
        setToDoModificato(indice);
    }

    public boolean getCompletatoToDo(Integer indice) {
        return utente.getToDo(indice).getCompletato();
    }

    public void setCompletatoToDo(Integer indice, boolean stato) {
        utente.getToDo(indice).setCompletato(stato);
        setToDoModificato(indice);
    }

    public byte[] getImmagineToDo(Integer indice) {
        return utente.getToDo(indice).getImmagine();
    }

    public void setImmagineToDo(Integer indice, byte[] immagine) {
        utente.getToDo(indice).setImmagine(immagine);
        setToDoModificato(indice);
    }

    public String getLinkToDo(Integer indice) {
        return utente.getToDo(indice).getLinkAttivita();
    }

    public void setLinkToDo(Integer indice, String linkAttivita) {
        utente.getToDo(indice).setLinkAttivita(linkAttivita);
        setToDoModificato(indice);
    }

    public String getColoreToDo(Integer indice) {
        return utente.getToDo(indice).getColoreSfondo();
    }

    public void setColoreToDo(Integer indice, String colore) {
        utente.getToDo(indice).setColoreSfondo(colore);
        setToDoModificato(indice);
    }

    public Date getScadenzaToDo(Integer indice) {
        return utente.getToDo(indice).getScadenza();
    }

    public void setScadenzaToDo(Integer indice, Date scadenza) {
        utente.getToDo(indice).setScadenza(scadenza);
        setToDoModificato(indice);
    }

    public int aggiungiAttivita(Integer indiceTodo) throws SQLException {
        if (isBachecaChiusa()) {
            return -1;
        }
        AttivitaDAO dao = new AttivitaPostgreDAO();

        ToDo todo = utente.getTodoMap().get(indiceTodo);
        Attivita attivita = new Attivita();
        int indice = dao.aggiungi(indiceTodo, attivita.aMap());
        todo.aggiungiAttivita(indice, attivita);
        return indice;
    }

    public void rimuoviAttivita(Integer indiceTodo, int indiceAttivita) throws SQLException {
        if (isBachecaChiusa()) {
            return;
        }
        AttivitaDAO dao = new AttivitaPostgreDAO();
        attivitaModificate.remove(indiceAttivita);

        dao.rimuovi(indiceAttivita);

        ToDo todo = utente.getTodoMap().get(indiceTodo);

        todo.eliminaAttivita(indiceAttivita);

    }

    public String getTitoloAttivita(Integer indice) {
        return utente.getAttivita(indice).getTitolo();
    }

    public void setTitoloAttivita(Integer indice, String titolo) {
        utente.getAttivita(indice).setTitolo(titolo);
        attivitaModificate.add(indice);
    }

    public boolean getCompletatoAttivita(Integer indice) {
        return utente.getAttivita(indice).getCompletato();
    }

    public void setCompletatoAttivita(Integer indice, boolean completato) {
        utente.getAttivita(indice).setCompletato(completato);
        attivitaModificate.add(indice);
    }

    public void salvaAttivita() throws SQLException {
        AttivitaDAO dao = new AttivitaPostgreDAO();
        for (Integer indice : attivitaModificate) {
            dao.aggiorna(indice, utente.getAttivita(indice).aMap());
        }
        attivitaModificate.clear();
    }

    public Set<Integer> attivitaTodo(Integer indice) {
        return utente.getToDo(indice).getListaAttivita().keySet();
    }
}
