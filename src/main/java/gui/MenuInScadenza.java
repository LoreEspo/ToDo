package gui;

import controller.Controller;
import logger.ToDoLogger;

import javax.swing.*;
        import java.awt.*;
        import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

/**
 * Dialog per vedere quali promemoria stanno scadendo
 */
public class MenuInScadenza extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JPanel lista;

    private String bachecaSelezionata = "";

    private MenuInScadenza(Date scadenza) {
        setContentPane(contentPane);
        setModal(true);

        lista.setLayout(new GridLayout(0, 1));

        getRootPane().setDefaultButton(buttonOK);

        setMinimumSize(new Dimension(300, 400));

        buttonOK.addActionListener(_ -> dispose());

        Controller controller = Controller.getInstance();
        try {
            for (Map<String, String> todo : controller.inScadenza(scadenza)) {
                aggiungiAllaLista(todo.get("titolo"), todo.get("titoloBacheca"));
            }
        } catch (SQLException e) {
            ToDoLogger.getInstance().logError(e);
            JOptionPane.showMessageDialog(
                    contentPane, "Errore durante la richiesta dei promemoria. Riprova.",
                    "Search error", JOptionPane.ERROR_MESSAGE
            );
            dispose();
        }
    }


    /// Aggiungi un promemoria alla lista e collega il tasto per aprire la bacheca del promemoria
    private void aggiungiAllaLista(String stringaTitolo, String stringaTitoloBacheca) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel left = new JPanel(new GridLayout(2, 1));
        left.add(new JLabel(stringaTitolo));
        JLabel labelBacheca = new JLabel(stringaTitoloBacheca);
        left.add(labelBacheca);
        labelBacheca.setFont(labelBacheca.getFont().deriveFont(10.0f));
        JButton apriButton = new JButton("Apri");
        panel.add(left);
        panel.add(apriButton, BorderLayout.EAST);


        panel.setMaximumSize(
                new Dimension(9999, panel.getPreferredSize().height)
        );

        apriButton.addActionListener(_ -> {
            bachecaSelezionata = stringaTitoloBacheca;
            dispose();
        });

        lista.add(panel);
    }

    /**
     * @return la bacheca da aprire
     */
    public String getBachecaSelezionata() { return bachecaSelezionata; }

    /**
     * Crea e mostra il menu di ricerca.
     *
     * @return il menu
     */
    public static MenuInScadenza create(Date scadenza) {
        MenuInScadenza dialog = new MenuInScadenza(scadenza);
        dialog.pack();
        dialog.setVisible(true);
        return dialog;
    }
}
