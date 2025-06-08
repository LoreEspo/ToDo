package gui;

import javax.swing.*;
import java.awt.*;

public class Attivita {
    private JPanel panel;
    private JTextField titolo;
    private JCheckBox stato;
    private JButton cancellaButton;


    public JPanel getPanel() {
        return panel;
    }

    public JButton getCancellaButton() {
        return cancellaButton;
    }

    public Attivita setTitolo(String titolo) {
        this.titolo.setText(titolo);
        return this;
    }
}
