package it.unibo.view.cliente;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

import it.unibo.controller.Controller;
import it.unibo.data.Carta;
import it.unibo.data.Indirizzo;
import it.unibo.data.MetodoPagamento;
import it.unibo.data.Ordine;
import it.unibo.data.Pagamento;
import it.unibo.data.Recensione;
import it.unibo.data.StatoOrdine;

public class ProfiloPanel extends JPanel {

    private final Controller controller;
    private final JPanel buttonPanel;
    private final JScrollPane tableScrollPane;
    private final JTable dataTable;
    private DefaultTableModel tableModel;
    private final JLabel titleLabel;
    private final JButton btnAggiungi;
    private final JButton btnElimina;

    // Enum per gestire la sezione attiva
    private enum Sezione {
        RECENSIONI, INDIRIZZI, CARTE, METODI, ORDINI, PAGAMENTI, RACCOLTA, DATI
    }
    private Sezione sezioneAttiva = null;

    public ProfiloPanel(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // Titolo
        titleLabel = new JLabel("Profilo Cliente", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Pannello pulsanti sezioni
        buttonPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnRecensioni = new JButton("Le Mie Recensioni");
        JButton btnOrdini = new JButton("I Miei Ordini");
        JButton btnMetodiPagamento = new JButton("Metodi di Pagamento");
        JButton btnIndirizzi = new JButton("I Miei Indirizzi");
        JButton btnCarte = new JButton("Le Mie Carte");
        JButton btnPagamenti = new JButton("Storico Pagamenti");
        JButton btnRaccoltaPunti = new JButton("Raccolta Punti");
        JButton btnMieiDati = new JButton("I miei Dati");

        btnRecensioni.addActionListener(e -> { visualizzaRecensioni(); sezioneAttiva = Sezione.RECENSIONI; aggiornaBottoni(); });
        btnOrdini.addActionListener(e -> { visualizzaOrdini(); sezioneAttiva = Sezione.ORDINI; aggiornaBottoni(); });
        btnMetodiPagamento.addActionListener(e -> { visualizzaMetodiPagamento(); sezioneAttiva = Sezione.METODI; aggiornaBottoni(); });
        btnIndirizzi.addActionListener(e -> { visualizzaIndirizzi(); sezioneAttiva = Sezione.INDIRIZZI; aggiornaBottoni(); });
        btnCarte.addActionListener(e -> { visualizzaCarte(); sezioneAttiva = Sezione.CARTE; aggiornaBottoni(); });
        btnPagamenti.addActionListener(e -> { visualizzaPagamenti(); sezioneAttiva = Sezione.PAGAMENTI; aggiornaBottoni(); });
        btnRaccoltaPunti.addActionListener(e -> { visualizzaRaccoltaPunti(); sezioneAttiva = Sezione.RACCOLTA; aggiornaBottoni(); });
        btnMieiDati.addActionListener(e -> { visualizzaMieiDati(); sezioneAttiva = Sezione.DATI; aggiornaBottoni(); });

        buttonPanel.add(btnRecensioni);
        buttonPanel.add(btnOrdini);
        buttonPanel.add(btnMetodiPagamento);
        buttonPanel.add(btnIndirizzi);
        buttonPanel.add(btnCarte);
        buttonPanel.add(btnPagamenti);
        buttonPanel.add(btnRaccoltaPunti);
        buttonPanel.add(btnMieiDati);

        // Tabella
        tableModel = new DefaultTableModel();
        dataTable = new JTable(tableModel);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableScrollPane = new JScrollPane(dataTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Dati"));

        // Pannello bottoni "Aggiungi" e "Elimina"
        btnAggiungi = new JButton("Aggiungi");
        btnElimina = new JButton("Elimina");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnAggiungi);
        bottomPanel.add(btnElimina);

        // Azioni aggiungi/elimina (delegano alla sezione attiva)
        btnAggiungi.addActionListener(e -> aggiungiElemento());
        btnElimina.addActionListener(e -> eliminaElemento());

        // Layout finale
        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.SOUTH);

        inizializzaTabella();
        aggiornaBottoni();
    }

