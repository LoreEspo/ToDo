import java.util.ArrayList;

public class Utente 
{
    private String username; 
    private String password;
    
    private ArrayList<PermessoBacheca> bacheche = new ArrayList<PermessoBacheca>(); 
    
    public boolean login(String username, String password) 
    {
        if (this.username == username && this.password == password) {
            return true;
        }
        return false;
    }

    public PermessoBacheca CreaBacheca(Bacheca.NomeBacheca titolo, String descrizione)
    {
        Bacheca bacheca = new Bacheca(titolo, descrizione);
        PermessoBacheca permesso = new PermessoBacheca(this, bacheca, true, true, true, true);

        bacheche.add(permesso);

        return permesso;
    }

    public void ModificaBacheca(PermessoBacheca permesso, Bacheca.NomeBacheca nuovo_titolo, String nuova_descrizione)
    {
        if (!permesso.Possessore(this) || !permesso.modifica) 
        {
            return;
        }
        
        permesso.GetBacheca().SetTitolo(nuovo_titolo);
        permesso.GetBacheca().SetDescrizione(nuova_descrizione);
    }

    public void Elimina(PermessoBacheca permesso)
    {
        if (!permesso.Possessore(this) || !permesso.autore) 
        {
            return;
        }

        bacheche.remove(permesso);

        
    }
}