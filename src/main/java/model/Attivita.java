package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Attività di un {@link ToDo}. È un elemento di una checklist.
 */
public class Attivita implements Mappabile {
    private String titolo = "";
    private boolean completato = false;

    /**
     * @return il titolo dell'attività
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * @param titolo il titolo da impostare
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * @return lo stato dell'attività
     */
    public boolean getCompletato()
    {
        return completato;
    }

    /**
     * @param completato lo stato da impostare
     */
    public void setCompletato(boolean completato) {
        this.completato = completato;
    }

    @Override
    public Map<String, Object> aMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("titolo", titolo);
        map.put("completato", completato);
        return map;
    }

    /**
     * Da {@link #aMap()} a oggetto.
     *
     * @param map la mappa
     * @return l'oggetto
     */
    public static Attivita daMap(Map<String, Object> map) {
        Attivita attivita = new Attivita();
        attivita.setTitolo((String) map.get("titolo"));
        attivita.setCompletato((boolean) map.get("completato"));
        return attivita;
    }
}