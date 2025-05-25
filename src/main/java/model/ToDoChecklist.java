package model;

import java.util.ArrayList;

public class ToDoChecklist extends ToDo {

    private ArrayList<Attivita> lista = new ArrayList<Attivita>();

    public boolean completato() {
        for (Attivita attivita : lista) {
            if (!attivita.completato()) {
                return false;
            }
        }
        return true;
    }

    public void aggiungiAttivita(Attivita attivita) {
        lista.add(attivita);
    }

    public void eliminaAttivita(Attivita attivita) {
        lista.remove(attivita);
    }

    public void eliminaAttivita(int indiceAttivita) {
        lista.remove(indiceAttivita);
    }

}