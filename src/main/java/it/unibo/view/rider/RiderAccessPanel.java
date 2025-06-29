package it.unibo.view.rider;

import javax.swing.*;
import java.awt.*;
import it.unibo.controller.Controller;
import it.unibo.data.Rider;
import it.unibo.model.CittaZona;

public class RiderAccessPanel extends JPanel {
    
    public RiderAccessPanel(Controller controller) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title = new JLabel("Accesso Rider");
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JButton loginBtn = new JButton("Login");
        JButton regBtn = new JButton("Registrati");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.insets = new Insets(10,10,10,10);
        add(title, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        add(loginBtn, gbc);

        gbc.gridx = 1;
        add(regBtn, gbc);

        // LOGIN
        loginBtn.addActionListener(e -> {
            JTextField emailField = new JTextField();
            JPanel panel = new JPanel(new GridLayout(0,1));
            panel.add(new JLabel("Email:"));
            panel.add(emailField);
            int result = JOptionPane.showConfirmDialog(
                this, panel, "Login Rider", JOptionPane.OK_CANCEL_OPTION);
            if(result == JOptionPane.OK_OPTION) {
                String email = emailField.getText();
                if(controller.loginRider(email)) {
                    controller.showRiderPanel();
                } else {
                    JOptionPane.showMessageDialog(this, "Email non trovata o errata.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // REGISTRAZIONE
        regBtn.addActionListener(e -> {
            JTextField nomeField = new JTextField();
            JTextField cognomeField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField telefonoField = new JTextField();
            JCheckBox patenteBox = new JCheckBox("Hai la patente?");
            JComboBox<CittaZona> comboCitta = new JComboBox<>(CittaZona.CITTA_ZONA);

            JPanel panel = new JPanel(new GridLayout(0,1));
            panel.add(new JLabel("Nome:"));
            panel.add(nomeField);
            panel.add(new JLabel("Cognome:"));
            panel.add(cognomeField);
            panel.add(new JLabel("Email:"));
            panel.add(emailField);
            panel.add(new JLabel("Telefono:"));
            panel.add(telefonoField);
            panel.add(patenteBox);
            panel.add(new JLabel("Zona geografica:"));
            panel.add(comboCitta);

            int res = JOptionPane.showConfirmDialog(
                this, panel, "Registrazione Rider", JOptionPane.OK_CANCEL_OPTION
            );

            if(res == JOptionPane.OK_OPTION) {
                CittaZona zona = (CittaZona) comboCitta.getSelectedItem();
                boolean ok = controller.getModel().insertRider(new Rider(
                    nomeField.getText(),
                    cognomeField.getText(),
                    emailField.getText(),
                    telefonoField.getText(),
                    patenteBox.isSelected(),
                    zona.codiceZona)
                );
                if(ok) {
                    JOptionPane.showMessageDialog(this, "Registrazione avvenuta con successo!");
                } else {
                    JOptionPane.showMessageDialog(this, "Errore nella registrazione.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
