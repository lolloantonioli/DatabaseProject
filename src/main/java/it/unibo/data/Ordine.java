package it.unibo.data;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Ordine {
    public final int codiceOrdine;
    public final int codicePagamento;
    public final int codiceStato;
    public final BigDecimal prezzoTotale;
    public final String piva;
    public final List<DettaglioOrdine> dettagli;

    public Ordine(int codiceOrdine, int codicePagamento, int codiceStato, 
                  BigDecimal prezzoTotale, String piva, List<DettaglioOrdine> dettagli) {
        this.codiceOrdine = codiceOrdine;
        this.codicePagamento = codicePagamento;
        this.codiceStato = codiceStato;
        this.prezzoTotale = prezzoTotale;
        this.piva = piva;
        this.dettagli = dettagli;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof Ordine)) return false;
        var o = (Ordine) other;
        return o.codiceOrdine == this.codiceOrdine &&
               o.codicePagamento == this.codicePagamento &&
               o.codiceStato == this.codiceStato &&
               o.prezzoTotale.equals(this.prezzoTotale) &&
               o.piva.equals(this.piva) &&
               o.dettagli.equals(this.dettagli);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceOrdine, codicePagamento, codiceStato, prezzoTotale, piva, dettagli);
    }

    @Override
    public String toString() {
        return Printer.stringify("Ordine", List.of(
            Printer.field("codiceOrdine", codiceOrdine),
            Printer.field("codicePagamento", codicePagamento),
            Printer.field("codiceStato", codiceStato),
            Printer.field("prezzoTotale", prezzoTotale),
            Printer.field("piva", piva),
            Printer.field("dettagli", dettagli)
        ));
    }

    public static final class DAO {

        public static Optional<Ordine> find(Connection connection, int codiceOrdine) {
            try (var stmt = DAOUtils.prepare(connection, Queries.FIND_ORDINE, codiceOrdine);
                 var rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    var dettagli = DettaglioOrdine.DAO.byOrdine(connection, codiceOrdine);
                    return Optional.of(new Ordine(
                        rs.getInt("codice_ordine"),
                        rs.getInt("codice_pagamento"),
                        rs.getInt("codice_stato"),
                        rs.getBigDecimal("prezzo_totale"),
                        rs.getString("piva"),
                        dettagli
                    ));
                }
                return Optional.empty();
                
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento dell'ordine", e);
            }
        }

        public static List<Ordine> byCliente(Connection connection, int codiceCliente) {
            try (var stmt = DAOUtils.prepare(connection, Queries.ORDINI_BY_CLIENTE, codiceCliente);
                 var rs = stmt.executeQuery()) {
                
                var ordini = new java.util.ArrayList<Ordine>();
                while (rs.next()) {
                    var codiceOrdine = rs.getInt("codice_ordine");
                    var dettagli = DettaglioOrdine.DAO.byOrdine(connection, codiceOrdine);
                    
                    ordini.add(new Ordine(
                        codiceOrdine,
                        rs.getInt("codice_pagamento"),
                        rs.getInt("codice_stato"),
                        rs.getBigDecimal("prezzo_totale"),
                        rs.getString("piva"),
                        dettagli
                    ));
                }
                return ordini;
                
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento degli ordini del cliente", e);
            }
        }
        /**
         * Restituisce gli ordini che un rider deve consegnare
         */
        public static List<Ordine> daConsegnareByRider(Connection connection, int codiceRider) {
            try (var stmt = DAOUtils.prepare(connection, Queries.ORDINI_DA_CONSEGNARE_BY_RIDER, codiceRider);
                 var rs = stmt.executeQuery()) {
                
                var ordini = new ArrayList<Ordine>();
                while (rs.next()) {
                    var codiceOrdine = rs.getInt("codice_ordine");
                    var dettagli = DettaglioOrdine.DAO.byOrdine(connection, codiceOrdine);
                    ordini.add(new Ordine(
                        codiceOrdine,
                        rs.getInt("codice_pagamento"),
                        rs.getInt("codice_stato"),
                        rs.getBigDecimal("prezzo_totale"),
                        rs.getString("piva"),
                        dettagli
                    ));
                }
                return ordini;
            } catch (Exception e) {
                throw new DAOException("Error loading to-deliver orders for rider " + codiceRider, e);
            }
        }

        /**
         * Restituisce gli ordini già consegnati da un rider
         */
        public static List<Ordine> consegnatiByRider(Connection connection, int codiceRider) {
            try (var stmt = DAOUtils.prepare(connection, Queries.ORDINI_CONSEGNATI_BY_RIDER, codiceRider);
                 var rs = stmt.executeQuery()) {
                
                var ordini = new ArrayList<Ordine>();
                while (rs.next()) {
                    var codiceOrdine = rs.getInt("codice_ordine");
                    var dettagli = DettaglioOrdine.DAO.byOrdine(connection, codiceOrdine);
                    ordini.add(new Ordine(
                        codiceOrdine,
                        rs.getInt("codice_pagamento"),
                        rs.getInt("codice_stato"),
                        rs.getBigDecimal("prezzo_totale"),
                        rs.getString("piva"),
                        dettagli
                    ));
                }
                return ordini;
            } catch (Exception e) {
                throw new DAOException("Error loading delivered orders for rider " + codiceRider, e);
            }
        }

        public int insertFullOrder(Connection conn,
                                   int codicePagamento,
                                   BigDecimal prezzoTotale,
                                   String piva,
                                   List<DettaglioOrdine> cart) {
            try {
                conn.setAutoCommit(false);
                // 1) crea ordine con stato=1 (In preparazione)
                int orderId;
                try (var ps = DAOUtils.prepare(conn, Queries.INSERT_ORDINE,
                                               codicePagamento, 1, prezzoTotale, piva)) {
                    ps.executeUpdate();
                    try (var rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) throw new DAOException("Nessun ID ordine");
                        orderId = rs.getInt(1);
                    }
                }
                // 2) inserisci dettagli
                try (var psDet = DAOUtils.prepare(conn, Queries.INSERT_DETTAGLIO_ORDINE)) {
                    int line = 1;
                    for (var d : cart) {
                        psDet.setInt(1, orderId);
                        psDet.setInt(2, d.codicePiatto);
                        psDet.setInt(3, line++);
                        psDet.setInt(4, d.quantita);
                        psDet.setBigDecimal(5, d.prezzoUnitario);
                        psDet.addBatch();
                    }
                    psDet.executeBatch();
                }
                conn.commit();
                return orderId;
            } catch (Exception e) {
                try { conn.rollback(); } catch (Exception ign) {}
                throw new DAOException("Errore creazione ordine completo", e);
            } finally {
                try { conn.setAutoCommit(true); } catch (Exception ign) {}
            }
        }
    }
}
