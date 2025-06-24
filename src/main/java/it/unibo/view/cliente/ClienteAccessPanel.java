package it.unibo.view.cliente;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.unibo.controller.Controller;
import it.unibo.data.Cliente;

public class ClienteAccessPanel extends JPanel {

    private JTextField txtIdLogin;
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
        txtTelefono = new JTextField(15);
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
        pnlLogin.add(new JLabel("Codice Cliente:"), gbc2);
        gbc2.gridx = 1;
        txtIdLogin = new JTextField(10);
        pnlLogin.add(txtIdLogin, gbc2);
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
        // Call service to register, returns new client ID
        controller.getModel().insertCliente(new Cliente(ABORT, nome, cognome, email, tel, null, username));
        Long newId = Service.registerCliente(nome, cognome, email, tel, data);
        JOptionPane.showMessageDialog(this, "Registrazione avvenuta! Il tuo codice cliente è: " + newId);
        cardLayout.show(cards, "main");
    }

    private void onLogin() {
        try {
            Long id = Long.parseLong(txtIdLogin.getText());
            if (Service.loginCliente(id)) {
                cardLayout.show(cards, "main");
            } else {
                JOptionPane.showMessageDialog(this, "Codice cliente non valido.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Inserisci un codice valido.");
        }
    }
}
