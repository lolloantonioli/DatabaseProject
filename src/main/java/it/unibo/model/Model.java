package it.unibo.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import it.unibo.data.Applicazione;
import it.unibo.data.Carta;
import it.unibo.data.Cliente;
import it.unibo.data.Contratto;
import it.unibo.data.DettaglioOrdine;
import it.unibo.data.GeneraPunti;
import it.unibo.data.Indirizzo;
import it.unibo.data.MetodoPagamento;
import it.unibo.data.Mezzo;
import it.unibo.data.Offre;
import it.unibo.data.Ordine;
import it.unibo.data.Pagamento;
import it.unibo.data.Piatto;
import it.unibo.data.Promozione;
import it.unibo.data.RaccoltaPunti;
import it.unibo.data.Recensione;
import it.unibo.data.Residenza;
import it.unibo.data.Rider;
import it.unibo.data.Ristorante;
import it.unibo.data.StatoOrdine;
import it.unibo.data.Visualizzazione;
import it.unibo.data.ZonaGeografica;

public interface Model {
    // Gestione clienti
    Optional<Cliente> findCliente(int codiceCliente);
    List<Cliente> loadClienti();
    void insertCliente(Cliente cliente);
    
    // Gestione ristoranti
    void insertRistorante(Ristorante ristorante);
    Optional<Ristorante> findRistoranteByPiva(String piva);
    List<Ristorante> loadRistorantiByZona(int codiceZona);

    // Gestione piatti
    void insertPiatto(Piatto piatto);
    List<Piatto> loadPiattiByRistorante(String piva);
    List<String> loadPiattoInOrdini(int codicePiatto);
    List<String> loadTop10Piatti();
    
    // Gestione ordini
    void insertOrdine(int codicePagamento, BigDecimal prezzoTotale, String piva, List<DettaglioOrdine> dettagli);
    Optional<Ordine> findOrdine(int codiceOrdine);
    List<Ordine> loadOrdiniByCliente(int codiceCliente);
    List<Ordine> loadOrdiniDaConsegnareByRider(int codiceRider);
    List<Ordine> loadOrdiniCOnsegnatiByRider(int codiceRider);
    
    // Gestione zona geografica
    void insertZona(ZonaGeografica zonaGeografica);
    
    // Gestione visualizzazioni
    List<Visualizzazione> loadVisualizzazioniByClienteOnDate(int codiceCliente, LocalDate date);
    int loadVisualizzazioniRistorante(String piva);
    
    // Gestione Utilizzo punti
    List<Integer> loadOrdersWithPoints();
    
    // Gestione stato ordine
    void insertState(StatoOrdine statoOrdine);
    void updateState(StatoOrdine statoOrdine);
    Optional<StatoOrdine> findStateByOrder(int codiceOrdine);
    
    // Gestione rider
    List<Rider> loadRidersByZona(int codiceZona);
    Optional<Rider> findRider(int codiceRider);
    List<Rider> findRidersAvailable(int codiceZona);
    void insertRider(Rider rider);

    // Gestione residenze
    List<Residenza> loadResidenzeByCliente(int codiceCliente);

    // Gestione recensioni
    void insertRecensione(Recensione recensione);
    List<Recensione> loadRecensioniByRistorante(String piva);
    List<Recensione> loadRecensioniByCliente(int codiceCliente);
    List<String> loadTop10Ristoranti();

    // Gestione raccolte punti
    void insertRaccoltaPunti(RaccoltaPunti raccoltaPunti);
    Optional<RaccoltaPunti> loadRaccoltePuntiByCliente(int codiceCliente);

    // Gestione promozioni
    void insertPromozione(Promozione promozione);
    List<Promozione> loadPromozioniByRistorante(String piva);

    // Gestione pagamenti
    void insertPagamento(Pagamento pagamento);
    List<Pagamento> loadPagamentiByCliente(int codiceCliente);

    // Gestione offerte di piatti
    void insertOffre(Offre offre);
    List<Offre> loadPiattiOffertiByRistorante(String piva);

    // Gestione mezzi
    void insertMezzo(Mezzo mezzo);
    List<Mezzo> loadMezziByRider(int codiceRider);

    // Gestione metodi di pagamento
    void insertMetodoPagamento(MetodoPagamento metodoPagamento);
    List<MetodoPagamento> loadMetodiPagamentoByCliente(int codiceCliente);

    // Gestione indirizzi
    void insertIndirizzo(Indirizzo indirizzo);
    List<Indirizzo> loadIndirizziByCliente(int codiceCliente);
    Optional<Indirizzo> findZonaByIndirizzo(int codiceIndirizzo);

    // Gestione generazione punti
    Optional<GeneraPunti> findGenerazionePuntiByOrder(int codiceOrdine);

    // Gestione dettaglio ordini
    List<DettaglioOrdine> loadDettagliByOrdine(int codiceOrdine);

    // Gestione contratti
    void insertContratto(Contratto contratto);
    Optional<Contratto> findContrattoByRider(int codiceRider);

    // Gestione carte
    List<Carta> loadCarteByCliente(int codiceCliente);
    void insertCarta(Carta carta);
    List<Integer> loadOrdiniByCarta(int codiceCliente, String numeroCarta);

    // Gestione applicazioni promozione
    List<Applicazione> loadOrdiniConPromozione();
    
    static Model fromConnection(Connection connection) {
        return new DBModel(connection);
    }
}
