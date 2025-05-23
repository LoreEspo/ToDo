package gui;

import javax.swing.*;

public class Attivita {
    private JPanel panel;
    private JTextField titolo;
    private JCheckBox stato;
    private JButton cancellaButton;

    public JPanel getPanel() {
        return panel;
    }

    public Attivita setTitolo(String titolo) {
        this.titolo.setText(titolo);
        return this;
    }
}
