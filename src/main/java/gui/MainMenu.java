package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class MainMenu {
    private JPanel panel;
    private JPanel topBar;
    private JPanel listaBacheche;
    private JButton logoutButton;
    private JLabel usernameLabel;
    private final Controller controller;
    private JFrame frame;


    public MainMenu() {
        this.controller = Controller.getInstance();
        listaBacheche.setLayout(new BoxLayout(listaBacheche, BoxLayout.Y_AXIS));

        logoutButton.addActionListener(_ -> logout());
        logoutButton.setVisible(false);

        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 30));

        usernameLabel.setFont(
                usernameLabel.getFont().deriveFont(16.0f)
        );
    }

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

        mainMenu.sincronizzaBacheche();
    }

    private void login() {
        LoginDialog.create();
        if (!controller.isLogged()) {
            System.exit(0);
            return;
        }
        usernameLabel.setText(controller.getLoggedUsername());
        logoutButton.setVisible(true);
    }

    private void logout() {
        controller.logout();
        usernameLabel.setText("");
        logoutButton.setVisible(false);
        login();
    }

    private void aggiungiBachecaAllaLista(int indice, String titolo, String autore, String descrizione) {
        JPanel pannelloBacheca = new JPanel();
        pannelloBacheca.setLayout(new BorderLayout());
        pannelloBacheca.setBorder(
                BorderFactory.createEmptyBorder(0, 8, 16, 0)
        );

        JPanel tmpPanel = new JPanel();
        tmpPanel.setLayout(
                new GridLayout(3, 1)
        );

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

        JButton buttonApri = new JButton("Apri");

        pannelloBacheca.add(buttonApri, BorderLayout.EAST);

        pannelloBacheca.setMaximumSize(
                new Dimension(9999, pannelloBacheca.getPreferredSize().height)
        );
        pannelloBacheca.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonApri.addActionListener(_ -> apriBacheca(indice));

        listaBacheche.add(pannelloBacheca);
        listaBacheche.repaint();
        listaBacheche.revalidate();
    }

    private void apriBacheca(int indice) {
        try {
            controller.apriBacheca(indice);
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(
                    this.panel,
                    "Errore nell'apertura della bacheca.",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        apriFrameBacheca();
    }

    public void sincronizzaBacheche() {
        for (Component component : listaBacheche.getComponents()) {
            listaBacheche.remove(component);
        }

        int numeroBacheche = 0;

        try {
            numeroBacheche = controller.richiediBacheche();
        } catch (SQLException e) {
            System.out.println(e);
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

    private void apriFrameBacheca() {
        JFrame frameBacheca = new JFrame();
        Bacheca guiBacheca = new Bacheca(frameBacheca, frame);
        frameBacheca.setContentPane(guiBacheca.getPanel());
        frame.setVisible(false);
        frameBacheca.setVisible(true);
        frameBacheca.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frameBacheca.setSize(frame.getSize());

    }
}
