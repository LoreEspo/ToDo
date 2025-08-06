CREATE OR REPLACE FUNCTION fun_todo_cancellato() RETURNS TRIGGER AS $$
BEGIN
	UPDATE TODO
	SET ordine = ordine - 1
	WHERE ordine > OLD.ordine AND titoloBacheca = OLD.titoloBacheca AND autore = OLD.autore;

	DELETE FROM CONDIVISI WHERE idTodo = OLD.idTodo;
	RETURN OLD;
END $$ LANGUAGE plpgsql;

CREATE TRIGGER todo_cancellato
    BEFORE DELETE ON TODO
    FOR EACH ROW
    EXECUTE FUNCTION fun_todo_cancellato();
