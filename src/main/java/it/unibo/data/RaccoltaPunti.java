package it.unibo.data;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RaccoltaPunti {

    public final int codiceCliente;
    public final int puntiTotali;
    public final int sogliaPunti;
    public final int percentualeSconto;

    public RaccoltaPunti(int codiceCliente, int puntiTotali, int sogliaPunti, int percentualeSconto) {
        this.codiceCliente = codiceCliente;
        this.puntiTotali = puntiTotali;
        this.sogliaPunti = sogliaPunti;
        this.percentualeSconto = percentualeSconto;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || !(other instanceof RaccoltaPunti)) return false;
        RaccoltaPunti r = (RaccoltaPunti) other;
        return r.codiceCliente == this.codiceCliente
            && r.puntiTotali == this.puntiTotali
            && r.sogliaPunti == this.sogliaPunti
            && r.percentualeSconto == this.percentualeSconto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceCliente, puntiTotali, sogliaPunti, percentualeSconto);
    }

    @Override
    public String toString() {
        return Printer.stringify("RaccoltaPunti", List.of(
            Printer.field("codice cliente", codiceCliente),
            Printer.field("punti totali", puntiTotali),
            Printer.field("soglia punti", sogliaPunti),
            Printer.field("percentuale sconto", percentualeSconto)
        ));
    }

    public static final class DAO {
        /**
         * Inserisce una nuova raccolta punti per un cliente (punti totali iniziali = 0)
         */
        public static void insertRaccolta(Connection conn, RaccoltaPunti r) {
            try (var ps = DAOUtils.prepare(conn,
                                           Queries.INSERT_RACCOLTA_PUNTI,
                                           r.codiceCliente,
                                           r.sogliaPunti,
                                           r.percentualeSconto)) {
                ps.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore inserimento raccolta punti per cliente " + r.codiceCliente, e);
            }
        }

        /**
         * Recupera la raccolta punti di un cliente
         */
        public static Optional<RaccoltaPunti> findByCliente(Connection conn, int codiceCliente) {
            try (var ps = DAOUtils.prepare(conn,
                                           Queries.SELECT_RACCOLTA_BY_CLIENTE,
                                           codiceCliente);
                 var rs = ps.executeQuery()) {
                if (rs.next()) {
                    int punti = rs.getInt("punti_totali");
                    int soglia = rs.getInt("soglia_punti");
                    int perc = rs.getInt("percentuale_sconto");
                    return Optional.of(new RaccoltaPunti(codiceCliente, punti, soglia, perc));
                }
            } catch (Exception e) {
                throw new DAOException("Errore recupero raccolta punti per cliente " + codiceCliente, e);
            }
            return Optional.empty();
        }
    }

}
