package model;

import java.util.ArrayList;

public class Utente {
    private String username;
    private String password;

    private ArrayList<PermessoBacheca> bacheche = new ArrayList<PermessoBacheca>();
    private ArrayList<PermessoToDo> todo = new ArrayList<PermessoToDo>();

    public Utente(String username_utente, String password_utente) {
        username = username_utente;
        password = password_utente;
    }

    public boolean login(String username, String password) {
        if (this.username == username && this.password == password) {
            return true;
        }
        return false;
    }

    public PermessoBacheca CreaBacheca(Bacheca.NomeBacheca titolo, String descrizione) {
        Bacheca bacheca = new Bacheca(titolo, descrizione);
        PermessoBacheca permesso = new PermessoBacheca(this, bacheca, true, true, true);

        bacheche.add(permesso);

        return permesso;
    }

    public void ModificaBacheca(PermessoBacheca permesso, Bacheca.NomeBacheca nuovo_titolo, String nuova_descrizione) {
        if (!permesso.Possessore(this) || !permesso.modifica) {
            return;
        }

        permesso.GetBacheca().SetTitolo(nuovo_titolo);
        permesso.GetBacheca().SetDescrizione(nuova_descrizione);
    }

    public void EliminaBacheca(PermessoBacheca permesso) {
        if (!permesso.Possessore(this) || !permesso.eliminazione) {
            return;
        }

        bacheche.remove(permesso);
    }

    public PermessoToDo CreaToDo() {
        ToDo nuovo_todo = new ToDo();
        PermessoToDo permesso = new PermessoToDo(this, nuovo_todo, true, true);
        nuovo_todo.AggiungiPermesso(permesso);
        this.todo.add(permesso);
        return permesso;
    }

    public void ModificaToDo(PermessoToDo permesso, String titolo, String data, String link_attivita) {
        if (!permesso.Possessore(this) || !permesso.modifica) {
            return;
        }

        permesso.GetToDo().SetTitolo(titolo);
        permesso.GetToDo().SetData(data);
        permesso.GetToDo().SetLinkAttivita(link_attivita);
    }

    public void EliminaToDo(PermessoToDo permesso) {
        if (!permesso.Possessore(this) || !permesso.eliminazione) {
            return;
        }

        todo.remove(permesso);
    }

    public void SpostaToDo(PermessoToDo permessoTodo, PermessoBacheca permessoDa, PermessoBacheca permessoA) {
        if (!permessoTodo.Possessore(this) || !permessoTodo.modifica) {
            return;
        }

        if (!permessoDa.Possessore(this) || !permessoDa.modifica) {
            return;
        }

        if (!permessoA.Possessore(this) || !permessoA.modifica) {
            return;
        }

        permessoDa.GetBacheca().RimuoviToDo(permessoTodo.GetToDo());
        permessoA.GetBacheca().AggiungiToDo(permessoTodo.GetToDo());
    }
}