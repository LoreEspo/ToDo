package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;


public class Bacheca {
    private JPanel panel;
    private JPanel todoContainer;
    private JLabel labelNome;
    private JButton chiudiButton;
    private JButton todoButton;
    private JLabel labelAutore;
    private final Controller controller;
    private final JFrame frame;
    private final JFrame mainFrame;

    public Bacheca(JFrame frame, JFrame mainFrame) {
        this.controller = Controller.getInstance();
        this.frame = frame;
        this.mainFrame = mainFrame;

        // Listener per assicurarsi di impostare la bacheca su "chiusa" nel database
        // In caso di errore, la finestra non verrà chiusa.
        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.frame.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        try {
                            controller.chiudiBacheca();
                        } catch (SQLException ex) {
                            System.out.println(ex);
                            JOptionPane.showMessageDialog(
                                    panel, "Errore durante la chiusura. Riprova.",
                                    "Error", JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }
                        e.getWindow().dispose();
                    }
                }
        );

        // Set todoContainer layout
        todoContainer.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );
        todoContainer.setLayout(
                new GridLayout(0, 4, 50, 30)
        );

        this.labelNome.setText(controller.getTitoloBacheca());
        this.labelNome.setFont(
                this.labelNome.getFont().deriveFont(16.0f)
        );
        this.labelAutore.setText(controller.getAutoreBacheca());

        // Azioni
        todoButton.addActionListener( _ -> aggiungiToDo() );

        chiudiButton.addActionListener( _ -> chiudi() );
    }

    public JPanel getPanel() {
        return panel;
    }


    public void chiudi() {
        try {
            controller.chiudiBacheca();
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(
                    this.panel,
                    "Errore nella chiusura della bacheca. Riprovare.",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        mainFrame.setVisible(true);
        frame.dispose();
    }

    public void aggiungiToDo() {
        Integer indice = controller.aggiungiToDo();

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BorderLayout());

        ToDo guiTodo = new ToDo();

        guiTodo.setIndice(indice);

        guiTodo.getCancellaButton().addActionListener(_ ->
            rimuoviToDo(wrapper, indice)
        );

        wrapper.add(guiTodo.getPanel(), BorderLayout.NORTH);

        todoContainer.add(wrapper);
        todoContainer.repaint();
        todoContainer.revalidate();
    }

    public void rimuoviToDo(JPanel wrapper, Integer indice) {
        controller.rimuoviToDo(indice);

        todoContainer.remove(wrapper);
        todoContainer.repaint();
        todoContainer.revalidate();

    }
}
