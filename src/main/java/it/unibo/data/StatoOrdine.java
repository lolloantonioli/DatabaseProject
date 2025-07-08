package it.unibo.data;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class StatoOrdine {

    public final int codiceOrdine;
    public final LocalDateTime data;
    public boolean inPreparazione;
    public LocalDateTime oraInPreparazione;
    public boolean inConsegna;
    public LocalDateTime oraInConsegna;
    public boolean consegnato;
    public LocalDateTime oraConsegnato;
    public int codiceRider;

    public StatoOrdine(int codiceOrdine, LocalDateTime data, boolean inPreparazione,
                       LocalDateTime oraInPreparazione, boolean inConsegna, LocalDateTime oraInConsegna,
                       boolean consegnato, LocalDateTime oraConsegnato, int codiceRider) {
        this.codiceOrdine = codiceOrdine;
        this.data = data;
        this.inPreparazione = inPreparazione;
        this.inConsegna = inConsegna;
        this.consegnato = consegnato;
        this.codiceRider = codiceRider;
        this.oraInPreparazione = oraInPreparazione;
        this.oraInConsegna = oraInConsegna;
        this.oraConsegnato = oraConsegnato;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || !(other instanceof StatoOrdine)) return false;
        StatoOrdine s = (StatoOrdine) other;
        return s.codiceOrdine == this.codiceOrdine
            && s.data.equals(this.data)
            && Objects.equals(s.inPreparazione, this.inPreparazione)
            && Objects.equals(s.inConsegna, this.inConsegna)
            && Objects.equals(s.consegnato, this.consegnato)
            && s.oraInPreparazione.equals(this.oraInPreparazione)
            && s.oraInConsegna.equals(this.oraInConsegna)
            && s.oraConsegnato.equals(this.oraConsegnato)
            && s.codiceRider == this.codiceRider;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceOrdine, data, inPreparazione, inConsegna, consegnato,
        oraInPreparazione, oraInConsegna, oraConsegnato, codiceRider);
    }

    @Override
    public String toString() {
        return Printer.stringify("StatoOrdine", List.of(
            Printer.field("codice ordine", codiceOrdine),
            Printer.field("data", data),
            Printer.field("in preparazione", inPreparazione),
            Printer.field("ora in preparazione", oraInPreparazione),
            Printer.field("in consegna", inConsegna),
            Printer.field("ora in consegna", oraInConsegna),
            Printer.field("consegnato", consegnato),
            Printer.field("ora consegnato", oraConsegnato),
            Printer.field("codice rider", codiceRider)
        ));
    }

    public static final class DAO {
        /**
         * Inserisce un nuovo stato ordine
         */
        public static void insertState(Connection conn, StatoOrdine s) {
            try (var ps = DAOUtils.prepare(conn,
                                           Queries.INSERT_STATO_ORDINE,
                                           s.codiceOrdine,
                                           Timestamp.valueOf(s.data),
                                           s.inPreparazione,
                                           Timestamp.valueOf(s.oraInPreparazione),
                                           s.inConsegna,
                                           Timestamp.valueOf(s.oraInConsegna),
                                           s.consegnato,
                                           Timestamp.valueOf(s.oraConsegnato),
                                           s.codiceRider)) {
                ps.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore inserimento stato ordine " + s.codiceOrdine, e);
            }
        }

        /**
         * Aggiorna uno stato ordine esistente (modifica timestamp campi)
         */
        public static void updateState(Connection conn, StatoOrdine s) {
            try (var ps = DAOUtils.prepare(conn,
                                           Queries.UPDATE_STATO_ORDINE,
                                           s.inPreparazione,
                                           Timestamp.valueOf(s.oraInPreparazione),
                                           s.inConsegna,
                                           Timestamp.valueOf(s.oraInConsegna),
                                           s.consegnato,
                                           Timestamp.valueOf(s.oraConsegnato),
                                           s.codiceOrdine,
                                           s.codiceRider)) {
                ps.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore aggiornamento stato ordine " + s.codiceOrdine, e);
            }
        }

        /**
         * Recupera l'ultimo stato di un ordine
         */
        public static Optional<StatoOrdine> findByOrder(Connection conn, int codiceOrdine) {
            try (var ps = DAOUtils.prepare(conn, Queries.SELECT_STATO_BY_ORDINE, codiceOrdine);
                 var rs = ps.executeQuery()) {
                if (rs.next()) {
                    var data = rs.getTimestamp("data").toLocalDateTime();
                    var prep = rs.getBoolean("in_preparazione");
                    var oraPrep = rs.getTimestamp("ora_in_preparazione").toLocalDateTime();
                    var conseg = rs.getBoolean("ora_in_consegna");
                    var oraConseg = rs.getTimestamp("ora_in_consegna").toLocalDateTime();
                    var cons = rs.getBoolean("ora_consegnato");
                    var oraCons = rs.getTimestamp("ora_consegnato").toLocalDateTime();
                    int rider = rs.getInt("codice_rider");
                    StatoOrdine so = new StatoOrdine(codiceOrdine, data, prep, oraPrep, conseg, oraConseg, cons, oraCons, rider);
                    return Optional.of(so);
                }
                return Optional.empty();
            } catch (Exception e) {
                throw new DAOException("Errore recupero stato ordine " + codiceOrdine, e);
            }
        }

        // a. Lista ordini "in preparazione" per zona
        public static List<Ordine> inPreparazioneByZona(Connection conn, int codiceZona) {
            var ordini = new ArrayList<Ordine>();
            try (var ps = DAOUtils.prepare(conn, Queries.ORDINI_PREPARAZIONE_ZONA, codiceZona);
                var rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ordine o = new Ordine(
                        rs.getInt("codice_pagamento"),
                        rs.getDouble("prezzo_totale"),
                        rs.getString("p_iva")
                    );
                    o.codiceOrdine = rs.getInt("codice_ordine");
                    ordini.add(o);
                }
            } catch (Exception e) {
                throw new DAOException("Errore caricamento ordini in preparazione", e);
            }
            return ordini;
        }
    }

}
