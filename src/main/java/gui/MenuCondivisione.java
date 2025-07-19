package gui;

import controller.Controller;
import logger.ToDoLogger;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Dialog per condividere un promemoria
 */
public class MenuCondivisione extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JPanel panel;
    private JButton aggiungiButton;
    private JTextField inputNome;

    private final transient Controller controller;
    private final int id;

    private MenuCondivisione(int id) {
        this.controller = Controller.getInstance();
        this.id = id;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setMinimumSize(new Dimension(400, 400));

        buttonOK.addActionListener(_ -> dispose());

        panel.setLayout(new GridLayout(0, 1));

        Container parent = inputNome.getParent();
        panel.remove(parent);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(parent, BorderLayout.NORTH);
        panel.add(wrapper);

        aggiungiButton.addActionListener(_ -> aggiungiAllaLista());

        try {
            for (String username : controller.condivisiDi(id)) {
                aggiungiAllaLista(username);
            }
        } catch (SQLException e) {
            ToDoLogger.getInstance().logError(e);
            JOptionPane.showMessageDialog(
                    contentPane,
                    "Non è stato possibile caricare gli utenti destinatari del promemoria.",
                    "Share error", JOptionPane.ERROR_MESSAGE
            );
            dispose();
        }
    }


    /// Condividi ad un utente e aggiungilo alla lista
    private void aggiungiAllaLista() {
        String username = inputNome.getText();
        if (username.isEmpty())
            return;
        try {
            controller.condividi(id, username);
        } catch (SQLException e) {
            ToDoLogger.getInstance().logError(e);
            JOptionPane.showMessageDialog(
                    contentPane,
                    "Non è stato possibile condividere il promemoria. \nL'utente non esiste o hai inserito il tuo username.",
                    "Username error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        inputNome.setText("");

        aggiungiAllaLista(username);
    }

    /// Aggiungi un username alla lista e collega il tasto per revocare la condivisione
    private void aggiungiAllaLista(String username) {
        JPanel jpanel = new JPanel(new BorderLayout());
        JLabel nome = new JLabel(username);
        JButton cancellaButton = new JButton("🗑");
        jpanel.add(nome, BorderLayout.CENTER);
        jpanel.add(cancellaButton, BorderLayout.EAST);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(jpanel, BorderLayout.NORTH);

        panel.add(wrapper);
        panel.revalidate();
        panel.repaint();
        pack();

        cancellaButton.addActionListener(_ -> {
            try {
                controller.rimuoviCondivisione(id, username);
            } catch (SQLException e) {
                ToDoLogger.getInstance().logError(e);
                JOptionPane.showMessageDialog(
                        contentPane,
                        "Errore nella rimozione della condivisione.",
                        "Share error", JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            panel.remove(jpanel);
            panel.revalidate();
            panel.repaint();
            pack();
        });
    }

    /**
     * Crea il menu e mostralo
     *
     * @param id l'id del promemoria da condividere
     * @return il menu
     */
    public static MenuCondivisione create(int id) {
        MenuCondivisione dialog = new MenuCondivisione(id);
        dialog.pack();
        dialog.setVisible(true);
        return dialog;
    }
}
