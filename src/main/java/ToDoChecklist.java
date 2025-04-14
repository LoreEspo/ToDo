import java.util.ArrayList;

public class ToDoChecklist extends ToDo 
{

    ArrayList<Attivita> lista = new ArrayList<Attivita>();


    public boolean Completato() 
    {
        for (Attivita attivita : lista) 
        {
            if (!attivita.Completato()) 
            {
                return false;
            }
        }
        return true;
    }

}