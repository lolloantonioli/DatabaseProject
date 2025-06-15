package it.unibo.data;

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

    }
}
