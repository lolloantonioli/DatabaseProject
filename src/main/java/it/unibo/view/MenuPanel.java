package it.unibo.view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import it.unibo.controller.Controller;

public class MenuPanel extends JPanel {

    private static final String CLIENTE = "Cliente";
    private static final String RISTORANTE = "Ristorante";
    private static final String RIDER = "Rider";
    private static final String AMMINISTRATORE = "Amministratore";

    public MenuPanel(Controller controller) {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Header
        JLabel title = new JLabel("PL8");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 48));
        title.setForeground(new Color(30, 144, 255));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(title, BorderLayout.NORTH);

        // Bottone factory
        JButton[] buttons = {
            makeButton(CLIENTE, e -> controller.goToClienteAccess()),
            makeButton(RISTORANTE, e -> controller.goToRistoranteAccess()),
            makeButton(RIDER, e -> controller.goToRiderAccess()),
            makeButton(AMMINISTRATORE, e -> controller.goToAmministratore())
        };

        // Container bottoni
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(0, 200, 0, 200));
        for (JButton btn : buttons) {
            center.add(btn);
            center.add(Box.createVerticalStrut(20));
        }
        add(center, BorderLayout.CENTER);
    }

    private JButton makeButton(String text, ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(30, 144, 255));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.addActionListener(listener);
        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(65, 105, 225)); // RoyalBlue
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(30, 144, 255)); // DodgerBlue
            }
        });
        return btn;
    }
}
