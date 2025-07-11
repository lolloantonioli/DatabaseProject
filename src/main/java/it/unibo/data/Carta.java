package it.unibo.data;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Carta {

    public final int codiceCliente;
    public final String nome;
    public final String numero;
    public final String titolare;
    public final Date dataScadenza;
    public final String cvv;

    public Carta(int codiceCliente, String nome, String numero, String titolare, Date dataScadenza, String cvv) {
        this.codiceCliente = codiceCliente;
        this.nome = nome;
        this.numero = numero;
        this.titolare = titolare;
        this.dataScadenza = dataScadenza;
        this.cvv = cvv;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || !(other instanceof Carta)) return false;
        Carta c = (Carta) other;
        return c.codiceCliente == this.codiceCliente
            && c.nome.equals(this.nome)
            && c.numero.equals(this.numero)
            && c.titolare.equals(this.titolare)
            && c.dataScadenza.equals(this.dataScadenza)
            && c.cvv.equals(this.cvv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceCliente, nome, numero, titolare, dataScadenza, cvv);
    }

    @Override
    public String toString() {
        return Printer.stringify("Carta", List.of(
            Printer.field("codice cliente", codiceCliente),
            Printer.field("nome", nome),
            Printer.field("numero", numero),
            Printer.field("titolare", titolare),
            Printer.field("data scadenza", dataScadenza),
            Printer.field("cvv", cvv)
        ));
    }

    public static final class DAO {
        /**
         * Restituisce tutte le carte possedute da un cliente
         */
        public static List<Carta> listCarteByCliente(Connection connection, int codiceCliente) {
            List<Carta> result = new ArrayList<>();
            try (var stmt = DAOUtils.prepare(connection, Queries.CARTE_BY_CLIENTE, codiceCliente);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String nome = rs.getString("nome");
                    String numero = rs.getString("numero");
                    String titolare = rs.getString("titolare");
                    Date scadenza = rs.getDate("data_scadenza");
                    String cvv = rs.getString("cvv");
                    result.add(new Carta(codiceCliente, nome, numero, titolare, scadenza, cvv));
                }
            } catch (Exception e) {
                throw new DAOException("Errore durante il recupero delle carte del cliente " + codiceCliente, e);
            }
            return result;
        }

        /**
         * Restituisce gli ordini effettuati con una specifica carta
         */
        public static List<Integer> listOrdiniByCarta(Connection connection, int codiceCliente, String numeroCarta) {
            List<Integer> result = new ArrayList<>();
            try (var stmt = DAOUtils.prepare(connection, Queries.ORDINI_BY_CARTA, codiceCliente, numeroCarta);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getInt("codice_ordine"));
                }
            } catch (Exception e) {
                throw new DAOException("Errore durante il recupero degli ordini per la carta " + numeroCarta + " del cliente " + codiceCliente, e);
            }
            return result;
        }

        public static void insertCarta(Connection conn, Carta c) {
            try (var ps = DAOUtils.prepare(conn, Queries.INSERT_CARTA,
                                        c.codiceCliente, c.nome, c.numero,
                                        c.titolare, c.dataScadenza, c.cvv)) {
                ps.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore inserimento carta per cliente " + c.codiceCliente, e);
            }
        }

        public static void deleteCarta(Connection connection, int codiceCliente, String nome, String numero) {
            try (var ps = DAOUtils.prepare(connection, Queries.DELETE_CARTA, codiceCliente, nome, numero)) {
                ps.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore durante la rimozione della carta", e);
            }
        }

    }

}
