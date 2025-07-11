package it.unibo.data;

import java.sql.Connection;
import java.util.ArrayList;
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

    public static final class DAO {
        /**
         * Restituisce tutti i metodi di pagamento di un cliente
         */
        public static List<MetodoPagamento> listByCliente(Connection connection, int codiceCliente) {
            List<MetodoPagamento> result = new ArrayList<>();
            try (var stmt = DAOUtils.prepare(connection, Queries.METODI_PAGAMENTO_BY_CLIENTE, codiceCliente);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String nome = rs.getString("nome");
                    result.add(new MetodoPagamento(codiceCliente, nome));
                }
            } catch (Exception e) {
                throw new DAOException("Error while retrieving payment methods for client " + codiceCliente, e);
            }
            return result;
        }

        public static void insertMetodo(Connection conn, MetodoPagamento m) {
            try (var ps = DAOUtils.prepare(conn, Queries.INSERT_METODO_PAGAMENTO,
                                        m.codiceCliente, m.nome)) {
                ps.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore inserimento metodo di pagamento per cliente " + m.codiceCliente, e);
            }
        }

        public static void deleteMetodoPagamento(Connection connection, int codiceCliente, String nome) {
            try (var ps = DAOUtils.prepare(connection, Queries.DELETE_METODO_PAGAMENTO, codiceCliente, nome)) {
                ps.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore durante la rimozione del metodo di pagamento", e);
            }
        }
        
    }

}
