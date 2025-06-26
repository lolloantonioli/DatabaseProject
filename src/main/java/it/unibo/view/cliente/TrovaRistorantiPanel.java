package it.unibo.view.cliente;

import it.unibo.controller.Controller;
import it.unibo.data.Indirizzo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TrovaRistorantiPanel extends JPanel {
    private final Controller controller;
    private final CardLayout cardLayout;
    private final JPanel cards;

    private final DefaultTableModel indirizziTableModel;
    private final JTable indirizziTable;
    private final JButton btnSeleziona;

    // Pannelli per CardLayout
    private final JPanel indirizzoPanel;
    private final JPanel ricercaPanel;

    private Indirizzo indirizzoSelezionato = null;

    public TrovaRistorantiPanel(Controller controller) {
        this.controller = controller;
        this.cardLayout = new CardLayout();
        this.cards = new JPanel(cardLayout);

        // ====== Indirizzo Panel ======
        indirizziTableModel = new DefaultTableModel(
            new Object[]{"Codice", "Via", "Numero", "CAP", "Interno", "Scala", "Zona"}, 0);
        indirizziTable = new JTable(indirizziTableModel);
        indirizziTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        btnSeleziona = new JButton("Seleziona Indirizzo");
        btnSeleziona.addActionListener(e -> {
            int row = indirizziTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Seleziona un indirizzo!", "Attenzione", JOptionPane.WARNING_MESSAGE);
                return;
            }
            indirizzoSelezionato = getIndirizzoFromRow(row);
            caricaRistorantiZona();
            cardLayout.show(cards, "ricerca");
        });

        indirizzoPanel = new JPanel(new BorderLayout());
        indirizzoPanel.add(new JLabel("Seleziona l'indirizzo con cui vuoi ordinare:", SwingConstants.CENTER), BorderLayout.NORTH);
        indirizzoPanel.add(new JScrollPane(indirizziTable), BorderLayout.CENTER);
        indirizzoPanel.add(btnSeleziona, BorderLayout.SOUTH);

        // ====== Ricerca Panel ======
        ricercaPanel = new JPanel(new BorderLayout());
        // Qui aggiungerai tabella ristoranti e bottone per visualizzare piatti (da riempire dopo)

        cards.add(indirizzoPanel, "indirizzo");
        cards.add(ricercaPanel, "ricerca");
        setLayout(new BorderLayout());
        add(cards, BorderLayout.CENTER);

        aggiornaIndirizzi(); // Popola la tabella all’avvio
        cardLayout.show(cards, "indirizzo");
    }

    /** Carica gli indirizzi del cliente nella tabella */
    public void aggiornaIndirizzi() {
        indirizziTableModel.setRowCount(0);
        if (!controller.isClienteLoggato()) {
            System.out.println("DEBUG: cliente NON loggato");
            return;
        }
        List<Indirizzo> indirizzi = controller.getModel().loadIndirizziByCliente(controller.getCurrentClienteId());
        System.out.println("DEBUG indirizzi trovati: " + indirizzi.size());
        for (Indirizzo i : indirizzi) {
            indirizziTableModel.addRow(new Object[]{
                i.codiceIndirizzo, i.via, i.numeroCivico, i.cap,
                i.interno == 0 ? "" : i.interno,
                i.scala == 0 ? "" : i.scala,
                i.codiceZona
            });
        }
    }

    /** Helper: ricrea l’oggetto Indirizzo dalla riga selezionata */
    private Indirizzo getIndirizzoFromRow(int row) {
        int codiceIndirizzo = Integer.parseInt(indirizziTableModel.getValueAt(row, 0).toString());
        String via = indirizziTableModel.getValueAt(row, 1).toString();
        String numero = indirizziTableModel.getValueAt(row, 2).toString();
        String cap = indirizziTableModel.getValueAt(row, 3).toString();
        int interno = indirizziTableModel.getValueAt(row, 4).toString().isBlank() ? 0 : Integer.parseInt(indirizziTableModel.getValueAt(row, 4).toString());
        int scala = indirizziTableModel.getValueAt(row, 5).toString().isBlank() ? 0 : Integer.parseInt(indirizziTableModel.getValueAt(row, 5).toString());
        int codiceZona = Integer.parseInt(indirizziTableModel.getValueAt(row, 6).toString());
        Indirizzo i = new Indirizzo(via, numero, cap, interno, scala, codiceZona);
        i.setCodiceIndirizzo(codiceIndirizzo);
        return i;
    }

    /** Carica i ristoranti della zona dell’indirizzo selezionato e mostra la tabella */
    private void caricaRistorantiZona() {
        ricercaPanel.removeAll();

        List<it.unibo.data.Ristorante> ristoranti = controller.getModel().loadRistorantiByZona(indirizzoSelezionato.codiceZona);
        DefaultTableModel modelRist = new DefaultTableModel(
            new Object[]{"P.IVA", "Nome", "Indirizzo"}, 0);
        JTable tabellaRist = new JTable(modelRist);
        tabellaRist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        for (it.unibo.data.Ristorante r : ristoranti) {
            modelRist.addRow(new Object[]{r.piva, r.nome, r.indirizzo});
        }

        JButton btnVisualizzaPiatti = new JButton("Visualizza Piatti");
        btnVisualizzaPiatti.addActionListener(e -> {
            int row = tabellaRist.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Seleziona un ristorante!", "Attenzione", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String pivaRistorante = modelRist.getValueAt(row, 0).toString();
            mostraTabellaPiatti(pivaRistorante);
        });

        ricercaPanel.add(new JLabel("Ristoranti nella tua zona:", SwingConstants.CENTER), BorderLayout.NORTH);
        ricercaPanel.add(new JScrollPane(tabellaRist), BorderLayout.CENTER);
        ricercaPanel.add(btnVisualizzaPiatti, BorderLayout.SOUTH);
        ricercaPanel.revalidate();
        ricercaPanel.repaint();
    }

    /** Mostra la tabella dei piatti del ristorante scelto */
    private void mostraTabellaPiatti(String pivaRistorante) {
        // Fai apparire una nuova finestra o una dialog con la tabella dei piatti (implementazione simile)
        // Qui puoi richiamare il tuo ViewPiattiPanel se vuoi integrarlo, o mostrare direttamente la tabella qui
        // Esempio minimale:
        List<it.unibo.data.Piatto> piatti = controller.getModel().loadPiattiByRistorante(pivaRistorante);
        DefaultTableModel modelPiatti = new DefaultTableModel(
            new Object[]{"Codice", "Nome", "Prezzo", "Aggiungi"}, 0);
        JTable tabellaPiatti = new JTable(modelPiatti);

        for (it.unibo.data.Piatto p : piatti) {
            modelPiatti.addRow(new Object[]{p.codicePiatto, p.nome, p.prezzo, "Aggiungi"});
        }

        // Qui puoi aggiungere pulsante per selezionare quantità ed effettivamente aggiungere al carrello ecc.

        JOptionPane.showMessageDialog(this, new JScrollPane(tabellaPiatti), "Piatti disponibili", JOptionPane.PLAIN_MESSAGE);
    }
}
