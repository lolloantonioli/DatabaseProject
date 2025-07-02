package it.unibo.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import it.unibo.controller.Controller;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {

    private static final String CLIENTE = "Cliente";
    private static final String RISTORANTE = "Ristorante";
    private static final String RIDER = "Rider";
    private static final String AMMINISTRATORE = "Amministratore";

    public MenuPanel(Controller controller) {
        setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 40, 10, 40);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        final JButton btnCliente = new JButton(CLIENTE);
        final JButton btnRistorante = new JButton(RISTORANTE);
        final JButton btnRider = new JButton(RIDER);
        final JButton btnAmministratore = new JButton(AMMINISTRATORE);

        btnCliente.addActionListener(e -> controller.goToClienteAccess());
        btnRistorante.addActionListener(e -> {
            System.out.println("Cliccato Ristorante!"); // DEBUG
            try {
                controller.goToRistoranteAccess();
            } catch (Exception ex) {
                System.out.println("Errore nel goToRistoranteAccess: " + ex.getMessage()); // DEBUG
                ex.printStackTrace();
            }
        });
        btnRider.addActionListener(e -> controller.goToRiderAccess());
        btnAmministratore.addActionListener(e -> controller.goToAmministratore());

        final JPanel buttonsPanel = new JPanel(new GridLayout(4, 1, 0, 20));
        buttonsPanel.add(btnCliente);
        buttonsPanel.add(btnRistorante);
        buttonsPanel.add(btnRider);
        buttonsPanel.add(btnAmministratore);
        buttonsPanel.setOpaque(false);

        add(buttonsPanel, gbc);
    }

}
