package it.unibo.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
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
    public Integer codiceRider;

    public StatoOrdine(int codiceOrdine, LocalDateTime data, boolean inPreparazione,
                       LocalDateTime oraInPreparazione, boolean inConsegna, LocalDateTime oraInConsegna,
                       boolean consegnato, LocalDateTime oraConsegnato, Integer codiceRider) {
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
            String sql = Queries.INSERT_STATO_ORDINE; 
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                // 1) Codice_Ordine
                ps.setInt(1, s.codiceOrdine);
                // 2) Data
                ps.setTimestamp(2, Timestamp.valueOf(s.data));
                // 3) In_Preparazione boolean
                ps.setBoolean(3, s.inPreparazione);
                // 4) Ora_In_Preparazione (può essere null)
                if (s.oraInPreparazione != null) {
                    ps.setTimestamp(4, Timestamp.valueOf(s.oraInPreparazione));
                } else {
                    ps.setNull(4, Types.TIMESTAMP);
                }
                // 5) In_Consegna boolean
                ps.setBoolean(5, s.inConsegna);
                // 6) Ora_In_Consegna (può essere null)
                if (s.oraInConsegna != null) {
                    ps.setTimestamp(6, Timestamp.valueOf(s.oraInConsegna));
                } else {
                    ps.setNull(6, Types.TIMESTAMP);
                }
                // 7) Consegnato boolean
                ps.setBoolean(7, s.consegnato);
                // 8) Ora_Consegnato (può essere null)
                if (s.oraConsegnato != null) {
                    ps.setTimestamp(8, Timestamp.valueOf(s.oraConsegnato));
                } else {
                    ps.setNull(8, Types.TIMESTAMP);
                }
                // 9) Codice_Rider: può restare NULL fino all’assegnazione
                if (s.codiceRider != null) {
                    ps.setInt(9, s.codiceRider);
                } else {
                    ps.setNull(9, Types.INTEGER);
                }

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
                    var oraPrep = rs.getTimestamp("ora_in_preparazione") == null ? null : rs.getTimestamp("ora_in_preparazione").toLocalDateTime();
                    var conseg = rs.getBoolean("in_consegna");
                    var oraConseg = rs.getTimestamp("ora_in_consegna") == null ? null : rs.getTimestamp("ora_in_consegna").toLocalDateTime();
                    var cons = rs.getBoolean("consegnato");
                    var oraCons = rs.getTimestamp("ora_consegnato") == null ? null : rs.getTimestamp("ora_consegnato").toLocalDateTime();
                    int riderValue = rs.getInt("codice_rider");
                    Integer rider = rs.wasNull() ? null : riderValue;
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
                    System.out.println("ordini trovati:" + ordini);
                }
            } catch (Exception e) {
                throw new DAOException("Errore caricamento ordini in preparazione", e);
            }
            return ordini;
        }
    }

}
