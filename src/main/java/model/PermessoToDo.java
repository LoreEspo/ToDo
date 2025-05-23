package model;

public class PermessoToDo {
    public boolean modifica;
    public boolean eliminazione;

    private Utente utente;
    private ToDo todo;

    public PermessoToDo(Utente utente, ToDo todo, boolean modifica, boolean eliminazione) {
        this.utente = utente;
        this.todo = todo;
        this.modifica = modifica;
        this.eliminazione = eliminazione;
    }

    public boolean Possessore(Utente possessore) {
        return possessore == utente;
    }

    public ToDo GetToDo() {
        return todo;
    }
}