    // Mostra solo i bottoni "Aggiungi" ed "Elimina" dove serve
    private void aggiornaBottoni() {
        boolean isAggiungi = sezioneAttiva == Sezione.RECENSIONI
            || sezioneAttiva == Sezione.INDIRIZZI
            || sezioneAttiva == Sezione.CARTE
            || sezioneAttiva == Sezione.METODI;
        boolean isElimina = isAggiungi;

        btnAggiungi.setVisible(isAggiungi);
        btnElimina.setVisible(isElimina);
    }

    // ---- GESTIONE AGGIUNTA ----
    private void aggiungiElemento() {
        switch (sezioneAttiva) {
            case RECENSIONI:
                aggiungiRecensione();
                break;
            case INDIRIZZI:
                aggiungiIndirizzo();
                break;
            case CARTE:
                aggiungiCarta();
                break;
            case METODI:
                aggiungiMetodoPagamento();
                break;
            default:
                break;
        }
    }

    // ---- GESTIONE ELIMINA ----
    private void eliminaElemento() {
        int sel = dataTable.getSelectedRow();
        if (sel < 0) {
            JOptionPane.showMessageDialog(this, "Seleziona una riga da eliminare.");
            return;
        }
        switch (sezioneAttiva) {
            case RECENSIONI:
                eliminaRecensione(sel);
                break;
            case INDIRIZZI:
                eliminaIndirizzo(sel);
                break;
            case CARTE:
                eliminaCarta(sel);
                break;
            case METODI:
                eliminaMetodoPagamento(sel);
                break;
            default:
                break;
        }
    }

    // --- METODI DI AGGIUNTA ---

    private void aggiungiRecensione() {
        JTextField nomeRistorante = new JTextField();
        JTextField titolo = new JTextField();
        JTextArea descrizione = new JTextArea(3, 20);
        JComboBox<Integer> stelle = new JComboBox<>(new Integer[]{1,2,3,4,5});
        JPanel panelForm = new JPanel(new GridLayout(0, 1));
        panelForm.add(new JLabel("Nome ristorante:"));
        panelForm.add(nomeRistorante);
        panelForm.add(new JLabel("Titolo recensione:"));
        panelForm.add(titolo);
        panelForm.add(new JLabel("Numero stelle:"));
        panelForm.add(stelle);
        panelForm.add(new JLabel("Descrizione:"));
        panelForm.add(new JScrollPane(descrizione));

        int res = JOptionPane.showConfirmDialog(this, panelForm, "Aggiungi Recensione", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                controller.getModel().insertRecensione(
                    new Recensione(
                        controller.getCurrentClienteId(),
                        controller.getModel().loadPivaByNome(nomeRistorante.getText()).get(),
                        (int) stelle.getSelectedItem(),
                        descrizione.getText(),
                        titolo.getText(),
                        java.sql.Date.valueOf(java.time.LocalDate.now())
                    )
                );
                visualizzaRecensioni();
                JOptionPane.showMessageDialog(this, "Recensione aggiunta!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
            }
        }
    }
    private void aggiungiIndirizzo() {
        JTextField via = new JTextField();
        JTextField numeroCivico = new JTextField();
        JTextField interno = new JTextField();
        JTextField scala = new JTextField();
        JTextField cap = new JTextField();
        JTextField zona = new JTextField();
        JPanel panelForm = new JPanel(new GridLayout(0, 1));
        panelForm.add(new JLabel("Via:"));
        panelForm.add(via);
        panelForm.add(new JLabel("Numero Civico:"));
        panelForm.add(numeroCivico);
        panelForm.add(new JLabel("Interno (opzionale):"));
        panelForm.add(interno);
        panelForm.add(new JLabel("Scala (opzionale):"));
        panelForm.add(scala);
        panelForm.add(new JLabel("CAP:"));
        panelForm.add(cap);
        panelForm.add(new JLabel("Codice Zona:"));
        panelForm.add(zona);

        int res = JOptionPane.showConfirmDialog(this, panelForm, "Aggiungi Indirizzo", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                Indirizzo ind = new Indirizzo(
                    via.getText(),
                    numeroCivico.getText(),
                    cap.getText(),
                    interno.getText().isBlank() ? 0 : Integer.parseInt(interno.getText()),
                    scala.getText().isBlank() ? 0 : Integer.parseInt(scala.getText()),
                    Integer.parseInt(zona.getText())
                );
                controller.getModel().insertIndirizzo(ind, controller.getCurrentClienteId());
                visualizzaIndirizzi();
                JOptionPane.showMessageDialog(this, "Indirizzo aggiunto!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
            }
        }
    }
    private void aggiungiCarta() {
        JTextField nomeMetodo = new JTextField();
        JTextField numero = new JTextField();
        JTextField titolare = new JTextField();
        JTextField scadenza = new JTextField("yyyy-mm-dd");
        JTextField cvv = new JTextField();
        JPanel panelForm = new JPanel(new GridLayout(0, 1));
        panelForm.add(new JLabel("Metodo di Pagamento associato:"));
        panelForm.add(nomeMetodo);
        panelForm.add(new JLabel("Numero Carta:"));
        panelForm.add(numero);
        panelForm.add(new JLabel("Titolare:"));
        panelForm.add(titolare);
        panelForm.add(new JLabel("Data Scadenza (yyyy-mm-dd):"));
        panelForm.add(scadenza);
        panelForm.add(new JLabel("CVV:"));
        panelForm.add(cvv);

        int res = JOptionPane.showConfirmDialog(this, panelForm, "Aggiungi Carta", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                controller.getModel().insertCarta(new Carta(
                    controller.getCurrentClienteId(), nomeMetodo.getText(), numero.getText(),
                    titolare.getText(), java.sql.Date.valueOf(scadenza.getText()), cvv.getText()));
                visualizzaCarte();
                JOptionPane.showMessageDialog(this, "Carta aggiunta!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
            }
        }
    }
    private void aggiungiMetodoPagamento() {
        String nome = JOptionPane.showInputDialog(this, "Nome metodo (es: ApplePay, PayPal, ...):");
        if (nome != null && !nome.isBlank()) {
            try {
                controller.getModel().insertMetodoPagamento(new MetodoPagamento(controller.getCurrentClienteId(), nome));
                visualizzaMetodiPagamento();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
            }
        }
    }

