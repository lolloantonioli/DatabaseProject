package it.unibo.data;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Promozione {

    public final String pIva;
    public final LocalDate dataInizio;
    public final LocalDate dataFine;
    public final String nome;
    public final String descrizione;
    public final int percentualeSconto;

    public Promozione(String pIva, LocalDate dataInizio, LocalDate dataFine, String nome, String descrizione, int percentualeSconto) {
        this.pIva = pIva;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.nome = nome;
        this.descrizione = descrizione;
        this.percentualeSconto = percentualeSconto;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || !(other instanceof Promozione)) return false;
        Promozione p = (Promozione) other;
        return p.pIva.equals(this.pIva)
            && p.dataInizio.equals(this.dataInizio)
            && p.dataFine.equals(this.dataFine)
            && p.nome.equals(this.nome)
            && p.descrizione.equals(this.descrizione)
            && p.percentualeSconto == this.percentualeSconto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pIva, dataInizio, dataFine, nome, descrizione, percentualeSconto);
    }

    @Override
    public String toString() {
        return Printer.stringify("Promozione", List.of(
            Printer.field("p.iva", pIva),
            Printer.field("data inizio", dataInizio),
            Printer.field("data fine", dataFine),
            Printer.field("nome", nome),
            Printer.field("descrizione", descrizione),
            Printer.field("percentuale sconto", percentualeSconto)
        ));
    }

    public static final class DAO {}

}
