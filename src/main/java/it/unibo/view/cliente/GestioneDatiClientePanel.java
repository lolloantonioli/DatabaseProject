package it.unibo.view.cliente;

import it.unibo.controller.Controller;
import it.unibo.data.Carta;
import it.unibo.data.Indirizzo;
import it.unibo.data.MetodoPagamento;
import it.unibo.data.Recensione;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class GestioneDatiClientePanel extends JPanel {

    private final Controller controller;

    public GestioneDatiClientePanel(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Metodi di Pagamento", creaPanelMetodiPagamento());
        tabbedPane.addTab("Carte", creaPanelCarte());
        tabbedPane.addTab("Indirizzi", creaPanelIndirizzi());
        tabbedPane.addTab("Scrivi Recensione", creaPanelRecensione());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // 1. Metodi di Pagamento
    private JPanel creaPanelMetodiPagamento() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> lista = new JList<>(model);
        JScrollPane scroll = new JScrollPane(lista);
        JButton btnAggiungi = new JButton("Aggiungi");
        JButton btnRimuovi = new JButton("Rimuovi");

        aggiornaListaMetodiPagamento(model);

        btnAggiungi.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(this, "Nome metodo (es: ApplePay, PayPal, ...):");
            if (nome != null && !nome.isBlank()) {
                try {
                    controller.getModel().insertMetodoPagamento(new MetodoPagamento(controller.getCurrentClienteId(), nome));
                    aggiornaListaMetodiPagamento(model);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
                }
            }
        });

        btnRimuovi.addActionListener(e -> {
            String selezionato = lista.getSelectedValue();
            if (selezionato != null) {
                try {
                    controller.getModel().deleteMetodoPagamento(controller.getCurrentClienteId(), selezionato);
                    aggiornaListaMetodiPagamento(model);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
                }
            }
        });

        JPanel buttons = new JPanel();
        buttons.add(btnAggiungi);
        buttons.add(btnRimuovi);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private void aggiornaListaMetodiPagamento(DefaultListModel<String> model) {
        model.clear();
        List<MetodoPagamento> metodi = controller.getModel().loadMetodiPagamentoByCliente(controller.getCurrentClienteId());
        metodi.forEach(m -> model.addElement(m.nome));
    }

    // 2. Carte
    private JPanel creaPanelCarte() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> lista = new JList<>(model);
        JScrollPane scroll = new JScrollPane(lista);
        JButton btnAggiungi = new JButton("Aggiungi");
        JButton btnRimuovi = new JButton("Rimuovi");

        aggiornaListaCarte(model);

        btnAggiungi.addActionListener(e -> {
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
                    controller.getModel().insertCarta(new Carta(controller.getCurrentClienteId(), nomeMetodo.getText(), numero.getText(),
                                                      titolare.getText(), Date.valueOf(scadenza.getText()), cvv.getText()));
                    aggiornaListaCarte(model);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
                }
            }
        });

        btnRimuovi.addActionListener(e -> {
            String selezionato = lista.getSelectedValue();
            if (selezionato != null && selezionato.contains(" (")) {
                try {
                    // Esempio: "1234567890123456 (Visa)"
                    String numero = selezionato.substring(0, selezionato.indexOf(" ("));
                    String nome = selezionato.substring(selezionato.indexOf(" (") + 2, selezionato.length() - 1); // tra parentesi
                    controller.getModel().deleteCarta(controller.getCurrentClienteId(), nome, numero);
                    aggiornaListaCarte(model);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
                }
            }
        });

        JPanel buttons = new JPanel();
        buttons.add(btnAggiungi);
        buttons.add(btnRimuovi);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private void aggiornaListaCarte(DefaultListModel<String> model) {
        model.clear();
        List<Carta> carte = controller.getModel().loadCarteByCliente(controller.getCurrentClienteId());
        for (Carta c : carte) {
            model.addElement(c.numero + " (" + c.nome + ")");
        }
    }

    // 3. Indirizzi
    private JPanel creaPanelIndirizzi() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> lista = new JList<>(model);
        JScrollPane scroll = new JScrollPane(lista);
        JButton btnAggiungi = new JButton("Aggiungi");
        JButton btnRimuovi = new JButton("Rimuovi");

        aggiornaListaIndirizzi(model);

        btnAggiungi.addActionListener(e -> {
            JTextField via = new JTextField();
            JTextField numeroCivico = new JTextField();
            JTextField interno = new JTextField();
            JTextField scala = new JTextField();
            JComboBox<CittaZona> comboCitta = new JComboBox<>(CITTA_ZONA);
            JPanel panelForm = new JPanel(new GridLayout(0, 1));
            panelForm.add(new JLabel("Città:"));
            panelForm.add(comboCitta);
            panelForm.add(new JLabel("Via:"));
            panelForm.add(via);
            panelForm.add(new JLabel("Numero Civico:"));
            panelForm.add(numeroCivico);
            panelForm.add(new JLabel("Interno (opzionale):"));
            panelForm.add(interno);
            panelForm.add(new JLabel("Scala (opzionale):"));
            panelForm.add(scala);

            int res = JOptionPane.showConfirmDialog(this, panelForm, "Aggiungi Indirizzo", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    CittaZona selezionata = (CittaZona) comboCitta.getSelectedItem();
                    Indirizzo ind = new Indirizzo(
                        via.getText(),
                        numeroCivico.getText(),
                        selezionata.cap,
                        interno.getText().isBlank() ? 0 : Integer.parseInt(interno.getText()),
                        scala.getText().isBlank() ? 0 : Integer.parseInt(scala.getText()),
                        selezionata.codiceZona
                    );
                    controller.getModel().insertIndirizzo(ind, controller.getCurrentClienteId());
                    aggiornaListaIndirizzi(model);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
                }
            }
        });

        btnRimuovi.addActionListener(e -> {
            String selezionato = lista.getSelectedValue();
            if (selezionato != null && selezionato.contains(":")) {
                try {
                    String codiceStr = selezionato.substring(0, selezionato.indexOf(":")).trim();
                    int codiceIndirizzo = Integer.parseInt(codiceStr);
                    controller.getModel().deleteIndirizzo(controller.getCurrentClienteId(), codiceIndirizzo);
                    aggiornaListaIndirizzi(model);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
                }
            }
        });

        JPanel buttons = new JPanel();
        buttons.add(btnAggiungi);
        buttons.add(btnRimuovi);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private void aggiornaListaIndirizzi(DefaultListModel<String> model) {
        model.clear();
        List<Indirizzo> indirizzi = controller.getModel().loadIndirizziByCliente(controller.getCurrentClienteId());
        for (Indirizzo i : indirizzi) {
            model.addElement(i.codiceIndirizzo + ": " + i.via + " " + i.numeroCivico + " (" + i.cap + ")");
        }
    }

    // 4. Scrivi Recensione
    private JPanel creaPanelRecensione() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton btnScrivi = new JButton("Scrivi Recensione");
        panel.add(btnScrivi, BorderLayout.CENTER);

        btnScrivi.addActionListener(e -> {
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

            int res = JOptionPane.showConfirmDialog(this, panelForm, "Scrivi recensione", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    controller.getModel().insertRecensione(new Recensione(controller.getCurrentClienteId(), controller.getModel().loadPivaByNome(nomeRistorante.getText()).get(),
                    (int) stelle.getSelectedItem(), descrizione.getText(), titolo.getText(), Date.valueOf(LocalDate.now())));
                    JOptionPane.showMessageDialog(this, "Recensione inviata!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
                }
            }
        });
        return panel;
    }

    private static class CittaZona {
        public final String citta;
        public final String cap;
        public final int codiceZona;
        public CittaZona(String citta, String cap, int codiceZona) {
            this.citta = citta;
            this.cap = cap;
            this.codiceZona = codiceZona;
        }
        @Override
        public String toString() {
            return citta + " (" + cap + ")";
        }
    }

    // Popola la lista delle città principali
    private static final CittaZona[] CITTA_ZONA = {
        new CittaZona("Roma", "00100", 1),
        new CittaZona("Milano", "20100", 2),
        new CittaZona("Napoli", "80100", 3),
        new CittaZona("Torino", "10100", 4),
        new CittaZona("Palermo", "90100", 5),
        new CittaZona("Genova", "16100", 6),
        new CittaZona("Bologna", "40100", 7),
        new CittaZona("Firenze", "50100", 8),
        new CittaZona("Bari", "70100", 9),
        new CittaZona("Catania", "95100", 10),
        new CittaZona("Venezia", "30100", 11),
        new CittaZona("Verona", "37100", 12),
        new CittaZona("Messina", "98100", 13),
        new CittaZona("Padova", "35100", 14),
        new CittaZona("Trieste", "34100", 15),
    };

}
