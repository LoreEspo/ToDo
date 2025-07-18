package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ToDo {
    private String titolo = "Titolo";
    private Date scadenza;
    private String linkAttivita;
    private String descrizione;
    private byte[] immagine;
    private String coloreSfondo;
    private boolean completato = false;
    private final List<Attivita> listaAttivita = new ArrayList<>();
    private Utente utente;
    private Bacheca bacheca;

    public ToDo(Utente utente, Bacheca bacheca) {
        this.utente = utente;
        this.bacheca = bacheca;
    }

    public String getColoreSfondo() {
        return coloreSfondo;
    }

    public void setColoreSfondo(String coloreSfondo) {
        this.coloreSfondo = coloreSfondo;
    }

    public boolean getCompletato() {
        if (!listaAttivita.isEmpty()) {
            for (Attivita attivita : listaAttivita) {
                if (!attivita.getCompletato()) {
                    return false;
                }
            }
            return true;
        }
        return completato;
    }

    public void setCompletato(boolean completato) { this.completato = completato; }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String nuovoTitolo) {
        titolo = nuovoTitolo;
    }

    public Date getScadenza() { return scadenza; }

    public void setScadenza(Date nuovaScadenza) {
        scadenza = nuovaScadenza;
    }

    public String getLinkAttivita() {
        return linkAttivita;
    }

    public void setLinkAttivita(String nuovoLink) {
        linkAttivita = nuovoLink;
    }

    public byte[] getImmagine() {
        return immagine;
    }

    public void setImmagine(byte[] nuovaImmagine) {
        immagine = nuovaImmagine;
        System.out.println(immagine);
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

    public Utente getUtente() {
        return utente;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Bacheca getBacheca() {
        return bacheca;
    }

    public void setBacheca(Bacheca bacheca) {
        this.bacheca = bacheca;
    }
}