package it.unibo.view.ristorante;

import javax.swing.*;
import java.awt.*;
import it.unibo.controller.Controller;
import it.unibo.data.Ristorante;
import it.unibo.model.CittaZona;

public class RistoranteAccessPanel extends JPanel {
    public RistoranteAccessPanel(Controller controller) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title = new JLabel("Accesso Ristorante");
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
            JTextField pivaField = new JTextField();
            JPanel panel = new JPanel(new GridLayout(0,1));
            panel.add(new JLabel("Partita IVA:"));
            panel.add(pivaField);
            int result = JOptionPane.showConfirmDialog(this, panel, "Login Ristorante", JOptionPane.OK_CANCEL_OPTION);
            if(result == JOptionPane.OK_OPTION) {
                String piva = pivaField.getText();
                if (controller.getModel().findRistoranteByPiva(piva).isPresent()) {
                    controller.goToRistorante(piva);
                } else {
                    JOptionPane.showMessageDialog(this, "Partita IVA non trovata.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // REGISTRAZIONE
        regBtn.addActionListener(e -> {
            JTextField pivaField = new JTextField();
            JTextField nomeField = new JTextField();
            JTextField indirizzoField = new JTextField();
            JTextField orarioField = new JTextField();
            JComboBox<CittaZona> comboCitta = new JComboBox<>(CittaZona.CITTA_ZONA);

            JPanel panel = new JPanel(new GridLayout(0,1));
            panel.add(new JLabel("Partita IVA:")); panel.add(pivaField);
            panel.add(new JLabel("Nome:")); panel.add(nomeField);
            panel.add(new JLabel("Indirizzo:")); panel.add(indirizzoField);
            panel.add(new JLabel("Orario:")); panel.add(orarioField);
            panel.add(new JLabel("Zona geografica:")); panel.add(comboCitta);

            int res = JOptionPane.showConfirmDialog(this, panel, "Registrazione Ristorante", JOptionPane.OK_CANCEL_OPTION);
            if(res == JOptionPane.OK_OPTION) {
                try {
                    CittaZona zona = (CittaZona) comboCitta.getSelectedItem();
                    controller.getModel().insertRistorante(new Ristorante(
                        pivaField.getText(),
                        nomeField.getText(),
                        indirizzoField.getText(),
                        orarioField.getText(),
                        zona.codiceZona
                    ));
                    JOptionPane.showMessageDialog(this, "Registrazione avvenuta!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore nei dati inseriti.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
