package model;

import java.util.*;

public class Utente implements Mappabile {
	private final String username;
	private final String password;

	private List<Bacheca> bacheche = new ArrayList<>();
	private Map<Integer, ToDo> todoMap = new HashMap<>();


	public Utente(String usernameUtente, String passwordUtente) {
		username = usernameUtente;
		password = passwordUtente;
	}


	public Utente(String usernameUtente) {
		this(usernameUtente, null);
	}

	public List<Bacheca> getBacheche() { return bacheche; }

	public void setBacheche(List<Bacheca> bacheche) { this.bacheche = bacheche; }

	public Map<Integer, ToDo> getTodoMap() { return todoMap; }

	public void setTodoMap(Map<Integer, ToDo> todoMap) { this.todoMap = todoMap; }

	public String getUsername() { return username; }

	public ToDo getToDo(int indice) {
		return todoMap.get(indice);
	}

	public ToDo creaToDo(int indiceBacheca) {
		return new ToDo(this.getUsername(), bacheche.get(indiceBacheca).getTitolo());
	}

	public void eliminaToDo(Integer indice) {
		this.todoMap.remove(indice);
	}

	public void spostaToDo(ToDo todo, Integer bachecaDa, Integer bachecaA) {
		bacheche.get(bachecaDa).rimuoviToDo(todo);
		bacheche.get(bachecaA).aggiungiToDo(todo);
	}

	public Attivita getAttivita(int indice) {
		for (ToDo todo : todoMap.values()) {
			if (todo.getListaAttivita().containsKey(indice)) {
				return todo.getListaAttivita().get(indice);
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

	public static Utente daMap(Map<String, Object> map) {
		return new Utente((String) map.get("username"), (String) map.get("password"));
	}
}