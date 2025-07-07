package it.unibo.view.ristorante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import it.unibo.controller.Controller;
import it.unibo.data.Promozione;
import java.sql.Date;

class RistorantePromozioniPanel extends JPanel {
    private final Controller controller;
    private final DefaultTableModel model;
    private final JTable table;

    public RistorantePromozioniPanel(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new String[]{"Data Inizio", "Data Fine", "Nome", "Descrizione", "% Sconto"}, 0) {
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
            JTextField desc = new JTextField();
            JTextField inizio = new JTextField("yyyy-mm-dd");
            JTextField fine = new JTextField("yyyy-mm-dd");
            JTextField sconto = new JTextField();
            JPanel panel = new JPanel(new GridLayout(0,1));
            panel.add(new JLabel("Nome:")); panel.add(nome);
            panel.add(new JLabel("Descrizione:")); panel.add(desc);
            panel.add(new JLabel("Data Inizio:")); panel.add(inizio);
            panel.add(new JLabel("Data Fine:")); panel.add(fine);
            panel.add(new JLabel("Percentuale Sconto:")); panel.add(sconto);
            int res = JOptionPane.showConfirmDialog(this, panel, "Nuova promozione", JOptionPane.OK_CANCEL_OPTION);
            if(res == JOptionPane.OK_OPTION) {
                try {
                    controller.getModel().insertPromozione(new Promozione(
                        controller.getCurrentPiva(),
                        LocalDate.parse(inizio.getText()),
                        LocalDate.parse(fine.getText()),
                        nome.getText(),
                        desc.getText(),
                        Byte.parseByte(sconto.getText())
                    ));
                    refreshTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore nei dati", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row >= 0) {
                Date inizio = Date.valueOf(model.getValueAt(row,0).toString());
                Date fine = Date.valueOf(model.getValueAt(row,1).toString());
                controller.getModel().deletePromozione(controller.getCurrentPiva(), inizio, fine);
                refreshTable();
            }
        });
    }
    public void refreshTable() {
        model.setRowCount(0);
        List<Promozione> promos = controller.getModel().loadPromozioniByRistorante(controller.getCurrentPiva());
        for(Promozione p : promos)
            model.addRow(new Object[]{p.dataInizio, p.dataFine, p.nome, p.descrizione, p.percentualeSconto});
    }
}
