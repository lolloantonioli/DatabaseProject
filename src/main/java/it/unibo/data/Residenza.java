package it.unibo.data;

import java.sql.Connection;
import java.util.ArrayList;
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
        /**
         * Restituisce le associazioni Residenza per un dato cliente
         */
        public List<Residenza> listByCliente(Connection connection, int codiceCliente) {
            List<Residenza> result = new ArrayList<>();
            try (var stmt = DAOUtils.prepare(connection, Queries.RESIDENZA_BY_CLIENTE, codiceCliente);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idCliente = rs.getInt("codice_cliente");
                    int idIndirizzo = rs.getInt("codice_indirizzo");
                    result.add(new Residenza(idCliente, idIndirizzo));
                }
            } catch (Exception e) {
                throw new DAOException("Errore durante il recupero delle residenze per il cliente " + codiceCliente, e);
            }
            return result;
        }
    }
}
