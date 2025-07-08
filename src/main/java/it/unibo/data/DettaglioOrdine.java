package it.unibo.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DettaglioOrdine {
    public final int codicePiatto;
    public final int codiceOrdine;
    public final int numeroLinea;
    public final int quantita;
    public final double prezzoUnitario;

    public DettaglioOrdine(int codicePiatto, int codiceOrdine, int numeroLinea, int quantita, 
                          double prezzoUnitario) {
        this.codicePiatto = codicePiatto;
        this.codiceOrdine = codiceOrdine;
        this.numeroLinea = numeroLinea;
        this.quantita = quantita;
        this.prezzoUnitario = prezzoUnitario;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof DettaglioOrdine)) return false;
        var d = (DettaglioOrdine) other;
        return d.codicePiatto == this.codicePiatto &&
               d.codiceOrdine == this.codiceOrdine &&
               d.numeroLinea == this.numeroLinea &&
               d.quantita == this.quantita &&
               d.prezzoUnitario == this.prezzoUnitario;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codicePiatto, codiceOrdine, numeroLinea, quantita, prezzoUnitario);
    }

    @Override
    public String toString() {
        return Printer.stringify("DettaglioOrdine", List.of(
            Printer.field("codicePiatto", codicePiatto),
            Printer.field("codiceOrdine", codiceOrdine),
            Printer.field("numeroLinea", numeroLinea),
            Printer.field("quantita", quantita),
            Printer.field("prezzoUnitario", prezzoUnitario)
        ));
    }

    public static final class DAO {

        public static List<DettaglioOrdine> byOrdine(Connection connection, int codiceOrdine) {
            try (var stmt = DAOUtils.prepare(connection, Queries.SELECT_DETTAGLI_BY_ORDINE, codiceOrdine);
                 var rs = stmt.executeQuery()) {
                
                var dettagli = new ArrayList<DettaglioOrdine>();
                while (rs.next()) {
                    dettagli.add(new DettaglioOrdine(
                        rs.getInt("codice_piatto"),
                        rs.getInt("codice_ordine"),
                        rs.getInt("numero_linea"),
                        rs.getInt("quantita"),
                        rs.getDouble("prezzo_unitario")
                    ));
                }
                return dettagli;
                
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento dei dettagli ordine", e);
            }
        }

        public static int getNextNumeroLinea(Connection conn, int codiceOrdine) {
            try (var ps = DAOUtils.prepare(conn, Queries.NEXT_NUMERO_LINEA_BY_ORDINE, codiceOrdine);
                var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("next_line");
                }
                // se non c’è neppure una riga, comincia da 1
                return 1;
            } catch (Exception e) {
                throw new DAOException(
                    "Errore calcolo prossimo numero_linea per ordine " + codiceOrdine, e);
            }
        }


        public static void insertDettaglio(Connection conn,
                                        int codiceOrdine,
                                        int codicePiatto,
                                        int quantita,
                                        double prezzoUnitario) {
            int numeroLinea = getNextNumeroLinea(conn, codiceOrdine);
            try (var ps = DAOUtils.prepare(conn,
                                        Queries.INSERT_DETTAGLIO_ORDINE,
                                        codicePiatto,
                                        codiceOrdine,
                                        numeroLinea,
                                        quantita,
                                        prezzoUnitario)) {
                ps.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore inserimento dettaglio per ordine " + codiceOrdine, e);
            }
        }

    }
}
