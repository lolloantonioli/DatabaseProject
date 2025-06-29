package it.unibo.view.rider;

import javax.swing.*;
import java.awt.*;
import it.unibo.controller.Controller;

public class RiderPanel extends JPanel {

    private final RiderProfiloPanel profiloPanel;

    public RiderPanel(Controller controller) {
        setLayout(new BorderLayout());
        profiloPanel = new RiderProfiloPanel(controller);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Profilo", profiloPanel);
        tabbedPane.addTab("Ordini", new JPanel(new BorderLayout()) {{
            add(new JLabel("Gestione ordini in arrivo... (work in progress)", SwingConstants.CENTER), BorderLayout.CENTER);
        }});
        add(tabbedPane, BorderLayout.CENTER);
    }

    public RiderProfiloPanel getRiderProfiloPanel() {
        return profiloPanel;
    }
    
}
