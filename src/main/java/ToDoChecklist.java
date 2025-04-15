import java.util.ArrayList;

public class ToDoChecklist extends ToDo {

    private ArrayList<Attivita> lista = new ArrayList<Attivita>();

    public boolean Completato() {
        for (Attivita attivita : lista) {
            if (!attivita.Completato()) {
                return false;
            }
        }
        return true;
    }

    public void AggiungiAttivita(Attivita attivita) {
        lista.add(attivita);
    }

    public void EliminaAttivita(Attivita attivita) {
        lista.remove(attivita);
    }

}