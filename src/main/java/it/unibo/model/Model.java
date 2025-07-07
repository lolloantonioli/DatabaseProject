package it.unibo.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
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
    Optional<Cliente> findClienteById(int codiceCliente);
    Optional<Cliente> findClienteByUsername(String username);
    List<Cliente> loadClienti();
    void insertCliente(Cliente cliente);
    
    // Gestione ristoranti
    void insertRistorante(Ristorante ristorante);
    Optional<Ristorante> findRistoranteByPiva(String piva);
    List<Ristorante> loadRistorantiByZona(int codiceZona);
    Optional<String> loadPivaByNome(String nome);
    List<Ristorante> listAllRistoranti();
    List<Object[]> loadRistorantiTopSpesaMedia();
    List<Object[]> loadTop10RistorantiPerRecensioni();

    // Gestione piatti
    int insertPiatto(Piatto piatto, String piva);
    List<Piatto> loadPiattiByRistorante(String piva);
    List<String> loadPiattoInOrdini(int codicePiatto);
    List<String> loadTop10Piatti();
    void deletePiatto(int codicePiatto);
    
    // Gestione ordini
    void insertOrdine(int codicePagamento, double prezzoTotale, String piva, List<DettaglioOrdine> dettagli);
    Optional<Ordine> findOrdine(int codiceOrdine);
    List<Ordine> loadOrdiniByCliente(int codiceCliente);
    List<Ordine> loadOrdiniDaConsegnareByRider(int codiceRider);
    List<Ordine> loadOrdiniConsegnatiByRider(int codiceRider);
    
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
    List<Ordine> inPreparazioneByZona(int codiceZona);
    
    // Gestione rider
    List<Rider> loadRidersByZona(int codiceZona);
    Optional<Rider> findRider(int codiceRider);
    List<Rider> findRidersAvailable(int codiceZona);
    int insertRider(Rider rider);
    Optional<Rider> findRiderByEmail(String email);
    void prendiInCaricoOrdine(int codiceOrdine, int codiceRider);
    Optional<Ordine> ordineInCaricoByRider(int codiceRider);
    void consegnaOrdine(int codiceOrdine, int codiceRider);
    List<Object[]> topRiderPerConsegneInPeriodo(Date from, Date to);

    // Gestione residenze
    List<Residenza> loadResidenzeByCliente(int codiceCliente);

    // Gestione recensioni
    void insertRecensione(Recensione recensione);
    List<Recensione> loadRecensioniByRistorante(String piva);
    List<Recensione> loadRecensioniByCliente(int codiceCliente);
    List<String> loadTop10Ristoranti();
    void deleteRecensione(int codiceCliente, String piva, String titolo);

    // Gestione raccolte punti
    void insertRaccoltaPunti(RaccoltaPunti raccoltaPunti);
    Optional<RaccoltaPunti> loadRaccoltePuntiByCliente(int codiceCliente);

    // Gestione promozioni
    void insertPromozione(Promozione promozione);
    List<Promozione> loadPromozioniByRistorante(String piva);
    void deletePromozione(String piva, Date dataInizio, Date dataFine);

    // Gestione pagamenti
    int insertPagamento(Pagamento pagamento);
    List<Pagamento> loadPagamentiByCliente(int codiceCliente);

    // Gestione offerte di piatti
    void insertOffre(Offre offre);
    List<Offre> loadPiattiOffertiByRistorante(String piva);

    // Gestione mezzi
    void insertMezzo(Mezzo mezzo);
    List<Mezzo> loadMezziByRider(int codiceRider);
    int getNextCodiceMezzo(int codiceRider);
    void deleteMezzo(int codiceRider, int codiceMezzo);

    // Gestione metodi di pagamento
    void insertMetodoPagamento(MetodoPagamento metodoPagamento);
    List<MetodoPagamento> loadMetodiPagamentoByCliente(int codiceCliente);
    void deleteMetodoPagamento(int codiceCliente, String nome);

    // Gestione indirizzi
    void insertIndirizzo(Indirizzo indirizzo, int codiceCliente);
    List<Indirizzo> loadIndirizziByCliente(int codiceCliente);
    Optional<ZonaGeografica> findZonaByIndirizzo(int codiceIndirizzo);
    void deleteIndirizzo(int codiceCliente, int codiceIndirizzo);

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
    void deleteCarta(int codiceCliente, String nome, String numero);

    // Gestione applicazioni promozione
    List<Applicazione> loadOrdiniConPromozione();
 /** Restituisce gli ordini ricevuti dal ristorante indicato. */
    List<Ordine> loadOrdiniByRistorante(String piva);
    void aggiungiAlCarrello(Piatto piatto, int quantita);
    List<RigaCarrello> getCarrello();
    void svuotaCarrello();
    double getTotaleCarrello();

    CarrelloInfo calcolaTotaleCheckout(int codiceCliente);
    List<Pagamento> getPagamentiCliente(int codiceCliente);
    int creaOrdineCompleto(int codiceCliente, Pagamento metodoSelezionato);
}
