package it.unibo.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Offre {

    public final String piva;
    public final int codicePiatto;

    public Offre(String piva, int codicePiatto) {
        this.piva = piva;
        this.codicePiatto = codicePiatto;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof Offre)) return false;
        var o = (Offre) other;
        return o.codicePiatto == this.codicePiatto &&
               o.piva.equals(this.piva);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codicePiatto, piva);
    }

    @Override
    public String toString() {
        return Printer.stringify("Offre", List.of(
            Printer.field("codice piatto", codicePiatto),
            Printer.field("partita iva", piva)
        ));
    }

    public static final class DAO {
        /**
         * Restituisce tutte le associazioni Offre per un dato ristorante
         */
        public static List<Offre> listByRistorante(Connection connection, String piva) {
            List<Offre> result = new ArrayList<>();
            try (var stmt = DAOUtils.prepare(connection, Queries.PIATTI_BY_RISTORANTE, piva);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int codicePiatto = rs.getInt("codice_piatto");
                    String partIva = rs.getString("p_iva");
                    result.add(new Offre(partIva, codicePiatto));
                }
            } catch (Exception e) {
                throw new DAOException("Error while retrieving offers for ristorante " + piva, e);
            }
            return result;
        }

        public static void insertOffre(Connection connection, Offre offre) {
            try (var stmt = DAOUtils.prepare(connection,
                                         Queries.INSERT_OFFRE,
                                         offre.piva,
                                         offre.codicePiatto)) {
            stmt.executeUpdate();
            }catch (Exception e) {
                throw new DAOException("Error while inserting offer for ristorante " + offre.piva, e);
            }
        }

    }

}
