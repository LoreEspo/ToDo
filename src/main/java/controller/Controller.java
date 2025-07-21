package controller;

import dao.AttivitaDAO;
import dao.BachecaDAO;
import dao.ToDoDAO;
import dao.UtenteDAO;
import model.*;
import postgredao.AttivitaPostgreDAO;
import postgredao.BachecaPostgreDAO;
import postgredao.ToDoPostgreDAO;
import postgredao.UtentePostgreDAO;

import java.sql.SQLException;
import java.util.*;


/// Singleton Controller.
/// Permette la comunicazione fra model e gui. Immagazzina informazioni utili a runtime
/// e gestisce le query al database.
public class Controller {
    /// L'istanza del singleton
    private static Controller instance = null;

    /// La bacheca aperta
    private Bacheca bachecaAperta = null;
    /// L'utente loggato
    private Utente utente = null;
    /// I promemoria con modifiche effettuate
    private final List<Integer> todoModificati = new ArrayList<>();
    /// Le attività con modifiche effettuate
    private final List<Integer> attivitaModificate = new ArrayList<>();

    /**
     * @return l'istanza del singleton.
     */
    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    /**
     * Effettua l'accesso, passando per il database.
     *
     * @param username l'username
     * @param password la password
     * @return true se il login ha avuto successo
     * @throws SQLException eventuali errori durante la query
     */
    public boolean login(String username, String password) throws SQLException {
        UtenteDAO udao = new UtentePostgreDAO();

        if (!udao.login(username, password)) {
            return false;
        }

        utente = new Utente(username, password);
        return true;
    }

    /**
     * Come {@link #login(String, String)}, ma effettua una registrazione.
     *
     * @param username l'username
     * @param password la password
     * @return true se la registrazione ha avuto successo
     * @throws SQLException eventuali errori durante la query
     */
    public boolean registrazione(String username, String password) throws SQLException {
        UtenteDAO udao = new UtentePostgreDAO();

        if (!udao.registrazione(username, password)) {
            return false;
        }

        utente = new Utente(username, password);
        return true;
    }

    /**
     * Effettua il logout.
     */
    public void logout() {
        utente = null;
    }

    /**
     * @return true se l'utente è loggato
     */
    public boolean isLogged() { return utente != null; }

    /**
     * @return il nome utente dell'utente loggato
     */
    public String getLoggedUsername() {
        if (isLogged()) {
            return utente.getUsername();
        }
        return "";
    }

    /**
     * @return true se non c'è bacheca aperta
     */
    public boolean isBachecaChiusa() {
        return bachecaAperta == null;
    }

    /**
     * @return true se sono state effettuate modifiche non salvate a dei promemoria/attività
     */
    public boolean modificheEffettuate() {
        return !todoModificati.isEmpty() || !attivitaModificate.isEmpty();
    }

    // Funzioni bacheche

    /**
     * Apre una bacheca.
     * Richiede anche tutti i promemoria e attivita annesse appartenenti a
     * quella bacheca (Condivisi con l'utente e non).
     *
     * @param indice indice della bacheca
     * @throws SQLException errori durante la richiesta di promemoria o attività
     */
    public void apriBacheca(int indice) throws SQLException {
        ToDoDAO todoDao = new ToDoPostgreDAO();
        AttivitaDAO attivitaDao = new AttivitaPostgreDAO();

        bachecaAperta = utente.getBacheche().get(indice);

        // Converti i promemoria da HashMap a classe promemoria
        Map<Integer, Map<String, Object>> hashmap = todoDao.todoBacheca(utente.getUsername(), bachecaAperta.getTitolo().valore);
        Map<Integer, ToDo> toDoMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Map<String, Object>> entry : hashmap.entrySet()) {
            toDoMap.put(entry.getKey(), ToDo.daMap(entry.getValue()));
        }
        utente.setTodoMap(toDoMap);
        // Converti le attività da HashMap a classe attività
        for (Map.Entry<Integer, ToDo> entryTodo : toDoMap.entrySet()) {
            for (Map.Entry<Integer, Map<String, Object>> entryAttivita : attivitaDao.listaTodo(entryTodo.getKey()).entrySet()) {
                entryTodo.getValue().aggiungiAttivita(entryAttivita.getKey(), Attivita.daMap(entryAttivita.getValue()));
            }
        }

