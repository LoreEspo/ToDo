import java.util.ArrayList;

public class Bacheca {
    enum NomeBacheca {
        UNIVERSITA,
        LAVORO,
        TEMPO_LIBERO,
    }

    private NomeBacheca titolo;
    private String descrizione;
    private ArrayList<PermessoBacheca> utenti = new ArrayList<PermessoBacheca>();
    private ArrayList<ToDo> todo = new ArrayList<ToDo>();

    public Bacheca(NomeBacheca titolo_bacheca, String descrizione_bacheca) {
        titolo = titolo_bacheca;
        descrizione = descrizione_bacheca;
    }

    public void SetTitolo(NomeBacheca nuovo_titolo) {
        titolo = nuovo_titolo;
    }

    public void SetDescrizione(String nuova_descrizione) {
        descrizione = nuova_descrizione;
    }

    public void AggiungiPermesso(PermessoBacheca permesso) {
        utenti.add(permesso);
    }

    public void AggiungiToDo(ToDo nuovo_todo) {
        todo.add(nuovo_todo);
    }

    public void RimuoviToDo(ToDo vecchio_todo) {
        todo.remove(vecchio_todo);
    }
}