package model;

import java.util.ArrayList;
import java.util.List;

public class ToDo {
    private String titolo;
    private String data;
    private String linkAttivita;
    private byte[] immagine;
    private String coloreSfondo;
    private boolean completato = false;
    private final List<Attivita> listaAttivita = new ArrayList<>();
    private PermessoToDo utente;


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

    public void aggiungiPermesso(PermessoToDo permesso) {
        utente = permesso;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String nuovoTitolo) {
        titolo = nuovoTitolo;
    }

    public String getData() {
        return data;
    }

    public void setData(String nuovoData) {
        data = nuovoData;
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