package it.unibo.data;

import java.util.List;
import java.util.Objects;

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

    public static final class DAO {}

}
