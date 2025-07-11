package it.unibo.view.ristorante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import it.unibo.controller.Controller;
import it.unibo.data.Ordine;
import it.unibo.data.DettaglioOrdine;

class RistoranteOrdiniPanel extends JPanel {
    private final Controller controller;
    private final DefaultTableModel model;
    private final JTable table;

    public RistoranteOrdiniPanel(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new String[]{"Codice Ordine", "Totale", "Cliente", "Stato"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);

        refreshTable();

        JButton btnDettaglio = new JButton("Dettaglio Ordine");
        btnDettaglio.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int codice = (int)model.getValueAt(row, 0);
                Ordine ordine = controller.getModel().findOrdine(codice).get();
                StringBuilder sb = new StringBuilder();
                sb.append("Codice Ordine: ").append(ordine.codiceOrdine).append("\n");
                sb.append("Totale: ").append(ordine.prezzoTotale).append("\n");
                for (DettaglioOrdine d : controller.getModel().loadDettagliByOrdine(ordine.codiceOrdine)) {
                    sb.append("- ").append(d.codicePiatto).append(" x").append(d.quantita).append("\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString(), "Dettaglio Ordine", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnDettaglio, BorderLayout.SOUTH);
    }
    public void refreshTable() {
        model.setRowCount(0);
        List<Ordine> ordini = controller.getModel().loadOrdiniByRistorante(controller.getCurrentPiva());
        for (Ordine o : ordini) {
            var cliente = controller.getModel().findClienteByOrder(o.codiceOrdine);
            var stato = controller.getModel().findStateByOrder(o.codiceOrdine);
            String lblstate;
            if (cliente.isPresent() && stato.isPresent()) {
                if (stato.get().consegnato == true) {
                    lblstate = "Consegnato il " + stato.get().oraConsegnato.toString();
                } else if (stato.get().inConsegna == true) {
                    lblstate = "In consegna dal " + stato.get().oraInConsegna.toString();
                } else if (stato.get().inPreparazione == true) {
                    lblstate = "In preparazione dal " + stato.get().oraInPreparazione.toString();
                } else if (stato.get().data != null) {
                    lblstate = "Creato il " + stato.get().data.toLocalDate().toString();
                } else {
                    lblstate = "Stato sconosciuto";
                }
                model.addRow(new Object[]{o.codiceOrdine, o.prezzoTotale, cliente.get().codiceCliente, lblstate});
            }
        }
    }
}
