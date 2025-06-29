package it.unibo.view.rider;

import javax.swing.*;
import java.awt.*;
import it.unibo.controller.Controller;

public class RiderPanel extends JPanel {
    public RiderPanel(Controller controller) {
        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Profilo", new RiderProfiloPanel(controller));
        tabbedPane.addTab("Ordini", new JPanel(new BorderLayout()) {{
            add(new JLabel("Gestione ordini in arrivo... (work in progress)", SwingConstants.CENTER), BorderLayout.CENTER);
        }});
        add(tabbedPane, BorderLayout.CENTER);
    }
}
