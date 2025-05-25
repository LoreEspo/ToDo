package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;

public class ToDoChecklist extends ToDo {
    private JPanel panel;
    private JButton cancellaButton;
    private JLabel titolo;
    private JPanel panelTitolo;
    private JPanel containerAttivita;
    private JButton aggiungiAttivitaButton;

    public ToDoChecklist(Controller controller) {
        super(controller);

        containerAttivita.setLayout(
                new GridLayout(0, 1)
        );

        System.out.println(containerAttivita);

        aggiungiAttivita(new Attivita());
    }



    public void aggiungiAttivita(Attivita attivita) {
        containerAttivita.add(attivita.getPanel());
        containerAttivita.repaint();
        containerAttivita.revalidate();
    }
}