    // --- METODI DI ELIMINAZIONE ---

    private void eliminaRecensione(int sel) {
        try {
            String piva = (String) tableModel.getValueAt(sel, 0);
            String titolo = (String) tableModel.getValueAt(sel, 1);
            controller.getModel().deleteRecensione(controller.getCurrentClienteId(), piva, titolo);
            visualizzaRecensioni();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
        }
    }
    private void eliminaIndirizzo(int sel) {
        try {
            int codice = Integer.parseInt(tableModel.getValueAt(sel, 0).toString());
            controller.getModel().deleteIndirizzo(controller.getCurrentClienteId(), codice);
            visualizzaIndirizzi();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
        }
    }
    private void eliminaCarta(int sel) {
        try {
            String nome = (String) tableModel.getValueAt(sel, 0);
            String numeroFinale = JOptionPane.showInputDialog(this, "Inserisci le ultime 4 cifre della carta per conferma:");
            controller.getModel().deleteCarta(controller.getCurrentClienteId(), nome, numeroFinale);
            visualizzaCarte();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
        }
    }
    private void eliminaMetodoPagamento(int sel) {
        try {
            String nome = (String) tableModel.getValueAt(sel, 1);
            controller.getModel().deleteMetodoPagamento(controller.getCurrentClienteId(), nome);
            visualizzaMetodiPagamento();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
        }
    }

    // ---- INIZIALIZZA ----

    private void inizializzaTabella() {
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
        tableModel.addColumn("Informazioni");
        tableModel.addRow(new Object[]{"Seleziona un'opzione dai pulsanti sopra per visualizzare i dati"});
    }

    // ---- VISUALIZZAZIONE SEZIONI ----

    private void visualizzaRecensioni() {
        try {
            int clienteId = controller.getCurrentClienteId();
            List<Recensione> recensioni = controller.getModel().loadRecensioniByCliente(clienteId);

            List<String> colonne = List.of("Ristorante (P.IVA)", "Titolo", "Stelle", "Descrizione", "Data");
            tableModel.setColumnCount(0);
            tableModel.setRowCount(0);

            colonne.forEach(s -> tableModel.addColumn(s));

            recensioni.forEach(rec -> {
                tableModel.addRow(new Object[]{
                    rec.piva,
                    rec.titolo,
                    rec.numeroStelle + " ⭐",
                    rec.descrizione.length() > 50 ?
                        rec.descrizione.substring(0, 50) + "..." : rec.descrizione,
                    rec.data
                });
            });

            if (recensioni.isEmpty()) {
                tableModel.addRow(new Object[]{"Nessuna recensione trovata", "", "", "", ""});
            }

        } catch (Exception e) {
            mostraErrore("Errore nel caricamento delle recensioni: " + e.getMessage());
        }
    }

