package it.unibo.data;

import java.sql.Connection;
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
        /**
         * Inserisce una nuova zona geografica
         */
        public static void insertZona(Connection conn, ZonaGeografica z) {
            try (var ps = DAOUtils.prepare(conn,
                                           Queries.INSERT_ZONA,
                                           z.codiceZona,
                                           z.nome)) {
                ps.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore inserimento zona geografica " + z.codiceZona, e);
            }
        }
        
    }
}
