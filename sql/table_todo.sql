CREATE TABLE TODO (
	idtodo INT PRIMARY KEY,
	titolo TEXT DEFAULT 'Titolo',
	scadenza DATE,
	linkattivita TEXT,
	descrizione TEXT DEFAULT '',
	immagine BYTEA,
    coloreSfondo TEXT,
    completato BOOLEAN DEFAULT false,
    ordine INT CHECK ( ordine >= 0 ),
    autore NOME_UTENTE NOT NULL,
    titoloBacheca NOME_BACHECA NOT NULL,
    FOREIGN KEY (autore, titoloBacheca) REFERENCES BACHECA(autore, titolo)
);