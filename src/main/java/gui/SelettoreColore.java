package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Dialog per selezionare un colore.
 */
public class SelettoreColore extends JDialog {

	private final JColorChooser colorChooser;
	private boolean ok = false;

	private SelettoreColore(Color coloreIniziale) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JPanel bottoni = new JPanel();
		bottoni.setLayout(new BorderLayout());

		JPanel tmpPanel = new JPanel();
		tmpPanel.setLayout(new FlowLayout());

		JButton buttonOK = new JButton("OK");
		JButton buttonCancel = new JButton("Annulla");

		tmpPanel.add(buttonOK);
		tmpPanel.add(buttonCancel);
		bottoni.add(tmpPanel, BorderLayout.EAST);

		colorChooser = new JColorChooser(coloreIniziale);
		panel.add(colorChooser, BorderLayout.CENTER);
		panel.add(bottoni, BorderLayout.SOUTH);

		setContentPane(panel);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

		buttonOK.addActionListener(_ -> onOK());

		buttonCancel.addActionListener(_ -> dispose());

		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(
			new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					dispose();
				}
			}
		);

		// call onCancel() on ESCAPE
		panel.registerKeyboardAction(
			_ -> dispose(),
			KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
			JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
		);
	}

	private void onOK() {
		ok = true;
		dispose();
	}

	/**
	 * @return il colore selezionato
	 */
	public Color getColore() {
		return colorChooser.getColor();
	}

	/**
	 * @return {@code true} se è stato premuto ok.
	 */
	public boolean isOk() {
		return ok;
	}

	/**
	 * Crea il selettore e lo mostra a schermo.
	 *
	 * @param coloreIniziale il colore iniziale del selettore
	 * @return il selettore
	 */
	public static SelettoreColore create(Color coloreIniziale) {
		SelettoreColore selettore = new SelettoreColore(coloreIniziale);
		selettore.pack();
		selettore.setVisible(true);
		return selettore;
	}
}
