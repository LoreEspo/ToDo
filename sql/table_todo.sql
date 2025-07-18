CREATE TABLE TODO (
	idtodo INT PRIMARY KEY,
	titolo TEXT DEFAULT 'Titolo',
	scadenza DATE,
	linkattivita TEXT,
	descrizione TEXT DEFAULT '',
	immagine BYTEA,
    coloreSfondo TEXT,
    completato BOOLEAN,
    autore NOME_UTENTE NOT NULL,
    titoloBacheca NOME_BACHECA NOT NULL,
    FOREIGN KEY (autore, titoloBacheca) REFERENCES BACHECA(autore, titolo)
);