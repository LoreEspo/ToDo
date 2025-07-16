package model;

public class PermessoToDo {
    public boolean modifica;
    public boolean eliminazione;

    private final Utente utente;
    private final ToDo todo;

    public PermessoToDo(Utente utente, ToDo todo, boolean modifica, boolean eliminazione) {
        this.utente = utente;
        this.todo = todo;
        this.modifica = modifica;
        this.eliminazione = eliminazione;
    }

    public boolean possessore(Utente possessore) {
        return possessore == utente;
    }

    public ToDo getToDo() {
        return todo;
    }

    public boolean isModifica() { return modifica; }

    public boolean isEliminazione() { return modifica; }
}