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
                    controller.getModel().rimuoviMetodoPagamento(controller.getCurrentClienteId(), selezionato);
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
        for (MetodoPagamento m : metodi) {
            model.addElement(m.nome);
        }
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
            if (selezionato != null) {
                try {
                    // assumiamo che selezionato sia il numero (puoi personalizzare!)
                    controller.getModel().rimuoviCarta(controller.getCurrentClienteId(), selezionato);
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
            JTextField cap = new JTextField();
            JTextField interno = new JTextField();
            JTextField scala = new JTextField();
            JTextField zona = new JTextField();
            JPanel panelForm = new JPanel(new GridLayout(0, 1));
            panelForm.add(new JLabel("Via:"));
            panelForm.add(via);
            panelForm.add(new JLabel("Numero Civico:"));
            panelForm.add(numeroCivico);
            panelForm.add(new JLabel("CAP:"));
            panelForm.add(cap);
            panelForm.add(new JLabel("Interno (opzionale):"));
            panelForm.add(interno);
            panelForm.add(new JLabel("Scala (opzionale):"));
            panelForm.add(scala);
            panelForm.add(new JLabel("Codice Zona:"));
            panelForm.add(zona);

            int res = JOptionPane.showConfirmDialog(this, panelForm, "Aggiungi Indirizzo", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    controller.getModel().aggiungiIndirizzo(
                        controller.getCurrentClienteId(),
                        via.getText(),
                        Integer.parseInt(numeroCivico.getText()),
                        cap.getText(),
                        interno.getText().isBlank() ? 0 : Integer.parseInt(interno.getText()),
                        scala.getText().isBlank() ? 0 : Integer.parseInt(scala.getText()),
                        Integer.parseInt(zona.getText())
                    );
                    aggiornaListaIndirizzi(model);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
                }
            }
        });

        btnRimuovi.addActionListener(e -> {
            String selezionato = lista.getSelectedValue();
            if (selezionato != null) {
                try {
                    // assumiamo che selezionato sia qualcosa che identifica l'indirizzo (personalizza se serve)
                    controller.getModel().rimuoviIndirizzo(controller.getCurrentClienteId(), selezionato);
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
                    controller.getModel().scriviRecensione(
                        controller.getCurrentClienteId(),
                        nomeRistorante.getText(),
                        titolo.getText(),
                        descrizione.getText(),
                        (Integer) stelle.getSelectedItem()
                    );
                    JOptionPane.showMessageDialog(this, "Recensione inviata!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
                }
            }
        });
        return panel;
    }
}
