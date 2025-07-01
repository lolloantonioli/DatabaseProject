package it.unibo.view.rider;

import javax.swing.*;
import java.awt.*;
import it.unibo.controller.Controller;

public class RiderPanel extends JPanel {

    private final RiderProfiloPanel profiloPanel;
    private final RiderOrdiniPanel ordiniPanel;

    public RiderPanel(Controller controller) {
        setLayout(new BorderLayout());
        profiloPanel = new RiderProfiloPanel(controller);
        ordiniPanel = new RiderOrdiniPanel(controller);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Profilo", profiloPanel);
        tabbedPane.addTab("Ordini", ordiniPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    public RiderProfiloPanel getRiderProfiloPanel() {
        return profiloPanel;
    }

    public RiderOrdiniPanel getRiderOrdiniPanel() {
        return ordiniPanel;
    }
}
