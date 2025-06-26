package it.unibo.controller;

import java.util.Optional;

import it.unibo.data.DAOUtils;
import it.unibo.model.DBModel;
import it.unibo.model.Model;
import it.unibo.view.CardName;
import it.unibo.view.MainFrame;

public final class Controller {

    private final Model model;
    private final MainFrame view;
    private Optional<Integer> currentClienteId;

    public Controller() {
        this.currentClienteId = Optional.empty();
        this.model = new DBModel(DAOUtils.localMySQLConnection());
        this.view = new MainFrame(this);
    }

    public void goToCliente(final int codiceCliente) {
        currentClienteId = Optional.of(codiceCliente);
        view.show(CardName.CLIENTE);
    }

    public void goToClienteAccess() {
        view.show(CardName.CLIENTE_ACCESS);
    }

    public void goToAmministratore() {
        view.show(CardName.AMMINISTRATORE);
    }

    public void goToRider() {
        view.show(CardName.RIDER);
    }

    public void goToRistorante() {
        view.show(CardName.RISTORANTE);
    }

    public void goToMenu() {
        view.show(CardName.MENU);
    }

    public void goToCheckout(){
        view.show(CardName.CHECKOUT);
    }

    /**
     * Restituisce l'ID del cliente attualmente loggato
     * @return l'ID del cliente corrente
     * @throws IllegalStateException se nessun cliente è loggato
     */
    public int getCurrentClienteId() {
        if (currentClienteId.isEmpty()) {
            throw new IllegalStateException("Nessun cliente attualmente loggato");
        }
        return currentClienteId.get();
    }

    /**
     * Restituisce il model per permettere l'accesso ai dati
     * @return il model
     */
    public Model getModel() {
        return this.model;
    }

    /**
     * Verifica se un cliente è attualmente loggato
     * @return true se un cliente è loggato, false altrimenti
     */
    public boolean isClienteLoggato() {
        return currentClienteId.isPresent();
    }

    /*public void userClickedCliente(Cliente cliente) {
        try {
            this.view.loadingClienteDetail();
            var ordini = this.model.loadOrdiniByCliente(cliente.codiceCliente);
            this.view.clienteDetailPage(cliente, ordini);
        } catch (DAOException e) {
            this.view.failedToLoadClienteDetail(cliente);
        }
    }

    public void userClickedRistorante(Ristorante ristorante) {
        try {
            this.view.loadingRistoranteDetail();
            var piatti = this.model.loadPiattiByRistorante(ristorante.piva);
            this.view.ristoranteDetailPage(ristorante, piatti);
        } catch (DAOException e) {
            this.view.failedToLoadRistoranteDetail(ristorante);
        }
    }

    public void userClickedBack() {
        this.loadClientList();
    }

    public void userClickedReloadClients() {
        this.loadClientList();
    }

    void loadClientList() {
        try {
            this.view.loadingClients();
            var clienti = this.model.loadClienti();
            this.view.clientListPage(clienti);
        } catch (DAOException e) {
            e.printStackTrace();
            this.view.failedToLoadClients();
        }
    }*/

}
