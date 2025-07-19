package model;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Date;

/**
 * Un promemoria. Ha diversi campi opzionali e può contenere {@link Attivita} come
 * elementi di una checklist.
 */
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
    private boolean condiviso = false;

    /**
     * Istanzia un promemoria.
     *
     * @param autore        l'autore
     * @param titoloBacheca il titolo della bacheca che lo contiene
     */
    public ToDo(String autore, Bacheca.NomeBacheca titoloBacheca) {
        this.autore = autore;
        this.titoloBacheca = titoloBacheca;
    }

    /**
     * @return il colore sfondo
     */
    public String getColoreSfondo() {
        return coloreSfondo;
    }

    /**
     * @param coloreSfondo il colore sfondo da impostare
     */
    public void setColoreSfondo(String coloreSfondo) {
        this.coloreSfondo = coloreSfondo;
    }

    /**
     * @return lo stato. Se vi sono attività, sarà completo solo se lo sono tutte le attività
     */
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

    /**
     * @param completato stato da impostare. Inutile se vi sono attività
     */
    public void setCompletato(boolean completato) { this.completato = completato; }

    /**
     * @return il titolo
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * @param nuovoTitolo il nuovo titolo
     */
    public void setTitolo(String nuovoTitolo) {
        titolo = nuovoTitolo;
    }

    /**
     * @return la scadenza
     */
    public Date getScadenza() { return scadenza; }

    /**
     * @param nuovaScadenza la nuova scadenza
     */
    public void setScadenza(Date nuovaScadenza) {
        scadenza = nuovaScadenza;
    }

    /**
     * @return il link attivita
     */
    public String getLinkAttivita() {
        return linkAttivita;
    }

    /**
     * @param nuovoLink il nuovo link
     */
    public void setLinkAttivita(String nuovoLink) {
        linkAttivita = nuovoLink;
    }

    /**
     * @return l'immagine come byte array
     */
    public byte[] getImmagine() {
        return immagine;
    }

    /**
     * @param nuovaImmagine la nuova immagine come byte array
     */
    public void setImmagine(byte[] nuovaImmagine) {
        immagine = nuovaImmagine;
    }

    /**
     * Restituisce la lista di attività, una mappa che mappa l'id dell'attività
     * all'attività stessa.
     *
     * @return la lista di attivita
     */
    public Map<Integer, Attivita> getListaAttivita() {
        return listaAttivita;
    }

    /**
     * Aggiunge un'attivita.
     *
     * @param id       l'id dell'attività
     * @param attivita l'attivita
     */
    public void aggiungiAttivita(int id, Attivita attivita) {
        listaAttivita.put(id, attivita);
    }

    /**
     * Elimina un'attivita.
     *
     * @param id l'id dell'attività
     */
    public void eliminaAttivita(int id) {
        listaAttivita.remove(id);
    }

    /**
     * @return l'autore
     */
    public String getAutore() {
        return autore;
    }

    /**
     * @return la descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @param descrizione la descrizione da impostare
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @return il titolo della bacheca
     */
    public Bacheca.NomeBacheca getTitoloBacheca() {
        return titoloBacheca;
    }

    /**
     * @param titoloBacheca il titolo della bacheca in cui spostare il promemoria
     */
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
        map.put("condiviso", condiviso);

        return map;
    }

    /**
     * Da {@link #aMap()} a oggetto.
     *
     * @param map la mappa
     * @return l 'oggetto
     */
    public static ToDo daMap(Map<String, Object> map) {
        ToDo todo = new ToDo(
                (String) map.get("autore"), Bacheca.NomeBacheca.daString((String) map.get("titoloBacheca"))
        );
        todo.titolo = (String) map.get("titolo");
        todo.scadenza = (Date) map.get("scadenza");
        todo.linkAttivita = (String) map.get("linkAttivita");
        todo.descrizione = (String) map.get("descrizione");
        todo.immagine = (byte[]) map.get("immagine");
        todo.coloreSfondo = (String) map.get("coloreSfondo");
        Boolean condiviso = (Boolean) map.get("condiviso");
        if (condiviso != null) {
            todo.condiviso = condiviso;
        }

        return todo;
    }

    /**
     * @return se il promemoria è stato condiviso con l'utente
     */
    public boolean isCondiviso() {
        return condiviso;
    }
}