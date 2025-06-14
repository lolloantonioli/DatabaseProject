package it.unibo.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Visualizzazione {
    public final int codiceCliente;
    public final String piva;

    public Visualizzazione(int codiceCliente, String piva) {
        this.codiceCliente = codiceCliente;
        this.piva = piva;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof Visualizzazione)) return false;
        var v = (Visualizzazione) other;
        return v.codiceCliente == this.codiceCliente &&
               v.piva.equals(this.piva);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceCliente, piva);
    }

    @Override
    public String toString() {
        return Printer.stringify("Visualizzazione", List.of(
            Printer.field("codice cliente", codiceCliente),
            Printer.field("partita iva", piva)
        ));
    }

    public static final class DAO {
        /**
         * Restituisce le visualizzazioni fatte da un cliente in una data specifica
         */
        public List<Visualizzazione> listByClienteOnDate(Connection conn, int codiceCliente, java.time.LocalDate date) {
            List<Visualizzazione> result = new ArrayList<>();
            try (var ps = DAOUtils.prepare(conn, Queries.VISUALIZZAZIONI_BY_CLIENTE_ON_DATE,
                                           codiceCliente,
                                           java.sql.Date.valueOf(date));
                 var rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new Visualizzazione(
                        rs.getInt("codice_cliente"),
                        rs.getString("piva")
                    ));
                }
            } catch (Exception e) {
                throw new DAOException("Errore recupero visualizzazioni per cliente " + codiceCliente + " nella data " + date, e);
            }
            return result;
        }

        /**
         * Restituisce il numero di visualizzazioni ricevute da un ristorante
         */
        public int countByRistorante(Connection conn, String piva) {
            try (var ps = DAOUtils.prepare(conn, Queries.COUNT_VISUALIZZAZIONI_BY_RISTORANTE, piva);
                 var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
                return 0;
            } catch (Exception e) {
                throw new DAOException("Errore conteggio visualizzazioni per ristorante " + piva, e);
            }
        }
   
        
    }
}
