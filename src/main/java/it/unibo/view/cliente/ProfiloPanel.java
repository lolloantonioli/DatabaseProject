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
    
    public ProfiloPanel(Controller controller) {
        this.controller = controller;
        this.setLayout(new BorderLayout());
        
        // Pannello superiore con titolo
        titleLabel = new JLabel("Profilo Cliente", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Pannello dei pulsanti
        buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Creazione pulsanti
        JButton btnRecensioni = new JButton("Le Mie Recensioni");
        JButton btnOrdini = new JButton("I Miei Ordini");
        JButton btnMetodiPagamento = new JButton("Metodi di Pagamento");
        JButton btnIndirizzi = new JButton("I Miei Indirizzi");
        JButton btnCarte = new JButton("Le Mie Carte");
        JButton btnPagamenti = new JButton("Storico Pagamenti");
        
        // Aggiunta dei listener
        btnRecensioni.addActionListener(e -> visualizzaRecensioni());
        btnOrdini.addActionListener(e -> visualizzaOrdini());
        btnMetodiPagamento.addActionListener(e -> visualizzaMetodiPagamento());
        btnIndirizzi.addActionListener(e -> visualizzaIndirizzi());
        btnCarte.addActionListener(e -> visualizzaCarte());
        btnPagamenti.addActionListener(e -> visualizzaPagamenti());
        
        // Aggiunta pulsanti al pannello
        buttonPanel.add(btnRecensioni);
        buttonPanel.add(btnOrdini);
        buttonPanel.add(btnMetodiPagamento);
        buttonPanel.add(btnIndirizzi);
        buttonPanel.add(btnCarte);
        buttonPanel.add(btnPagamenti);
        
        // Creazione tabella
        tableModel = new DefaultTableModel();
        dataTable = new JTable(tableModel);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        tableScrollPane = new JScrollPane(dataTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Dati"));
        
        // Aggiunta componenti al pannello principale
        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(tableScrollPane, BorderLayout.SOUTH);
        
        // Inizializzazione con messaggio
        inizializzaTabella();
    }
    
    private void inizializzaTabella() {
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
        tableModel.addColumn("Informazioni");
        tableModel.addRow(new Object[]{"Seleziona un'opzione dai pulsanti sopra per visualizzare i dati"});
    }
    
    private void visualizzaRecensioni() {
        try {
            int clienteId = controller.getCurrentClienteId();
            List<Recensione> recensioni = controller.getModel().loadRecensioniByCliente(clienteId);
            
            List<String> colonne = List.of("Ristorante (P.IVA)", "Titolo", "Stelle", "Descrizione", "Data");
            tableModel.setColumnCount(0);
            tableModel.setRowCount(0);
            
            colonne.forEach(s -> tableModel.addColumn(s));
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            recensioni.forEach(rec -> {
                tableModel.addRow(new Object[]{
                    rec.piva,
                    rec.titolo,
                    rec.numeroStelle + " ⭐",
                    rec.descrizione.length() > 50 ? 
                        rec.descrizione.substring(0, 50) + "..." : rec.descrizione,
                    rec.data.format(formatter)
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
            
            for (String colonna : colonne) {
                tableModel.addColumn(colonna);
            }

            colonne.forEach(s -> tableModel.addColumn(s));
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
            carte.forEach(carta -> {
                // Nascondere la maggior parte del numero della carta per sicurezza
                String numeroMascherato = "**** **** **** " + 
                    carta.numero.substring(Math.max(0, carta.numero.length() - 4));
                
                tableModel.addRow(new Object[]{
                    carta.nome,
                    numeroMascherato,
                    carta.titolare,
                    carta.dataScadenza.format(formatter),
                    "***" // Nascondere CVV per sicurezza
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
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            pagamenti.forEach(pag -> {
                tableModel.addRow(new Object[]{
                    pag.codicePagamento,
                    pag.nomeMetodo,
                    "€ " + pag.importo.toString(),
                    pag.data.format(formatter),
                });
            });
            
            if (pagamenti.isEmpty()) {
                tableModel.addRow(new Object[]{"Nessun pagamento trovato", "", "", "", ""});
            }
            
        } catch (Exception e) {
            mostraErrore("Errore nel caricamento dei pagamenti: " + e.getMessage());
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
