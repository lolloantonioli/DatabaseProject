package it.unibo.view.rider;

import javax.swing.*;
import java.awt.*;

public class RiderOrdiniPanel extends JPanel {
    public final RiderOrdiniPreparazionePanel tabPreparazione;
    public final RiderOrdineInCaricoPanel tabInCarico;

    public RiderOrdiniPanel(it.unibo.controller.Controller controller) {
        setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();

        tabPreparazione = new RiderOrdiniPreparazionePanel(controller, this);
        tabInCarico = new RiderOrdineInCaricoPanel(controller, this);

        tabs.addTab("Ordini in preparazione", tabPreparazione);
        tabs.addTab("Ordine in carico", tabInCarico);

        add(tabs, BorderLayout.CENTER);
    }

    // Metodo comodo per aggiornare entrambi i tab insieme
    public void aggiorna() {
        tabPreparazione.aggiorna();
        tabInCarico.aggiorna();
    }
}
