package it.unibo.view.cliente;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.*;

import it.unibo.controller.Controller;
import it.unibo.data.Pagamento;
import it.unibo.model.DBModel.CarrelloInfo; // Vedi sopra

public class CheckoutPanel extends JPanel {
    private final Controller controller;
    private final JComboBox<Pagamento> comboPagamenti;
    private final JButton btnConferma, btnIndietro;
    private final JLabel lblDettagliOrdine;
    private final JLabel lblTotale;
    private final JLabel lblSconti;
    private final JLabel lblTotaleFinale;

    public CheckoutPanel(final Controller controller) {
        this.controller = controller;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        lblDettagliOrdine = new JLabel();
        lblTotale = new JLabel();
        lblSconti = new JLabel();
        lblTotaleFinale = new JLabel();

        add(new JLabel("Dettaglio Ordine:"));
        add(lblDettagliOrdine);
        add(lblTotale);
        add(lblSconti);
        add(lblTotaleFinale);
        add(Box.createVerticalStrut(10));

        add(new JLabel("Seleziona Metodo di Pagamento:"));
        comboPagamenti = new JComboBox<>();
        add(comboPagamenti);

        final JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnIndietro = new JButton("Annulla");
        btnConferma = new JButton("Conferma Ordine");
        btns.add(btnIndietro);
        btns.add(btnConferma);
        add(btns);

        btnIndietro.addActionListener(e -> controller.goToMenu());
        btnConferma.addActionListener(e -> effettuaOrdine());

        // Quando entri in questo panel, chiama aggiornaDettaglioOrdine()
    }

    /** Da chiamare sempre quando entri nel checkout */
    public void aggiornaDettaglioOrdine() {
        comboPagamenti.removeAllItems();
        int codCliente = controller.getCurrentClienteId();
        CarrelloInfo carrello = controller.getModel().calcolaTotaleCheckout(codCliente);
        List<Pagamento> pagamenti = controller.getModel().getPagamentiCliente(codCliente);

        StringBuilder dettagli = new StringBuilder("<html><ul>");
        carrello.dettagli.forEach(d -> dettagli.append(
            "<li>").append(d.nomePiatto)
                  .append(" x").append(d.quantita)
                  .append(" (").append(d.prezzoUnitario).append("€)")
                  .append("</li>"));
        dettagli.append("</ul></html>");
        lblDettagliOrdine.setText(dettagli.toString());
        lblTotale.setText("Totale Parziale: € " + carrello.totaleParziale);
        lblSconti.setText("<html>Sconto Promozioni: <b>-€ " + carrello.scontoPromozioni + "</b> (" + carrello.descScontoPromozioni + ")<br>"
                + "Sconto Raccolta Punti: <b>-€ " + carrello.scontoPunti + "</b> (" + carrello.descScontoPunti + ")</html>");
        lblTotaleFinale.setText("<html><h2>Totale da Pagare: € " + carrello.totaleFinale + "</h2></html>");

        for (Pagamento p : pagamenti) comboPagamenti.addItem(p);
    }

    private void effettuaOrdine() {
        Pagamento selezionato = (Pagamento) comboPagamenti.getSelectedItem();
        if (selezionato == null) {
            JOptionPane.showMessageDialog(this, "Seleziona un metodo di pagamento!");
            return;
        }
        try {
            controller.getModel().creaOrdineCompleto(
                controller.getCurrentClienteId(),
                selezionato
                // altri dati come indirizzo selezionato se serve
            );
            JOptionPane.showMessageDialog(this, "Ordine effettuato! Ti arriverà una conferma.");
            controller.goToMenu();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore nell'effettuare l'ordine: " + ex.getMessage());
        }
    }
}
