package it.unibo.data;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
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

    public static final class DAO {
        /**
         * Inserisce una nuova promozione per un ristorante
         */
        public static void insertPromozione(Connection connection, Promozione promo) {
            try (var ps = DAOUtils.prepare(connection,
                                           Queries.INSERT_PROMOZIONE,
                                           promo.pIva,
                                           Date.valueOf(promo.dataInizio),
                                           Date.valueOf(promo.dataFine),
                                           promo.nome,
                                           promo.descrizione,
                                           promo.percentualeSconto)) {
                ps.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore inserimento promozione per ristorante " + promo.pIva, e);
            }
        }

        /**
         * Rimuove una promozione identificata da piva, data inizio e data fine.
         */
        public static void deletePromozione(Connection connection, String piva, Date dataInizio, Date dataFine) {
            try (var ps = DAOUtils.prepare(connection,
                    Queries.DELETE_PROMOZIONE,
                    piva,
                    dataInizio,
                    dataFine
            )) {
                ps.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore eliminazione promozione per ristorante " + piva, e);
            }
        }

        /**
         * Restituisce le promozioni attive di un ristorante
         */
        public static List<Promozione> listActiveByRistorante(Connection connection, String piva) {
            List<Promozione> result = new ArrayList<>();
            try (var ps = DAOUtils.prepare(connection,
                                           Queries.SELECT_PROMOZIONI_ATTIVE_BY_RISTORANTE,
                                           piva);
                 var rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate inizio = rs.getDate("data_inizio").toLocalDate();
                    LocalDate fine = rs.getDate("data_fine").toLocalDate();
                    String nome = rs.getString("nome");
                    String desc = rs.getString("descrizione");
                    int perc = rs.getInt("percentuale_sconto");
                    result.add(new Promozione(piva, inizio, fine, nome, desc, perc));
                }
            } catch (Exception e) {
                throw new DAOException("Errore recupero promozioni attive per ristorante " + piva, e);
            }
            return result;
        }
    }

}
