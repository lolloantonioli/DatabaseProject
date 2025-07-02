package it.unibo.view.ristorante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import it.unibo.controller.Controller;
import it.unibo.data.Piatto;

class RistorantePiattiPanel extends JPanel {
    private final Controller controller;
    private final DefaultTableModel model;
    private final JTable table;

    public RistorantePiattiPanel(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new String[]{"Codice", "Nome", "Prezzo", "Descrizione"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        refreshTable();

        JButton addBtn = new JButton("Aggiungi");
        JButton delBtn = new JButton("Elimina");

        JPanel south = new JPanel();
        south.add(addBtn); south.add(delBtn);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            JTextField nome = new JTextField();
            JTextField prezzo = new JTextField();
            JTextField desc = new JTextField();
            JPanel panel = new JPanel(new GridLayout(0,1));
            panel.add(new JLabel("Nome:")); panel.add(nome);
            panel.add(new JLabel("Prezzo:")); panel.add(prezzo);
            panel.add(new JLabel("Descrizione:")); panel.add(desc);
            int res = JOptionPane.showConfirmDialog(this, panel, "Nuovo piatto", JOptionPane.OK_CANCEL_OPTION);
            if(res == JOptionPane.OK_OPTION) {
                try {
                    controller.getModel().insertPiatto(new Piatto(
                                                                  nome.getText(), BigDecimal.valueOf(Double.valueOf(prezzo.getText())), desc.getText()),
                                                                  controller.getCurrentPiva());
                    refreshTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore nei dati", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row >= 0) {
                int codice = (int)model.getValueAt(row,0);
                controller.getModel().deletePiatto(codice);
                refreshTable();
            }
        });
    }
    private void refreshTable() {
        model.setRowCount(0);
        List<Piatto> piatti = controller.getModel().loadPiattiByRistorante(controller.getCurrentPiva());
        for(Piatto p : piatti)
            model.addRow(new Object[]{p.codicePiatto, p.nome, p.prezzo, p.descrizione});
    }
}
