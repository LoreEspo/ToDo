package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Dialog per selezionare una data
 */
public class SelettoreData extends JDialog {
    public static final int RISPOSTA_CONFERMA = 0;
    public static final int RISPOSTA_RIMUOVI = 1;
    public static final int RISPOSTA_CANCEL = 2;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton buttonRimuovi;
    private JSpinner spinnerGiorno;
    private JSpinner spinnerMese;
    private JSpinner spinnerAnno;
    private JPanel spinnerContainer;

    private final Calendar calendario = Calendar.getInstance();
    private final SpinnerNumberModel modelGiorno;
    private Date data;
    private int risposta = RISPOSTA_CANCEL;

    private SelettoreData(Date dataIniziale, boolean mostraRimuovi) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        spinnerContainer.setLayout(
                new GridLayout(1, 3)
        );
        // Modello per limitare lo spinner del giorno
        modelGiorno = new SpinnerNumberModel(1, 1, 31, 1);
        spinnerGiorno.setModel(modelGiorno);
        spinnerMese.setModel(
                new SpinnerNumberModel(1, 1, 12, 1)
        );
        spinnerAnno.setModel(
                new SpinnerNumberModel(2025, 2000, 2099, 1)
        );
        spinnerAnno.setEditor(new JSpinner.NumberEditor(spinnerAnno, "#"));

        spinnerMese.addChangeListener(_ -> setGiornoMassimo());
        spinnerAnno.addChangeListener(_ -> setGiornoMassimo());


        buttonOK.addActionListener(_ -> onOK());

        if (mostraRimuovi) {
            buttonRimuovi.addActionListener(_ -> onRimuovi());
        } else {
            buttonRimuovi.getParent().remove(buttonRimuovi);
            buttonRimuovi = null;
        }

        buttonCancel.addActionListener(_ -> dispose());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(_ -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        if (dataIniziale != null)
            setData(dataIniziale);
    }

    /// Limita il giorno al massimo del mese selezionato
    private void setGiornoMassimo() {
        calendario.set(Calendar.YEAR, (int) spinnerAnno.getValue());
        calendario.set(Calendar.MONTH, (int) spinnerMese.getValue() - 1);
        modelGiorno.setMaximum(
                calendario.getActualMaximum(Calendar.DAY_OF_MONTH)
        );
        if ((Integer) spinnerGiorno.getValue() > (Integer) modelGiorno.getMaximum()) {
            spinnerGiorno.setValue(modelGiorno.getMaximum());
        }
    }

    private void onOK() {
        int anno = (int) spinnerAnno.getValue();
        int mese = (int) spinnerMese.getValue();
        int giorno = (int) spinnerGiorno.getValue();
        //noinspection MagicConstant
        calendario.set(anno, mese - 1, giorno, 0, 0, 0);

        data = calendario.getTime();

        risposta = RISPOSTA_CONFERMA;
        dispose();
    }

    private void onRimuovi() {
        data = null;
        risposta = RISPOSTA_RIMUOVI;
        dispose();
    }

    private void setData(Date data) {
        this.data = data;
        calendario.setTime(data);

        spinnerAnno.setValue(calendario.get(Calendar.YEAR));
        spinnerMese.setValue(calendario.get(Calendar.MONTH) + 1);
        spinnerGiorno.setValue(calendario.get(Calendar.DAY_OF_MONTH));

        setGiornoMassimo();
    }

    /**
     * @return la data selezionata
     */
    public Date getData() {
        return data;
    }

    /**
     * @return la risposta selezionata, pari alle costanti RISPOSTA
     */
    public int getRisposta() { return risposta; }

    /**
     * Crea e mostra il selettore.
     *
     * @param dataIniziale la data da cui parte il selettore
     * @return il selettore
     */
    public static SelettoreData create(Date dataIniziale, boolean mostraRimuovi) {
        SelettoreData dialog = new SelettoreData(dataIniziale, mostraRimuovi);
        dialog.pack();
        dialog.setVisible(true);
        return dialog;
    }

    /**
     * Crea il selettore e lo mostra,
     * partendo dalla data attuale.
     *
     * @return il selettore
     */
    public static SelettoreData create(Date dataIniziale) {
        return create(dataIniziale, true);
    }
}
