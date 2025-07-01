package it.unibo.view.rider;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;
import it.unibo.controller.Controller;
import it.unibo.data.Ordine;

public class RiderOrdineInCaricoPanel extends JPanel {
    private final Controller controller;
    private final JLabel lblOrdine;
    private final JButton btnConsegnato;
    private final RiderOrdiniPanel mainPanel;

    public RiderOrdineInCaricoPanel(Controller controller, RiderOrdiniPanel mainPanel) {
        this.controller = controller;
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());

        lblOrdine = new JLabel("Nessun ordine in carico");
        lblOrdine.setHorizontalAlignment(SwingConstants.CENTER);
        btnConsegnato = new JButton("Consegna ordine");
        btnConsegnato.setEnabled(false);

        add(lblOrdine, BorderLayout.CENTER);
        add(btnConsegnato, BorderLayout.SOUTH);

        btnConsegnato.addActionListener(e -> consegnaOrdine());
    }

    public void aggiorna() {
        Optional<Ordine> opt = controller.getModel().ordineInCaricoByRider(controller.getCurrentRiderId());
        if (opt.isPresent()) {
            Ordine o = opt.get();
            lblOrdine.setText("<html>Ordine #" + o.codiceOrdine +
                "<br>Ristorante: " + o.piva +
                "<br>Totale: " + o.prezzoTotale +
                "</html>");
            btnConsegnato.setEnabled(true);
        } else {
            lblOrdine.setText("Nessun ordine in carico");
            btnConsegnato.setEnabled(false);
        }
    }

    private void consegnaOrdine() {
        Optional<Ordine> opt = controller.getModel().ordineInCaricoByRider(controller.getCurrentRiderId());
        if (opt.isEmpty()) return;
        int codOrdine = opt.get().codiceOrdine;
        controller.getModel().consegnaOrdine(codOrdine, controller.getCurrentRiderId());
        mainPanel.aggiorna(); // aggiorna anche tab 1!
    }
}