    private void visualizzaOrdini() {
        try {
            int clienteId = controller.getCurrentClienteId();
            List<Ordine> ordini = controller.getModel().loadOrdiniByCliente(clienteId);

            List<String> colonne = List.of("Codice Ordine", "Ristorante", "Prezzo Totale", "Stato", "Numero Piatti");
            tableModel.setColumnCount(0);
            tableModel.setRowCount(0);

            colonne.forEach(s -> tableModel.addColumn(s));

            ordini.forEach(ordine -> {
                String statoDescrizione = getDescrizioneStato(ordine.codiceOrdine);
                tableModel.addRow(new Object[]{
                    ordine.codiceOrdine,
                    ordine.piva,
                    "€ " + ordine.prezzoTotale.toString(),
                    statoDescrizione,
                    ordine.dettagli.size() + " piatti"
                });
            });

            if (ordini.isEmpty()) {
                tableModel.addRow(new Object[]{"Nessun ordine trovato", "", "", "", ""});
            }

        } catch (Exception e) {
            mostraErrore("Errore nel caricamento degli ordini: " + e.getMessage());
        }
    }

    private void visualizzaMetodiPagamento() {
        try {
            int clienteId = controller.getCurrentClienteId();
            List<MetodoPagamento> metodi = controller.getModel().loadMetodiPagamentoByCliente(clienteId);

            List<String> colonne = List.of("Codice", "Tipo");
            tableModel.setColumnCount(0);
            tableModel.setRowCount(0);

            colonne.forEach(s -> tableModel.addColumn(s));

            metodi.forEach(metodo -> {
                tableModel.addRow(new Object[]{
                    metodo.codiceCliente,
                    metodo.nome,
                });
            });

            if (metodi.isEmpty()) {
                tableModel.addRow(new Object[]{"Nessun metodo di pagamento trovato", "", ""});
            }

        } catch (Exception e) {
            mostraErrore("Errore nel caricamento dei metodi di pagamento: " + e.getMessage());
        }
    }

    private void visualizzaIndirizzi() {
        try {
            int clienteId = controller.getCurrentClienteId();
            List<Indirizzo> indirizzi = controller.getModel().loadIndirizziByCliente(clienteId);

            List<String> colonne = List.of("Codice", "Via", "Numero", "CAP", "Interno", "Scala", "Zona");
            tableModel.setColumnCount(0);
            tableModel.setRowCount(0);

            colonne.forEach(s -> tableModel.addColumn(s));

            indirizzi.forEach(ind -> {
                tableModel.addRow(new Object[]{
                    ind.codiceIndirizzo,
                    ind.via,
                    ind.numeroCivico,
                    ind.cap,
                    ind.interno == 0 ? "-" : ind.interno,
                    ind.scala == 0 ? "-" : ind.scala,
                    ind.codiceZona
                });
            });

            if (indirizzi.isEmpty()) {
                tableModel.addRow(new Object[]{"Nessun indirizzo trovato", "", "", "", "", "", ""});
            }

        } catch (Exception e) {
            mostraErrore("Errore nel caricamento degli indirizzi: " + e.getMessage());
        }
    }

    private void visualizzaCarte() {
        try {
            int clienteId = controller.getCurrentClienteId();
            List<Carta> carte = controller.getModel().loadCarteByCliente(clienteId);

            List<String> colonne = List.of("Nome", "Numero (Parziale)", "Titolare", "Scadenza", "CVV");
            tableModel.setColumnCount(0);
            tableModel.setRowCount(0);

            colonne.forEach(s -> tableModel.addColumn(s));

            carte.forEach(carta -> {
                String numeroMascherato = "**** **** **** " +
                    carta.numero.substring(Math.max(0, carta.numero.length() - 4));

                tableModel.addRow(new Object[]{
                    carta.nome,
                    numeroMascherato,
                    carta.titolare,
                    carta.dataScadenza,
                    "***"
                });
            });

            if (carte.isEmpty()) {
                tableModel.addRow(new Object[]{"Nessuna carta trovata", "", "", "", ""});
            }

        } catch (Exception e) {
            mostraErrore("Errore nel caricamento delle carte: " + e.getMessage());
        }
    }

