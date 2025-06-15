package it.unibo.data;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Applicazione {

    public final int codiceOrdine;
    public final BigDecimal scontoApplicato;
    public final String piva;
    public final LocalDate dataInizio;
    public final LocalDate dataFine;

    public Applicazione(int codiceOrdine, BigDecimal scontoApplicato, String piva, LocalDate dataInizio, LocalDate dataFine) {
        this.codiceOrdine = codiceOrdine;
        this.scontoApplicato = scontoApplicato;
        this.piva = piva;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof Applicazione)) return false;
        var a = (Applicazione) other;
        return a.codiceOrdine == this.codiceOrdine &&
               a.piva.equals(this.piva) &&
               a.scontoApplicato.equals(this.scontoApplicato) &&
               a.dataInizio.equals(this.dataInizio) &&
               a.dataFine.equals(this.dataFine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceOrdine, scontoApplicato, piva, dataInizio, dataFine);
    }

    @Override
    public String toString() {
        return Printer.stringify("Applicazione", List.of(
            Printer.field("codice ordine", codiceOrdine),
            Printer.field("partita iva", piva),
            Printer.field("sconto applicato", scontoApplicato),
            Printer.field("data inizio", dataInizio.toString()),
            Printer.field("data fine", dataFine.toString())
        ));
    }

    public static final class DAO {
        public List<Applicazione> listAllWithPromozione(Connection connection) {
            List<Applicazione> result = new ArrayList<>();
            try (PreparedStatement ps = DAOUtils.prepare(connection, Queries.ORDINI_CON_PROMOZIONE)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int codiceOrdine = rs.getInt("codice_ordine");
                    BigDecimal sconto = rs.getBigDecimal("sconto_applicato");
                    String piva = rs.getString("piva");
                    LocalDate inizio = rs.getDate("data_inizio").toLocalDate();
                    LocalDate fine = rs.getDate("data_fine").toLocalDate();
                    result.add(new Applicazione(codiceOrdine, sconto, piva, inizio, fine));
                }
            } catch( Exception e) {
                throw new DAOException( "Errore durante il recupero delle applicazioni con promozione", e);
            }
            return result;
        }
    }

}
