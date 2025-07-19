package gui;

import controller.Controller;
import logger.ToDoLogger;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Dialog per spostare un promemoria in un'altra bacheca
 */
public class MenuSpostaToDo extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> comboBox;

    private final List<String> opzioni = new ArrayList<>();
    private final int id;
    private boolean spostato = false;

    private MenuSpostaToDo(int id, String bachecaIniziale) {
        this.id = id;
        // Rimuovi dalle opzioni la bacheca in cui è già presente
        opzioni.add("Università");
        opzioni.add("Tempo libero");
        opzioni.add("Lavoro");
        opzioni.remove(bachecaIniziale);
        for (String s : opzioni) {
            comboBox.addItem(s);
        }

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(_ -> onOK());

        buttonCancel.addActionListener(_ -> dispose());
    }

    private void onOK() {
        Controller controller = Controller.getInstance();
        try {
            controller.spostaToDo(
                    id, opzioni.get(comboBox.getSelectedIndex())
            );
        } catch (SQLException e) {
            ToDoLogger.getInstance().logError(e);
            JOptionPane.showMessageDialog(
                    contentPane, "Errore durante lo spostamento.",
                    "Todo error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        spostato = true;
        dispose();
    }

    /**
     * @return {@code true} se il promemoria è stato spostato
     */
    public boolean isSpostato() { return spostato; }

    /**
     * Crea e mostra il menu.
     *
     * @param id              l'id del promemoria
     * @param bachecaIniziale la bacheca iniziale
     * @return il menu
     */
    public static MenuSpostaToDo create(int id, String bachecaIniziale) {
        MenuSpostaToDo dialog = new MenuSpostaToDo(id, bachecaIniziale);
        dialog.pack();
        dialog.setVisible(true);
        return dialog;
    }
}
