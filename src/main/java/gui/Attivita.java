package gui;

import javax.swing.*;
import java.awt.*;

public class Attivita {
    private JPanel panel;
    private JTextField titolo;
    private JCheckBox stato;
    private JButton cancellaButton;

    public Attivita() {
        panel = new JPanel();
        panel.setLayout(
                new BorderLayout()
        );
        titolo = new JTextField();
        panel.add(titolo, BorderLayout.CENTER);

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        panel.add(container, BorderLayout.EAST);

        cancellaButton = new JButton("🗑");
        container.add(cancellaButton, BorderLayout.CENTER);

        stato = new JCheckBox();
        container.add(stato, BorderLayout.EAST);
        stato.setAlignmentX(Component.RIGHT_ALIGNMENT);
    }

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
