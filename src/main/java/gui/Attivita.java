package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class Attivita {
    private final JPanel panel;
    private final JTextField titolo;
    private final JCheckBox stato;
    private final JButton cancellaButton;

    private final int indice;

    public Attivita(int indice) {
        Controller controller = Controller.getInstance();
        this.indice = indice;

        panel = new JPanel(new BorderLayout());
        titolo = new JTextField(controller.getTitoloAttivita(indice));
        panel.add(titolo, BorderLayout.CENTER);
        titolo.setBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED)
        );
        titolo.getDocument().addDocumentListener(
                new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        controller.setTitoloAttivita(indice, titolo.getText());
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        controller.setTitoloAttivita(indice, titolo.getText());
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        // Non necessario per i JTextField
                    }
                }
        );

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        panel.add(container, BorderLayout.EAST);

        cancellaButton = new JButton("🗑");
        container.add(cancellaButton, BorderLayout.CENTER);

        stato = new JCheckBox();
        stato.setSelected(controller.getCompletatoAttivita(indice));
        container.add(stato, BorderLayout.EAST);
        stato.setAlignmentX(Component.RIGHT_ALIGNMENT);
        stato.addActionListener(_ -> controller.setCompletatoAttivita(indice, stato.isSelected()));
    }

    public JPanel getPanel() {
        return panel;
    }

    public JCheckBox getCheckbox() { return stato; }

    public JButton getCancellaButton() {
        return cancellaButton;
    }

    public Attivita setTitolo(String titolo) {
        this.titolo.setText(titolo);
        return this;
    }

    public void setColore(Color colore) {
        stato.setBackground(colore);
        cancellaButton.setBackground(colore);
        titolo.setBackground(colore);
    }

    public int getIndice() {
        return indice;
    }
}
