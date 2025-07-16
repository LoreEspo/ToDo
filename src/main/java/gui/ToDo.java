package gui;

import controller.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;


public class ToDo {
    protected JPanel panel;
    private JLabel titolo;
    private JTextArea descrizione;
    private JCheckBox stato;
    private JLabel labelImmagine;
    private JPanel containerAttivita;
    private JButton dataButton;
    private JButton colorButton;
    private JButton cancellaButton;
    private JButton condividiButton;

    private final ArrayList<Attivita> listaAttivita = new ArrayList<>();
    private final Controller controller;
    private Integer indice = -1;


    public ToDo() {
        this.controller = Controller.getInstance();

        creaUI();

        titolo.addMouseListener(
                new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        cambiaTitolo();
                    }


                    @Override
                    public void mousePressed(MouseEvent e) {
                        // Non fare niente
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        // Non fare niente
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
        colorButton.addActionListener(_ -> cambiaColore());

    }

    private void cambiaTitolo() {
        String nuovoTitolo = JOptionPane.showInputDialog("Scegli il titolo");
        if (nuovoTitolo != null && !nuovoTitolo.isEmpty() && !nuovoTitolo.equals(titolo.getText()))
            titolo.setText(nuovoTitolo);
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


        JPanel tmpPanel = new JPanel();
        tmpPanel.setLayout(new BorderLayout());
        descrizione = new JTextArea();
        tmpPanel.add(descrizione, BorderLayout.CENTER);
        descrizione.setText("Descrizione");
        descrizione.setBackground(new Color(240, 240, 240 ));
        descrizione.setLineWrap(true);
        descrizione.setWrapStyleWord(true);

        stato = new JCheckBox();
        tmpPanel.add(stato, BorderLayout.EAST);
        panel.add(tmpPanel);
        tmpPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tmpPanel.setMaximumSize(
                new Dimension(9999, tmpPanel.getPreferredSize().height)
        );

        labelImmagine = new JLabel();

        tmpPanel = new JPanel();
        tmpPanel.setLayout(new GridLayout(1, 2));
        colorButton = new JButton();
        tmpPanel.add(colorButton);
        colorButton.setText("Sfondo");

        dataButton = new JButton();
        tmpPanel.add(dataButton);
        dataButton.setText("No scadenza");
        panel.add(tmpPanel);
        tmpPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tmpPanel.setMaximumSize(
                new Dimension(9999, tmpPanel.getPreferredSize().height)
        );

        containerAttivita = new JPanel();
        containerAttivita.setLayout(
                new GridLayout(0, 1)
        );
        panel.add(containerAttivita);

        JButton aggiungiButton = new JButton("Aggiungi attività");
        aggiungiButton.addActionListener(_ -> aggiungiAttivita());
        containerAttivita.add(aggiungiButton);

        tmpPanel = new JPanel();
        tmpPanel.setLayout(new GridLayout(1, 2));
        cancellaButton = new JButton();
        cancellaButton.setText("Cancella");
        tmpPanel.add(cancellaButton);

        condividiButton = new JButton();
        condividiButton.setText("Condividi");
        tmpPanel.add(condividiButton);
        panel.add(tmpPanel);
        tmpPanel.setMaximumSize(
                new Dimension(9999, tmpPanel.getPreferredSize().height)
        );

        panel.setMaximumSize(
                new Dimension(9999, panel.getPreferredSize().height)
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

    public void setImmagine(byte[] dati) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(dati);
        BufferedImage image;
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(
                    panel, "Errore nell'apertura dell'immagine.",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        float scale = (float)panel.getWidth() / (float)image.getWidth();
        ImageIcon icona = new ImageIcon(image.getScaledInstance(panel.getWidth(), (int)(image.getHeight() * scale), Image.SCALE_FAST));
        labelImmagine.setIcon(icona);

    }

    public void aggiungiAttivita() {
        Attivita attivita = new Attivita();

        containerAttivita.add(attivita.getPanel());
        containerAttivita.repaint();
        containerAttivita.revalidate();
        listaAttivita.add(attivita);
        attivita.setColore(panel.getBackground());


        attivita.getCancellaButton().addActionListener(
                _ -> rimuoviAttivita(listaAttivita.indexOf(attivita))
        );
    }

    public void rimuoviAttivita(int indiceAttivita) {
        Attivita attivita = listaAttivita.get(indiceAttivita);
        containerAttivita.remove(attivita.getPanel());
        containerAttivita.repaint();
        containerAttivita.revalidate();
        listaAttivita.remove(indiceAttivita);
        controller.rimuoviAttivita(this.indice, indiceAttivita);
    }

    protected void cambiaColore() {
        SelettoreColore selettore = SelettoreColore.create(panel.getBackground());
        if (!selettore.isOk()) {
            return;
        }

        System.out.println(selettore.getColore().toString());

        panel.setBackground(selettore.getColore());
        descrizione.setBackground(selettore.getColore());
        stato.setBackground(descrizione.getBackground());
        for (Attivita attivita : listaAttivita) {
            attivita.setColore(panel.getBackground());
        }
    }
}
