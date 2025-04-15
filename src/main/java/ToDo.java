public class ToDo {
    private String titolo;
    private String data;
    private String link_attivita;
    private String immagine; // Temporaneamente String
    private String colore_sfondo;
    private boolean completato = false;

    private PermessoToDo utente;

    public boolean Completato() {
        return completato;
    }

    public void AggiungiPermesso(PermessoToDo permesso) {
        utente = permesso;
    }

    public String GetTitolo() {
        return titolo;
    }

    public void SetTitolo(String nuovo_titolo) {
        titolo = nuovo_titolo;
    }

    public String GetData() {
        return data;
    }

    public void SetData(String nuovo_data) {
        data = nuovo_data;
    }

    public String GetLinkAttivita() {
        return link_attivita;
    }

    public void SetLinkAttivita(String nuovo_link) {
        link_attivita = nuovo_link;
    }

    public String GetImmagine() {
        return immagine;
    }

    public void SetImmagine(String nuova_immagine) {
        immagine = nuova_immagine;
    }
}