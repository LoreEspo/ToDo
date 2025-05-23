package gui;

import javax.swing.*;
import java.awt.*;

public class ToDoChecklist {
    private JPanel panel;
    private JButton cancellaButton;
    private JLabel titolo;
    private JPanel panelTitolo;
    private JPanel containerAttivita;

    public ToDoChecklist() {
        containerAttivita.setLayout(
                new GridLayout(0, 1)
        );
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setTitolo(String titolo) {
        this.titolo.setText(titolo);
    }

    public void aggiungiAttivita(Attivita attivita) {
        containerAttivita.add(attivita.getPanel());
    }
}
