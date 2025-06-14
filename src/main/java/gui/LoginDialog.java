package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;


public class LoginDialog extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JCheckBox registerCheck;

	private final Controller controller;

	public LoginDialog(Controller controller) {
		this.controller = controller;

		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					onOK();
				} catch (SQLException ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { onCancel(); }
		});

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

	private void onOK() throws SQLException {
		String usernameRegex = "^[a-zA-Z0-9]{3,16}$";
		if (usernameField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(
					this,
					"L'username non può essere vuoto.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		} else if (!usernameField.getText().matches(usernameRegex)) {
			JOptionPane.showMessageDialog(
					this,
					"L'username può avere solo caratteri alfanumerici e tra i 3 e 16 caratteri.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		} else if (passwordValidity()) {
			JOptionPane.showMessageDialog(
					this,
					"La password non deve essere vuota e deve avere tra i 6 e 16 caratteri alfanumerici o speciali." +
							"\nI caratteri speciali validi sono ._?!/@*",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (registerCheck.isSelected()) {
			if (!controller.registrazione(usernameField.getText(), String.valueOf(passwordField.getPassword()))) {
				if (!controller.registrazione(usernameField.getText(), String.valueOf(passwordField.getPassword()))) {
					JOptionPane.showMessageDialog(
							this,
							"Username non disponibile",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			if (!controller.login(usernameField.getText(), String.valueOf(passwordField.getPassword()))) {
				JOptionPane.showMessageDialog(
						this,
						"Username o password errati.",
						"Error", JOptionPane.ERROR_MESSAGE);
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

	private boolean passwordValidity() {
		String password = String.valueOf(passwordField.getPassword());
		String passwordRegex = "^[a-zA-Z0-9._?!/@*]{6,16}$]";
		return !password.isEmpty() && password.matches(passwordRegex);
	}

	static public LoginDialog create(Controller controller) {
		LoginDialog dialog = new LoginDialog(controller);
		dialog.pack();
		dialog.setVisible(true);
		return dialog;
	}
}
