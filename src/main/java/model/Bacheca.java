package model;

import java.util.ArrayList;

public class Bacheca {

    public enum NomeBacheca {
		UNIVERSITA("Università"),
		LAVORO("Lavoro"),
		TEMPO_LIBERO("Tempo libero");

		public final String valore;

		private NomeBacheca(String valore) { this.valore = valore; }

		public static NomeBacheca daString(String stringa) {
			for (NomeBacheca nome : values()) {
				if (nome.valore.equals(stringa))
					return nome;
			}
			return null;
		}
	}

	private NomeBacheca titolo;
	private String descrizione;
	private String autore;
	private final ArrayList<ToDo> todo = new ArrayList<>();


	public Bacheca(NomeBacheca titoloBacheca, String descrizioneBacheca, String autoreBacheca) {
		titolo = titoloBacheca;
		descrizione = descrizioneBacheca;
		autore = autoreBacheca;
	}

	public NomeBacheca getTitolo() {
		return titolo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setTitolo(NomeBacheca titolo) {
		this.titolo = titolo;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getAutore() {
		return autore;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public void aggiungiToDo(ToDo nuovoTodo) {
		todo.add(nuovoTodo);
	}

	public void rimuoviToDo(ToDo vecchioTodo) {
		todo.remove(vecchioTodo);
	}
}