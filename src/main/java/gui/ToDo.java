package gui;

import javax.swing.*;

public class ToDo {
    private JPanel panel;
    private JCheckBox stato;
    private JButton cancellaButton;
    private JTextField titolo;
    private JPanel innerPanel;

    public JPanel getPanel() {
        return panel;
    }

    public void setTitolo(String titolo) {
        this.titolo.setText(titolo);
    }

}
