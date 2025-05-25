package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;

public class MainMenu {
    private JPanel panel;
    private JPanel topBar;
    private JButton creaBachecaButton;
    private JPanel listaBacheche;
    private Controller controller;
    private JFrame frame;


    public MainMenu(Controller controller) {
        this.controller = controller;
        listaBacheche.setLayout(new BoxLayout(listaBacheche, BoxLayout.Y_AXIS));

        creaBachecaButton.addActionListener(_ -> {creaBacheca();});
    }

    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu(new Controller());

        JFrame frame = new JFrame("ToDo app");
        mainMenu.frame = frame;
        frame.setContentPane(mainMenu.panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setMinimumSize(new Dimension(200, 200));
        frame.setSize(800, 600);

        // Test
        mainMenu.controller.login("testUsername", "testPassword");
    }

    private void aggiungiBachecaAllaLista(String nome, String titolo, Integer idBacheca) {
        JPanel pannelloBacheca = new JPanel();
        pannelloBacheca.setLayout(new FlowLayout(FlowLayout.LEFT));


        JLabel labelNome = new JLabel(nome + ": ");
        JLabel labelTitolo = new JLabel(titolo);
        JButton buttonApri = new JButton("Apri");
        JButton buttonCancella = new JButton("Cancella");

        pannelloBacheca.add(labelNome);
        pannelloBacheca.add(labelTitolo);
        pannelloBacheca.add(buttonApri);
        pannelloBacheca.add(buttonCancella);

        listaBacheche.add(pannelloBacheca);
        listaBacheche.repaint();
        listaBacheche.revalidate();
    }

    private void apriBacheca(Integer idBacheca) {

    }

    private void creaBacheca() {
        DatiBacheca dialog = DatiBacheca.create();
        if (!dialog.isOk()) return;

        controller.creaNuovaBacheca(
                dialog.getNome(),
                dialog.getTitolo(),
                dialog.getDescrizione()
        );

        apriFrameBacheca();
    }


    private void apriFrameBacheca() {
        JFrame frameBacheca = new JFrame();
        Bacheca guiBacheca = new Bacheca(controller, frameBacheca, frame);
        frameBacheca.setContentPane(guiBacheca.getPanel());
        frame.setVisible(false);
        frameBacheca.setVisible(true);
        frameBacheca.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frameBacheca.setSize(frame.getSize());

    }
}
