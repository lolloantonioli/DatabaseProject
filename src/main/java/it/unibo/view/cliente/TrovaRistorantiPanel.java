package it.unibo.view.cliente;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unibo.controller.Controller;
import it.unibo.data.Indirizzo;
import it.unibo.data.Ristorante;

public class TrovaRistorantiPanel extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel cards;

    private final JPanel indirizzoPanel;     // form per scegliere/aggiungere indirizzo
    private final JPanel ricercaPanel;       // vera UI di ricerca ristoranti

    public TrovaRistorantiPanel() {
        this.cardLayout = new CardLayout();
        this.cards = new JPanel(cardLayout);
        setLayout(new BorderLayout());
        initIndirizzoPanel();
        initRicercaPanel();
        cards.add(indirizzoPanel, "indirizzo");
        cards.add(ricercaPanel,   "ricerca");
        add(cards, BorderLayout.CENTER);

        // Mostra prima il panel di indirizzo
        cardLayout.show(cards, "indirizzo");
    }

    private void initIndirizzoPanel() {
        indirizzoPanel = new JPanel(new BorderLayout());
        final JComboBox<Indirizzo> comboIndirizzi = new JComboBox<>();
        final JButton btnSeleziona = new JButton("Seleziona");
        btnSeleziona.addActionListener(e -> {
            // salva indirizzo selezionato nel model
            cardLayout.show(cards, "ricerca");
            loadRistorantiPerZona(selezionato);
        });
        indirizzoPanel.add(new JLabel("Scegli un indirizzo:"), BorderLayout.NORTH);
        indirizzoPanel.add(comboIndirizzi, BorderLayout.CENTER);
        indirizzoPanel.add(btnSeleziona, BorderLayout.SOUTH);
    }

    private void initRicercaPanel() {
        ricercaPanel = new JPanel(new BorderLayout());
        // qui metti txtSearch, comboZone disabilitato (zona fissa dall’indirizzo)
        // lista ristoranti ecc.
    }

    private void loadRistorantiPerZona(Indirizzo ind) {
        List<Ristorante> list = service.getRistorantiPerZona(ind.getZona());
        // popola la JList/Tabella
    }
}
