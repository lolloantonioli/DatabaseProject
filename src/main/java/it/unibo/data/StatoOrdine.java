package it.unibo.data;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class StatoOrdine {

    public final int codiceOrdine;
    public final LocalDateTime data;
    public final LocalDateTime inPreparazione;
    public final LocalDateTime inConsegna;
    public final LocalDateTime consegnato;
    public final int codiceRider;

    public StatoOrdine(int codiceOrdine, LocalDateTime data, LocalDateTime inPreparazione,
                       LocalDateTime inConsegna, LocalDateTime consegnato, int codiceRider) {
        this.codiceOrdine = codiceOrdine;
        this.data = data;
        this.inPreparazione = inPreparazione;
        this.inConsegna = inConsegna;
        this.consegnato = consegnato;
        this.codiceRider = codiceRider;
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
            && s.codiceRider == this.codiceRider;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceOrdine, data, inPreparazione, inConsegna, consegnato, codiceRider);
    }

    @Override
    public String toString() {
        return Printer.stringify("StatoOrdine", List.of(
            Printer.field("codice ordine", codiceOrdine),
            Printer.field("data", data),
            Printer.field("in preparazione", inPreparazione),
            Printer.field("in consegna", inConsegna),
            Printer.field("consegnato", consegnato),
            Printer.field("codice rider", codiceRider)
        ));
    }

    public static final class DAO {
            /**
         * Inserisce un nuovo stato ordine
         */
        public void insertState(Connection conn, StatoOrdine s) {
            try (var ps = DAOUtils.prepare(conn,
                                           Queries.INSERT_STATO_ORDINE,
                                           s.codiceOrdine,
                                           Timestamp.valueOf(s.data),
                                           Timestamp.valueOf(s.inPreparazione),
                                           Timestamp.valueOf(s.inConsegna),
                                           Timestamp.valueOf(s.consegnato),
                                           s.codiceRider)) {
                ps.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore inserimento stato ordine " + s.codiceOrdine, e);
            }
        }

        /**
         * Aggiorna uno stato ordine esistente (modifica timestamp campi)
         */
        public void updateState(Connection conn, StatoOrdine s) {
            try (var ps = DAOUtils.prepare(conn,
                                           Queries.UPDATE_STATO_ORDINE,
                                           Timestamp.valueOf(s.inPreparazione),
                                           Timestamp.valueOf(s.inConsegna),
                                           Timestamp.valueOf(s.consegnato),
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
        public Optional<StatoOrdine> findByOrder(Connection conn, int codiceOrdine) {
            try (var ps = DAOUtils.prepare(conn, Queries.SELECT_STATO_BY_ORDINE, codiceOrdine);
                 var rs = ps.executeQuery()) {
                if (rs.next()) {
                    var data    = rs.getTimestamp("data").toLocalDateTime();
                    var prep    = rs.getTimestamp("in_preparazione").toLocalDateTime();
                    var conseg  = rs.getTimestamp("in_consegna").toLocalDateTime();
                    var cons    = rs.getTimestamp("consegnato").toLocalDateTime();
                    int rider   = rs.getInt("codice_rider");
                    return Optional.of(new StatoOrdine(codiceOrdine, data, prep, conseg, cons, rider));
                }
                return Optional.empty();
            } catch (Exception e) {
                throw new DAOException("Errore recupero stato ordine " + codiceOrdine, e);
            }
        }
    }

}
