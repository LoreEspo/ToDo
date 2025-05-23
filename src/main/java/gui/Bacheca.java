package gui;

import javax.swing.*;
import java.awt.*;


public class Bacheca {
    private JButton aggiungiButton;
    private JButton condividiButton;
    private JPanel panel;
    private JPanel todoContainer;
    private JPanel topBar;
    private JScrollPane scrollPane;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bacheca");
        Bacheca bacheca = new Bacheca();
        frame.setContentPane(bacheca.panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setMinimumSize(new Dimension(200, 200));
        frame.setSize(new Dimension(800, 600));

        // Set todoContainer layout
        bacheca.todoContainer.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );
        bacheca.todoContainer.setLayout(
                new GridLayout(0, 4, 50, 50)

        );

        for (int i = 0; i < 100; i++) {
            ToDoChecklist todo = new ToDoChecklist();
            bacheca.todoContainer.add(todo.getPanel());
            todo.setTitolo("TODO: " + i);

            for (int j = 0; j < 3; j++) {
                todo.aggiungiAttivita(
                        new Attivita().setTitolo("" + (j + 1))
                );
            }
        }
    }
}
