package it.unibo.view.amministratore;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import it.unibo.controller.Controller;

class RistorantiTopSpesaPanel extends JPanel {

    public RistorantiTopSpesaPanel(Controller controller) {
        setLayout(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"P.IVA", "Spesa Media"}, 0);
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
        model.setRowCount(0);
        List<Object[]> results = controller.getModel().loadRistorantiTopSpesaMedia();
        for (Object[] row : results)
            model.addRow(row);
    }
}
