package it.unibo.data;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    }

}
