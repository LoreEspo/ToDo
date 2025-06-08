CREATE TABLE BACHECA (
    idbacheca INT PRIMARY KEY,
    nome VARCHAR(50) DEFAULT 'Bacheca',
    titolo NOME_BACHECA,
    descrizione VARCHAR(150) DEFAULT '',
    autore VARCHAR(16) NOT NULL,
    FOREIGN KEY (autore) REFERENCES UTENTE(username)
)