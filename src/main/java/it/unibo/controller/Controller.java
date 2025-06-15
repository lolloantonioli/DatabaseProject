package it.unibo.controller;

import java.util.Objects;

import javax.swing.text.View;

import it.unibo.data.Cliente;
import it.unibo.data.Ristorante;
import it.unibo.model.Model;

public final class Controller {

    private final Model model;
    private final View view;

    public Controller(Model model, View view) {
        Objects.requireNonNull(model, "Controller created with null model");
        Objects.requireNonNull(view, "Controller created with null view");
        this.model = model;
        this.view = view;
    }

    public void userRequestedInitialPage() {
        this.loadClientList();
    }

    public void userClickedCliente(Cliente cliente) {
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
    }

}
