package it.unibo.view.cliente;

import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.*;

import it.unibo.controller.Controller;
import it.unibo.data.MetodoPagamento;
import it.unibo.data.Pagamento;
import it.unibo.data.StatoOrdine;
import it.unibo.model.CarrelloInfo;

public class CheckoutPanel extends JPanel {
    private final Controller controller;
    private final JComboBox<MetodoPagamento> comboPagamenti;
    private final JButton btnConferma, btnIndietro;
    private final JLabel lblDettagliOrdine, lblTotale, lblSconti, lblTotaleFinale;

    public CheckoutPanel(final Controller controller) {
        this.controller = controller;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        lblDettagliOrdine = new JLabel();
        lblTotale          = new JLabel();
        lblSconti         = new JLabel();
        lblTotaleFinale   = new JLabel();

        add(new JLabel("Dettaglio Ordine:"));
        add(lblDettagliOrdine);
        add(lblTotale);
        add(lblSconti);
        add(lblTotaleFinale);
        add(Box.createVerticalStrut(10));

        add(new JLabel("Seleziona Metodo di Pagamento:"));
        comboPagamenti = new JComboBox<>();
        add(comboPagamenti);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnIndietro  = new JButton("Annulla");
        btnConferma  = new JButton("Conferma Ordine");
        btns.add(btnIndietro);
        btns.add(btnConferma);
        add(btns);

        btnIndietro.addActionListener(e ->
            controller.goToCliente(controller.getCurrentClienteId()));

        btnConferma.addActionListener(e -> effettuaOrdine());
    }

    /** Da chiamare ogni volta che arrivi su questo card */
    public void aggiornaDettaglioOrdine() {
        comboPagamenti.removeAllItems();

        int codCliente = controller.getCurrentClienteId();
        CarrelloInfo carrello = controller.getModel().calcolaTotaleCheckout(codCliente);
        List<MetodoPagamento> metodi = controller.getModel().loadMetodiPagamentoByCliente(codCliente);

        // dettaglio
        StringBuilder sb = new StringBuilder("<html><ul>");
        for (var d : carrello.dettagli) {
            sb.append("<li>")
              .append(d.nomePiatto).append(" x").append(d.quantita)
              .append(" @").append(d.prezzoUnitario).append("€")
              .append("</li>");
        }
        sb.append("</ul></html>");
        lblDettagliOrdine.setText(sb.toString());

        lblTotale.setText(
            String.format("Totale Parziale: € %.2f", carrello.totaleParziale));
        lblSconti.setText(String.format(
            "<html>Sconto Promozioni: <b>-€ %.2f</b> (%s)<br>"
          + "Sconto Punti: <b>-€ %.2f</b> (%s)</html>",
            carrello.scontoPromozioni,
            carrello.descScontoPromozioni,
            carrello.scontoPunti,
            carrello.descScontoPunti));

        lblTotaleFinale.setText(
            String.format("<html><h2>Totale da Pagare: € %.2f</h2></html>",
            carrello.totaleFinale));

        metodi.forEach(comboPagamenti::addItem);
    }

    private void effettuaOrdine() {
        var metodo = (MetodoPagamento) comboPagamenti.getSelectedItem();
        if (metodo == null) {
            JOptionPane.showMessageDialog(this, "Seleziona un metodo di pagamento!");
            return;
        }

        try {
            int codCliente = controller.getCurrentClienteId();
            CarrelloInfo carrello = controller.getModel().calcolaTotaleCheckout(codCliente);

            if(carrello.puntiUsati > 0) {
                controller.getModel().sottraiPunti(codCliente, carrello.puntiUsati);
            }

            
            int codPagamento = controller.getModel().insertPagamento(
                new Pagamento(Date.valueOf(LocalDate.now()), carrello.totaleFinale, codCliente, metodo.nome));
            int codOrdine = controller.getModel().insertOrdine(codPagamento, carrello.totaleFinale, controller.getOrderPiva());
            
            var piatti = controller.getModel().loadPiattiByRistorante(controller.getOrderPiva());
            
            for (var dettaglio : carrello.dettagli) {
                String nomePiatto = dettaglio.nomePiatto;
                var piattoOpt = piatti.stream()
                .filter(p -> p.nome.equals(nomePiatto))
                .findFirst();
                if (piattoOpt.isPresent()) {
                    int codicePiatto = piattoOpt.get().codicePiatto;
                int quantita = dettaglio.quantita;
                double prezzo = dettaglio.prezzoUnitario;
                controller.getModel().insertDettaglioOrdine(codOrdine, codicePiatto, quantita, prezzo);
            } else {
                System.out.println("Piatto non trovato: " + nomePiatto);
            }
            }
            
            controller.getModel().insertState(new StatoOrdine(codOrdine, LocalDateTime.now(), true, LocalDateTime.now(),
            false, null, false, null, -1));

            // Calcola i punti da generare (1 punto ogni euro speso, arrotondato per difetto)
            int puntiDaAggiungere = (int) Math.floor(carrello.totaleFinale);
            controller.getModel().aggiungiPunti(codCliente, puntiDaAggiungere);

            JOptionPane.showMessageDialog(this, "Ordine effettuato!");
            controller.goToCliente(controller.getCurrentClienteId());
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Errore durante il checkout: " + ex.getMessage(),
                "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}
