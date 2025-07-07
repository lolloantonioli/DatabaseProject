package it.unibo.view.ristorante;

import javax.swing.*;
import java.awt.*;
import it.unibo.controller.Controller;

public class RistorantePanel extends JPanel {
    private final RistoranteOrdiniPanel ordiniPanel;
    private final RistorantePiattiPanel piattiPanel;
    private final RistorantePromozioniPanel promozioniPanel;
    private final RistoranteRecensioniPanel recensioniPanel;

    public RistorantePanel(final Controller controller) {
        setLayout(new BorderLayout());
        
        // Inizializza i pannelli
        this.ordiniPanel = new RistoranteOrdiniPanel(controller);
        this.piattiPanel = new RistorantePiattiPanel(controller);
        this.promozioniPanel = new RistorantePromozioniPanel(controller);
        this.recensioniPanel = new RistoranteRecensioniPanel(controller);
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Ordini", ordiniPanel);
        tabs.addTab("Piatti", piattiPanel);
        tabs.addTab("Promozioni", promozioniPanel);
        tabs.addTab("Recensioni", recensioniPanel);
        
        // Aggiungi listener per il cambio di tab
        tabs.addChangeListener(e -> {
            int selectedIndex = tabs.getSelectedIndex();
            switch (selectedIndex) {
                case 0: ordiniPanel.refreshTable(); break;
                case 1: piattiPanel.refreshTable(); break;
                case 2: promozioniPanel.refreshTable(); break;
                case 3: recensioniPanel.refreshTable(); break;
            }
        });
        
        add(tabs, BorderLayout.CENTER);
    }

    public RistoranteRecensioniPanel getRecensioniPanel() {
        return this.recensioniPanel;
    }
    
    // Metodo per aggiornare tutti i pannelli quando cambia il ristorante
    public void refreshAllPanels() {        
        ordiniPanel.refreshTable();
        piattiPanel.refreshTable();
        promozioniPanel.refreshTable();
        recensioniPanel.refreshTable();
    }
}