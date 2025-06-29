
package it.unibo.view.ristorante;

import it.unibo.controller.Controller;

import javax.swing.*;
import java.awt.*;

/**
 * Pannello che mostra semplici statistiche sul ristorante.
 */
public class StatistichePanel extends JPanel {

    private final Controller controller;
    private final String piva;
    private final JLabel lblOrdini = new JLabel();
    private final JLabel lblRecensioni = new JLabel();
    private final JLabel lblVisualizzazioni = new JLabel();

    public StatistichePanel(Controller controller, String piva) {
        this.controller = controller;
        this.piva = piva;
        setLayout(new GridLayout(3,1,5,5));
        add(lblOrdini);
        add(lblRecensioni);
        add(lblVisualizzazioni);
        aggiorna();
    }

    private void aggiorna() {
        try {
            int numOrdini = controller.getModel().loadOrdiniByRistorante(piva).size();
            int numRec = controller.getModel().loadRecensioniByRistorante(piva).size();
            int numVis = controller.getModel().loadVisualizzazioniRistorante(piva);
            lblOrdini.setText("Ordini totali: " + numOrdini);
            lblRecensioni.setText("Recensioni totali: " + numRec);
            lblVisualizzazioni.setText("Visualizzazioni: " + numVis);
        } catch (Exception e) {
            lblOrdini.setText("Errore nel caricamento statistiche");
        }
    }
}
