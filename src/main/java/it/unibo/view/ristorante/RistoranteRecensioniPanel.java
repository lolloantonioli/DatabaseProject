package it.unibo.view.ristorante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import it.unibo.controller.Controller;
import it.unibo.data.Recensione;

class RistoranteRecensioniPanel extends JPanel {
    private final Controller controller;
    private final DefaultTableModel model;
    private final JTable table;

    public RistoranteRecensioniPanel(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new String[]{"Cliente", "Titolo", "Stelle", "Descrizione", "Data"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        refreshTable();

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<Recensione> recs = controller.getModel().loadRecensioniByRistorante(controller.getCurrentPiva());
        for(Recensione r : recs)
            model.addRow(new Object[]{r.codiceCliente, r.titolo, r.numeroStelle, r.descrizione, r.data});
    }
}
