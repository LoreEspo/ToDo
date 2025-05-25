package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;


public class ToDo {
    private JPanel panel;
    private JCheckBox stato;
    private JButton cancellaButton;
    private JTextField titolo;
    private JPanel innerPanel;
    private JButton condividiButton;

    private final Controller controller;
    private Integer indice = -1;


    public ToDo(Controller controller) {
        this.controller = controller;
        titolo.addActionListener(_ -> aggiorna());
        if (stato != null) {
            stato.addActionListener(_ -> aggiorna());
        }

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
