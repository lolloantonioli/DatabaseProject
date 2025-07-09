package it.unibo.view.amministratore;

import javax.swing.*;
import java.awt.*;
import it.unibo.controller.Controller;

public class AmministratorePanel extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public AmministratorePanel(Controller controller) {
        this.cardLayout = new CardLayout();
        this.mainPanel = new JPanel(cardLayout);

        // LOGIN PANEL
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        JLabel label = new JLabel("Password amministratore:");
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginBtn = new JButton("Accedi");
        gbc.gridx = 0; gbc.gridy = 0;
        loginPanel.add(label, gbc);
        gbc.gridy = 1;
        loginPanel.add(passwordField, gbc);
        gbc.gridy = 2;
        loginPanel.add(loginBtn, gbc);

        // TABBED PANE
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Gestione Ristoranti", new GestioneRistorantiPanel(controller));
        tabs.addTab("Rider Top Consegne", new TopRiderConsegnePanel(controller));
        tabs.addTab("Ristoranti Top Spesa", new RistorantiTopSpesaPanel(controller));
        tabs.addTab("Top 10 Recensioni", new Top10RecensioniPanel(controller));

        mainPanel.add(loginPanel, "login");
        mainPanel.add(tabs, "tabs");
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        loginBtn.addActionListener(e -> {
            String password = new String(passwordField.getPassword());
            if ("amministratore".equals(password)) {
                cardLayout.show(mainPanel, "tabs");
            } else {
                JOptionPane.showMessageDialog(this, "Password errata!", "Errore", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        });
    }
}
