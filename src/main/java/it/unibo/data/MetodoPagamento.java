package it.unibo.data;

import java.util.List;
import java.util.Objects;

public class MetodoPagamento {

    public final int codiceCliente;
    public final String nome;

    public MetodoPagamento(int codiceCliente, String nome) {
        this.codiceCliente = codiceCliente;
        this.nome = nome;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || !(other instanceof MetodoPagamento)) return false;
        MetodoPagamento m = (MetodoPagamento) other;
        return m.codiceCliente == this.codiceCliente && m.nome.equals(this.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceCliente, nome);
    }

    @Override
    public String toString() {
        return Printer.stringify("MetodoPagamento", List.of(
            Printer.field("codice cliente", codiceCliente),
            Printer.field("nome", nome)
        ));
    }

    public static final class DAO {}

}
