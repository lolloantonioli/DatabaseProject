package it.unibo.data;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Rider {

    public final int codiceRider;
    public final String nome;
    public final String cognome;
    public final String email;
    public final String telefono;
    public final LocalDate dataNascita;
    public final String iban;
    public final String cf;
    public final boolean patente;
    public final boolean disponibile;
    public final int codiceZona;

    public Rider(int codiceRider, String nome, String cognome, String email, 
                 String telefono, LocalDate dataNascita, String iban, String cf,
                 boolean patente, boolean disponibile, int codiceZona) {
        this.codiceRider = codiceRider;
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

    public static final class DAO {

        public static Optional<Rider> find(Connection connection, int codiceRider) {
            try (var stmt = DAOUtils.prepare(connection, Queries.FIND_RIDER, codiceRider);
                 var rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    return Optional.of(new Rider(
                        rs.getInt("codice_rider"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getDate("data_nascita") != null ? 
                            rs.getDate("data_nascita").toLocalDate() : null,
                        rs.getString("iban"),
                        rs.getString("cf"),
                        rs.getBoolean("patente"),
                        rs.getBoolean("disponibile"),
                        rs.getInt("codice_zona")
                    ));
                }
                return Optional.empty();
                
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento del rider", e);
            }
        }

        public static List<Rider> findByZona(Connection connection, int codiceZona) {
            try (var stmt = DAOUtils.prepare(connection, Queries.RIDERS_BY_ZONA, codiceZona);
                 var rs = stmt.executeQuery()) {
                
                var riders = new java.util.ArrayList<Rider>();
                while (rs.next()) {
                    riders.add(new Rider(
                        rs.getInt("codice_rider"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getDate("data_nascita") != null ? 
                            rs.getDate("data_nascita").toLocalDate() : null,
                        rs.getString("iban"),
                        rs.getString("cf"),
                        rs.getBoolean("patente"),
                        rs.getBoolean("disponibile"),
                        rs.getInt("codice_zona")
                    ));
                }
                return riders;
                
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento dei riders per zona", e);
            }
        }

        public static List<Rider> findAvailable(Connection connection, int codiceZona) {
            try (var stmt = DAOUtils.prepare(connection, Queries.AVAILABLE_RIDERS_BY_ZONA, codiceZona);
                 var rs = stmt.executeQuery()) {
                
                var riders = new java.util.ArrayList<Rider>();
                while (rs.next()) {
                    riders.add(new Rider(
                        rs.getInt("codice_rider"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getDate("data_nascita") != null ? 
                            rs.getDate("data_nascita").toLocalDate() : null,
                        rs.getString("iban"),
                        rs.getString("cf"),
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
    }
}
