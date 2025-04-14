import java.util.ArrayList;

public class Bacheca 
{
    enum NomeBacheca 
    {
        UNIVERSITA,
        LAVORO,
        TEMPO_LIBERO,
    }    
    private NomeBacheca titolo;
    private String descrizione;
    private ArrayList<PermessoBacheca> utenti = new ArrayList<PermessoBacheca>();



    public Bacheca(NomeBacheca titolo_bacheca, String descrizione_bacheca) 
    {
        titolo = titolo_bacheca;
        descrizione = descrizione_bacheca;
    }

    public void SetTitolo(NomeBacheca nuovo_titolo)
    {
        titolo = nuovo_titolo;
    }

    public void SetDescrizione(String nuova_descrizione)
    {
        descrizione = nuova_descrizione;
    }
}