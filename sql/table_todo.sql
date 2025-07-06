CREATE TABLE TODO (
	idtodo INT PRIMARY KEY,
	titolo VARCHAR(30) DEFAULT '',
	scadenza DATE,
	linkattivita BPCHAR,
	descrizione VARCHAR(200) DEFAULT '',
	immagine BYTEA,
);