
package it.unibo.view.ristorante;

import it.unibo.controller.Controller;
import it.unibo.data.Offre;
import it.unibo.data.Piatto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Pannello per la gestione del menu del ristorante.
 */
public class PiattiPanel extends JPanel {

    private final Controller controller;
    private final String piva;
    private final DefaultTableModel model;
    private final JTable table;

    public PiattiPanel(Controller controller, String piva) {
        this.controller = controller;
        this.piva = piva;
        setLayout(new BorderLayout());

        this.model = new DefaultTableModel(new Object[]{"Codice", "Nome", "Prezzo", "Descrizione"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        this.table = new JTable(model);

        JButton btnReload = new JButton("Aggiorna");
        JButton btnAggiungi = new JButton("Aggiungi Piatto");
        JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT));
        north.add(btnReload);
        north.add(btnAggiungi);

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnReload.addActionListener(e -> caricaMenu());
        btnAggiungi.addActionListener(e -> aggiungiPiatto());

        caricaMenu();
    }

    private void caricaMenu() {
        model.setRowCount(0);
        try {
            List<Piatto> piatti = controller.getModel().loadPiattiByRistorante(piva);
            for (Piatto p : piatti) {
                model.addRow(new Object[]{p.codicePiatto, p.nome, p.prezzo, p.descrizione});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore caricamento menu: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aggiungiPiatto() {
        JTextField txtCodice = new JTextField();
        JTextField txtNome = new JTextField();
        JTextField txtPrezzo = new JTextField();
        JTextArea txtDescrizione = new JTextArea(3,20);
        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.add(new JLabel("Codice"));
        panel.add(txtCodice);
        panel.add(new JLabel("Nome"));
        panel.add(txtNome);
        panel.add(new JLabel("Prezzo"));
        panel.add(txtPrezzo);
        panel.add(new JLabel("Descrizione"));
        panel.add(new JScrollPane(txtDescrizione));

        int res = JOptionPane.showConfirmDialog(this, panel, "Nuovo Piatto", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                int codice = Integer.parseInt(txtCodice.getText().trim());
                BigDecimal prezzo = new BigDecimal(txtPrezzo.getText().trim());
                Piatto p = new Piatto(codice, txtNome.getText(), prezzo, txtDescrizione.getText());
                controller.getModel().insertPiatto(p);
                controller.getModel().insertOffre(new Offre(piva, codice));
                caricaMenu();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore inserimento piatto: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
