package it.unibo.model;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import it.unibo.data.Cliente;
import it.unibo.data.Ordine;
import it.unibo.data.Piatto;
import it.unibo.data.Ristorante;

public interface Model {
    // Gestione clienti
    Optional<Cliente> findCliente(int codiceCliente);
    List<Cliente> loadClienti();
    
    // Gestione ristoranti
    Optional<Ristorante> findRistorante(String piva);
    List<Ristorante> loadRistorantiByZona(int codiceZona);
    
    // Gestione piatti
    List<Piatto> loadPiattiByRistorante(String piva);
    
    // Gestione ordini
    Optional<Ordine> findOrdine(int codiceOrdine);
    List<Ordine> loadOrdiniByCliente(int codiceCliente);
    
    static Model fromConnection(Connection connection) {
        return new DBModel(connection);
    }
}
