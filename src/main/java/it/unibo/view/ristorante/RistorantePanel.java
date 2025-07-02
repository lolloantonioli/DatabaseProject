package it.unibo.view.ristorante;

import javax.swing.*;
import java.awt.*;
import it.unibo.controller.Controller;

public class RistorantePanel extends JPanel {

    public RistorantePanel(Controller controller) {
        setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Ordini", new RistoranteOrdiniPanel(controller));
        tabs.addTab("Piatti", new RistorantePiattiPanel(controller));
        tabs.addTab("Promozioni", new RistorantePromozioniPanel(controller));
        tabs.addTab("Recensioni", new RistoranteRecensioniPanel(controller));
        add(tabs, BorderLayout.CENTER);
    }

}
