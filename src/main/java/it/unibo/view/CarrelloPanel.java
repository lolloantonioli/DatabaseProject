package it.unibo.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import it.unibo.controller.Controller;

public class CarrelloPanel extends JPanel {

    private final JTable cartTable;
    private final JLabel lblTotale;
    private final JButton btnCheckout;
    private final Controller controller;

    public CarrelloPanel(final Controller controller) {
        this.cartTable = new JTable();
        this.lblTotale = new JLabel();
        this.btnCheckout = new JButton("Vai al Checkout");
        this.controller = controller;
        setLayout(new BorderLayout());
        
        add(new JScrollPane(cartTable), BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(lblTotale);
        south.add(btnCheckout);
        add(south, BorderLayout.SOUTH);

        // Quando clicca, passa al pannello di Checkout
        btnCheckout.addActionListener(e ->
            mainLayout.show(rootPanel, "checkoutCard")
        );
    }
}
