package it.unibo.data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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

    }
}
