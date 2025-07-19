package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Bacheca.
 */
public class Bacheca implements Mappabile {

	/**
	 * Enumeratore con i diversi tipi di bacheca.
	 */
	public enum NomeBacheca {
		/// Bacheca dell'università
		UNIVERSITA("Università"),
		/// Bacheca del lavoro
		LAVORO("Lavoro"),
		/// Bacheca per il tempo libero
		TEMPO_LIBERO("Tempo libero");

		/// Valore costante in stringa dell'enumeratore.
		public final String valore;

		NomeBacheca(String valore) { this.valore = valore; }

		/**
		 * Restituisce un oggetto enumeratore
		 * con valore in stringa
		 * uguale a quello passato in input.
		 *
		 * @param stringa il valore in stringa
		 * @return l'oggetto enumeratore
		 */
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


	/**
	 * Instanzia una bacheca.
	 *
	 * @param titoloBacheca      il titolo della bacheca
	 * @param descrizioneBacheca la descrizione della bacheca
	 * @param autoreBacheca      l'autore della bacheca
	 */
	public Bacheca(NomeBacheca titoloBacheca, String descrizioneBacheca, String autoreBacheca) {
		titolo = titoloBacheca;
		descrizione = descrizioneBacheca;
		autore = autoreBacheca;
	}

	/**
	 * @return il titolo
	 */
	public NomeBacheca getTitolo() {
		return titolo;
	}

	/**
	 * @return la descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param titolo il titolo da impostare
	 */
	public void setTitolo(NomeBacheca titolo) {
		this.titolo = titolo;
	}

	/**
	 * @param descrizione la descrizione da impostare
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @return l'autore della bacheca
	 */
	public String getAutore() {
		return autore;
	}

	/**
	 * @param autore l'autore da impostare
	 */
	public void setAutore(String autore) {
		this.autore = autore;
	}

	/**
	 * Aggiungi un promemoria.
	 *
	 * @param nuovoTodo il promemoria da aggiungere
	 */
	public void aggiungiToDo(ToDo nuovoTodo) {
		todo.add(nuovoTodo);
	}

	/**
	 * Rimuovi un promemoria.
	 *
	 * @param vecchioTodo il promemoria da rimuovere
	 */
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

	/**
	 * Da {@link #aMap()} a oggetto.
	 *
	 * @param map la mappa
	 * @return l 'oggetto
	 */
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