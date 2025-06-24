package it.unibo.view.cliente;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.Date;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.unibo.controller.Controller;
import it.unibo.data.Cliente;
import it.unibo.data.RaccoltaPunti;

public class ClienteAccessPanel extends JPanel {

    private JTextField txtUsernameLogin;
    private JTextField txtNome, txtCognome, txtEmail, txtTelefono, txtDataNasc, txtUsername;
    private final Controller controller;

    public ClienteAccessPanel(Controller controller) {
        this.controller = controller;
        setLayout(new GridLayout(1, 2, 10, 10));
        // Registration form
        JPanel pnlReg = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        pnlReg.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        txtNome = new JTextField(15);
        pnlReg.add(txtNome, gbc);
        gbc.gridx = 0; gbc.gridy++;
        pnlReg.add(new JLabel("Cognome:"), gbc);
        gbc.gridx = 1;
        txtCognome = new JTextField(15);
        pnlReg.add(txtCognome, gbc);
        gbc.gridx = 0; gbc.gridy++;
        pnlReg.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(15);
        pnlReg.add(txtEmail, gbc);
        gbc.gridx = 0; gbc.gridy++;
        pnlReg.add(new JLabel("Telefono:"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(15);
        pnlReg.add(txtTelefono, gbc);
        gbc.gridx = 0; gbc.gridy++;
        pnlReg.add(new JLabel("Data di Nascita:"), gbc);
        gbc.gridx = 1;
        txtDataNasc = new JTextField(15);
        pnlReg.add(txtDataNasc, gbc);
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        pnlReg.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        txtUsername = new JTextField(15);
        pnlReg.add(txtUsername, gbc);
        gbc.gridx = 0; gbc.gridy++;
        JButton btnReg = new JButton("Registrati");
        btnReg.addActionListener(e -> onRegister());
        pnlReg.add(btnReg, gbc);

        // Login form
        JPanel pnlLogin = new JPanel(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 5, 5);
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.gridx = 0; gbc2.gridy = 0;
        pnlLogin.add(new JLabel("Username:"), gbc2);
        gbc2.gridx = 1;
        txtUsernameLogin = new JTextField(10);
        pnlLogin.add(txtUsernameLogin, gbc2);
        gbc2.gridx = 0; gbc2.gridy++;
        gbc2.gridwidth = 2;
        JButton btnLogin = new JButton("Accedi");
        btnLogin.addActionListener(e -> onLogin());
        pnlLogin.add(btnLogin, gbc2);

        add(pnlReg);
        add(pnlLogin);
    }

    private void onRegister() {
        String nome = txtNome.getText();
        String cognome = txtCognome.getText();
        String email = txtEmail.getText();
        String tel = txtTelefono.getText();
        String data = txtDataNasc.getText();
        String username = txtUsername.getText();

        try {
            Date dataNascita = Date.valueOf(data); // Deve essere nel formato yyyy-MM-dd
            Cliente c = new Cliente(nome, cognome, email, tel, dataNascita, username);
            controller.getModel().insertCliente(c);
            controller.getModel().insertRaccoltaPunti(new RaccoltaPunti(c.codiceCliente, 0, 100, 10));
            JOptionPane.showMessageDialog(this, "Registrazione avvenuta! Il tuo username è: " + username);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Data di nascita non valida. Usa il formato yyyy-MM-dd (es: 2000-12-31).");
        }
    }

    private void onLogin() {
        final Optional<Cliente> cliente = controller.getModel().findClienteByUsername(txtUsernameLogin.getText());
        if (cliente.isPresent()) {
            controller.goToCliente(cliente.get().codiceCliente);
        } else {
            JOptionPane.showMessageDialog(this, "Username non valido.");
        }
    }
}
