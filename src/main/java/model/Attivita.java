package model;

public class Attivita
{
    private String titolo; 
    private boolean completato = false;

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public boolean getCompletato()
    {
        return completato;
    }

    public void setCompletato(boolean completato) {
        this.completato = completato;
    }
}