package it.unibo.view.cliente;

import java.awt.CardLayout;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unibo.controller.Controller;
import it.unibo.data.Pagamento;

public class CheckoutPanel extends JPanel {
    private final JComboBox<Pagamento> comboPagamenti;
    private final JButton btnConferma, btnIndietro;

    public CheckoutPanel(final Controller controller) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Seleziona Metodo di Pagamento:"));
        comboPagamenti = new JComboBox<>();
        add(comboPagamenti);

        final JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnIndietro = new JButton("Annulla");
        btnConferma  = new JButton("Conferma Ordine");
        btns.add(btnIndietro);
        btns.add(btnConferma);
        add(btns);

        btnIndietro.addActionListener(e -> controller.goToMenu());
        btnConferma.addActionListener(e -> {
            // raccogli indirizzo, carrello e pagamento e invia
            service.effettuaOrdine();
            JOptionPane.showMessageDialog(this, "Ordine effettuato!");
            mainLayout.show(rootPanel, "trovaRistorantiCard");
        });
    }
}
