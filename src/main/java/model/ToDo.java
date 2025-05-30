package model;

public class ToDo {
    private String titolo;
    private String data;
    private String linkAttivita;
    private byte[] immagine; // Temporaneamente String
    private String coloreSfondo;
    private boolean completato = false;

    private PermessoToDo utente;

    public boolean getCompletato() {
        return completato;
    }

    public void setCompletato(boolean completato) { this.completato = completato; }

    public void aggiungiPermesso(PermessoToDo permesso) {
        utente = permesso;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String nuovo_titolo) {
        titolo = nuovo_titolo;
    }

    public String getData() {
        return data;
    }

    public void setData(String nuovo_data) {
        data = nuovo_data;
    }

    public String getLinkAttivita() {
        return linkAttivita;
    }

    public void setLinkAttivita(String nuovo_link) {
        linkAttivita = nuovo_link;
    }

    public byte[] getImmagine() {
        return immagine;
    }

    public void setImmagine(byte[] nuova_immagine) {
        immagine = nuova_immagine;
    }
}