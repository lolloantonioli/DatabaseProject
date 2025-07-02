package it.unibo.data;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Piatto {
    public int codicePiatto;
    public final String nome;
    public final BigDecimal prezzo;
    public final String descrizione;

    public Piatto(String nome, BigDecimal prezzo, String descrizione) {
        this.nome = nome;
        this.prezzo = prezzo;
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

                var piatti = new ArrayList<Piatto>();
                while (rs.next()) {
                    Piatto p = new Piatto(
                        rs.getString("nome"),
                        rs.getBigDecimal("prezzo"),
                        rs.getString("descrizione")
                    );
                    p.codicePiatto = rs.getInt("codice_piatto");
                    piatti.add(p);
                }
                return piatti;

            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento dei piatti", e);
            }
        }
        /**
         * Restituisce gli ordini che contengono un dato piatto
         */
        public static List<String> ordiniByPiatto(Connection connection, int codicePiatto) {
            List<String> result = new ArrayList<>();
            try (var stmt = DAOUtils.prepare(connection, Queries.ORDINI_BY_PIATTO, codicePiatto);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(Printer.stringify("Ordine", List.of(
                        Printer.field("codice_ordine", rs.getInt("codice_ordine")),
                        Printer.field("codice_pagamento", rs.getInt("codice_pagamento")),
                        Printer.field("codice_stato", rs.getInt("codice_stato")),
                        Printer.field("prezzo_totale", rs.getBigDecimal("prezzo_totale")),
                        Printer.field("p_iva", rs.getString("piva"))
                    )));
                }
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento degli ordini per piatto " + codicePiatto, e);
            }
            return result;
        }

        /**
         * Restituisce la classifica dei 10 piatti più ordinati in assoluto
         */
        public static List<String> top10Piatti(Connection connection) {
            List<String> result = new ArrayList<>();
            try (var stmt = DAOUtils.prepare(connection, Queries.TOP10_PIATTI);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(Printer.stringify("PiattoTop", List.of(
                        Printer.field("codice_piatto", rs.getInt("codice_piatto")),
                        Printer.field("nome", rs.getString("nome")),
                        Printer.field("totale_ordini", rs.getInt("totale_piatti"))
                    )));
                }
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento della classifica piatti", e);
            }
            return result;
        }
        /**
         * Inserisce un nuovo piatto per un ristorante
         */
        public static int insertPiatto(Connection connection, Piatto p, String piva) {
            try (
                var stmt = connection.prepareStatement(
                    Queries.INSERT_PIATTO,
                    java.sql.Statement.RETURN_GENERATED_KEYS
                )
            ) {
                stmt.setString(1, p.nome);
                stmt.setBigDecimal(2, p.prezzo);
                stmt.setString(3, p.descrizione);
                stmt.executeUpdate();

                try (var rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int nuovoId = rs.getInt(1);
                        // Inserisce anche nella tabella Offre
                        try (var stmtOffre = DAOUtils.prepare(connection, Queries.INSERT_OFFRE, piva, nuovoId)) {
                            stmtOffre.executeUpdate();
                        }
                        return nuovoId;
                    } else {
                        throw new DAOException("Nessun codice piatto generato");
                    }
                }
            } catch (Exception e) {
                throw new DAOException("Errore inserimento piatto", e);
            }
        }

        public static void deletePiatto(Connection connection, int codicePiatto) {
            try {
                // Prima elimina da Offre (relazione)
                try (var stmtOffre = DAOUtils.prepare(connection, Queries.DELETE_OFFRE, codicePiatto)) {
                    stmtOffre.executeUpdate();
                }
                // Poi elimina da Piatti
                try (var stmtPiatto = DAOUtils.prepare(connection, Queries.DELETE_PIATTO, codicePiatto)) {
                    stmtPiatto.executeUpdate();
                }
            } catch (Exception e) {
                throw new DAOException("Errore eliminazione piatto", e);
            }
        }

    }
}
