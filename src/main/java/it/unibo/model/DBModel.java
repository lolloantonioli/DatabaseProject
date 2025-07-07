package it.unibo.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import it.unibo.controller.Controller;
import it.unibo.data.Applicazione;
import it.unibo.data.Carta;
import it.unibo.data.Cliente;
import it.unibo.data.Contratto;
import it.unibo.data.DAOUtils;
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
import it.unibo.data.UtilizzaPunti;
import it.unibo.data.Visualizzazione;
import it.unibo.data.ZonaGeografica;

public class DBModel implements Model {
    
    private final Connection connection;
    private final List<RigaCarrello> carrello = new ArrayList<>();
    private final Controller controller;

    public DBModel(final Connection connection, final Controller controller) {
        Objects.requireNonNull(connection, "Model created with null connection");
        this.connection = connection;
        this.controller = controller;
    }

    @Override
    public Optional<Cliente> findClienteById(int codiceCliente) {
        return Cliente.DAO.find(connection, codiceCliente);
    }

    @Override
    public Optional<Cliente> findClienteByUsername(String username) {
        return Cliente.DAO.findByUsername(connection, username);
    }

    @Override
    public List<Cliente> loadClienti() {
        return Cliente.DAO.findAll(connection);
    }

    @Override
    public Optional<Ristorante> findRistoranteByPiva(String piva) {
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
    public List<Ordine> loadOrdiniByRistorante(String piva) {
        return Ordine.DAO.byRistorante(connection, piva);
    }

    @Override
    public List<Ordine> loadOrdiniByCliente(int codiceCliente) {
        return Ordine.DAO.byCliente(connection, codiceCliente);
    }

    @Override
    public void insertCliente(Cliente cliente) {
        Cliente.DAO.save(connection, cliente);
    }

    @Override
    public void insertRistorante(Ristorante ristorante) {
        Ristorante.DAO.insertRistorante(connection, ristorante);
    }

    @Override
    public Optional<String> loadPivaByNome(final String nome) {
        return Ristorante.DAO.findPivaByNome(connection, nome);
    }

    @Override
    public int insertPiatto(Piatto piatto, String piva) {
        return Piatto.DAO.insertPiatto(connection, piatto, piva);
    }

    @Override
    public List<String> loadPiattoInOrdini(int codicePiatto) {
        return Piatto.DAO.ordiniByPiatto(connection, codicePiatto);
    }

    @Override
    public List<String> loadTop10Piatti() {
        return Piatto.DAO.top10Piatti(connection);
    }

    @Override
    public void insertOrdine(int codicePagamento, double prezzoTotale, String piva, List<DettaglioOrdine> dettagli) {
        Ordine.DAO.insertFullOrder(connection, codicePagamento, prezzoTotale, piva, dettagli);
    }

    @Override
    public List<Ordine> loadOrdiniDaConsegnareByRider(int codiceRider) {
        return Ordine.DAO.daConsegnareByRider(connection, codiceRider);
    }

    @Override
    public List<Ordine> loadOrdiniConsegnatiByRider(int codiceRider) {
        return Ordine.DAO.consegnatiByRider(connection, codiceRider);
    }

    @Override
    public void insertZona(ZonaGeografica zonaGeografica) {
        ZonaGeografica.DAO.insertZona(connection, zonaGeografica);
    }

    @Override
    public List<Visualizzazione> loadVisualizzazioniByClienteOnDate(int codiceCliente, LocalDate date) {
        return Visualizzazione.DAO.listByClienteOnDate(connection, codiceCliente, date);
    }

    @Override
    public int loadVisualizzazioniRistorante(String piva) {
        return Visualizzazione.DAO.countByRistorante(connection, piva);
    }

    @Override
    public List<Integer> loadOrdersWithPoints() {
        return UtilizzaPunti.DAO.listOrdersWithPoints(connection);
    }

    @Override
    public void insertState(StatoOrdine statoOrdine) {
        StatoOrdine.DAO.insertState(connection, statoOrdine);
    }

    @Override
    public void updateState(StatoOrdine statoOrdine) {
        StatoOrdine.DAO.updateState(connection, statoOrdine);
    }

    @Override
    public Optional<StatoOrdine> findStateByOrder(int codiceOrdine) {
        return StatoOrdine.DAO.findByOrder(connection, codiceOrdine);
    }

    @Override
    public List<Rider> loadRidersByZona(int codiceZona) {
        return Rider.DAO.findByZona(connection, codiceZona);
    }

    @Override
    public Optional<Rider> findRider(int codiceRider) {
        return Rider.DAO.find(connection, codiceRider);
    }

    @Override
    public List<Rider> findRidersAvailable(int codiceZona) {
        return Rider.DAO.findAvailable(connection, codiceZona);
    }

    @Override
    public int insertRider(Rider rider) {
        return Rider.DAO.insertRider(connection, rider);
    }

    @Override
    public Optional<Rider> findRiderByEmail(String email) {
        return Rider.DAO.findRiderByEmail(connection, email);
    }

    @Override
    public List<Residenza> loadResidenzeByCliente(int codiceCliente) {
        return Residenza.DAO.listByCliente(connection, codiceCliente);
    }

    @Override
    public void insertRecensione(Recensione recensione) {
        Recensione.DAO.insertRecensione(connection, recensione);
    }

    @Override
    public List<Recensione> loadRecensioniByRistorante(String piva) {
        return Recensione.DAO.listByRistorante(connection, piva);
    }

    @Override
    public List<Recensione> loadRecensioniByCliente(int codiceCliente) {
        return Recensione.DAO.listByCliente(connection, codiceCliente);
    }

    @Override
    public List<String> loadTop10Ristoranti() {
        return Recensione.DAO.top10Ristoranti(connection);
    }

    @Override
    public void insertRaccoltaPunti(RaccoltaPunti raccoltaPunti) {
        RaccoltaPunti.DAO.insertRaccolta(connection, raccoltaPunti);
    }

    @Override
    public Optional<RaccoltaPunti> loadRaccoltePuntiByCliente(int codiceCliente) {
        return RaccoltaPunti.DAO.findByCliente(connection, codiceCliente);
    }

    @Override
    public void insertPromozione(Promozione promozione) {
        Promozione.DAO.insertPromozione(connection, promozione);
    }

    @Override
    public List<Promozione> loadPromozioniByRistorante(String piva) {
        return Promozione.DAO.listActiveByRistorante(connection, piva);
    }

    @Override
    public int insertPagamento(Pagamento pagamento) {
        return Pagamento.DAO.insertPagamento(connection, pagamento);
    }

    @Override
    public List<Pagamento> loadPagamentiByCliente(int codiceCliente) {
        return Pagamento.DAO.listByCliente(connection, codiceCliente);
    }

    @Override
    public void insertOffre(Offre offre) {
        Offre.DAO.insertOffre(connection, offre);
    }

    @Override
    public List<Offre> loadPiattiOffertiByRistorante(String piva) {
        return Offre.DAO.listByRistorante(connection, piva);
    }

    @Override
    public void insertMezzo(Mezzo mezzo) {
        Mezzo.DAO.insertMezzo(connection, mezzo);
    }

    @Override
    public List<Mezzo> loadMezziByRider(int codiceRider) {
        return Mezzo.DAO.listByRider(connection, codiceRider);
    }

    @Override
    public int getNextCodiceMezzo(int codiceRider) {
        return Mezzo.DAO.getNextCodiceMezzo(connection, codiceRider);
    }

    @Override
    public void insertMetodoPagamento(MetodoPagamento metodoPagamento) {
        MetodoPagamento.DAO.insertMetodo(connection, metodoPagamento);
    }

    @Override
    public List<MetodoPagamento> loadMetodiPagamentoByCliente(int codiceCliente) {
        return MetodoPagamento.DAO.listByCliente(connection, codiceCliente);
    }

    @Override
    public void insertIndirizzo(Indirizzo indirizzo, int codiceCliente) {
        Indirizzo.DAO.insertIndirizzoEAssociaResidenza(connection, indirizzo, codiceCliente);
    }

    @Override
    public List<Indirizzo> loadIndirizziByCliente(int codiceCliente) {
        return Indirizzo.DAO.listByCliente(connection, codiceCliente);
    }

    @Override
    public Optional<ZonaGeografica> findZonaByIndirizzo(int codiceIndirizzo) {
        return Indirizzo.DAO.findZonaByIndirizzo(connection, codiceIndirizzo);
    }

    @Override
    public Optional<GeneraPunti> findGenerazionePuntiByOrder(int codiceOrdine) {
        return GeneraPunti.DAO.findByOrdine(connection, codiceOrdine);
    }

    @Override
    public List<DettaglioOrdine> loadDettagliByOrdine(int codiceOrdine) {
        return DettaglioOrdine.DAO.byOrdine(connection, codiceOrdine);
    }

    @Override
    public void insertContratto(Contratto contratto) {
        Contratto.DAO.insertContratto(connection, contratto);
    }

    @Override
    public Optional<Contratto> findContrattoByRider(int codiceRider) {
        return Contratto.DAO.findByRider(connection, codiceRider);
    }

    @Override
    public List<Carta> loadCarteByCliente(int codiceCliente) {
        return Carta.DAO.listCarteByCliente(connection, codiceCliente);
    }

    @Override
    public void insertCarta(Carta carta) {
        Carta.DAO.insertCarta(connection, carta);
    }

    @Override
    public List<Integer> loadOrdiniByCarta(int codiceCliente, String numeroCarta) {
        return Carta.DAO.listOrdiniByCarta(connection, codiceCliente, numeroCarta);
    }

    @Override
    public List<Applicazione> loadOrdiniConPromozione() {
        return Applicazione.DAO.listAllWithPromozione(connection);
    }

    @Override
    public void deleteMetodoPagamento(int codiceCliente, String nome) {
        MetodoPagamento.DAO.deleteMetodoPagamento(connection, codiceCliente, nome);
    }

    @Override
    public void deleteIndirizzo(int codiceCliente, int codiceIndirizzo) {
        Indirizzo.DAO.deleteIndirizzo(connection, codiceCliente, codiceIndirizzo);
    }

    @Override
    public void deleteCarta(int codiceCliente, String nome, String numero) {
        Carta.DAO.deleteCarta(connection, codiceCliente, nome, numero);
    }

    @Override
    public void deleteRecensione(int codiceCliente, String piva, String titolo) {
        Recensione.DAO.deleteRecensione(connection, codiceCliente, piva, titolo);
    }

    @Override
    public void aggiungiAlCarrello(Piatto piatto, int quantita) {
        for (RigaCarrello r : carrello) {
            if (r.piatto.codicePiatto == piatto.codicePiatto) {
                r.quantita += quantita;
                return;
            }
        }
        carrello.add(new RigaCarrello(piatto, quantita));
    }

    @Override
    public List<RigaCarrello> getCarrello() {
        return Collections.unmodifiableList(carrello);
    }

    @Override
    public void svuotaCarrello() {
        carrello.clear();
    }

    @Override
    public double getTotaleCarrello() {
        return carrello.stream().mapToDouble(r -> r.piatto.prezzo.doubleValue() * r.quantita).sum();
    }

    @Override
    public void deleteMezzo(int codiceRider, int codiceMezzo) {
        Mezzo.DAO.deleteMezzo(connection, codiceRider, codiceMezzo);
    }

    @Override
    public List<Ordine> inPreparazioneByZona(int codiceZona) {
        return StatoOrdine.DAO.inPreparazioneByZona(connection, codiceZona);
    }

    @Override
    public void prendiInCaricoOrdine(int codiceOrdine, int codiceRider) {
        Rider.DAO.prendiInCaricoOrdine(connection, codiceOrdine, codiceRider);
    }

    @Override
    public Optional<Ordine> ordineInCaricoByRider(int codiceRider) {
        return Rider.DAO.ordineInCaricoByRider(connection, codiceRider);
    }

    @Override
    public void consegnaOrdine(int codiceOrdine, int codiceRider) {
        Rider.DAO.consegnaOrdine(connection, codiceOrdine, codiceRider);
    }

    @Override
    public List<Ristorante> listAllRistoranti() {
        return Ristorante.DAO.listAllRistoranti(connection);
    }

    @Override
    public List<Object[]> loadRistorantiTopSpesaMedia() {
        return Ristorante.DAO.ristorantiTopSpesa(connection);
    }

    @Override
    public List<Object[]> loadTop10RistorantiPerRecensioni() {
        return Ristorante.DAO.top10RistorantiPerRecensioni(connection);
    }

    @Override
    public List<Object[]> topRiderPerConsegneInPeriodo(Date from, Date to) {
        return Rider.DAO.topRiderConsegne(connection, from, to);
    }

    @Override
    public void deletePiatto(int codicePiatto) {
        Piatto.DAO.deletePiatto(connection, codicePiatto);
    }

    @Override
    public void deletePromozione(String piva, Date dataInizio, Date dataFine) {
        Promozione.DAO.deletePromozione(connection, piva, dataInizio, dataFine);
    }

    @Override
    public CarrelloInfo calcolaTotaleCheckout(int codiceCliente) {
        // total parziale e dettagli
        List<RigaCarrello> car = getCarrello(); // assume exists
        List<DettaglioInfo> dettagli = car.stream()
            .map(r -> new DettaglioInfo(r.piatto.nome, r.quantita, r.piatto.prezzo.doubleValue()))
            .collect(Collectors.toList());
        double totaleParziale = car.stream()
            .mapToDouble(r -> r.piatto.prezzo.doubleValue() * r.quantita)
            .sum();
        // promozioni
        var promo = Applicazione.DAO.listAllWithPromozione(connection).stream()
            .filter(a -> a.codiceOrdine == 0) // none yet, skip
            .findFirst();
        // invece calcola promozioni attive per ogni ristorante
        double scontoProm = 0; String descProm = "Nessuna";
        // omitted: query attive, pick best
        // raccolta punti
        var rp = RaccoltaPunti.DAO.findByCliente(connection, codiceCliente);
        double scontoPunti = 0; String descPunti = "Nessuna";
        if (rp.isPresent() && totaleParziale >= rp.get().sogliaPunti) {
            scontoPunti = totaleParziale * rp.get().percentualeSconto / 100.0;
            descPunti = rp.get().percentualeSconto + "%";
        }
        double totaleFinale = totaleParziale - scontoProm - scontoPunti;
        return new CarrelloInfo(dettagli, totaleParziale, scontoProm, descProm, scontoPunti, descPunti, totaleFinale);
    }

    @Override
    public List<Pagamento> getPagamentiCliente(int codiceCliente) {
        return Pagamento.DAO.listByCliente(connection, codiceCliente);
    }

    @Override
    public int creaOrdineCompleto(int codiceCliente, Pagamento metodoSelezionato) {
        try {
            connection.setAutoCommit(false);
            // 1) inserisci pagamento
            int idPag = Pagamento.DAO.insertPagamento(connection, metodoSelezionato);
            // 2) carrello
            var car = getCarrello();
            BigDecimal prezzoTot = car.stream()
                .map(r -> r.piatto.prezzo.multiply(BigDecimal.valueOf(r.quantita)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            String piva = controller.getCurrentPiva();
            int ordineId = Ordine.DAO.insertFullOrder(connection, idPag, prezzoTot, piva, car.stream()
                .map(r -> new DettaglioOrdine(r.piatto.codicePiatto, 0, r.quantita, r.piatto.prezzo, r.piatto.nome, r.piatto.descrizione))
                .collect(Collectors.toList())
            );
            connection.commit();
            return ordineId;
        } catch (Exception e) {
            try { connection.rollback(); } catch (Exception ign) {}
            throw new RuntimeException(e);
        } finally {
            try { connection.setAutoCommit(true); } catch (Exception ign) {}
        }
    }

    public void creaOrdineCompleto(
        int codiceCliente,
        MetodoPagamento metodo,
        CarrelloInfo carrello
    ) throws SQLException {
        try (var conn = DAOUtils.localMySQLConnection()) {
            conn.setAutoCommit(false);
            // 1) inserisci pagamento
            int idPagamento = Pagamento.DAO.insertPagamento(
                conn,
                new Pagamento(
                    0,
                    new java.sql.Date(System.currentTimeMillis()),
                    BigDecimal.valueOf(carrello.totaleFinale),
                    codiceCliente,
                    metodo.nome
                )
            );

            // 2) inserisci ordine + dettagli
            int idOrdine = Ordine.DAO.insertFullOrder(
                conn,
                idPagamento,
                BigDecimal.valueOf(carrello.totaleFinale),
                carrello.piva,
                carrello.dettagli.stream()
                    .map(d -> new DettaglioOrdine(
                        d.codicePiatto,
                        /* numeroLinea */ 0,
                        d.quantita,
                        d.prezzoUnitario,
                        d.nomePiatto,
                        d.descrizionePiatto
                    ))
                    .toList()
            );

            // 3) inserisci stato "in preparazione"
            StatoOrdine stato = new StatoOrdine(
                idOrdine,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null,
                /* nessun rider assegnato */ 0
            );
            StatoOrdine.DAO.insertState(conn, stato);

            // 4) applica eventuale promozione usata
            if (carrello.scontoPromozioni > 0 && carrello.promozioneUsata != null) {
                var pr = carrello.promozioneUsata;
                Applicazione.DAO.insertApplicazione(
                    conn,
                    new Applicazione(
                        idOrdine,
                        BigDecimal.valueOf(carrello.scontoPromozioni),
                        pr.piva,
                        pr.dataInizio,
                        pr.dataFine
                    )
                );
            }

            // 5) utilizzo punti se presenti
            if (carrello.scontoPunti > 0) {
                UtilizzaPunti.DAO.insertUtilizzo(
                    conn,
                    new UtilizzaPunti(
                        idOrdine,
                        carrello.puntiUsati,
                        carrello.scontoPunti,
                        codiceCliente
                    )
                );
            }

            // 6) genera nuovi punti
            if (carrello.puntiGenerati > 0) {
                GeneraPunti.DAO.insertGenerazione(
                    conn,
                    new GeneraPunti(
                        idOrdine,
                        carrello.puntiGenerati,
                        codiceCliente
                    )
                );
            }

            conn.commit();
        } catch (SQLException|RuntimeException ex) {
            throw ex;
        }
    }


}
