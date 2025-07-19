package gui;

import controller.Controller;
import logger.ToDoLogger;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;


/**
 * Dialog per effettuare un accesso o una registrazione.
 */
public class LoginDialog extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JCheckBox registerCheck;

	private final transient Controller controller;


	private LoginDialog() {
		this.controller = Controller.getInstance();

		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

		registerCheck.addActionListener(_ -> setNomeFinestra());

		buttonOK.addActionListener(_ -> {
            try {
                onOK();
            } catch (SQLException e) {
				ToDoLogger.getInstance().logError(e);
            }
        });

		buttonCancel.addActionListener(e -> onCancel());

		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(_ -> onCancel(),
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		setNomeFinestra();
	}

	private void setNomeFinestra() {
		if (registerCheck.isSelected())
			setTitle("Registrazione");
		else setTitle("Accesso");
	}

	private void onOK() throws SQLException {
		final String ERROR_STRING = "Login error";

		String usernameRegex = "^[a-zA-Z0-9]{3,16}$";
		// Mostra i vari messaggi di errore
		if (usernameField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(
					this,
					"L'username non può essere vuoto.",
					ERROR_STRING, JOptionPane.ERROR_MESSAGE);
			return;
		} else if (!usernameField.getText().matches(usernameRegex)) {
			JOptionPane.showMessageDialog(
					this,
					"L'username può avere solo caratteri alfanumerici e tra i 3 e 16 caratteri.",
					ERROR_STRING, JOptionPane.ERROR_MESSAGE);
			return;
		} else if (passwordValidity()) {
			JOptionPane.showMessageDialog(
					this,
					"La password non deve essere vuota e deve avere tra i 6 e 16 caratteri alfanumerici o speciali." +
							"\nI caratteri speciali validi sono ._?!/@*",
					ERROR_STRING, JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (registerCheck.isSelected()) {
			if (!controller.registrazione(usernameField.getText(), String.valueOf(passwordField.getPassword()))) {
				JOptionPane.showMessageDialog(
						this,
						"Username non disponibile",
						ERROR_STRING, JOptionPane.ERROR_MESSAGE);
			}
		} else {
			if (!controller.login(usernameField.getText(), String.valueOf(passwordField.getPassword()))) {
				JOptionPane.showMessageDialog(
						this,
						"Username o password errati.",
						ERROR_STRING, JOptionPane.ERROR_MESSAGE);
			}
		}

		if (!controller.isLogged()) {
			return;
		}

		dispose();
	}


	private void onCancel() {
		dispose();
		System.exit(0);
	}

	/// Controlla la validita della password
	private boolean passwordValidity() {
		String password = String.valueOf(passwordField.getPassword());
		String passwordRegex = "^[a-zA-Z0-9._?!/@*]{6,16}$]";
		return !password.isEmpty() && password.matches(passwordRegex);
	}

	/**
	 * Crea e mostra il dialog.
	 *
	 * @return il dialog
	 */
	public static LoginDialog create() {
		LoginDialog dialog = new LoginDialog();
		dialog.pack();
		dialog.setVisible(true);
		return dialog;
	}
}
