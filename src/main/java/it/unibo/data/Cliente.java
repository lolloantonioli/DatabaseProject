package it.unibo.data;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Cliente {

    public final int codiceCliente;
    public final String nome;
    public final String cognome;
    public final String email;
    public final String telefono;
    public final LocalDate dataNascita;
    public final String username;

    public Cliente(int codiceCliente, String nome, String cognome, String email, 
                   String telefono, LocalDate dataNascita, String username) {
        this.codiceCliente = codiceCliente;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
        this.dataNascita = dataNascita;
        this.username = username;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof Cliente)) return false;
        var c = (Cliente) other;
        return c.codiceCliente == this.codiceCliente &&
               c.nome.equals(this.nome) &&
               c.cognome.equals(this.cognome) &&
               c.email.equals(this.email) &&
               c.telefono.equals(this.telefono) &&
               Objects.equals(c.dataNascita, this.dataNascita) &&
               c.username.equals(this.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceCliente, nome, cognome, email, telefono, dataNascita, username);
    }

    @Override
    public String toString() {
        return Printer.stringify("Cliente", List.of(
            Printer.field("codiceCliente", codiceCliente),
            Printer.field("nome", nome),
            Printer.field("cognome", cognome),
            Printer.field("email", email),
            Printer.field("telefono", telefono),
            Printer.field("dataNascita", dataNascita),
            Printer.field("username", username)
        ));
    }

    public static final class DAO {

        public static Optional<Cliente> find(Connection connection, int codiceCliente) {
            try (var stmt = DAOUtils.prepare(connection, Queries.FIND_CLIENTE, codiceCliente);
                 var rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    return Optional.of(new Cliente(
                        rs.getInt("codice_cliente"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getDate("data_nascita") != null ? 
                            rs.getDate("data_nascita").toLocalDate() : null,
                        rs.getString("username")
                    ));
                }
                return Optional.empty();
                
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento del cliente", e);
            }
        }

        public static List<Cliente> findAll(Connection connection) {
            try (var stmt = DAOUtils.prepare(connection, Queries.LIST_CLIENTI);
                 var rs = stmt.executeQuery()) {
                
                var clienti = new ArrayList<Cliente>();
                while (rs.next()) {
                    clienti.add(new Cliente(
                        rs.getInt("codice_cliente"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getDate("data_nascita") != null ? 
                            rs.getDate("data_nascita").toLocalDate() : null,
                        rs.getString("username")
                    ));
                }
                return clienti;
                
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento dei clienti", e);
            }
        }

        public static void save(Connection connection, Cliente cliente) {
            try (var stmt = DAOUtils.prepare(connection, Queries.INSERT_CLIENTE,
                    cliente.codiceCliente, cliente.nome, cliente.cognome,
                    cliente.email, cliente.telefono, cliente.dataNascita, cliente.username)) {
                
                stmt.executeUpdate();
                
            } catch (Exception e) {
                throw new DAOException("Errore durante il salvataggio del cliente", e);
            }
        }
    }

}
