package it.unibo.view.rider;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Optional;

import it.unibo.controller.Controller;
import it.unibo.data.Rider;
import it.unibo.data.Contratto;
import it.unibo.data.Mezzo;
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
                final Optional<Rider> rider = controller.getModel().findRiderByEmail(email);
                if (rider.isPresent()) {
                    controller.goToRider(rider.get().codiceRider);
                    controller.getRiderProfiloPanel().mostraProfiloRider();
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
            JTextField dataNascitaField = new JTextField();
            JTextField ibanField = new JTextField();
            JTextField cfField = new JTextField();
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
            panel.add(new JLabel("Data di nascita: (yyyy-mm-dd)"));
            panel.add(dataNascitaField);
            panel.add(new JLabel("Iban:"));
            panel.add(ibanField);
            panel.add(new JLabel("Codice fiscale:"));
            panel.add(cfField);
            panel.add(patenteBox);
            panel.add(new JLabel("Zona geografica:"));
            panel.add(comboCitta);

            int res = JOptionPane.showConfirmDialog(
                this, panel, "Registrazione Rider", JOptionPane.OK_CANCEL_OPTION
            );

            if(res == JOptionPane.OK_OPTION) {
                try {
                    CittaZona zona = (CittaZona) comboCitta.getSelectedItem();
                    boolean haPatente = patenteBox.isSelected();
                    Rider nuovo = new Rider(
                        nomeField.getText(),
                        cognomeField.getText(),
                        emailField.getText(),
                        telefonoField.getText(),
                        Date.valueOf(dataNascitaField.getText()),
                        ibanField.getText(),
                        cfField.getText(),
                        haPatente,
                        true,
                        zona.codiceZona
                    );
                    final int codiceNuovoRider = controller.getModel().insertRider(nuovo);
                    controller.getModel().insertContratto(new Contratto(codiceNuovoRider,
                                                                        BigDecimal.valueOf(8),
                                                                        "Contratto standard Rider"));
                    
                    // Chiede di inserire almeno un mezzo
                    inserisciMezzo(controller, codiceNuovoRider, nuovo.patente);

                    JOptionPane.showMessageDialog(this, "Registrazione avvenuta con successo!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Errore nella registrazione.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Mostra un dialog per inserire un mezzo associato al rider,
     * limitando la scelta dei tipi in base al flag patente
     */
    private void inserisciMezzo(Controller controller, int codiceRider, boolean haPatente) {
        // Combo dinamico in base al flag patente
        JComboBox<String> tipoBox = new JComboBox<>(haPatente
                ? new String[]{"BICICLETTA", "MOTO", "AUTO", "SCOOTER"}
                : new String[]{"BICICLETTA"});
        JTextField targaField = new JTextField();
        JTextField modelloField = new JTextField();

        JPanel mezzoPanel = new JPanel(new GridLayout(0, 1));
        mezzoPanel.add(new JLabel("Tipo mezzo:"));
        mezzoPanel.add(tipoBox);
        mezzoPanel.add(new JLabel("Targa (solo per mezzi a motore):"));
        mezzoPanel.add(targaField);
        mezzoPanel.add(new JLabel("Modello:"));
        mezzoPanel.add(modelloField);

        int resMezzo = JOptionPane.showConfirmDialog(
            this, mezzoPanel, "Inserisci un mezzo", JOptionPane.OK_CANCEL_OPTION
        );
        if (resMezzo == JOptionPane.OK_OPTION) {
            String tipo = tipoBox.getSelectedItem().toString();
            String targa = targaField.getText();
            String modello = modelloField.getText();

            // Targa obbligatoria per mezzi a motore
            if (!tipo.equals("BICICLETTA") && (targa == null || targa.isBlank())) {
                JOptionPane.showMessageDialog(this, "La targa è obbligatoria per auto, moto e scooter.", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int codiceMezzo = controller.getModel().getNextCodiceMezzo(codiceRider);
            Mezzo mezzo = new Mezzo(
                codiceRider,
                codiceMezzo,
                tipo,
                tipo.equals("BICICLETTA") ? null : targa,
                modello
            );
            controller.getModel().insertMezzo(mezzo);
        }
    }
}
