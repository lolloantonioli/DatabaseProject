 package it.unibo.view;
 
import it.unibo.controller.Controller;
import it.unibo.view.ristorante.MenuPanel;
import it.unibo.view.ristorante.OrdiniPanel;
import it.unibo.view.ristorante.StatistichePanel;
 
 import javax.swing.JPanel;
 import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
 
/**
+ * Pannello principale per il ristoratore.
+ * Contiene varie schede per la gestione del locale.
+ */
 public class RistorantePanel extends JPanel {
   private final JTabbedPane tabbedPane;
   private final MenuPanel menuPanel;
   private final OrdiniPanel ordiniPanel;
   private final StatistichePanel statistichePanel;

    public RistorantePanel(final Controller controller) {
        setLayout(new BorderLayout());
        this.tabbedPane = new JTabbedPane();
        this.menuPanel = new MenuPanel();
        this.ordiniPanel = new OrdiniPanel();
       this.statistichePanel = new StatistichePanel();
 
       tabbedPane.addTab("Menu", menuPanel);
       tabbedPane.addTab("Ordini", ordiniPanel);
       tabbedPane.addTab("Statistiche", statistichePanel);
        add(tabbedPane, BorderLayout.CENTER);
    }
 }
