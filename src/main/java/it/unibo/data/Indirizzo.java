package it.unibo.data;

import java.util.List;
import java.util.Objects;

public class Indirizzo {

    public final int codiceIndirizzo;
    public final String via;
    public final String numeroCivico;
    public final String cap;
    public final int interno;
    public final int scala;
    public final int codiceZona; 

    public Indirizzo(int codiceIndirizzo, String via, String numeroCivico, String cap, Integer interno, Integer scala, int codiceZona) {
        this.codiceIndirizzo = codiceIndirizzo;
        this.via = via;
        this.numeroCivico = numeroCivico;
        this.cap = cap;
        this.interno = interno == null ? 0 : interno;
        this.scala = scala == null ? 0 : scala;
        this.codiceZona = codiceZona;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof Indirizzo)) return false;
        var i = (Indirizzo) other;
        return i.codiceIndirizzo == this.codiceIndirizzo &&
               i.via.equals(this.via) &&
               i.numeroCivico.equals(this.numeroCivico) &&
               i.cap.equals(this.cap) &&
               i.interno == this.interno &&
               i.scala == this.scala &&
               i.codiceZona == this.codiceZona;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceIndirizzo, via, numeroCivico, cap, interno, scala, codiceZona);
    }

    @Override
    public String toString() {
        return Printer.stringify("Indirizzo", List.of(
            Printer.field("codice indirizzo", codiceIndirizzo),
            Printer.field("via", via),
            Printer.field("numero civico", numeroCivico),
            Printer.field("cap", cap),
            Printer.field("interno", interno),
            Printer.field("scala", scala),
            Printer.field("codice zona", codiceZona)
        ));
    }

    public static final class DAO {

        
    }

}
