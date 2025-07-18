CREATE TABLE ATTIVITA (
    idAttivita INT PRIMARY KEY,
    titolo TEXT DEFAULT '',
    completato BOOLEAN DEFAULT false,
    idTodo INT,
    FOREIGN KEY (idTodo) REFERENCES TODO(idTodo)
)