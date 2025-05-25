package model;

import java.util.ArrayList;

public class Utente {
	private String username;
	private String password;

	private ArrayList<PermessoBacheca> bacheche = new ArrayList<PermessoBacheca>();
	private ArrayList<PermessoToDo> todo = new ArrayList<PermessoToDo>();

	public Utente(String username_utente, String password_utente) {
		username = username_utente;
		password = password_utente;
	}


	public PermessoBacheca creaBacheca(String nome, Bacheca.NomeBacheca titolo, String descrizione) {
		Bacheca bacheca = new Bacheca(nome, titolo, descrizione);
		PermessoBacheca permesso = new PermessoBacheca(this, bacheca, true, true, true);

		bacheche.add(permesso);

		return permesso;
	}

	public void modificaBacheca(PermessoBacheca permesso, Bacheca.NomeBacheca nuovo_titolo, String nuova_descrizione) {
		if (!permesso.possessore(this) || !permesso.modifica) {
			return;
		}

		permesso.getBacheca().setTitolo(nuovo_titolo);
		permesso.getBacheca().setDescrizione(nuova_descrizione);
	}

	public void eliminaBacheca(PermessoBacheca permesso) {
		if (!permesso.possessore(this) || !permesso.eliminazione) {
			return;
		}

		bacheche.remove(permesso);
	}

	public PermessoToDo creaToDo(boolean checklist) {
		ToDo nuovo_todo = checklist ? new ToDoChecklist() : new ToDo();
		PermessoToDo permesso = new PermessoToDo(this, nuovo_todo, true, true);
		nuovo_todo.aggiungiPermesso(permesso);
		this.todo.add(permesso);
		return permesso;
	}

	public void modificaToDo(PermessoToDo permesso, String titolo, String data, String link_attivita) {
		if (!permesso.possessore(this) || !permesso.modifica) {
			return;
		}

		permesso.getToDo().setTitolo(titolo);
		permesso.getToDo().setData(data);
		permesso.getToDo().setLinkAttivita(link_attivita);
	}

	public void eliminaToDo(PermessoToDo permesso) {
		if (!permesso.possessore(this) || !permesso.eliminazione) {
			return;
		}

		todo.remove(permesso);
	}

	public void spostaToDo(PermessoToDo permessoTodo, PermessoBacheca permessoDa, PermessoBacheca permessoA) {
		if (!permessoTodo.possessore(this) || !permessoTodo.modifica) {
			return;
		}

		if (!permessoDa.possessore(this) || !permessoDa.modifica) {
			return;
		}

		if (!permessoA.possessore(this) || !permessoA.modifica) {
			return;
		}

		permessoDa.getBacheca().rimuoviToDo(permessoTodo.getToDo());
		permessoA.getBacheca().aggiungiToDo(permessoTodo.getToDo());
	}
}