package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DatiBachecaDialog extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JTextArea nome;
	private JComboBox titolo;
	private JTextPane descrizione;

	private boolean ok = false;

	public DatiBachecaDialog() {
		setContentPane(contentPane);
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

		setMinimumSize(new Dimension(400, 400));

		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	private void onOK() {
		if (getNome().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Inserisci un nome", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		ok = true;
		dispose();
	}

	private void onCancel() {
		// add your code here if necessary
		dispose();
	}

	public boolean isOk() {
		return ok;
	}

	public String getNome() {
		return nome.getText();
	}

	public int getTitolo() {
		return titolo.getSelectedIndex();
	}

	public String getDescrizione() {
		return descrizione.getText();
	}

	public static DatiBachecaDialog create() {
		DatiBachecaDialog dialog = new DatiBachecaDialog();
		dialog.pack();
		dialog.setVisible(true);
		return dialog;
	}
}
