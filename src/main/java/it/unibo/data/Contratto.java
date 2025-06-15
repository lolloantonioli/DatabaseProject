package it.unibo.data;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Contratto {

    public final int codiceRider;
    public final BigDecimal pagaOraria;
    public final String testo;

    public Contratto(int codiceRider, BigDecimal pagaOraria, String testo) {
        this.codiceRider = codiceRider;
        this.pagaOraria = pagaOraria;
        this.testo = testo;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof Contratto)) return false;
        var c = (Contratto) other;
        return c.codiceRider == this.codiceRider &&
               c.pagaOraria == this.pagaOraria &&
               c.testo.equals(this.testo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceRider, pagaOraria, testo);
    }

    @Override
    public String toString() {
        return Printer.stringify("DettaglioOrdine", List.of(
            Printer.field("codice rider", codiceRider),
            Printer.field("paga oraria", pagaOraria),
            Printer.field("testo", testo)
        ));
    }

    public static final class DAO {
        /**
         * Restituisce il contratto associato a un dato rider, se esistente
         */
        public Optional<Contratto> findByRider(Connection connection, int codiceRider) {
            try (var stmt = DAOUtils.prepare(connection, Queries.MOSTRA_CONTRATTO, codiceRider);
                 var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal paga = rs.getBigDecimal("paga_oraria");
                    String testo = rs.getString("testo");
                    Contratto c = new Contratto(codiceRider, paga, testo);
                    return Optional.of(c);
                }
                return Optional.empty();
            } catch (Exception e) {
                throw new DAOException("Errore durante la ricerca del contratto per il rider con codice " + codiceRider, e);
            }
        }
    }
}
