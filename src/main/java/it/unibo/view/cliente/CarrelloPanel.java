package it.unibo.view.cliente;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import it.unibo.controller.Controller;
import it.unibo.model.RigaCarrello;

public class CarrelloPanel extends JPanel {

    private final JTable cartTable;
    private final JLabel lblTotale;
    private final JButton btnCheckout;
    private final Controller controller;
    private final DefaultTableModel modelTable;

    public CarrelloPanel(final Controller controller) {
        this.controller = controller;
        this.modelTable = new DefaultTableModel(new Object[]{"Piatto", "Prezzo", "Quantità", "Totale"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        this.cartTable = new JTable(modelTable);
        this.lblTotale = new JLabel();
        this.btnCheckout = new JButton("Vai al Checkout");

        setLayout(new BorderLayout());
        add(new JScrollPane(cartTable), BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(lblTotale);
        south.add(btnCheckout);
        add(south, BorderLayout.SOUTH);

        btnCheckout.addActionListener(e -> {
            controller.goToCheckout();
        });
    }

    // Metodo pubblico per aggiornare il carrello
    public void aggiornaCarrello() {
        modelTable.setRowCount(0);
        double totale = 0.0;
        for (RigaCarrello riga : controller.getModel().getCarrello()) {
            double sub = riga.piatto.prezzo.doubleValue() * riga.quantita;
            modelTable.addRow(new Object[]{
                riga.piatto.nome,
                String.format("€ %.2f", riga.piatto.prezzo),
                riga.quantita,
                String.format("€ %.2f", sub)
            });
            totale += sub;
        }
        lblTotale.setText("Totale: € " + String.format("%.2f", totale));
    }
}
