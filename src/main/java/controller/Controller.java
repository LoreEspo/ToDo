package controller;

import dao.BachecaDAO;
import dao.UtenteDAO;
import model.*;
import postgreDAO.BachecaPostgreDAO;
import postgreDAO.UtentePostgreDAO;

import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;


public class Controller {
    private static Controller instance = null;

    private Bacheca bachecaAperta = null;
    private Utente utente = null;
    private List<Bacheca> bacheche;
    private final Map<Integer, PermessoToDo> toDoMap = new HashMap<>();

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

    // Funzioni Bacheche
    public void apriBacheca(int indice) throws SQLException {
        BachecaDAO dao = new BachecaPostgreDAO();
        dao.setStatoBacheca(getAutoreBacheca(indice), getTitoloBacheca(indice), true);
        bachecaAperta = bacheche.get(indice);
    }

    public void chiudiBacheca() throws SQLException {
        if (!isLogged()) return;
        BachecaDAO dao = new BachecaPostgreDAO();
        dao.setStatoBacheca(bachecaAperta.getAutore(), bachecaAperta.getTitolo().valore, false);
        bachecaAperta = null;
    }

    public int richiediBacheche() throws SQLException {
        if (!isLogged()) return 0;

        BachecaDAO bachecheDao = new BachecaPostgreDAO();

        bacheche = bachecheDao.bachecheDisponibili(utente);

        return bacheche.size();
    }

    public String getAutoreBacheca(int indice) {
        Bacheca bacheca = bacheche.get(indice);
        return bacheca.getAutore();
    }

    public String getAutoreBacheca() {
        if (bachecaAperta == null) {
            return "";
        }
        return bachecaAperta.getAutore();
    }

    public String getTitoloBacheca(int indice) {
        Bacheca bacheca = bacheche.get(indice);
        return bacheca.getTitolo().valore;
    }

    public String getTitoloBacheca() {
        if (bachecaAperta == null) {
            return null;
        }
        return bachecaAperta.getTitolo().valore;
    }

    public String getDescrizioneBacheca(int indice) {
        Bacheca bacheca = bacheche.get(indice);
        return bacheca.getDescrizione();
    }

    public String getDescrizioneBacheca() {
        if (bachecaAperta == null) {
            return "";
        }
        return bachecaAperta.getDescrizione();
    }

    // Funzioni promemoria
    public Integer aggiungiToDo() {
        if (isBachecaChiusa()) {
            return -1;
        }

        PermessoToDo permesso = utente.creaToDo();
        bachecaAperta.aggiungiToDo(permesso.getToDo());

        int indice = -1;

        for (Integer i : toDoMap.keySet()) {
            indice = Integer.max(indice, i);
        }
        indice++;
        toDoMap.put(indice, permesso);

        // Update database

        return indice;
    }

    public void rimuoviToDo(Integer indice) {
        if (isBachecaChiusa()) {
            return;
        }

        PermessoToDo permesso = toDoMap.get(indice);
        utente.eliminaToDo(permesso);
        bachecaAperta.rimuoviToDo(permesso.getToDo());
        // Update database
    }

    public void modificaToDo(Integer indice, String titolo, boolean completato) {
        if (isBachecaChiusa()) {
            return;
        }

        PermessoToDo permesso = toDoMap.get(indice);
        ToDo todo = permesso.getToDo();

        todo.setTitolo(titolo);
        todo.setCompletato(completato);
    }

    public void aggiungiAttivita(Integer indice) {
        if (isBachecaChiusa()) {
            return;
        }

        PermessoToDo permesso = toDoMap.get(indice);
        ToDo todo = permesso.getToDo();

        todo.aggiungiAttivita(new Attivita());
    }

    public void rimuoviAttivita(Integer indiceTodo, int indiceAttivita) {
        if (isBachecaChiusa()) {
            return;
        }

        PermessoToDo permesso = toDoMap.get(indiceTodo);
        ToDo todo = permesso.getToDo();

        todo.eliminaAttivita(indiceAttivita);

    }


}
