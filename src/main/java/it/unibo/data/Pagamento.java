package it.unibo.data;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pagamento {

    public int codicePagamento;
    public final Date data;
    public final double importo;
    public final int codiceCliente;
    public final String nomeMetodo;

    public Pagamento(Date data, double importo, int codiceCliente, String nomeMetodo) {
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
            && p.importo == this.importo
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

    public static final class DAO {
        /**
         * Restituisce tutti i pagamenti effettuati da un cliente
         */
        public static List<Pagamento> listByCliente(Connection connection, int codiceCliente) {
            List<Pagamento> result = new ArrayList<>();
            try (var stmt = DAOUtils.prepare(connection, Queries.PAGAMENTI_BY_CLIENTE, codiceCliente);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pagamento p = new Pagamento(rs.getDate("data"),
                                                rs.getDouble("importo"),
                                                codiceCliente,
                                                rs.getString("nome"));
                    p.codicePagamento = rs.getInt("codice_pagamento");
                    result.add(p);
                }
            } catch (Exception e) {
                throw new DAOException("Error while retrieving payments for client " + codiceCliente, e);
            }
            return result;
        }
        
        public static int insertPagamento(Connection conn, Pagamento p) {
            try (var ps = conn.prepareStatement(Queries.INSERT_PAGAMENTO, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                ps.setDate(1, p.data);
                ps.setDouble(2, p.importo);
                ps.setInt(3, p.codiceCliente);
                ps.setString(4, p.nomeMetodo);
                ps.executeUpdate();
                try (var rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.codicePagamento = rs.getInt(1);
                        return rs.getInt(1);
                    } else {
                        throw new DAOException("Inserimento pagamento fallito, nessun ID generato.");
                    }
                }
            } catch (Exception e) {
                throw new DAOException("Errore inserimento pagamento", e);
            }
        }
    }

}
