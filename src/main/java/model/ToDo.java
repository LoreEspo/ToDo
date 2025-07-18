package model;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Date;

public class ToDo implements Mappabile {
    private String titolo = "Titolo";
    private Date scadenza;
    private String linkAttivita;
    private String descrizione;
    private byte[] immagine;
    private String coloreSfondo;
    private boolean completato = false;
    private final Map<Integer, Attivita> listaAttivita = new LinkedHashMap<>();
    private final String autore;
    private Bacheca.NomeBacheca titoloBacheca;

    public ToDo(String autore, Bacheca.NomeBacheca titoloBacheca) {
        this.autore = autore;
        this.titoloBacheca = titoloBacheca;
    }

    public String getColoreSfondo() {
        return coloreSfondo;
    }

    public void setColoreSfondo(String coloreSfondo) {
        this.coloreSfondo = coloreSfondo;
    }

    public boolean getCompletato() {
        if (!listaAttivita.isEmpty()) {
            for (Attivita attivita : listaAttivita.values()) {
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
    }

    public Map<Integer, Attivita> getListaAttivita() {
        return listaAttivita;
    }

    public void aggiungiAttivita(Integer indice, Attivita attivita) {
        listaAttivita.put(indice, attivita);
    }

    public void eliminaAttivita(int indice) {
        listaAttivita.remove(indice);
    }

    public String getAutore() {
        return autore;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Bacheca.NomeBacheca getTitoloBacheca() {
        return titoloBacheca;
    }

    public void setTitoloBacheca(Bacheca.NomeBacheca titoloBacheca) {
        this.titoloBacheca = titoloBacheca;
    }

    @Override
    public Map<String, Object> aMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("titolo", titolo);
        map.put("scadenza", scadenza);
        map.put("linkAttivita", linkAttivita);
        map.put("descrizione", descrizione);
        map.put("immagine", immagine);
        map.put("coloreSfondo", coloreSfondo);
        map.put("completato", completato);
        map.put("autore", autore);
        map.put("titoloBacheca", titoloBacheca.valore);

        return map;
    }

    public static ToDo daMap(Map<String, Object> map) {
        ToDo todo = new ToDo(
                (String) map.get("autore"), Bacheca.NomeBacheca.daString((String) map.get("titoloBacheca"))
        );
        todo.setTitolo((String) map.get("titolo"));
        todo.setScadenza((Date) map.get("scadenza"));
        todo.setLinkAttivita((String) map.get("linkAttivita"));
        todo.setDescrizione((String) map.get("descrizione"));
        todo.setImmagine((byte[]) map.get("immagine"));
        todo.setColoreSfondo((String) map.get("coloreSfondo"));

        return todo;
    }
}