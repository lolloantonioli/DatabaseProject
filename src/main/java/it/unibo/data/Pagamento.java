package it.unibo.data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Pagamento {

    public final int codicePagamento;
    public final LocalDateTime data;
    public final BigDecimal importo;
    public final int codiceCliente;
    public final String nomeMetodo;

    public Pagamento(int codicePagamento, LocalDateTime data, BigDecimal importo, int codiceCliente, String nomeMetodo) {
        this.codicePagamento = codicePagamento;
        this.data = data;
        this.importo = importo;
        this.codiceCliente = codiceCliente;
        this.nomeMetodo = nomeMetodo;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || !(other instanceof Pagamento)) return false;
        Pagamento p = (Pagamento) other;
        return p.codicePagamento == this.codicePagamento
            && p.data.equals(this.data)
            && p.importo.equals(this.importo)
            && p.codiceCliente == this.codiceCliente
            && p.nomeMetodo.equals(this.nomeMetodo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codicePagamento, data, importo, codiceCliente, nomeMetodo);
    }

    @Override
    public String toString() {
        return Printer.stringify("Pagamento", List.of(
            Printer.field("codice pagamento", codicePagamento),
            Printer.field("data", data),
            Printer.field("importo", importo),
            Printer.field("codice cliente", codiceCliente),
            Printer.field("nome metodo", nomeMetodo)
        ));
    }

    public static final class DAO {}

}
