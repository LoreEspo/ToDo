package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;


public class ToDo {
    private JPanel panel;
    private JLabel titolo;
    private JTextArea descrizione;
    private JCheckBox stato;
    private JButton dataButton;
    private JButton colorButton;
    private JButton cancellaButton;
    private JButton condividiButton;

    protected final Controller controller;
    protected Integer indice = -1;


    public ToDo(Controller controller) {
        this.controller = controller;

        creaUI();

        titolo.addMouseListener(
                new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        cambiaTitolo();
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        titolo.setForeground(Color.getColor("#333333"));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        titolo.setForeground(Color.BLACK);
                    }
                }
        );
        if (stato != null) {
            stato.addActionListener(_ -> aggiorna());
        }

    }

    private void cambiaTitolo() {
        String nuovo_titolo = JOptionPane.showInputDialog("Scegli il titolo");
        if (nuovo_titolo != null && !nuovo_titolo.isEmpty() && !nuovo_titolo.equals(titolo.getText()))
            titolo.setText(nuovo_titolo);
    }

    protected void creaUI() {
        panel = new JPanel();
        panel.setLayout(
                new BoxLayout(panel, BoxLayout.Y_AXIS)
        );

        titolo = new JLabel();
        panel.add(titolo);
        titolo.setText("Titolo");
        titolo.setAlignmentX(Component.CENTER_ALIGNMENT);
        Font font = titolo.getFont();
        titolo.setFont(font.deriveFont(18.0f));


        JPanel tmp_panel = new JPanel();
        tmp_panel.setLayout(new BorderLayout());
        descrizione = new JTextArea();
        tmp_panel.add(descrizione, BorderLayout.CENTER);
        descrizione.setText("Descrizione");
        descrizione.setBackground(new Color(240, 240, 240 ));
        descrizione.setLineWrap(true);
        descrizione.setWrapStyleWord(true);

        stato = new JCheckBox();
        tmp_panel.add(stato, BorderLayout.EAST);
        panel.add(tmp_panel);
        tmp_panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tmp_panel.setMaximumSize(
                new Dimension(9999, tmp_panel.getPreferredSize().height)
        );

        tmp_panel = new JPanel();
        tmp_panel.setLayout(new GridLayout(1, 2));
        colorButton = new JButton();
        tmp_panel.add(colorButton);
        colorButton.setText("Sfondo");

        dataButton = new JButton();
        tmp_panel.add(dataButton);
        dataButton.setText("No scadenza");
        panel.add(tmp_panel);
        tmp_panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tmp_panel.setMaximumSize(
                new Dimension(9999, tmp_panel.getPreferredSize().height)
        );

        // Spazio libero per le attività nella checklist

        tmp_panel = new JPanel();
        tmp_panel.setLayout(new GridLayout(1, 2));
        cancellaButton = new JButton();
        cancellaButton.setText("Cancella");
        tmp_panel.add(cancellaButton);

        condividiButton = new JButton();
        condividiButton.setText("Condividi");
        tmp_panel.add(condividiButton);
        panel.add(tmp_panel);
        tmp_panel.setMaximumSize(
                new Dimension(9999, tmp_panel.getPreferredSize().height)
        );

        panel.setVisible(true);
        panel.repaint();
        panel.revalidate();
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setTitolo(String titolo) {
        this.titolo.setText(titolo);
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    public JButton getCancellaButton() {
        return cancellaButton;
    }

    public void aggiorna() {
        if (indice == -1) return;

        controller.modificaToDo(indice, titolo.getText(), stato.isSelected());

    }
}
