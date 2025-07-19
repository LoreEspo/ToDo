package gui;

import controller.Controller;
import logger.ToDoLogger;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuSpostaToDo extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBox;

    private final List<String> opzioni = new ArrayList<>();
    private final Integer indice;
    private boolean spostato = false;

    public MenuSpostaToDo(Integer indice, String bachecaIniziale) {
        this.indice = indice;
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
                    indice, opzioni.get(comboBox.getSelectedIndex())
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

    public boolean isSpostato() { return spostato; }

    public static MenuSpostaToDo create(Integer indice, String bachecaIniziale) {
        MenuSpostaToDo dialog = new MenuSpostaToDo(indice, bachecaIniziale);
        dialog.pack();
        dialog.setVisible(true);
        return dialog;
    }
}
