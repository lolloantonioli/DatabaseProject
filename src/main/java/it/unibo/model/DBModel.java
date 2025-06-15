package it.unibo.model;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.unibo.data.Cliente;
import it.unibo.data.Ordine;
import it.unibo.data.Piatto;
import it.unibo.data.Ristorante;

public class DBModel implements Model {
    
    private final Connection connection;

    public DBModel(Connection connection) {
        Objects.requireNonNull(connection, "Model created with null connection");
        this.connection = connection;
    }

    @Override
    public Optional<Cliente> findCliente(int codiceCliente) {
        return Cliente.DAO.find(connection, codiceCliente);
    }

    @Override
    public List<Cliente> loadClienti() {
        return Cliente.DAO.findAll(connection);
    }

    @Override
    public Optional<Ristorante> findRistorante(String piva) {
        return Ristorante.DAO.find(connection, piva);
    }

    @Override
    public List<Ristorante> loadRistorantiByZona(int codiceZona) {
        return Ristorante.DAO.findByZona(connection, codiceZona);
    }

    @Override
    public List<Piatto> loadPiattiByRistorante(String piva) {
        return Piatto.DAO.byRistorante(connection, piva);
    }

    @Override
    public Optional<Ordine> findOrdine(int codiceOrdine) {
        return Ordine.DAO.find(connection, codiceOrdine);
    }

    @Override
    public List<Ordine> loadOrdiniByCliente(int codiceCliente) {
        return Ordine.DAO.byCliente(connection, codiceCliente);
    }
}
