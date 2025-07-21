package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * Gui di una attivita.
 */
public class Attivita {
    private final JPanel panel;
    private final JTextField titolo;
    private final JCheckBox stato;
    private final JButton cancellaButton;

    private final int id;

    /**
     * Instanzia una nuova attività
     *
     * @param id the id
     */
    public Attivita(int id) {
        Controller controller = Controller.getInstance();
        this.id = id;

        // Crea i vari componenti e li dispone correttamente
        panel = new JPanel(new BorderLayout());
        titolo = new JTextField(
                controller.getTitoloAttivita(id)
        );
        panel.add(titolo, BorderLayout.CENTER);
        titolo.setBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED)
        );
        titolo.getDocument().addDocumentListener(
                new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        controller.setTitoloAttivita(id, titolo.getText());
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        controller.setTitoloAttivita(id, titolo.getText());
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
        stato.setSelected(controller.getCompletatoAttivita(id));
        container.add(stato, BorderLayout.EAST);
        stato.setAlignmentX(Component.RIGHT_ALIGNMENT);
        stato.addActionListener(_ -> controller.setCompletatoAttivita(id, stato.isSelected()));
    }

    /**
     * @return il JPanel dell'attività
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * @return la JCheckBox dello stato dell'attività
     */
    public JCheckBox getCheckbox() { return stato; }

    /**
     * @return il JButton per cancellare l'attività
     */
    public JButton getCancellaButton() {
        return cancellaButton;
    }

    /**
     * Disabilita l'attività, rendendola read-only.
     */
    public void disabilita() {
        cancellaButton.setEnabled(false);
        panel.remove(cancellaButton);
        titolo.setEnabled(false);
        stato.setEnabled(false);
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Imposta il colore della gui.
     *
     * @param colore il colore da impostare
     */
    public void setColore(Color colore) {
        stato.setBackground(colore);
        cancellaButton.setBackground(colore);
        titolo.setBackground(colore);
    }

    /**
     * @return l'id legato alla gui
     */
    public int getId() {
        return id;
    }
}
