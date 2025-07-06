CREATE OR REPLACE FUNCTION fun_creaBachecheNuovoUtente() RETURNS TRIGGER AS $$
BEGIN
	INSERT INTO BACHECA
	VALUES (NEW.username, 'Università', '', false);
	INSERT INTO BACHECA
	VALUES (NEW.username, 'Lavoro', '', false);
	INSERT INTO BACHECA
	VALUES (NEW.username, 'Tempo libero', '', false);
	RETURN NEW;
END $$ LANGUAGE PLPGSQL;

CREATE TRIGGER creaBachecheNuovoUtente
AFTER INSERT ON UTENTE
FOR EACH ROW
EXECUTE FUNCTION fun_creaBachecheNuovoUtente();