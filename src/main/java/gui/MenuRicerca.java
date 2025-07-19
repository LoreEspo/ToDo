package gui;

import controller.Controller;
import logger.ToDoLogger;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Map;

/**
 * Dialog per cercare un promemoria fra le bacheche
 */
public class MenuRicerca extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JPanel topBar;
    private JTextField inputField;
    private JPanel lista;
    private final JCheckBox titolo;
    private final JCheckBox descrizione;

    private String bachecaSelezionata = "";

    private MenuRicerca() {
        setContentPane(contentPane);
        setModal(true);

        JPanel bottom = new JPanel(new GridLayout(1, 3));
        titolo = new JCheckBox("Titolo");
        titolo.setSelected(true);
        descrizione = new JCheckBox("Descrizione");
        JButton ricercaButton = new JButton("Cerca");
        bottom.add(titolo);
        bottom.add(descrizione);
        bottom.add(ricercaButton);
        topBar.add(bottom, BorderLayout.CENTER);
        lista.setLayout(new GridLayout(0, 1));

        getRootPane().setDefaultButton(ricercaButton);

        setMinimumSize(new Dimension(300, 400));

        ricercaButton.addActionListener(_ -> ricerca());

        buttonOK.addActionListener(_ -> dispose());
    }

    /// Effettua la ricerca e aggiungi i risultati alla lista
    private void ricerca() {
        if (!(titolo.isSelected() || descrizione.isSelected()) || inputField.getText().isEmpty()) {
            return;
        }
        // Ripulisci la lista dalla vecchia ricerca
        for (Component comp : lista.getComponents()) {
            lista.remove(comp);
        }

        try {
            for (Map<String, String> todo : Controller.getInstance().ricerca(inputField.getText(), titolo.isSelected(), descrizione.isSelected())) {
                aggiungiAllaLista(todo.get("titolo"), todo.get("titoloBacheca"));
            }
            lista.revalidate();
            lista.repaint();
        } catch (SQLException e) {
            ToDoLogger.getInstance().logError(e);
            JOptionPane.showMessageDialog(
                    contentPane, "Errore durante la ricerca.",
                    "Search error", JOptionPane.ERROR_MESSAGE
            );
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
    public static MenuRicerca create() {
        MenuRicerca dialog = new MenuRicerca();
        dialog.pack();
        dialog.setVisible(true);
        return dialog;
    }
}
