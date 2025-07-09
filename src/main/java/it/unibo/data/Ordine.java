package it.unibo.data;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Ordine {
    public int codiceOrdine;
    public final int codicePagamento;
    public final double prezzoTotale;
    public final String piva;

    public Ordine(int codicePagamento, 
                  double prezzoTotale, String piva) {
        this.codicePagamento = codicePagamento;
        this.prezzoTotale = prezzoTotale;
        this.piva = piva;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof Ordine)) return false;
        var o = (Ordine) other;
        return o.codiceOrdine == this.codiceOrdine &&
               o.codicePagamento == this.codicePagamento &&
               o.prezzoTotale == this.prezzoTotale &&
               o.piva.equals(this.piva);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceOrdine, codicePagamento, prezzoTotale, piva);
    }

    @Override
    public String toString() {
        return Printer.stringify("Ordine", List.of(
            Printer.field("codiceOrdine", codiceOrdine),
            Printer.field("codicePagamento", codicePagamento),
            Printer.field("prezzoTotale", prezzoTotale),
            Printer.field("piva", piva)
        ));
    }

    public static final class DAO {

        public static Optional<Ordine> find(Connection connection, int codiceOrdine) {
            try (var stmt = DAOUtils.prepare(connection, Queries.FIND_ORDINE, codiceOrdine);
                 var rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    var o = new Ordine(
                        rs.getInt("codice_pagamento"),
                        rs.getDouble("prezzo_totale"),
                        rs.getString("p_iva")
                    );
                    o.codiceOrdine = rs.getInt("codice_ordine");
                    return Optional.of(o); 
                }
                return Optional.empty();
                
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento dell'ordine", e);
            }
        }

        public static List<Ordine> byCliente(Connection connection, int codiceCliente) {
            try (var stmt = DAOUtils.prepare(connection, Queries.ORDINI_BY_CLIENTE, codiceCliente);
                 var rs = stmt.executeQuery()) {
                
                var ordini = new ArrayList<Ordine>();
                while (rs.next()) {    
                    Ordine o = new Ordine(
                        rs.getInt("codice_pagamento"),
                        rs.getDouble("prezzo_totale"),
                        rs.getString("p_iva")
                    );
                    o.codiceOrdine = rs.getInt("codice_ordine");    
                    ordini.add(o);
                }
                return ordini;
                
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("Errore durante il caricamento degli ordini del cliente", e);
            }
        }
        /**
         * Restituisce gli ordini che un rider deve consegnare
         */
        public static List<Ordine> daConsegnareByRider(Connection connection, int codiceRider) {
            try (var stmt = DAOUtils.prepare(connection, Queries.ORDINI_DA_CONSEGNARE_BY_RIDER, codiceRider);
                 var rs = stmt.executeQuery()) {
                
                var ordini = new ArrayList<Ordine>();
                while (rs.next()) {
                    Ordine o = new Ordine(
                        rs.getInt("codice_pagamento"),
                        rs.getDouble("prezzo_totale"),
                        rs.getString("p_iva")
                    );
                    o.codiceOrdine = rs.getInt("codice_ordine");    
                    ordini.add(o);
                }
                return ordini;
            } catch (Exception e) {
                throw new DAOException("Error loading to-deliver orders for rider " + codiceRider, e);
            }
        }

        /**
         * Restituisce gli ordini già consegnati da un rider
         */
        public static List<Ordine> consegnatiByRider(Connection connection, int codiceRider) {
            try (var stmt = DAOUtils.prepare(connection, Queries.ORDINI_CONSEGNATI_BY_RIDER, codiceRider);
                 var rs = stmt.executeQuery()) {
                
                var ordini = new ArrayList<Ordine>();
                while (rs.next()) {
                    Ordine o = new Ordine(
                        rs.getInt("codice_pagamento"),
                        rs.getDouble("prezzo_totale"),
                        rs.getString("p_iva")
                    );
                    o.codiceOrdine = rs.getInt("codice_ordine");    
                    ordini.add(o);
                }
                return ordini;
            } catch (Exception e) {
                throw new DAOException("Error loading delivered orders for rider " + codiceRider, e);
            }
        }

        public static int insertOrdine(Connection conn, int codicePagamento, double prezzoTotale, String piva) {
            try (var ps = conn.prepareStatement(Queries.INSERT_ORDINE, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, codicePagamento);
                ps.setDouble(2, prezzoTotale);
                ps.setString(3, piva);
                ps.executeUpdate();
                try (var rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) throw new DAOException("Nessun ID ordine");
                    return rs.getInt(1);
                }
            } catch (Exception e) {
                throw new DAOException("Errore inserimento ordine", e);
            }
        }

        /**
         * Recupera il Cliente che ha effettuato questo ordine.
         * 
         * @param conn         la connessione al DB
         * @param codiceOrdine l'ID dell'ordine
         * @return Optional con il Cliente, vuoto se non trovato
         */
        public static Optional<Cliente> findClienteByOrder(Connection conn, int codiceOrdine) {
            try (var ps = DAOUtils.prepare(conn, Queries.SELECT_CLIENTE_BY_ORDINE, codiceOrdine);
                 var rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente(
                        rs.getString("Nome"),
                        rs.getString("Cognome"),
                        rs.getString("E_mail"),
                        rs.getString("Telefono"),
                        rs.getDate("Data_di_Nascita"),
                        rs.getString("Username")
                    );
                    cliente.codiceCliente = rs.getInt("codice_cliente");
                    return Optional.of(cliente);
                }
                return Optional.empty();
            } catch (Exception e) {
                throw new DAOException(
                  "Errore recupero cliente per ordine " + codiceOrdine, e);
            }
        }

        /**
         * Restituisce tutti gli ordini ricevuti da un ristorante.
         */
        public static List<Ordine> byRistorante(Connection connection, String piva) {
            try (var stmt = DAOUtils.prepare(connection, Queries.ORDINI_BY_RISTORANTE, piva);
                 var rs = stmt.executeQuery()) {

                var ordini = new ArrayList<Ordine>();
                while (rs.next()) {
                    Ordine o = new Ordine(
                        rs.getInt("codice_pagamento"),
                        rs.getDouble("prezzo_totale"),
                        rs.getString("p_iva")
                    );
                    o.codiceOrdine = rs.getInt("codice_ordine");    
                    ordini.add(o);
                }
                return ordini;

            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento degli ordini del ristorante " + piva, e);
            }
        }
    }
}