        todoModificati.clear();
        attivitaModificate.clear();
    }

    /**
     * Chiude la bacheca aperta.
     */
    public void chiudiBacheca() {
        if (!isLogged()) return;
        bachecaAperta = null;
        utente.getTodoMap().clear();
    }

    /**
     * Richiede le bacheche dell'utente dal database
     *
     * @return il numero di bacheche
     * @throws SQLException errori durante la query
     */
    public int richiediBacheche() throws SQLException {
        if (!isLogged()) return 0;

        BachecaDAO bachecheDao = new BachecaPostgreDAO();

        // Converti le bacheche da mappe a oggetto
        List<Map<String, Object>> bacheche = bachecheDao.bachecheDisponibili(utente.getUsername());
        List<Bacheca> lista = new ArrayList<>();
        for (Map<String, Object> map : bacheche) {
            lista.add(Bacheca.daMap(map));
        }
        utente.setBacheche(lista);

        return utente.getBacheche().size();
    }

    /**
     * @param id l'indice della bacheca
     * @return l'username dell'autore della bacheca
     */
    public String getAutoreBacheca(int id) {
        Bacheca bacheca = utente.getBacheche().get(id);
        return bacheca.getAutore();
    }

    /**
     * @return l'username dell'autore della bacheca aperta
     */
    public String getAutoreBacheca() {
        if (isBachecaChiusa()) {
            return "";
        }
        return bachecaAperta.getAutore();
    }

    /**
     * @param id l'indice della bacheca
     * @return il titolo della bacheca
     */
    public String getTitoloBacheca(int id) {
        Bacheca bacheca = utente.getBacheche().get(id);
        return bacheca.getTitolo().valore;
    }

    /**
     * @return il titolo della bacheca aperta
     */
    public String getTitoloBacheca() {
        if (isBachecaChiusa()) {
            return null;
        }
        return bachecaAperta.getTitolo().valore;
    }

    /**
     * @param id l'indice della bacheca
     * @return la descrizione della bacheca
     */
    public String getDescrizioneBacheca(int id) {
        Bacheca bacheca = utente.getBacheche().get(id);
        return bacheca.getDescrizione();
    }

    /**
     * Imposta la descrizione della bacheca, eseguendo
     * la query al database
     *
     * @param id      l'indice della bacheca
     * @param descrizione la descrizione da impostare
     * @throws SQLException errori durante la query
     */
    public void setDescrizioneBacheca(int id, String descrizione) throws SQLException {
        Bacheca bacheca = utente.getBacheche().get(id);

        BachecaDAO dao = new BachecaPostgreDAO();
        dao.setDescrizione(bacheca.getAutore(), bacheca.getTitolo().valore, descrizione);

        bacheca.setDescrizione(descrizione);
    }

    // Funzioni promemoria

    /**
     * Aggiunge il promemoria alla lista dei promemoria modificati,
     * solo se non è già presente.
     */
    private void setToDoModificato(int id) {
        if (todoModificati.contains(id)) {
            return;
        }
        todoModificati.add(id);
    }

    /**
     * Salva i promemoria modificati.
     *
     * @throws SQLException errori durante la query
     */
    public void salvaToDo() throws SQLException {
        ToDoDAO dao = new ToDoPostgreDAO();
        for (int id: todoModificati) {
            dao.aggiornaTodo(id, utente.getToDo(id).aMap());
        }
        todoModificati.clear();
    }

    /**
     * Aggiunge un promemoria al database
     * nella bacheca aperta.
     *
     * @return l'id del promemoria aggiunto
     * @throws SQLException errori durante la query
     */
    public int aggiungiToDo() throws SQLException {

        if (isBachecaChiusa()) {
            return -1;
        }
        ToDoDAO dao = new ToDoPostgreDAO();

        ToDo todo = new ToDo(utente.getUsername(), bachecaAperta.getTitolo());
        int id = dao.aggiungi(todo.aMap());
        utente.getTodoMap().put(id, todo);

        return id;
    }

    /**
     * Rimuove il promemoria dal database.
     *
     * @param id id del promemoria
     * @throws SQLException errori durante la query
     */
    public void rimuoviToDo(int id) throws SQLException {
        if (isBachecaChiusa()) {
            return;
        }
        todoModificati.remove(Integer.valueOf(id)); // Integer o altrimenti rimuove per indice d'array

        ToDoDAO dao = new ToDoPostgreDAO();

        dao.rimuovi(id);

        utente.eliminaToDo(id);
    }

    /**
     * @return set di id dei promemoria in memoria,
     * aggiunti o richiesti dal database con {@link #apriBacheca(int)}
     */
    public Set<Integer> listaToDo() { return utente.getTodoMap().keySet(); }

    /**
     * @param id id del promemoria
     * @return il titolo del promemoria
     */
    public String getTitoloToDo(int id) {
        return utente.getToDo(id).getTitolo();
    }

    /**
     * Imposta il titolo del promemoria
     * e lo aggiunge ai modificati.
     *
     * @param id id del promemoria
     * @param titolo il titolo del promemoria
     */
    public void setTitoloToDo(int id, String titolo) {
        utente.getToDo(id).setTitolo(titolo);
        setToDoModificato(id);
    }

    /**
     * @param id id del promemoria
     * @return la descrizione del promemoria
     */
    public String getDescrizioneToDo(int id) {
        return utente.getToDo(id).getDescrizione();
    }

    /**
     * Imposta la descrizione del promemoria
     * e lo aggiunge ai modificati.
     *
     * @param id id del promemoria
     * @param descrizione la descrizione del promemoria
     */
    public void setDescrizioneToDo(int id, String descrizione) {
        utente.getToDo(id).setDescrizione(descrizione);
        setToDoModificato(id);
    }

    /**
     * @param id id del promemoria
     * @return lo stato del promemoria (Completato o meno)
     */
    public boolean getCompletatoToDo(int id) {
        return utente.getToDo(id).getCompletato();
    }

    /**
     * Imposta lo stato del promemoria
     * e lo aggiunge ai modificati.
     *
     * @param id id del promemoria
     * @param stato lo stato del promemoria (Completato o meno)
     */
    public void setCompletatoToDo(int id, boolean stato) {
        utente.getToDo(id).setCompletato(stato);
        setToDoModificato(id);
    }

    /**
     * @param id id del promemoria
     * @return l'immagine del promemoria come array di byte
     */
    public byte[] getImmagineToDo(int id) {
        return utente.getToDo(id).getImmagine();
    }

    /**
     * Chiama {@link #setImmagineToDo(int, byte[], boolean)} con
     * {@code aggiorna = true} di default.
     *
     * @param id id del promemoria
     * @param immagine l'immagine del promemoria come array di byte
     */
    public void setImmagineToDo(int id, byte[] immagine) {
        setImmagineToDo(id, immagine, true);
    }

    /**
     * Imposta l'immagine del promemoria
     * come array di byte
     * e lo aggiunge ai modificati.
     *
     * @param id id del promemoria
     * @param immagine l'immagine del promemoria come array di byte
     * @param aggiorna se aggiungere il promemoria ai promemoria modificati
     *                 (Per evitare di impostarlo come modificato quando si carica l'immagine dal database)
     */
    public void setImmagineToDo(int id, byte[] immagine, boolean aggiorna) {
        utente.getToDo(id).setImmagine(immagine);
        if (aggiorna)
            setToDoModificato(id);
    }

    /**
     * @param id id del promemoria
     * @return il link all'attività del promemoria
     */
    public String getLinkToDo(int id) {
        return utente.getToDo(id).getLinkAttivita();
    }

    /**
     * Imposta il link all'attività del promemoria
     * e lo aggiunge ai modificati.
     *
     * @param id id del promemoria
     * @param linkAttivita il link all'attività del promemoria
     */
    public void setLinkToDo(int id, String linkAttivita) {
        utente.getToDo(id).setLinkAttivita(linkAttivita);
        setToDoModificato(id);
    }

    /**
     * @param id id del promemoria
     * @return il colore di sfondo del promemoria come stringa esadecimale
     */
    public String getColoreToDo(int id) {
        return utente.getToDo(id).getColoreSfondo();
    }

    /**
     * Imposta il colore di sfondo del promemoria
     * e lo aggiunge ai modificati.
     *
     * @param id id del promemoria
     * @param colore il colore di sfondo del promemoria
     */
    public void setColoreToDo(int id, String colore) {
        utente.getToDo(id).setColoreSfondo(colore);
        setToDoModificato(id);
    }

    /**
     * @param id id del promemoria
     * @return la data di scadenza del promemoria
     */
    public Date getScadenzaToDo(int id) {
        return utente.getToDo(id).getScadenza();
    }

    /**
     * Imposta la data di scadenza del promemoria
     * e lo aggiunge ai modificati.
     *
     * @param id id del promemoria
     * @param scadenza la data di scadenza del promemoria
     */
    public void setScadenzaToDo(int id, Date scadenza) {
        utente.getToDo(id).setScadenza(scadenza);
        setToDoModificato(id);
    }

    /**
     * @param id id del promemoria
     * @return il titolo della bacheca del promemoria
     */
    public String getTitoloBachecaToDo(int id) {
        return utente.getToDo(id).getTitoloBacheca().valore;
    }

    /**
     * Imposta la bacheca al quale appartiene il promemoria
     * (Da {@link String} a {@link Bacheca.NomeBacheca}
     * e lo aggiunge ai modificati.
     *
     * @param id id del promemoria
     * @param titolo il titolo della bacheca del promemoria
     */
    public void setTitoloBachecaToDo(int id, String titolo) {
        utente.getToDo(id).setTitoloBacheca(Bacheca.NomeBacheca.daString(titolo));
        setToDoModificato(id);
    }

    /**
     * @param id id del promemoria
     * @return l'autore del promemoria
     */
    public String getAutoreToDo(int id) {
        return utente.getToDo(id).getAutore();
    }

    /**
     * @param id id del promemoria
     * @return {@code true} se il promemoria e stato condiviso con l'utente
     */
    public boolean isToDoCondiviso(int id) {
        return utente.getToDo(id).isCondiviso();
    }

    /**
     * Aggiorna l'ordine dei promemoria sul database
     * secondo la mappa data come parametro.
     * Wrapper per {@link ToDoDAO#aggiornaOrdine(Map)}.
     *
     * @param mappaOrdine la mappa dell'ordine. La chiave è l'ordine e il valore è l'id del promemoria.
     * @throws SQLException errori durante la query
     */
    public void aggiornaOrdineToDo(Map<Integer, Integer> mappaOrdine) throws SQLException {
        ToDoDAO dao = new ToDoPostgreDAO();
        dao.aggiornaOrdine(mappaOrdine);
    }

    /**
     * Condivide un promemoria con un utente.
     * Controlla che il nome utente non sia
     * lo stesso dell'utente loggato.
     *
     * @param id       l'id del promemoria
     * @param username l'utente destinatario
     * @throws SQLException errori durante la query o se il nome utente è uguale a quello loggato
     */
    public void condividi(int id, String username) throws SQLException {
        if (username.equals(utente.getUsername())) {
            throw new SQLException("Non si può condividere un todo con se stessi.");
        }
        ToDoDAO dao = new ToDoPostgreDAO();
        dao.condividi(id, username);
    }

    /**
     * Rimuove la condivisione di un promemoria con un utente.
     *
     * @param id       l'id del promemoria
     * @param username l'utente al quale revocare la condivisione
     * @throws SQLException errori durante la query
     */
    public void rimuoviCondivisione(int id, String username) throws SQLException {
        ToDoDAO dao = new ToDoPostgreDAO();
        dao.rimuoviCondivisione(id, username);
    }

    /**
     * Restituisce la lista di utenti
     * al quale è stato condiviso il promemoria.
     * Wrapper per {@link ToDoDAO#condivisiDi(int)}.
     *
     * @param id id del promemoria
     * @return la lista di utenti
     * @throws SQLException errori durante la query
     */
    public List<String> condivisiDi(int id) throws SQLException {
        ToDoDAO dao = new ToDoPostgreDAO();
        return dao.condivisiDi(id);
    }

    /**
     * Sposta un promemoria in un'altra bacheca.
     *
     * @param id      id del promemoria
     * @param bacheca la bacheca di destinazione
     * @throws SQLException errori durante la query
     */
    public void spostaToDo(int id, String bacheca) throws SQLException {
        ToDoDAO dao = new ToDoPostgreDAO();
        dao.sposta(id, bacheca);
        setTitoloBachecaToDo(id, bacheca);
    }

    /**
     * Ricerca un promemoria che includa il contenuto
     * passato in input.
     *
     * @param contenuto     il contenuto da ricercare
     * @param inTitolo      se cercare il contenuto nel titolo
     * @param inDescrizione se cercare il contenuto nella descrizione
     * @return la lista di id dei promemoria
     * @throws SQLException errori durante la query
     */
    public List<Map<String, String>> ricerca(String contenuto, boolean inTitolo, boolean inDescrizione) throws SQLException {
        ToDoDAO dao = new ToDoPostgreDAO();
        return dao.ricerca(utente.getUsername(), contenuto, inTitolo, inDescrizione);
    }

    /**
     * Restituisce la lista di promemoria
     * che scadono oltre la data data
     * in input.
     * Wrapper per {@link ToDoDAO#inScadenza(String, Date)}
     *
     * @param scadenza la scadenza
     * @return the list
     * @throws SQLException errori durante la ricerca
     */
    public List<Map<String, String>> inScadenza(Date scadenza) throws SQLException {
        ToDoDAO dao = new ToDoPostgreDAO();
        return dao.inScadenza(utente.getUsername(), scadenza);
    }

    // Funzioni attivita

    /**
     * Aggiungi attività ad un promemoria.
     *
     * @param idTodo id del promemmoria
     * @return l'id dell'attività
     * @throws SQLException errori durante la query
     */
    public int aggiungiAttivita(int idTodo) throws SQLException {
        if (isBachecaChiusa()) {
            return -1;
        }
        AttivitaDAO dao = new AttivitaPostgreDAO();

        ToDo todo = utente.getTodoMap().get(idTodo);
        Attivita attivita = new Attivita();
        int id = dao.aggiungi(idTodo, attivita.aMap());
        todo.aggiungiAttivita(id, attivita);
        return id;
    }

    /**
     * Rimuovi attività dal promemoria.
     *
     * @param idTodo     id del promemoria
     * @param idAttivita id dell'attività
     * @throws SQLException errori durante la query
     */
    public void rimuoviAttivita(int idTodo, int idAttivita) throws SQLException {
        if (isBachecaChiusa()) {
            return;
        }
        AttivitaDAO dao = new AttivitaPostgreDAO();
        attivitaModificate.remove(Integer.valueOf(idAttivita)); // Integer o controlla per indice d'array

        dao.rimuovi(idAttivita);

        ToDo todo = utente.getTodoMap().get(idTodo);

        todo.eliminaAttivita(idAttivita);

    }

    /**
     * @param id id dell'attività
     * @return il titolo dell'attività
     */
    public String getTitoloAttivita(int id) {
        return utente.getAttivita(id).getTitolo();
    }

    /**
     * Imposta il titolo dell'attività
     * aggiungendola alle attività modificate.
     *
     * @param id     id dell'attività
     * @param titolo il titolo dell'attività
     */
    public void setTitoloAttivita(int id, String titolo) {
        utente.getAttivita(id).setTitolo(titolo);
        if (attivitaModificate.contains(id)) {
            return;
        }
        attivitaModificate.add(id);
    }

    /**
     * @param id id dell'attività
     * @return lo stato dell'attività (Completato o meno)
     */
    public boolean getCompletatoAttivita(int id) {
        return utente.getAttivita(id).getCompletato();
    }

    /**
     * Imposta lo stato dell'attività
     * aggiungendola alle attività modificate.
     *
     * @param id     id dell'attività
     * @param completato lo stato dell'attività (Completato o meno)
     */
    public void setCompletatoAttivita(int id, boolean completato) {
        utente.getAttivita(id).setCompletato(completato);
        if (attivitaModificate.contains(id)) {
            return;
        }
        attivitaModificate.add(id);
    }

    /**
     * Salva le attività modificate sul database.
     *
     * @throws SQLException errori durante la query
     */
    public void salvaAttivita() throws SQLException {
        AttivitaDAO dao = new AttivitaPostgreDAO();
        for (int id : attivitaModificate) {
            dao.aggiorna(id, utente.getAttivita(id).aMap());
        }
        attivitaModificate.clear();
    }

    /**
     * @param id id del promemoria
     * @return lista di attività del promemoria
     */
    public Set<Integer> attivitaToDo(int id) {
        return utente.getToDo(id).getListaAttivita().keySet();
    }
}
