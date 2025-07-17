CREATE TYPE NOME_BACHECA AS ENUM ('Università', 'Lavoro', 'Tempo libero');
CREATE DOMAIN NOME_UTENTE AS VARCHAR(16) CHECK
    (value ~* '^[a-zA-Z0-9]{3,16}$');
CREATE DOMAIN DOM_PASSWORD AS VARCHAR(16) CHECK
    (value ~* '^[a-zA-Z0-9._?!/@*]{6,16}$');
