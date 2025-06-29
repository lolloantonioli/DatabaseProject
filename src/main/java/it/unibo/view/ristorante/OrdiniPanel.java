package it.unibo.view.ristorante;

import it.unibo.controller.Controller;
import it.unibo.data.Ordine;
import it.unibo.data.StatoOrdine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

/**
 * Pannello per visualizzare gli ordini ricevuti dal ristorante.
 */
public class OrdiniPanel extends JPanel {

    private final Controller controller;
    private final String piva;
    private final DefaultTableModel model;

    public OrdiniPanel(Controller controller, String piva) {
        this.controller = controller;
        this.piva = piva;
        setLayout(new BorderLayout());

        this.model = new DefaultTableModel(new Object[]{"Codice", "Totale", "Stato"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        JButton btnReload = new JButton("Aggiorna");

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnReload, BorderLayout.SOUTH);

        btnReload.addActionListener(e -> caricaOrdini());

        caricaOrdini();
    }

    private void caricaOrdini() {
        model.setRowCount(0);
        try {
            List<Ordine> ordini = controller.getModel().loadOrdiniByRistorante(piva);
            for (Ordine o : ordini) {
                model.addRow(new Object[]{o.codiceOrdine, "€ " + o.prezzoTotale, descrizioneStato(o.codiceOrdine)});
            }
            if (ordini.isEmpty()) {
                model.addRow(new Object[]{"Nessun ordine", "", ""});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore caricamento ordini: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String descrizioneStato(int codiceOrdine) {
        Optional<StatoOrdine> s = controller.getModel().findStateByOrder(codiceOrdine);
        if (s.isEmpty()) { return "-"; }
        StatoOrdine st = s.get();
        if (st.consegnato != null) return "Consegnato";
        if (st.inConsegna != null) return "In consegna";
        if (st.inPreparazione != null) return "In preparazione";
        return "Creato";
    }
}
