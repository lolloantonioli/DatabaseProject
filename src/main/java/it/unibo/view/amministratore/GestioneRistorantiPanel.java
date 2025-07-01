package it.unibo.view.amministratore;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import it.unibo.controller.Controller;
import it.unibo.data.Ristorante;

class GestioneRistorantiPanel extends JPanel {
    private final Controller controller;
    private final DefaultTableModel model;
    private final JTable table;

    public GestioneRistorantiPanel(Controller controller) {
        this.controller = controller;
        this.setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"P.IVA", "Nome", "Indirizzo", "Orario", "Cod. Zona"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);

        refreshTable();

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<Ristorante> lista = controller.getModel().listAllRistoranti();
        for (Ristorante r : lista) {
            model.addRow(new Object[]{r.piva, r.nome, r.indirizzo, r.orario, r.codiceZona});
        }
    }
}
