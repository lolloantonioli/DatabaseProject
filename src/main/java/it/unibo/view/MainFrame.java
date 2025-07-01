package it.unibo.view;

import javax.swing.*;
import java.awt.*;
import it.unibo.controller.Controller;
import it.unibo.view.amministratore.AmministratorePanel;
import it.unibo.view.cliente.CheckoutPanel;
import it.unibo.view.cliente.ClienteAccessPanel;
import it.unibo.view.cliente.ClientePanel;
import it.unibo.view.rider.RiderAccessPanel;
import it.unibo.view.rider.RiderPanel;
import it.unibo.view.ristorante.PiattiPanel;
import it.unibo.view.ristorante.RistorantePanel;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainFrame extends JFrame {
    
    private final CardLayout layout;
    private final JPanel root;
    private final MenuPanel menuPanel;
    private final ClientePanel clientePanel;
    private final AmministratorePanel amministratorePanel;
    private final RiderPanel riderPanel;
    private final RistorantePanel ristorantePanel;
    private final CheckoutPanel checkoutPanel;
    private final ClienteAccessPanel clienteAccessPanel;
    private final RiderAccessPanel riderAccessPanel;
    private final PiattiPanel piattiPanel;

    /**
     * Temporary P.IVA used while login is not integrated.
     */
    private static final String TEST_PIVA = "00000000000";
    private static final String FRAME_NAME = "PL8";
    private static final String MSG = "CardName cannot be null";
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;

    public MainFrame(final Controller controller) {
        this.layout = new CardLayout();
        this.root = new JPanel(this.layout);
        this.menuPanel = new MenuPanel(controller);
        this.clientePanel = new ClientePanel(controller);
        this.amministratorePanel = new AmministratorePanel(controller);
        this.riderPanel = new RiderPanel(controller);
        this.ristorantePanel = new RistorantePanel(controller, TEST_PIVA);
        this.checkoutPanel = new CheckoutPanel(controller);
        this.clienteAccessPanel = new ClienteAccessPanel(controller);
        this.riderAccessPanel = new RiderAccessPanel(controller);
        this.piattiPanel = new PiattiPanel(controller, TEST_PIVA);

        this.setTitle(FRAME_NAME);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        root.add((Component) menuPanel, CardName.MENU.toString());
        root.add((Component) clientePanel, CardName.CLIENTE.toString());
        root.add((Component) amministratorePanel, CardName.AMMINISTRATORE.toString());
        root.add((Component) riderPanel, CardName.RIDER.toString());
        root.add((Component) ristorantePanel, CardName.RISTORANTE.toString());
        root.add((Component) checkoutPanel, CardName.CHECKOUT.toString());
        root.add((Component) clienteAccessPanel, CardName.CLIENTE_ACCESS.toString());
        root.add((Component) riderAccessPanel, CardName.RIDER_ACCESS.toString());
        this.add(root);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void show(final CardName name) {
        checkNotNull(name, MSG);
        this.layout.show(this.root, name.toString());
    }

    public PiattiPanel getMenuPanel() {
        return this.piattiPanel;
    }

    public ClientePanel getClientePanel() {
        return this.clientePanel;
    }

    public AmministratorePanel getAmministratorePanel() {
        return this.amministratorePanel;
    }

    public RiderPanel getRiderPanel() {
        return this.riderPanel;
    }

    public RistorantePanel getRistorantePanel() {
        return this.ristorantePanel;
    }

}

