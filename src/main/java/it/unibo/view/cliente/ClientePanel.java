package it.unibo.view.cliente;

import it.unibo.controller.Controller;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ClientePanel extends JPanel {

    private final BorderLayout layout;
    private final JTabbedPane tabbedPane;
    private final TrovaRistorantiPanel trovaRistorantiPanel;
    private final CarrelloPanel carrelloPanel;
    private final ProfiloPanel profiloPanel;
    private final ViewPiattiPanel piattiPanel;

    public ClientePanel(final Controller controller) {
        this.layout = new BorderLayout();
        this.tabbedPane = new JTabbedPane();
        this.trovaRistorantiPanel = new TrovaRistorantiPanel(controller);
        this.carrelloPanel = new CarrelloPanel(controller);
        this.profiloPanel = new ProfiloPanel(controller);
        this.piattiPanel = new ViewPiattiPanel(controller, carrelloPanel);
        setLayout(layout);
        tabbedPane.addTab("Trova Ristoranti", trovaRistorantiPanel);
        tabbedPane.addTab("Carrello", carrelloPanel);
        tabbedPane.addTab("Profilo", profiloPanel);
        System.out.println("Aggiunto tab");
        add(tabbedPane, BorderLayout.CENTER);
    }

    public void aggiornaIndirizzi() {
        trovaRistorantiPanel.aggiornaIndirizzi();
    }

}
