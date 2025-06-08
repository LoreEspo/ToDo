package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ToDoChecklist {
    private JPanel panel;
    private JButton cancellaButton;
    private JTextField titolo;
    private JPanel panelTitolo;
    private JPanel containerAttivita;
    private JButton aggiungiAttivitaButton;
    private JButton condividiButton;

    private Controller controller;
    private Integer indice = -1;
    private ArrayList<Attivita> listaAttivita = new ArrayList<>();

    public ToDoChecklist(Controller controller) {
        this.controller = controller;

        containerAttivita.setLayout(
                new GridLayout(0, 1)
        );

        titolo.addActionListener(_ -> aggiorna());
        aggiungiAttivitaButton.addActionListener(_ -> aggiungiAttivita());

        panel.setMinimumSize(new Dimension(500, 500));

        aggiungiAttivita();
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

        controller.modificaToDo(indice, titolo.getText(), false);

    }

    public void aggiungiAttivita() {
        Attivita attivita = new Attivita();

        containerAttivita.add(attivita.getPanel());
        containerAttivita.repaint();
        containerAttivita.revalidate();
        listaAttivita.add(attivita);


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
}
