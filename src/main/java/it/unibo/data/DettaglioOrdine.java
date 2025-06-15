package it.unibo.data;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

public class DettaglioOrdine {
    public final int codicePiatto;
    public final int numeroLinea;
    public final int quantita;
    public final BigDecimal prezzoUnitario;
    public final String nomePiatto;
    public final String descrizionePiatto;

    public DettaglioOrdine(int codicePiatto, int numeroLinea, int quantita, 
                          BigDecimal prezzoUnitario, String nomePiatto, String descrizionePiatto) {
        this.codicePiatto = codicePiatto;
        this.numeroLinea = numeroLinea;
        this.quantita = quantita;
        this.prezzoUnitario = prezzoUnitario == null ? BigDecimal.ZERO : prezzoUnitario;
        this.nomePiatto = nomePiatto == null ? "" : nomePiatto;
        this.descrizionePiatto = descrizionePiatto == null ? "" : descrizionePiatto;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof DettaglioOrdine)) return false;
        var d = (DettaglioOrdine) other;
        return d.codicePiatto == this.codicePiatto &&
               d.numeroLinea == this.numeroLinea &&
               d.quantita == this.quantita &&
               d.prezzoUnitario.equals(this.prezzoUnitario) &&
               d.nomePiatto.equals(this.nomePiatto) &&
               d.descrizionePiatto.equals(this.descrizionePiatto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codicePiatto, numeroLinea, quantita, prezzoUnitario, nomePiatto, descrizionePiatto);
    }

    @Override
    public String toString() {
        return Printer.stringify("DettaglioOrdine", List.of(
            Printer.field("codicePiatto", codicePiatto),
            Printer.field("numeroLinea", numeroLinea),
            Printer.field("quantita", quantita),
            Printer.field("prezzoUnitario", prezzoUnitario),
            Printer.field("nomePiatto", nomePiatto)
        ));
    }

    public static final class DAO {

        public static List<DettaglioOrdine> byOrdine(Connection connection, int codiceOrdine) {
            try (var stmt = DAOUtils.prepare(connection, Queries.DETTAGLI_ORDINE, codiceOrdine);
                 var rs = stmt.executeQuery()) {
                
                var dettagli = new java.util.ArrayList<DettaglioOrdine>();
                while (rs.next()) {
                    dettagli.add(new DettaglioOrdine(
                        rs.getInt("codice_piatto"),
                        rs.getInt("numero_linea"),
                        rs.getInt("quantita"),
                        rs.getBigDecimal("prezzo_unitario"),
                        rs.getString("nome_piatto"),
                        rs.getString("descrizione_piatto")
                    ));
                }
                return dettagli;
                
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento dei dettagli ordine", e);
            }
        }
    }
}
