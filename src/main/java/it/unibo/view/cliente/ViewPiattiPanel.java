package it.unibo.view.cliente;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import it.unibo.controller.Controller;
import it.unibo.data.Piatto;

public class ViewPiattiPanel extends JPanel {
    private final Controller controller;
    private final CarrelloPanel carrelloPanel;
    private final JTable piattiTable;
    private final JButton btnAggiungiAlCarrello;
    private List<Piatto> piattiList;

    public ViewPiattiPanel(Controller controller, CarrelloPanel carrelloPanel) {
        this.controller = controller;
        this.carrelloPanel = carrelloPanel;
        setLayout(new BorderLayout());

        piattiTable = new JTable(new String[][]{}, new String[]{"Nome", "Prezzo", "Descrizione"});
        add(new JScrollPane(piattiTable), BorderLayout.CENTER);

        btnAggiungiAlCarrello = new JButton("Aggiungi al Carrello");
        add(btnAggiungiAlCarrello, BorderLayout.SOUTH);

        btnAggiungiAlCarrello.addActionListener(e -> {
            int row = piattiTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Seleziona un piatto!");
                return;
            }
            String quantitaStr = JOptionPane.showInputDialog(this, "Quantità:");
            int quantita;
            try {
                quantita = Integer.parseInt(quantitaStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Quantità non valida.");
                return;
            }
            Piatto piatto = piattiList.get(row);
            controller.getModel().aggiungiAlCarrello(piatto, quantita);
            carrelloPanel.aggiornaCarrello();
            JOptionPane.showMessageDialog(this, "Piatto aggiunto al carrello!");
        });
    }

    // Metodo da chiamare per popolare la tabella
    public void mostraPiatti(List<Piatto> piatti) {
        this.piattiList = piatti;
        String[][] data = new String[piatti.size()][3];
        for (int i = 0; i < piatti.size(); i++) {
            data[i][0] = piatti.get(i).nome;
            data[i][1] = String.format("€ %.2f", piatti.get(i).prezzo);
            data[i][2] = piatti.get(i).descrizione;
        }
        piattiTable.setModel(new javax.swing.table.DefaultTableModel(data, new String[]{"Nome", "Prezzo", "Descrizione"}));
    }
}
