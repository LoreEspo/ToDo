package gui;

import controller.Controller;
import logger.ToDoLogger;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MenuCondivisione extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JPanel panel;
    private JButton aggiungiButton;
    private JTextField inputNome;

    private Controller controller;
    private final int indice;

    public MenuCondivisione(int indice) {
        this.controller = Controller.getInstance();
        this.indice = indice;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setMinimumSize(new Dimension(400, 400));

        buttonOK.addActionListener(e -> dispose());

        panel.setLayout(new GridLayout(0, 1));

        Container parent = inputNome.getParent();
        panel.remove(parent);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(parent, BorderLayout.NORTH);
        panel.add(wrapper);

        aggiungiButton.addActionListener(_ -> aggiungiAllaLista());

        try {
            for (String username : controller.condivisiDi(indice)) {
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

    private void aggiungiAllaLista() {
        String username = inputNome.getText();
        if (username.isEmpty())
            return;
        try {
            controller.condividi(indice, username);
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
                controller.rimuoviCondivisione(indice, username);
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

    public static MenuCondivisione create(int indice) {
        MenuCondivisione dialog = new MenuCondivisione(indice);
        dialog.pack();
        dialog.setVisible(true);
        return dialog;
    }
}
