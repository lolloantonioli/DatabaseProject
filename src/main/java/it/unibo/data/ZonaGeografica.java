package it.unibo.data;

import java.util.List;
import java.util.Objects;

public class ZonaGeografica {
    
    public final int codiceZona;
    public final String nome;

    public ZonaGeografica(int codiceZona, String nome) {
        this.codiceZona = codiceZona;
        this.nome = nome;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof ZonaGeografica)) return false;
        var z = (ZonaGeografica) other;
        return z.codiceZona == this.codiceZona &&
               z.nome.equals(this.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceZona, nome);
    }

    @Override
    public String toString() {
        return Printer.stringify("Zona Geografica", List.of(
            Printer.field("Codice Zona", codiceZona),
            Printer.field("nome", nome)
        ));
    }

    public static final class DAO {

        
    }
}
