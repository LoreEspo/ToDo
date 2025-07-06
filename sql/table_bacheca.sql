CREATE TABLE BACHECA (
    autore VARCHAR(16) NOT NULL,
    titolo NOME_BACHECA,
    descrizione VARCHAR(150) DEFAULT '',
    aperta BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (autore, titolo),
    FOREIGN KEY (autore) REFERENCES UTENTE(username)
)