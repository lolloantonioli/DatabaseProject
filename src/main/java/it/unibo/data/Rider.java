package it.unibo.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Rider {

    public int codiceRider;
    public final String nome;
    public final String cognome;
    public final String email;
    public final String telefono;
    public final Date dataNascita;
    public final String iban;
    public final String cf;
    public final boolean patente;
    public final boolean disponibile;
    public final int codiceZona;

    public Rider(String nome, String cognome, String email, 
                 String telefono, Date dataNascita, String iban, String cf,
                 boolean patente, boolean disponibile, int codiceZona) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
        this.dataNascita = dataNascita;
        this.iban = iban;
        this.cf = cf;
        this.patente = patente;
        this.disponibile = disponibile;
        this.codiceZona = codiceZona;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof Rider)) return false;
        var r = (Rider) other;
        return r.codiceRider == this.codiceRider &&
               r.nome.equals(this.nome) &&
               r.cognome.equals(this.cognome) &&
               r.email.equals(this.email) &&
               r.telefono.equals(this.telefono) &&
               Objects.equals(r.dataNascita, this.dataNascita) &&
               r.iban.equals(this.iban) &&
               r.cf.equals(this.cf) &&
               r.patente == this.patente &&
               r.disponibile == this.disponibile &&
               r.codiceZona == this.codiceZona;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceRider, nome, cognome, email, telefono, 
                           dataNascita, iban, cf, patente, disponibile, codiceZona);
    }

    @Override
    public String toString() {
        return Printer.stringify("Rider", List.of(
            Printer.field("codiceRider", codiceRider),
            Printer.field("nome", nome),
            Printer.field("cognome", cognome),
            Printer.field("email", email),
            Printer.field("telefono", telefono),
            Printer.field("dataNascita", dataNascita),
            Printer.field("iban", iban),
            Printer.field("cf", cf),
            Printer.field("patente", patente),
            Printer.field("disponibile", disponibile),
            Printer.field("codiceZona", codiceZona)
        ));
    }

    public void setCodiceRider(final int codiceRider) {
        this.codiceRider = codiceRider;
    }

    public static final class DAO {

        public static Optional<Rider> find(Connection connection, int codiceRider) {
            try (var stmt = DAOUtils.prepare(connection, Queries.FIND_RIDER, codiceRider);
                 var rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    Rider r = new Rider(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("e_mail"),
                        rs.getString("telefono"),
                        rs.getDate("data_di_nascita") != null ? 
                            rs.getDate("data_di_nascita") : null,
                        rs.getString("iban"),
                        rs.getString("codice_fiscale"),
                        rs.getBoolean("patente"),
                        rs.getBoolean("disponibile"),
                        rs.getInt("codice_zona")
                    );
                    r.setCodiceRider(rs.getInt("codice_rider"));
                    return Optional.of(r);
                }
                return Optional.empty();
                
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento del rider", e);
            }
        }

        public static List<Rider> findByZona(Connection connection, int codiceZona) {
            try (var stmt = DAOUtils.prepare(connection, Queries.RIDERS_BY_ZONA, codiceZona);
                 var rs = stmt.executeQuery()) {
                
                var riders = new ArrayList<Rider>();
                while (rs.next()) {
                    Rider r = new Rider(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("e_mail"),
                        rs.getString("telefono"),
                        rs.getDate("data_di_nascita") != null ? 
                            rs.getDate("data_di_nascita") : null,
                        rs.getString("iban"),
                        rs.getString("codice_fiscale"),
                        rs.getBoolean("patente"),
                        rs.getBoolean("disponibile"),
                        rs.getInt("codice_zona")
                    );
                    r.setCodiceRider(rs.getInt("codice_rider"));
                    riders.add(r);
                }
                return riders;
                
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento dei riders per zona", e);
            }
        }

        public static List<Rider> findAvailable(Connection connection, int codiceZona) {
            try (var stmt = DAOUtils.prepare(connection, Queries.AVAILABLE_RIDERS_BY_ZONA, codiceZona);
                 var rs = stmt.executeQuery()) {
                
                var riders = new ArrayList<Rider>();
                while (rs.next()) {
                    riders.add(new Rider(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("e_mail"),
                        rs.getString("telefono"),
                        rs.getDate("data_di_nascita") != null ? 
                            rs.getDate("data_di_nascita") : null,
                        rs.getString("iban"),
                        rs.getString("codice_fiscale"),
                        rs.getBoolean("patente"),
                        rs.getBoolean("disponibile"),
                        rs.getInt("codice_zona")
                    ));
                }
                return riders;
                
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento dei riders disponibili", e);
            }
        }

        public static Optional<Rider> findRiderByEmail(Connection connection, String email) {
            try (var stmt = DAOUtils.prepare(connection, Queries.FIND_RIDER_BY_EMAIL, email);
                 var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Rider r = new Rider(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("e_mail"),
                        rs.getString("telefono"),
                        rs.getDate("data_di_nascita") != null ? 
                            rs.getDate("data_di_nascita") : null,
                        rs.getString("iban"),
                        rs.getString("codice_fiscale"),
                        rs.getBoolean("patente"),
                        rs.getBoolean("disponibile"),
                        rs.getInt("codice_zona")
                    );
                    r.setCodiceRider(rs.getInt("codice_rider"));
                    return Optional.of(r);
                }
                return Optional.empty();
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento del rider dall'email", e);
            }
        }

        /**
         * Inserisce un nuovo rider
         */
        public static int insertRider(Connection conn, Rider r) {
            try (var ps = conn.prepareStatement(Queries.INSERT_RIDER, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, r.nome);
                ps.setString(2, r.cognome);
                ps.setDate(3, r.dataNascita);
                ps.setString(4, r.email);
                ps.setString(5, r.telefono);
                ps.setString(6, r.iban);
                ps.setString(7, r.cf);
                ps.setBoolean(8, r.patente);
                ps.setBoolean(9, r.disponibile);
                ps.setInt(10, r.codiceZona);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int codiceRider = keys.getInt(1);
                        r.codiceRider = codiceRider;
                        return codiceRider;
                    }
                    return 0;
                }
            } catch (Exception e) {
                throw new DAOException("Errore inserimento rider " + r.codiceRider, e);
            }
        }

        public static void prendiInCaricoOrdine(Connection conn, int codiceOrdine, int codiceRider) {
            try (var ps = DAOUtils.prepare(conn, Queries.PRENDI_IN_CARICO_ORDINE, codiceRider, codiceOrdine)) {
                ps.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore presa in carico ordine", e);
            }
        }

        public static Optional<Ordine> ordineInCaricoByRider(Connection conn, int codiceRider) {
            try (var ps = DAOUtils.prepare(conn, Queries.ORDINE_IN_CARICO_RIDER, codiceRider);
                var rs = ps.executeQuery()) {
                if (rs.next()) {
                    var o = new Ordine(
                        rs.getInt("codice_pagamento"),
                        rs.getDouble("prezzo_totale"),
                        rs.getString("p_iva")
                    );
                    o.codiceOrdine = rs.getInt("codice_ordine");
                    return Optional.of(o); 
                }
            } catch (Exception e) {
                throw new DAOException("Errore ordine in carico", e);
            }
            return Optional.empty();
        }

        public static void consegnaOrdine(Connection conn, int codiceOrdine, int codiceRider) {
            try (var ps = DAOUtils.prepare(conn, Queries.CONSEGNA_ORDINE, codiceOrdine, codiceRider)) {
                ps.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore consegna ordine", e);
            }
        }

        public static List<Object[]> topRiderConsegne(Connection connection, Timestamp from, Timestamp to) {
            List<Object[]> list = new ArrayList<>();
            try (var ps = DAOUtils.prepare(connection, Queries.TOP_RIDER_PER_CONSEGNE_PERIODO, from, to)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(new Object[] {
                            rs.getInt("Codice_Rider"),
                            rs.getString("NomeRider"),
                            rs.getString("CognomeRider"),
                            rs.getInt("ConsegneCompletate")
                        });
                    }
                }
            } catch (Exception e) {
                throw new DAOException("Errore caricamento migliori rider per consegne in un periodo", e);
            }
            return list;
        }
    }
}
