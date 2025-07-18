package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bacheca implements Mappabile {

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

	@Override
	public Map<String, Object> aMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("titolo", titolo.valore);
		map.put("descrizione", descrizione);
		map.put("autore", autore);
		map.put("todo", new ArrayList<Map<String, Object>>());

		for (ToDo singoloTodo : todo) {
			List<Map<String, Object>> lista = (List<Map<String, Object>>) map.get("todo");
			lista.add(singoloTodo.aMap());
		}
		return map;
	}

	public static Bacheca daMap(Map<String, Object> map) {
		Bacheca bacheca = new Bacheca(
				NomeBacheca.daString((String) map.get("titolo")),
				(String) map.get("descrizione"),
				(String) map.get("autore")
		);
		List<Map<String, Object>> lista = (List<Map<String, Object>>) map.get("todo");
		if (lista != null) {
			for (Map<String, Object> mappaTodo : lista) {
				bacheca.todo.add(ToDo.daMap(mappaTodo));
			}
		}
		return bacheca;
	}
}