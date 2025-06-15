package it.unibo.data;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Ristorante {

    public final String piva;
    public final String nome;
    public final String indirizzo;
    public final String orario;
    public final int codiceZona;
    public final Set<Piatto> piatti;

    public Ristorante(String piva, String nome, String indirizzo, String orario, int codiceZona, Set<Piatto> piatti) {
        this.piva = piva;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.orario = orario;
        this.codiceZona = codiceZona;
        this.piatti = piatti == null ? Set.of() : Set.copyOf(piatti);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof Ristorante)) return false;
        var r = (Ristorante) other;
        return r.piva.equals(this.piva) &&
               r.nome.equals(this.nome) &&
               r.indirizzo.equals(this.indirizzo) &&
               r.orario.equals(this.orario) &&
               r.codiceZona == this.codiceZona &&
               r.piatti.equals(this.piatti);
    }

    @Override
    public int hashCode() {
        return Objects.hash(piva, nome, indirizzo, orario, codiceZona, piatti);
    }

    @Override
    public String toString() {
        return Printer.stringify("Ristorante", List.of(
            Printer.field("piva", piva),
            Printer.field("nome", nome),
            Printer.field("indirizzo", indirizzo),
            Printer.field("orario", orario),
            Printer.field("codiceZona", codiceZona),
            Printer.field("piatti", piatti.stream().map(Piatto::toString).toList())
        ));
    }

    public static final class DAO {

        public static Optional<Ristorante> find(Connection connection, String piva) {
            try (var stmt = DAOUtils.prepare(connection, Queries.FIND_RISTORANTE, piva);
                 var rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    return Optional.of(new Ristorante(
                        rs.getString("piva"),
                        rs.getString("nome"),
                        rs.getString("indirizzo"),
                        rs.getString("orario"),
                        rs.getInt("codice_zona")
                    ));
                }
                return Optional.empty();
                
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento del ristorante", e);
            }
        }

        public static List<Ristorante> findByZona(Connection connection, int codiceZona) {
            try (var stmt = DAOUtils.prepare(connection, Queries.RISTORANTI_BY_ZONA, codiceZona);
                 var rs = stmt.executeQuery()) {
                
                var ristoranti = new java.util.ArrayList<Ristorante>();
                while (rs.next()) {
                    ristoranti.add(new Ristorante(
                        rs.getString("piva"),
                        rs.getString("nome"),
                        rs.getString("indirizzo"),
                        rs.getString("orario"),
                        rs.getInt("codice_zona")
                    ));
                }
                return ristoranti;
                
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento dei ristoranti per zona", e);
            }
        }
    }

}
