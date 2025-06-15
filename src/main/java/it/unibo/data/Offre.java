package it.unibo.data;

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

    }

}