    private void visualizzaPagamenti() {
        try {
            int clienteId = controller.getCurrentClienteId();
            List<Pagamento> pagamenti = controller.getModel().loadPagamentiByCliente(clienteId);

            List<String> colonne = List.of("Codice", "Metodo", "Importo", "Data");
            tableModel.setColumnCount(0);
            tableModel.setRowCount(0);

            colonne.forEach(s -> tableModel.addColumn(s));

            pagamenti.forEach(pag -> {
                tableModel.addRow(new Object[]{
                    pag.codicePagamento,
                    pag.nomeMetodo,
                    "€ " + pag.importo.toString(),
                    pag.data,
                });
            });

            if (pagamenti.isEmpty()) {
                tableModel.addRow(new Object[]{"Nessun pagamento trovato", "", "", "", ""});
            }

        } catch (Exception e) {
            mostraErrore("Errore nel caricamento dei pagamenti: " + e.getMessage());
        }
    }

    private void visualizzaRaccoltaPunti() {
        try {
            int clienteId = controller.getCurrentClienteId();
            var raccolta = controller.getModel().loadRaccoltePuntiByCliente(clienteId);
            if (!raccolta.isPresent()) {
                tableModel.setColumnCount(0);
                tableModel.setRowCount(0);
                tableModel.addColumn("Info");
                tableModel.addRow(new Object[]{"Nessuna raccolta punti trovata."});
                return;
            }
            tableModel.setColumnCount(0);
            tableModel.setRowCount(0);
            List<String> colonne = List.of("Punti Totali", "Soglia Punti", "Percentuale Sconto");
            colonne.forEach(tableModel::addColumn);
            tableModel.addRow(new Object[]{
                raccolta.get().puntiTotali,
                raccolta.get().sogliaPunti,
                raccolta.get().percentualeSconto + " %"
            });
        } catch (Exception e) {
            mostraErrore("Errore nel caricamento della raccolta punti: " + e.getMessage());
        }
    }

    private void visualizzaMieiDati() {
        try {
            var cliente = controller.getModel().findClienteById(controller.getCurrentClienteId());
            if (!cliente.isPresent()) {
                tableModel.setColumnCount(0);
                tableModel.setRowCount(0);
                tableModel.addColumn("Info");
                tableModel.addRow(new Object[]{"Cliente non trovato"});
                return;
            }
            tableModel.setColumnCount(0);
            tableModel.setRowCount(0);
            List<String> colonne = List.of("Nome", "Cognome", "Email", "Telefono", "Data di Nascita", "Username");
            colonne.forEach(tableModel::addColumn);
            tableModel.addRow(new Object[]{
                cliente.get().nome,
                cliente.get().cognome,
                cliente.get().email,
                cliente.get().telefono,
                cliente.get().dataNascita != null ? cliente.get().dataNascita.toString() : "",
                cliente.get().username
            });
        } catch (Exception e) {
            mostraErrore("Errore nel caricamento dati cliente: " + e.getMessage());
        }
    }

    private String getDescrizioneStato(int codiceOrdine) {
        Optional<StatoOrdine> statoOpt = controller.getModel().findStateByOrder(codiceOrdine);
        if (statoOpt.isEmpty()) {
            return "Non disponibile";
        }
        StatoOrdine stato = statoOpt.get();
        if (stato.consegnato != null) {
            return "Consegnato il " + stato.consegnato.toLocalDate().toString();
        } else if (stato.inConsegna != null) {
            return "In consegna dal " + stato.inConsegna.toLocalDate().toString();
        } else if (stato.inPreparazione != null) {
            return "In preparazione dal " + stato.inPreparazione.toLocalDate().toString();
        } else if (stato.data != null) {
            return "Creato il " + stato.data.toLocalDate().toString();
        } else {
            return "Stato sconosciuto";
        }
    }

    private void mostraErrore(String messaggio) {
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
        tableModel.addColumn("Errore");
        tableModel.addRow(new Object[]{messaggio});
        JOptionPane.showMessageDialog(this, messaggio, "Errore", JOptionPane.ERROR_MESSAGE);
    }

}
