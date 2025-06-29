package it.unibo.view.rider;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import it.unibo.controller.Controller;
import it.unibo.data.Mezzo;
import it.unibo.data.Contratto;

public class RiderProfiloPanel extends JPanel {
    private final Controller controller;
    private DefaultTableModel mezziTableModel;

    public RiderProfiloPanel(final Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

    }

    private void aggiornaTabellaMezzi() {
        mezziTableModel.setRowCount(0);
        List<Mezzo> mezzi = controller.getModel().loadMezziByRider(controller.getCurrentRiderId());
        for (Mezzo m : mezzi) {
            mezziTableModel.addRow(new Object[]{m.codiceMezzo, m.tipo, m.targa == null ? "" : m.targa});
        }
    }

    public void mostraProfiloRider() {
        this.removeAll();
        // --- Contratto ---
        Contratto contratto = controller.getModel().findContrattoByRider(controller.getCurrentRiderId()).get();
        JPanel infoPanel = new JPanel(new GridLayout(0,1));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Contratto attuale"));
        infoPanel.add(new JLabel("Paga oraria: " + contratto.pagaOraria));
        infoPanel.add(new JLabel("Testo: " + contratto.testo));

        add(infoPanel, BorderLayout.NORTH);

        // --- Mezzi ---
        mezziTableModel = new DefaultTableModel(new String[]{"ID", "Tipo", "Targa", "Modello"}, 0);
        JTable mezziTable = new JTable(mezziTableModel);
        aggiornaTabellaMezzi();

        JPanel mezziPanel = new JPanel(new BorderLayout());
        mezziPanel.setBorder(BorderFactory.createTitledBorder("Mezzi"));
        mezziPanel.add(new JScrollPane(mezziTable), BorderLayout.CENTER);

        JButton btnAggiungiMezzo = new JButton("Aggiungi Mezzo");
        mezziPanel.add(btnAggiungiMezzo, BorderLayout.SOUTH);

        btnAggiungiMezzo.addActionListener(e -> {
            String[] tipi = controller.riderHaPatente() ? new String[]{"Bicicletta", "Scooter", "Auto", "Moto"} : new String[]{"Bicicletta"};
            JComboBox<String> tipoCombo = new JComboBox<>(tipi);
            JTextField targaField = new JTextField();
            JTextField modelloField = new JTextField();
            JPanel form = new JPanel(new GridLayout(0,1));
            form.add(new JLabel("Tipo veicolo:"));
            form.add(tipoCombo);
            form.add(new JLabel("Targa (se necessario):"));
            form.add(targaField);
            form.add(new JLabel("Modello:"));
            form.add(modelloField);

            int res = JOptionPane.showConfirmDialog(this, form, "Aggiungi Mezzo", JOptionPane.OK_CANCEL_OPTION);
            if(res == JOptionPane.OK_OPTION) {
                String tipo = tipoCombo.getSelectedItem().toString();
                String targa = targaField.getText();
                String modello = modelloField.getText();
                int codiceMezzo = controller.getModel().getNextCodiceMezzo(controller.getCurrentRiderId());
                controller.getModel().insertMezzo(new Mezzo(controller.getCurrentRiderId(), codiceMezzo, tipo, targa, modello));
                aggiornaTabellaMezzi();
            }
        });

        add(mezziPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
