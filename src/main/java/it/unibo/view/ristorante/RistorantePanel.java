package it.unibo.view.ristorante;

import javax.swing.*;
import java.awt.*;
import it.unibo.controller.Controller;

public class RistorantePanel extends JPanel {

    private final RistoranteRecensioniPanel ristoranteRecensioniPanel;

    public RistorantePanel(Controller controller) {
        this.ristoranteRecensioniPanel = new RistoranteRecensioniPanel(controller);
        setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Ordini", new RistoranteOrdiniPanel(controller));
        tabs.addTab("Piatti", new RistorantePiattiPanel(controller));
        tabs.addTab("Promozioni", new RistorantePromozioniPanel(controller));
        tabs.addTab("Recensioni", ristoranteRecensioniPanel);
        add(tabs, BorderLayout.CENTER);
    }

    public RistoranteRecensioniPanel getRecensioniPanel() {
        return this.ristoranteRecensioniPanel;
    }

}
