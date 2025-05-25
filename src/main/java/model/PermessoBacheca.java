package model;

public class PermessoBacheca {
    public boolean modifica;
    public boolean eliminazione;
    public boolean condivisione;

    private Utente utente;
    private Bacheca bacheca;

    public PermessoBacheca(Utente utente, Bacheca bacheca, boolean modifica, boolean eliminazione,
            boolean condivisione) {
        this.utente = utente;
        this.bacheca = bacheca;
        bacheca.aggiungiPermesso(this);
        this.modifica = modifica;
        this.eliminazione = eliminazione;
        this.condivisione = condivisione;
    }

    public boolean possessore(Utente utente_possessore) {
        return utente_possessore == utente;
    }

    public Bacheca getBacheca() {
        return bacheca;
    }
}