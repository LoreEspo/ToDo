public class PermessoBacheca 
{
    public boolean modifica;
    public boolean eliminazione;
    public boolean condivisione;
    public boolean autore;

    private Utente utente;
    private Bacheca bacheca;
    
    public PermessoBacheca(Utente utente, Bacheca bacheca, boolean modifica, boolean eliminazione, boolean condivisione, boolean autore) 
    {
        this.utente = utente;
        this.bacheca = bacheca;
        this.modifica = modifica;
        this.eliminazione = eliminazione;
        this.condivisione = condivisione;
        this.autore = autore;
    }

    public boolean Possessore(Utente possessore) 
    {
        return possessore == utente;
    }

    public Bacheca GetBacheca() 
    {
        return bacheca;
    }
}