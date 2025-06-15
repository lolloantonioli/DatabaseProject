package it.unibo.data;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Recensione {
    public final int codiceCliente;
    public final String piva;
    public final int numeroStelle;
    public final String descrizione;
    public final String titolo;
    public final LocalDate data;

    public Recensione(int codiceCliente, String piva, int numeroStelle, String descrizione, String titolo, LocalDate data) {
        this.codiceCliente = codiceCliente;
        this.piva = piva;
        this.numeroStelle = numeroStelle;
        this.descrizione = descrizione == null ? "" : descrizione;
        this.titolo = titolo;
        this.data = data;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof Recensione)) return false;
        var r = (Recensione) other;
        return r.codiceCliente == this.codiceCliente &&
               r.piva.equals(this.piva) &&
               r.numeroStelle == this.numeroStelle &&
               r.descrizione.equals(this.descrizione) &&
               r.titolo.equals(this.titolo) &&
               r.data.equals(this.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceCliente, piva, numeroStelle, descrizione, titolo, data);
    }

    @Override
    public String toString() {
        return Printer.stringify("Recensione", List.of(
            Printer.field("codice cliente", codiceCliente),
            Printer.field("partita iva", piva),
            Printer.field("numero stelle", numeroStelle),
            Printer.field("descrizione", descrizione),
            Printer.field("titolo", titolo),
            Printer.field("data", data.toString())
        ));
    }

    public static final class DAO {

    }
}
