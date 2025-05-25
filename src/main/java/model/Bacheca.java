package model;

import java.util.ArrayList;

public class Bacheca {
	public enum NomeBacheca {
		UNIVERSITA,
		LAVORO,
		TEMPO_LIBERO,
	}

	private NomeBacheca titolo;
	private String nome;
	private String descrizione;
	private ArrayList<PermessoBacheca> utenti = new ArrayList<PermessoBacheca>();
	private ArrayList<ToDo> todo = new ArrayList<ToDo>();

	private Integer id = -1;


	public Bacheca(String nome_bacheca, NomeBacheca titolo_bacheca, String descrizione_bacheca) {
		nome = nome_bacheca;
		titolo = titolo_bacheca;
		descrizione = descrizione_bacheca;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTitolo(NomeBacheca nuovo_titolo) {
		titolo = nuovo_titolo;
	}

	public void setNome(String nome_bacheca) {
		nome = nome_bacheca;
	}

	public String getNome() {
		return nome;
	}

	public void setDescrizione(String nuova_descrizione) {
		descrizione = nuova_descrizione;
	}

	public void aggiungiPermesso(PermessoBacheca permesso) {
		utenti.add(permesso);
	}

	public void aggiungiToDo(ToDo nuovo_todo) {
		todo.add(nuovo_todo);
	}

	public void rimuoviToDo(ToDo vecchio_todo) {
		todo.remove(vecchio_todo);
	}
}