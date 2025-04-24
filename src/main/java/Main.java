public class Main {
    public static void main(String[] args) {
        // Testing
        Utente utente = new Utente("admin", "test123");

        PermessoBacheca bacheca = utente.CreaBacheca(Bacheca.NomeBacheca.UNIVERSITA, "Bacheca università");
        PermessoToDo todo = utente.CreaToDo();

        utente.ModificaToDo(todo, "Test", null, null);
        bacheca.GetBacheca().AggiungiToDo(todo.GetToDo());
    }
}
