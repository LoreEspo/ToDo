package gui;

import controller.Controller;
import logger.ToDoLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.*;
import java.util.List;


public class Bacheca {
    private JPanel panel;
    private JPanel todoContainer;
    private JLabel labelNome;
    private JButton chiudiButton;
    private JButton todoButton;
    private JLabel labelAutore;
    private JButton salvaButton;
    private JPanel mainContainer;
    private JPanel condivisiContainer;
    private final Controller controller;
    private final JFrame frame;
    private final JFrame mainFrame;

    private final Map<Component, ToDo> wrapperATodo = new HashMap<>();
    private boolean ordineModificato = false;

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
                            ToDoLogger.getInstance().logError(ex);
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

        mainContainer.setLayout(new GridLayout(2, 1));

        // Set todoContainer layout
        todoContainer.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );
        todoContainer.setLayout(
            new GridLayout(0, 4, 50, 30)
        );
        condivisiContainer.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );
        condivisiContainer.setLayout(
                new GridLayout(0, 4, 50, 30)
        );

        this.labelNome.setText(controller.getTitoloBacheca());
        this.labelNome.setFont(
                this.labelNome.getFont().deriveFont(16.0f)
        );
        this.labelAutore.setText(controller.getAutoreBacheca());

        // Azioni
        todoButton.addActionListener( _ -> aggiungiToDo() );
        salvaButton.addActionListener(_ -> salva());
        chiudiButton.addActionListener( _ -> chiudi() );

        for (int indice : controller.listaToDo()) {
            aggiungiToDo(indice, controller.isToDoCondiviso(indice));
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    private boolean salva() {
        try {
            controller.salvaToDo();
            controller.salvaAttivita();
            if (ordineModificato) {
                Map<Integer, Integer> mappa = new HashMap<>();
                for (int i = 0; i < todoContainer.getComponentCount(); i++) {
                    mappa.put(
                            i, wrapperATodo.get(todoContainer.getComponent(i)).getIndice());
                }
                controller.aggiornaOrdineToDo(mappa);

                ordineModificato = false;
            }
            JOptionPane.showMessageDialog(
                    panel, "Promemoria salvati.",
                    "Save complete", JOptionPane.PLAIN_MESSAGE
            );
            return true;
        } catch (SQLException e) {
            ToDoLogger.getInstance().logError(e);
            JOptionPane.showMessageDialog(
                    panel, "Errore durante il salvataggio dei promemoria.",
                    "Save error", JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    private void chiudi() {
        if (controller.modificheEffettuate()) {
            int result = JOptionPane.showConfirmDialog(
                    panel, "Ci sono modifiche non salvate. Salvare?",
                    "Save", JOptionPane.YES_NO_CANCEL_OPTION
            );
            if ((result == JOptionPane.CANCEL_OPTION) || (result == JOptionPane.YES_OPTION && !salva())) {
                return;
            }
        }

        try {
            controller.chiudiBacheca();
        } catch (SQLException e) {
            ToDoLogger.getInstance().logError(e);
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
        Integer indice;
        try {
            indice = controller.aggiungiToDo();
        } catch (SQLException e) {
            ToDoLogger.getInstance().logError(e);
            JOptionPane.showMessageDialog(
                    panel, "Errore nella creazione del promemoria.",
                    "ToDo error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        aggiungiToDo(indice, false);
    }

    private void aggiungiToDo(Integer indice, boolean condiviso) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BorderLayout());

        ToDo guiTodo = new ToDo(frame, indice, condiviso);

        if (!condiviso) {
            guiTodo.getCancellaButton().addActionListener(_ ->
                    rimuoviToDo(wrapper, indice)
            );
            guiTodo.getSpostaDestraButton().addActionListener(_ -> spostaToDo(wrapper, true));
            guiTodo.getSpostaSinistraButton().addActionListener(_ -> spostaToDo(wrapper, false));
            guiTodo.getSpostaBachecaButton().addActionListener(_ -> {
                System.out.println(controller.getTitoloToDo(indice));
                MenuSpostaToDo menu = MenuSpostaToDo.create(indice, controller.getTitoloBachecaToDo(indice));
                if (menu.isSpostato()) {
                    todoContainer.remove(wrapper);
                    todoContainer.revalidate();
                    todoContainer.repaint();
                }
            });
        }

        wrapper.add(guiTodo.getPanel(), BorderLayout.NORTH);
        wrapperATodo.put(wrapper, guiTodo);

        JPanel container = condiviso ? condivisiContainer : todoContainer;

        container.add(wrapper);
        container.repaint();
        container.revalidate();

    }

    public void rimuoviToDo(JPanel wrapper, Integer indice) {
        int result = JOptionPane.showConfirmDialog(
                panel, "Cancellare il promemoria?",
                "Delete todo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE
        );
        if (result == JOptionPane.CANCEL_OPTION) {
            return;
        }

        try {
            controller.rimuoviToDo(indice);
        } catch (SQLException e) {
            ToDoLogger.getInstance().logError(e);
            JOptionPane.showMessageDialog(
                    panel, "Errore nella rimozione del promemoria.",
                    "ToDo error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        wrapperATodo.remove(wrapper);
        todoContainer.remove(wrapper);
        todoContainer.repaint();
        todoContainer.revalidate();

    }

    public void spostaToDo(JComponent wrapper, boolean destra) {
        int offset = destra ? 1 : -1;

        int indiceOriginale = -1;
        for (int i = 0; i < todoContainer.getComponentCount(); i++) {
            if (todoContainer.getComponent(i) == wrapper) {
                indiceOriginale = i;
                break;
            }
        }
        if (indiceOriginale == -1 || indiceOriginale + offset < 0 || indiceOriginale + offset >= todoContainer.getComponentCount()) {
            return;
        }

        todoContainer.remove(wrapper);
        todoContainer.add(wrapper, indiceOriginale + offset);

        todoContainer.repaint();
        todoContainer.revalidate();

        ordineModificato = true;
    }
}
