package it.unibo.view.cliente;

import it.unibo.controller.Controller;
import it.unibo.data.Piatto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewPiattiPanel extends JPanel {
    private final JTable piattiTable;
    private final JButton btnAggiungiAlCarrello;
    private List<Piatto> piattiList;

    public ViewPiattiPanel(Controller controller, CarrelloPanel carrelloPanel) {

        setLayout(new BorderLayout());

        // Modello tabella non editabile
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Nome", "Prezzo", "Descrizione"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        piattiTable = new JTable(tableModel);
        piattiTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(piattiTable);
        add(scrollPane, BorderLayout.CENTER);

        btnAggiungiAlCarrello = new JButton("Aggiungi al Carrello");
        add(btnAggiungiAlCarrello, BorderLayout.SOUTH);

        btnAggiungiAlCarrello.addActionListener(e -> {
            int row = piattiTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Seleziona un piatto!");
                return;
            }
            String quantitaStr = JOptionPane.showInputDialog(this, "Quantità:");
            if (quantitaStr == null) return; // annullato
            int quantita;
            try {
                quantita = Integer.parseInt(quantitaStr);
                if (quantita <= 0) throw new NumberFormatException();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Quantità non valida.");
                return;
            }
            Piatto piatto = piattiList.get(row);
            controller.getModel().aggiungiAlCarrello(piatto, quantita);
            if (carrelloPanel != null) {
                carrelloPanel.aggiornaCarrello();
            }
            JOptionPane.showMessageDialog(this, "Piatto aggiunto al carrello!");
        });
    }

    /** 
     * Mostra la lista dei piatti e popola la tabella.
     * Puoi chiamare questo metodo ogni volta che vuoi aggiornare la tabella.
     */
    public void mostraPiatti(List<Piatto> piatti) {
        this.piattiList = piatti;
        DefaultTableModel model = (DefaultTableModel) piattiTable.getModel();
        model.setRowCount(0);
        for (Piatto p : piatti) {
            model.addRow(new Object[]{
                p.nome,
                String.format("€ %.2f", p.prezzo),
                p.descrizione
            });
        }
    }
}
