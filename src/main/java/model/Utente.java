package model;

import java.util.*;

/**
 * Un utente dell'app. Normalmente esiste un solo oggetto a volta,
 * che rappresenta l'utente dell'applicazione
 */
public class Utente implements Mappabile {
	private final String username;
	private final String password;

	private List<Bacheca> bacheche = new ArrayList<>();
	private Map<Integer, ToDo> todoMap = new HashMap<>();


	/**
	 * Istanzia un nuovo utente.
	 *
	 * @param usernameUtente l'username
	 * @param passwordUtente la password
	 */
	public Utente(String usernameUtente, String passwordUtente) {
		username = usernameUtente;
		password = passwordUtente;
	}

	/**
	 * Istanzia un utente senza password
	 *
	 * @param usernameUtente l'username
	 */
	public Utente(String usernameUtente) {
		this(usernameUtente, null);
	}

	/**
	 * @return la lista di bacheche dell'utente
	 */
	public List<Bacheca> getBacheche() { return bacheche; }

	/**
	 * @param bacheche la lista di bacheche da impostare
	 */
	public void setBacheche(List<Bacheca> bacheche) { this.bacheche = bacheche; }

	/**
	 * @return la mappa di promemoria (id -> {@link ToDo})
	 */
	public Map<Integer, ToDo> getTodoMap() { return todoMap; }

	/**
	 * @param todoMap la mappa di promemoria (id -> {@link ToDo}) da impostare
	 */
	public void setTodoMap(Map<Integer, ToDo> todoMap) { this.todoMap = todoMap; }

	/**
	 * @return l'username
	 */
	public String getUsername() { return username; }

	/**
	 * @param id id del promemoria
	 * @return il promemoria
	 */
	public ToDo getToDo(int id) {
		return todoMap.get(id);
	}

	/**
	 * @param id il promemoria da eliminare
	 */
	public void eliminaToDo(int id) {
		this.todoMap.remove(id);
	}

	/**
	 * @param id id dell'attività
	 * @return l'attivita
	 */
	public Attivita getAttivita(int id) {
		for (ToDo todo : todoMap.values()) {
			if (todo.getListaAttivita().containsKey(id)) {
				return todo.getListaAttivita().get(id);
			}
		}
		return null;
	}

	@Override
	public Map<String, Object> aMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("username", username);
		map.put("password", password);

		return map;
	}

	/**
	 * Da {@link #aMap()} a oggetto.
	 *
	 * @param map la mappa
	 * @return l 'oggetto
	 */
	public static Utente daMap(Map<String, Object> map) {
		return new Utente((String) map.get("username"), (String) map.get("password"));
	}
}