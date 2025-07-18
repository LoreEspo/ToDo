package model;

import java.util.HashMap;
import java.util.Map;

public class Attivita implements Mappabile {
    private String titolo = "";
    private boolean completato = false;

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public boolean getCompletato()
    {
        return completato;
    }

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

    public static Attivita daMap(Map<String, Object> map) {
        Attivita attivita = new Attivita();
        attivita.setTitolo((String) map.get("titolo"));
        attivita.setCompletato((boolean) map.get("completato"));
        return attivita;
    }
}