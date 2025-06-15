package it.unibo.data;

import java.util.List;
import java.util.Objects;

public class Residenza {
    public final int codiceCliente;
    public final int codiceIndirizzo;
    
    
    public Residenza(int codiceCliente, int codiceIndirizzo) {
        this.codiceCliente = codiceCliente;
        this.codiceIndirizzo = codiceIndirizzo;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof Residenza)) return false;
        var r = (Residenza) other;
        return r.codiceCliente == this.codiceCliente && r.codiceIndirizzo == this.codiceIndirizzo;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codiceCliente) ^ Integer.hashCode(codiceIndirizzo);
    }

    @Override
    public String toString() {
        return Printer.stringify("Residenza", List.of(
            Printer.field("codice cliente", codiceCliente),
            Printer.field("codice indirizzo", codiceIndirizzo)
        ));
    }

    public static final class DAO {
        // Implement DAO methods here if needed
    }
}
