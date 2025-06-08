package model;

import java.util.ArrayList;

public class ToDoChecklist extends ToDo {

    private ArrayList<Attivita> listaAttivita = new ArrayList<Attivita>();

    public boolean getCompletato() {
        for (Attivita attivita : listaAttivita) {
            if (!attivita.getCompletato()) {
                return false;
            }
        }
        return true;
    }

    public void aggiungiAttivita(Attivita attivita) {
        listaAttivita.add(attivita);
    }

    public void eliminaAttivita(Attivita attivita) {
        listaAttivita.remove(attivita);
    }

    public void eliminaAttivita(int indiceAttivita) {
        listaAttivita.remove(indiceAttivita);
    }

}