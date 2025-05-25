package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;


public class Bacheca {
    private JButton aggiungiButton;
    private JButton condividiButton;
    private JPanel panel;
    private JPanel todoContainer;
    private JPanel topBar;
    private JScrollPane scrollPane;
    private JLabel nome;
    private JButton chiudiButton;
    private JButton todoButton;
    private JButton checklistButton;
    private Controller controller;
    private final JFrame frame;
    private final JFrame mainFrame;

    private boolean ultimoChecklist = false;

    public Bacheca(Controller controller, JFrame frame, JFrame mainFrame) {
        this.controller = controller;
        this.frame = frame;
        this.mainFrame = mainFrame;

        // Set todoContainer layout
        todoContainer.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );
        todoContainer.setLayout(
                new GridLayout(0, 4, 50, 30)
        );

        // Azioni
        aggiungiButton.addActionListener(_ -> {
            aggiungiToDo(ultimoChecklist);
        });
        todoButton.addActionListener(_ -> {
            ultimoChecklist = false;
            aggiungiToDo(false);
        });
        checklistButton.addActionListener(_ -> {
            // TODO: I checklist aggiunti sono uguali ai ToDo. Risolvere.
            ultimoChecklist = true;
            aggiungiToDo(true);
        });

        chiudiButton.addActionListener(_ -> {chiudi();});
    }

    public JPanel getPanel() {
        return panel;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bacheca");
        Bacheca bacheca = new Bacheca(new Controller(), frame, null);
        frame.setContentPane(bacheca.panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setMinimumSize(new Dimension(200, 200));
        frame.setSize(new Dimension(800, 600));

    }

    public void chiudi() {
        controller.chiudiBacheca();
        mainFrame.setVisible(true);
        frame.dispose();
    }

    public void aggiungiToDo(boolean checklist) {
        Integer indice = controller.aggiungiToDo(checklist);

        // JPanel wrapper = new JPanel();

        if (checklist) {
            ToDoChecklist guiToDo = new ToDoChecklist(controller);
            guiToDo.setIndice(indice);

            guiToDo.getCancellaButton().addActionListener(_ -> {
                rimuoviToDo(guiToDo.getPanel(), indice);
            });

            // wrapper.add(guiToDo.getPanel());
            todoContainer.add(guiToDo.getPanel());
        } else {
            ToDo guiToDo = new ToDo(controller);
            guiToDo.setIndice(indice);

            guiToDo.getCancellaButton().addActionListener(_ -> {
                rimuoviToDo(guiToDo.getPanel(), indice);
            });

            // wrapper.add(guiToDo.getPanel());
            todoContainer.add(guiToDo.getPanel());
        }
        // todoContainer.add(wrapper);
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
