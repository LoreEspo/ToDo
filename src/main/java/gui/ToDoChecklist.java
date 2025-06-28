package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ToDoChecklist extends ToDo {

    private JPanel containerAttivita;
    private final ArrayList<Attivita> listaAttivita = new ArrayList<>();

    public ToDoChecklist(Controller controller) {
        super(controller);

        aggiungiAttivita();
    }

    protected void creaUI() {
       super.creaUI();

       containerAttivita = new JPanel();
       containerAttivita.setLayout(
               new GridLayout(0, 1)
       );
       getPanel().add(containerAttivita, 2);

       JButton aggiungiButton = new JButton("Aggiungi attività");
       aggiungiButton.addActionListener(_ -> {aggiungiAttivita();});
       containerAttivita.add(aggiungiButton);
    }

    public void aggiungiAttivita() {
        Attivita attivita = new Attivita();

        containerAttivita.add(attivita.getPanel());
        containerAttivita.repaint();
        containerAttivita.revalidate();
        listaAttivita.add(attivita);


        attivita.getCancellaButton().addActionListener(
                _ -> rimuoviAttivita(listaAttivita.indexOf(attivita))
        );
    }

    public void rimuoviAttivita(int indiceAttivita) {
        Attivita attivita = listaAttivita.get(indiceAttivita);
        containerAttivita.remove(attivita.getPanel());
        containerAttivita.repaint();
        containerAttivita.revalidate();
        listaAttivita.remove(indiceAttivita);
        controller.rimuoviAttivita(this.indice, indiceAttivita);
    }
}
