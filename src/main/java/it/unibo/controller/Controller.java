package it.unibo.controller;

import java.util.Objects;

import javax.swing.text.View;

import it.unibo.data.Cliente;
import it.unibo.data.DAOException;
import it.unibo.data.DAOUtils;
import it.unibo.data.Ristorante;
import it.unibo.model.DBModel;
import it.unibo.model.Model;
import it.unibo.view.CardName;
import it.unibo.view.MainFrame;

public final class Controller {

    private final Model model;
    private final MainFrame view;

    public Controller() {
        this.model = new DBModel(DAOUtils.localMySQLConnection("root", ""));
        this.view = new MainFrame(this);
    }

    public void goToCliente() {
        view.show(CardName.CLIENTE);
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
