package it.unibo.data;

import java.util.List;
import java.util.Objects;

public class UtilizzaPunti {
    public final int codiceOrdine;
    public final int puntiUsati;
    public final int scontoApplicato;
    public final int codiceRaccolta;

    public UtilizzaPunti(int codiceOrdine, int puntiUsati, int scontoApplicato, int codiceRaccolta) {
        this.codiceOrdine = codiceOrdine;
        this.puntiUsati = puntiUsati;
        this.scontoApplicato = scontoApplicato;
        this.codiceRaccolta = codiceRaccolta;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof UtilizzaPunti)) return false;
        var up = (UtilizzaPunti) other;
        return up.codiceOrdine == this.codiceOrdine &&
               up.puntiUsati == this.puntiUsati &&
               up.scontoApplicato == this.scontoApplicato &&
               up.codiceRaccolta == this.codiceRaccolta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceOrdine, puntiUsati, scontoApplicato, codiceRaccolta);
    }

    @Override
    public String toString() {
        return Printer.stringify("UtilizzaPunti", List.of(
            Printer.field("codice ordine", codiceOrdine),
            Printer.field("punti usati", puntiUsati),
            Printer.field("sconto applicato", scontoApplicato),
            Printer.field("codice raccolta", codiceRaccolta)
        ));
    }

    public static final class DAO {
        // Implement DAO methods here if needed
    }
}
