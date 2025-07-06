CREATE TABLE BACHECA_CONDIVISA (
    autore NOME_UTENTE NOT NULL,
    titolo NOME_BACHECA NOT NULL,
	utente NOME_UTENTE NOT NULL,
    FOREIGN KEY (autore, titolo) REFERENCES BACHECA(autore, titolo),
	FOREIGN KEY (utente) REFERENCES UTENTE(username),
    CHECK ( autore <> utente )
)