package it.unibo.controller;

import java.util.Optional;

import it.unibo.data.DAOUtils;
import it.unibo.model.DBModel;
import it.unibo.model.Model;
import it.unibo.view.CardName;
import it.unibo.view.MainFrame;
import it.unibo.view.rider.RiderProfiloPanel;


public final class Controller {

    private final Model model;
    private final MainFrame view;
    private Optional<Integer> currentClienteId;
    private Optional<Integer> currentRiderId;
    private Optional<String> currentRistorantePiva;
    private Optional<String> orderPiva;

    public Controller() {
        this.currentClienteId = Optional.empty();
        this.currentRiderId = Optional.empty();
        this.currentRistorantePiva = Optional.empty();
        this.orderPiva = Optional.empty();
        this.model = new DBModel(DAOUtils.localMySQLConnection(), this);
        this.view = new MainFrame(this);
    }

    public void goToCliente(final int codiceCliente) {
        currentClienteId = Optional.of(codiceCliente);
        view.getClientePanel().aggiornaIndirizzi();
        view.show(CardName.CLIENTE);
    }

    public void goToClienteAccess() {
        view.show(CardName.CLIENTE_ACCESS);
    }

    public void goToAmministratore() {
        view.show(CardName.AMMINISTRATORE);
    }

    public void goToRistoranteAccess() {
        view.show(CardName.RISTORANTE_ACCESS);
    }

    public void goToRider(final int codiceRider) {
        this.currentRiderId = Optional.of(codiceRider);
        view.getRiderPanel().getRiderOrdiniPanel().aggiorna();
        view.show(CardName.RIDER);
    }

    public void goToRiderAccess() {
        view.show(CardName.RIDER_ACCESS);
    }

    public void goToRistorante(final String piva) {
        this.currentRistorantePiva = Optional.of(piva);
        view.getRistorantePanel().getRecensioniPanel().refreshTable();
        view.show(CardName.RISTORANTE);
    }

    public void goToMenu() {
        view.show(CardName.MENU);
    }

    public void goToCheckout(){
        view.gCheckoutPanel().aggiornaDettaglioOrdine();
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

    public int getCurrentRiderId() {
        if (currentRiderId.isEmpty()) {
            throw new IllegalStateException("Nessun rider attualmente loggato");
        }
        return currentRiderId.get();
    }

    public boolean isRiderLoggato() {
        return currentRiderId.isPresent();
    }

    public boolean riderHaPatente() {
        if (currentRiderId.isEmpty()) {
            throw new IllegalStateException("Nessun rider attualmente loggato");
        }
        return model.findRider(currentRiderId.get()).get().patente;
    }

    public RiderProfiloPanel getRiderProfiloPanel() {
        return view.getRiderPanel().getRiderProfiloPanel();
    }

    public String getCurrentPiva() {
        if (currentRistorantePiva.isEmpty()) {
            return "";
        }
        return this.currentRistorantePiva.get();
    }

    public String getOrderPiva() {
        if (orderPiva.isEmpty()) {
            return "";
        }
        return this.orderPiva.get(); 
    }

    public void setOrderPiva(final String piva) {
        this.orderPiva = Optional.of(piva);
    }

}
