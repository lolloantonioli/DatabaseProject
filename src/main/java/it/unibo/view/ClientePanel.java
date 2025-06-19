package it.unibo.view;

import it.unibo.controller.Controller;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ClientePanel extends JPanel {

    private final Controller controller;
    private final BorderLayout layout;
    private final JTabbedPane tabbedPane;
    private final TrovaRistorantiPanel trovaRistorantiPanel;
    private final CarrelloPanel carrelloPanel;
    private final ProfiloPanel profiloPanel;

    public ClientePanel(final Controller controller) {
        this.controller = controller;
        this.layout = new BorderLayout();
        this.tabbedPane = new JTabbedPane();
        this.trovaRistorantiPanel = new TrovaRistorantiPanel();
        this.carrelloPanel = new CarrelloPanel(controller);
        this.profiloPanel = new ProfiloPanel();
        setLayout(layout);
        tabbedPane.addTab("Trova Ristoranti", trovaRistorantiPanel);
        tabbedPane.addTab("Carrello", carrelloPanel);
        tabbedPane.addTab("Profilo", profiloPanel);
        add(tabbedPane, BorderLayout.CENTER);
    }

}
