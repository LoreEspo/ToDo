package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SelettoreColore extends JDialog {
    private JPanel panel;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JColorChooser colorChooser;
    private boolean ok = false;


    public SelettoreColore() {
        this(Color.BLACK);
    }

    public SelettoreColore(Color coloreIniziale) {
        panel = new JPanel();
        panel.setLayout(
                new BorderLayout()
        );
        JPanel bottoni = new JPanel();
        bottoni.setLayout(
                new BorderLayout()
        );

        JPanel tmp_panel = new JPanel();
        tmp_panel.setLayout(
                new FlowLayout()
        );

        buttonOK = new JButton("OK");
        buttonCancel = new JButton("Annulla");

        tmp_panel.add(buttonOK);
        tmp_panel.add(buttonCancel);
        bottoni.add(tmp_panel, BorderLayout.EAST);

        colorChooser = new JColorChooser(coloreIniziale);
        panel.add(colorChooser, BorderLayout.CENTER);
        panel.add(bottoni, BorderLayout.SOUTH);

        setContentPane(panel);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        panel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        ok = true;
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public Color getColore() {
        return colorChooser.getColor();
    }

    public boolean isOk() {
        return ok;
    }

    static SelettoreColore create(Color coloreIniziale) {
        SelettoreColore selettore = new SelettoreColore(coloreIniziale);
        selettore.pack();
        selettore.setVisible(true);
        return selettore;
    }

    static SelettoreColore create() {
        SelettoreColore selettore = new SelettoreColore();
        selettore.pack();
        selettore.setVisible(true);
        return selettore;
    }
}
