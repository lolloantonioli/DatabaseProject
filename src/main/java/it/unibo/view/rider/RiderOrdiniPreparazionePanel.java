package it.unibo.view.rider;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import it.unibo.controller.Controller;
import it.unibo.data.Ordine;

public class RiderOrdiniPreparazionePanel extends JPanel {
    private final Controller controller;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JButton btnPrendi;
    private final RiderOrdiniPanel mainPanel;

    public RiderOrdiniPreparazionePanel(Controller controller, RiderOrdiniPanel mainPanel) {
        this.controller = controller;
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"Codice", "Ristorante", "Totale"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        
        btnPrendi = new JButton("Prendi in carico");

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnPrendi, BorderLayout.SOUTH);

        btnPrendi.addActionListener(e -> prendiInCarico());
    }

    public void aggiorna() {
        tableModel.setRowCount(0);
        int zona = controller.getModel().findRider(controller.getCurrentRiderId()).get().codiceZona;
        List<Ordine> ordini = controller.getModel().inPreparazioneByZona(zona);
        for (Ordine o : ordini) {
            tableModel.addRow(new Object[]{o.codiceOrdine, o.piva, o.prezzoTotale + "€"});
        }
    }

    private void prendiInCarico() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un ordine.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int codOrdine = (int) tableModel.getValueAt(row, 0);
        controller.getModel().prendiInCaricoOrdine(codOrdine, controller.getCurrentRiderId());
        mainPanel.aggiorna(); // aggiorna anche tab 2!
    }
}
