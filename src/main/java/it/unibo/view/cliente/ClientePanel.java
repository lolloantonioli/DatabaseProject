package it.unibo.view.cliente;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import it.unibo.controller.Controller;

public class ClientePanel extends JPanel {
    private final JTabbedPane tabbedPane;
    private final TrovaRistorantiPanel trovaRistorantiPanel;
    private final CarrelloPanel carrelloPanel;
    private final ProfiloPanel profiloPanel;

    public ClientePanel(final Controller controller) {
        this.tabbedPane = new JTabbedPane();
        this.carrelloPanel = new CarrelloPanel(controller);
        this.trovaRistorantiPanel = new TrovaRistorantiPanel(controller, carrelloPanel);
        this.profiloPanel = new ProfiloPanel(controller);
        setLayout(new BorderLayout());
        tabbedPane.addTab("Trova Ristoranti", trovaRistorantiPanel);
        tabbedPane.addTab("Carrello", carrelloPanel);
        tabbedPane.addTab("Profilo", profiloPanel);
        add(tabbedPane, BorderLayout.CENTER);
    }
    public void aggiornaIndirizzi() {
        trovaRistorantiPanel.aggiornaIndirizzi();
    }
}
