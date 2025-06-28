package controller;

import dao.UtenteDAO;
import db.ConnessioneDatabase;
import model.*;
import postgreDAO.UtentePostgreDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


public class Controller {
    private PermessoBacheca permessoBacheca = null;
    private Bacheca bachecaAperta = null;
    private Utente utente = null;
    private final ArrayList<Integer> idBacheche = new ArrayList<>();
    private Map<Integer, PermessoToDo> toDoMap = new HashMap<>();



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

    public void logout() {
        utente = null;
    }

    public boolean isLogged() {
        return utente != null;
    }

    public String getLoggedUsername() {
        if (isLogged()) {
            return utente.getUsername();
        }
        return "";
    }

    public boolean isBachecaAperta() {
        return bachecaAperta != null;
    }

    private Bacheca.NomeBacheca titoloDaIndice(int indice) {
		return switch (indice) {
			case 0 -> Bacheca.NomeBacheca.UNIVERSITA;
			case 1 -> Bacheca.NomeBacheca.LAVORO;
			case 2 -> Bacheca.NomeBacheca.TEMPO_LIBERO;
			default -> null;
		};
	}

    // Funzioni Bacheche
    public void creaNuovaBacheca(String nome, int titolo) {
        if (!isLogged()) return;
        permessoBacheca = utente.creaBacheca(nome, titoloDaIndice(titolo), "");
        bachecaAperta = permessoBacheca.getBacheca();
        // Carica sul database
    }

    public void creaNuovaBacheca(String nome, int titolo, String descrizione) {
        if (!isLogged()) return;
        permessoBacheca = utente.creaBacheca(nome, titoloDaIndice(titolo), descrizione);
        bachecaAperta = permessoBacheca.getBacheca();
        // Carica sul database
    }

    public void chiudiBacheca() {
        if (!isLogged()) return;
        bachecaAperta = null;
    }

    public void setNomeBacheca(String nuovoNome) {
        if (isBachecaAperta()) {
            bachecaAperta.setNome(nuovoNome);
        }
    }

    public void setTitoloBacheca(int nuovoTitolo) {
        if (isBachecaAperta()) {
            bachecaAperta.setTitolo(titoloDaIndice(nuovoTitolo));
            // Update database
        }
    }

    public ArrayList<Integer> richiediBacheche() {
        if (!isLogged()) return null;
        return (ArrayList<Integer>) idBacheche.clone();
    }

    // Funzioni ToDo
    public Integer aggiungiToDo(boolean checklist) {
        if (!isBachecaAperta()) {
            return -1;
        }

        PermessoToDo permesso = utente.creaToDo(checklist);
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
        if (!isBachecaAperta()) {
            return;
        }

        PermessoToDo permesso = toDoMap.get(indice);
        utente.eliminaToDo(permesso);
        bachecaAperta.rimuoviToDo(permesso.getToDo());
        // Update database
    }

    public void modificaToDo(Integer indice, String titolo, boolean completato) {
        if (!isBachecaAperta()) {
            return;
        }

        PermessoToDo permesso = toDoMap.get(indice);
        ToDo todo = permesso.getToDo();

        todo.setTitolo(titolo);
        todo.setCompletato(completato);
    }

    public void aggiungiAttivita(Integer indice) {
        if (!isBachecaAperta()) {
            return;
        }

        PermessoToDo permesso = toDoMap.get(indice);
        ToDoChecklist todo = (ToDoChecklist) permesso.getToDo();

        todo.aggiungiAttivita(new Attivita());
    }

    public void rimuoviAttivita(Integer indiceTodo, int indiceAttivita) {
        if (!isBachecaAperta()) {
            return;
        }

        PermessoToDo permesso = toDoMap.get(indiceTodo);
        ToDoChecklist todo = (ToDoChecklist) permesso.getToDo();

        todo.eliminaAttivita(indiceAttivita);

    }


}
