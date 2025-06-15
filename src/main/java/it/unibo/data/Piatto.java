package it.unibo.data;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;

public class Piatto {
    public final int codicePiatto;
    public final String nome;
    public final BigDecimal prezzo;
    public final String descrizione;

    public Piatto(int codicePiatto, String nome, BigDecimal prezzo, String descrizione) {
        this.codicePiatto = codicePiatto;
        this.nome = nome == null ? "" : nome;
        this.prezzo = prezzo == null ? BigDecimal.ZERO : prezzo;
        this.descrizione = descrizione == null ? "" : descrizione;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof Piatto)) return false;
        var p = (Piatto) other;
        return p.codicePiatto == this.codicePiatto &&
               p.nome.equals(this.nome) &&
               p.prezzo.equals(this.prezzo) &&
               p.descrizione.equals(this.descrizione);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codicePiatto, nome, prezzo, descrizione);
    }

    @Override
    public String toString() {
        return Printer.stringify("Piatto", List.of(
            Printer.field("codicePiatto", codicePiatto),
            Printer.field("nome", nome),
            Printer.field("prezzo", prezzo),
            Printer.field("descrizione", descrizione)
        ));
    }

    public static final class DAO {

        public static List<Piatto> byRistorante(Connection connection, String piva) {
            try (var stmt = DAOUtils.prepare(connection, Queries.PIATTI_BY_RISTORANTE, piva);
                 var rs = stmt.executeQuery()) {
                
                var piatti = new java.util.ArrayList<Piatto>();
                while (rs.next()) {
                    piatti.add(new Piatto(
                        rs.getInt("codice_piatto"),
                        rs.getString("nome"),
                        rs.getBigDecimal("prezzo"),
                        rs.getString("descrizione")
                    ));
                }
                return piatti;
                
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento dei piatti", e);
            }
        }
    }
}
