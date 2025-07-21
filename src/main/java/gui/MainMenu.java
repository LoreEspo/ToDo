package gui;

import controller.Controller;
import logger.ToDoLogger;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Date;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * Gui del menu principale.
 */
public class MainMenu {
    private JPanel panel;
    private JPanel topBar;
    private JPanel listaBacheche;
    private JButton logoutButton;
    private JLabel usernameLabel;
    private JButton ricercaButton;
    private JButton scadenzaButton;
    private final Controller controller;
    private JFrame frame;

    private int numeroBacheche;

    private MainMenu() {
        this.controller = Controller.getInstance();
        listaBacheche.setLayout(new BoxLayout(listaBacheche, BoxLayout.Y_AXIS));

        ricercaButton.addActionListener(_ -> {
            MenuRicerca menu = MenuRicerca.create();
            if (menu.getBachecaSelezionata().isEmpty())
                return;

            for (int i = 0; i < numeroBacheche; i++) {
                if (controller.getTitoloBacheca(i).equals(menu.getBachecaSelezionata())) {
                    apriBacheca(i);
                    break;
                }
            }
        });
        scadenzaButton.addActionListener(_ -> mostraInScadenza());
        logoutButton.addActionListener(_ -> logout());
        logoutButton.setVisible(false);

        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 30));

        usernameLabel.setFont(
                usernameLabel.getFont().deriveFont(16.0f)
        );
    }

    /**
     * Punto d'accesso dell'applicazione
     *
     * @param args gli argomenti da terminale
     */
    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();

        JFrame frame = new JFrame("ToDo app");
        mainMenu.frame = frame;
        frame.setContentPane(mainMenu.panel);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setMinimumSize(new Dimension(200, 200));
        frame.setSize(800, 600);

        mainMenu.login();

    }

    /// Apri il dialog per l'accesso e sincronizza le bacheche.
    private void login() {
        LoginDialog.create();
        if (!controller.isLogged()) {
            System.exit(0);
            return;
        }
        usernameLabel.setText(controller.getLoggedUsername());
        logoutButton.setVisible(true);
        sincronizzaBacheche();
    }

    private void logout() {
        controller.logout();
        usernameLabel.setText("");
        logoutButton.setVisible(false);
        login();
    }

    /// Crea da zero la gui per aprire una bacheca e aggiungila alla lista.
    /// Mostra il titolo, l'autore e la descrizione
    private void aggiungiBachecaAllaLista(int indice, String titolo, String autore, String descrizione) {
        JPanel pannelloBacheca = new JPanel();
        pannelloBacheca.setLayout(new BorderLayout());
        pannelloBacheca.setBorder(
                BorderFactory.createEmptyBorder(0, 8, 16, 0)
        );

        JPanel tmpPanel = new JPanel(new GridLayout(3, 1));

        JLabel labelTitolo = new JLabel(titolo);
        labelTitolo.setFont(
                labelTitolo.getFont().deriveFont(16.0f)
        );

        JLabel labelAutore = new JLabel(autore);
        JLabel labelDescrizione = new JLabel(descrizione);

        tmpPanel.add(labelTitolo);
        tmpPanel.add(labelAutore);
        tmpPanel.add(labelDescrizione);


        pannelloBacheca.add(tmpPanel, BorderLayout.WEST);

        tmpPanel = new JPanel(new GridLayout(2, 1));
        JButton buttonApri = new JButton("Apri");
        JButton buttonDescrizione = new JButton("Descrizione");
        tmpPanel.add(buttonApri);
        tmpPanel.add(buttonDescrizione);

        pannelloBacheca.add(tmpPanel, BorderLayout.EAST);

        pannelloBacheca.setMaximumSize(
                new Dimension(9999, pannelloBacheca.getPreferredSize().height)
        );
        pannelloBacheca.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonApri.addActionListener(_ -> apriBacheca(indice));
        // Apri il menu per cambiare la descrizione e effettua la richiesta al database
        buttonDescrizione.addActionListener(_ -> {
            String nuovaDescrizione = "";

            JTextArea textArea = new JTextArea(10, 30);
            textArea.setText(labelDescrizione.getText());
            JScrollPane scrollPane = new JScrollPane(textArea);

            int result = JOptionPane.showConfirmDialog(
                    panel, scrollPane,
                    "Inserisci la descrizione.",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.CANCEL_OPTION) {
                return;
            }
            nuovaDescrizione = textArea.getText();
            if (nuovaDescrizione.equals(labelDescrizione.getText())) {
                return;
            }

            try {
                controller.setDescrizioneBacheca(indice, nuovaDescrizione);
            } catch (SQLException e) {
                ToDoLogger.getInstance().logError(e);
                JOptionPane.showMessageDialog(
                        panel, "Errore nella modifica della descrizione.",
                        "Board error", JOptionPane.ERROR_MESSAGE
                );
            }
            labelDescrizione.setText(nuovaDescrizione);
        });

        listaBacheche.add(pannelloBacheca);
        listaBacheche.repaint();
        listaBacheche.revalidate();
    }

    private void apriBacheca(int indice) {
        try {
            controller.apriBacheca(indice);
        } catch (SQLException e) {
            ToDoLogger.getInstance().logError(e);
            JOptionPane.showMessageDialog(
                    this.panel,
                    "Errore nella richiesta dei promemoria della bacheca. Riprovare.",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        apriFrameBacheca();
    }

    /// Richiedi la lista di bacheche dell'utente e mostrale
    private void sincronizzaBacheche() {
        for (Component component : listaBacheche.getComponents()) {
            listaBacheche.remove(component);
        }


        try {
            numeroBacheche = controller.richiediBacheche();
        } catch (SQLException e) {
            ToDoLogger.getInstance().logError(e);
            JOptionPane.showMessageDialog(
                    this.panel,
                    "Non è stato possibile trovare bacheche.",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }


        for (int i = 0; i < numeroBacheche; i++) {
            aggiungiBachecaAllaLista(
                    i, controller.getTitoloBacheca(i),
                    controller.getAutoreBacheca(i), controller.getDescrizioneBacheca(i)
            );
        }

    }

    /// Seleziona una data e apri il menu per visualizzare i promemoria
    /// in scadenza.
    private void mostraInScadenza() {
        SelettoreData selettoreData = SelettoreData.create(new Date(), false);

        if (selettoreData.getRisposta() == SelettoreData.RISPOSTA_CANCEL) {
            return;
        }

        MenuInScadenza menu = MenuInScadenza.create(selettoreData.getData());

        if (menu.getBachecaSelezionata().isEmpty())
            return;

        for (int i = 0; i < numeroBacheche; i++) {
            if (controller.getTitoloBacheca(i).equals(menu.getBachecaSelezionata())) {
                apriBacheca(i);
                break;
            }
        }
    }

    /// Crea il frame per una bacheca e mostrala
    private void apriFrameBacheca() {
        JFrame frameBacheca = new JFrame();
        Bacheca guiBacheca = new Bacheca(frameBacheca, frame);
        frameBacheca.setContentPane(guiBacheca.getPanel());
        frame.setVisible(false);
        frameBacheca.setVisible(true);
        frameBacheca.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frameBacheca.setExtendedState(Frame.MAXIMIZED_BOTH);

    }
}
