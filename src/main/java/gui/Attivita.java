package gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class Attivita {
    private final JPanel panel;
    private final JTextField titolo;
    private final JCheckBox stato;
    private final JButton cancellaButton;

    public Attivita() {
        panel = new JPanel();
        panel.setLayout(
                new BorderLayout()
        );
        titolo = new JTextField();
        panel.add(titolo, BorderLayout.CENTER);
        titolo.setBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED)
        );

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

    public boolean getStato() { return stato.isSelected(); }

    public JButton getCancellaButton() {
        return cancellaButton;
    }

    public Attivita setTitolo(String titolo) {
        this.titolo.setText(titolo);
        return this;
    }

    public void setColore(Color colore) {
        stato.setBackground(colore);
        titolo.setBackground(colore);
    }
}
