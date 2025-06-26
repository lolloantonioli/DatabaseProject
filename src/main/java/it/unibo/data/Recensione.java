package it.unibo.data;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Recensione {
    public final int codiceCliente;
    public final String piva;
    public final int numeroStelle;
    public final String descrizione;
    public final String titolo;
    public final Date data;

    public Recensione(int codiceCliente, String piva, int numeroStelle, String descrizione, String titolo, Date data) {
        this.codiceCliente = codiceCliente;
        this.piva = piva;
        this.numeroStelle = numeroStelle;
        this.descrizione = descrizione == null ? "" : descrizione;
        this.titolo = titolo;
        this.data = data;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof Recensione)) return false;
        var r = (Recensione) other;
        return r.codiceCliente == this.codiceCliente &&
               r.piva.equals(this.piva) &&
               r.numeroStelle == this.numeroStelle &&
               r.descrizione.equals(this.descrizione) &&
               r.titolo.equals(this.titolo) &&
               r.data.equals(this.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceCliente, piva, numeroStelle, descrizione, titolo, data);
    }

    @Override
    public String toString() {
        return Printer.stringify("Recensione", List.of(
            Printer.field("codice cliente", codiceCliente),
            Printer.field("partita iva", piva),
            Printer.field("numero stelle", numeroStelle),
            Printer.field("descrizione", descrizione),
            Printer.field("titolo", titolo),
            Printer.field("data", data.toString())
        ));
    }

    public static final class DAO {
        /**
         * Inserisce una nuova recensione
         */
        public static void insertRecensione(Connection conn, Recensione rec) {
            try (var ps = DAOUtils.prepare(conn, Queries.INSERT_RECENSIONE,
                                           rec.codiceCliente,
                                           rec.piva,
                                           rec.numeroStelle,
                                           rec.descrizione,
                                           rec.titolo,
                                           rec.data)) {
                ps.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore inserimento recensione per cliente " + rec.codiceCliente, e);
            }
        }

        /**
         * Visualizza tutte le recensioni fatte da un cliente
         */
        public static List<Recensione> listByCliente(Connection conn, int codiceCliente) {
            var result = new ArrayList<Recensione>();
            try (var ps = DAOUtils.prepare(conn, Queries.SELECT_RECENSIONI_BY_CLIENTE, codiceCliente);
                 var rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new Recensione(
                        rs.getInt("codice_cliente"),
                        rs.getString("p_iva"),
                        rs.getInt("numero_stelle"),
                        rs.getString("descrizione"),
                        rs.getString("titolo"),
                        rs.getDate("data")
                    ));
                }
            } catch (Exception e) {
                throw new DAOException("Errore recupero recensioni per cliente " + codiceCliente, e);
            }
            return result;
        }

        /**
         * Visualizza le recensioni di un ristorante ordinate per stelle
         */
        public static List<Recensione> listByRistorante(Connection conn, String piva) {
            var result = new ArrayList<Recensione>();
            try (var ps = DAOUtils.prepare(conn, Queries.SELECT_RECENSIONI_BY_RISTORANTE, piva);
                 var rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new Recensione(
                        rs.getInt("codice_cliente"),
                        rs.getString("p_iva"),
                        rs.getInt("numero_stelle"),
                        rs.getString("descrizione"),
                        rs.getString("titolo"),
                        rs.getDate("data")
                    ));
                }
            } catch (Exception e) {
                throw new DAOException("Errore recupero recensioni per ristorante " + piva, e);
            }
            return result;
        }
        /**
         * Top 10 ristoranti per media stelle
         */
        public static List<String> top10Ristoranti(Connection conn) {
            var result = new ArrayList<String>();
            try (var ps = DAOUtils.prepare(conn, Queries.TOP10_RISTORANTI);
                 var rs = ps.executeQuery()) {
                while (rs.next()) {
                    String pivaTop = rs.getString("p_iva");
                    result.add(pivaTop);
                }
            } catch (Exception e) {
                throw new DAOException("Errore recupero top10 ristoranti", e);
            }
            return result;
        }

        public static void deleteRecensione(Connection conn, int codiceCliente, String piva, String titolo) {
            try (var ps = DAOUtils.prepare(conn, Queries.DELETE_RECENSIONE, codiceCliente, piva, titolo)) {
                ps.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore nella cancellazione della recensione", e);
            }
        }

    }
}
