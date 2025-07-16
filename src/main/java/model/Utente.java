package model;

import java.util.List;
import java.util.ArrayList;

public class Utente {
	private final String username;
	private final String password;

	private final List<Bacheca> bacheche = new ArrayList<>();
	private final List<PermessoToDo> todo = new ArrayList<>();


	public Utente(String usernameUtente, String passwordUtente) {
		username = usernameUtente;
		password = passwordUtente;
	}


	public Utente(String usernameUtente) {
		this(usernameUtente, null);
	}


	private boolean autorizzato() { return password != null; }

	public List<Bacheca> getBacheche() { return bacheche; }

	public List<PermessoToDo> getToDo() { return todo; }

	public String getUsername() { return username; }

	public Bacheca creaBacheca(Bacheca.NomeBacheca titolo, String descrizione) {
		Bacheca bacheca = new Bacheca(titolo, descrizione, username);

		bacheche.add(bacheca);

		return bacheca;
	}

	public void modificaBacheca(Bacheca bacheca, Bacheca.NomeBacheca nuovoTitolo, String nuovaDescrizione) {
		bacheca.setTitolo(nuovoTitolo);
		bacheca.setDescrizione(nuovaDescrizione);
	}

	public void eliminaBacheca(Bacheca bacheca) {
		bacheche.remove(bacheca);
	}

	public PermessoToDo creaToDo() {
		ToDo nuovoTodo = new ToDo();
		PermessoToDo permesso = new PermessoToDo(this, nuovoTodo, true, true);
		nuovoTodo.aggiungiPermesso(permesso);
		this.todo.add(permesso);
		return permesso;
	}

	public void modificaToDo(PermessoToDo permesso, String titolo, String data, String linkAttivita) {
		if (!permesso.possessore(this) || !permesso.modifica) {
			return;
		}

		permesso.getToDo().setTitolo(titolo);
		permesso.getToDo().setData(data);
		permesso.getToDo().setLinkAttivita(linkAttivita);
	}

	public void eliminaToDo(PermessoToDo permesso) {
		if (!permesso.possessore(this) || !permesso.eliminazione) {
			return;
		}

		todo.remove(permesso);
	}

	public void spostaToDo(PermessoToDo permessoTodo, Bacheca bachecaDa, Bacheca bachecaA) {

		if (!permessoTodo.possessore(this) || !permessoTodo.modifica) {
			return;
		}

		bachecaDa.rimuoviToDo(permessoTodo.getToDo());
		bachecaA.aggiungiToDo(permessoTodo.getToDo());
	}
}