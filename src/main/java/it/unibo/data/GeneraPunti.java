package it.unibo.data;

import java.util.List;
import java.util.Objects;

public class GeneraPunti {
    public final int codiceOrdine;
    public final int puntiGenerati;
    public final int codiceRaccolta;

    public GeneraPunti(int codiceOrdine, int puntiGenerati, int codiceRaccolta) {
        this.codiceOrdine = codiceOrdine;
        this.puntiGenerati = puntiGenerati;
        this.codiceRaccolta = codiceRaccolta;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof GeneraPunti)) return false;
        var gp = (GeneraPunti) other;
        return gp.codiceOrdine == this.codiceOrdine &&
               gp.puntiGenerati == this.puntiGenerati &&
               gp.codiceRaccolta == this.codiceRaccolta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceOrdine, puntiGenerati, codiceRaccolta);
    }

    @Override
    public String toString() {
        return Printer.stringify("GeneraPunti", List.of(
            Printer.field("codice ordine", codiceOrdine),
            Printer.field("punti generati", puntiGenerati),
            Printer.field("codice raccolta", codiceRaccolta)
        ));
    }

    public static final class DAO {
        // Implement DAO methods here if needed
    }
}
