package it.unibo.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Cliente {

    public int codiceCliente;
    public final String nome;
    public final String cognome;
    public final String email;
    public final String telefono;
    public final Date dataNascita;
    public final String username;

    public Cliente(String nome, String cognome, String email, 
                   String telefono, Date dataNascita, String username) {
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

    public void setCodiceCliente(final int codiceCliente) {
        this.codiceCliente = codiceCliente;
    }

    public static final class DAO {

        public static Optional<Cliente> find(Connection connection, int codiceCliente) {
        try (var stmt = DAOUtils.prepare(connection, Queries.FIND_CLIENTE_BY_ID, codiceCliente);
             var rs   = stmt.executeQuery()) {
            if (rs.next()) {
                Cliente c = new Cliente(
                    rs.getString("nome"),
                    rs.getString("cognome"),
                    rs.getString("e_mail"),
                    rs.getString("telefono"),
                    rs.getDate("data_di_nascita") != null 
                        ? rs.getDate("data_di_nascita") 
                        : null,
                    rs.getString("username")
                );
                c.setCodiceCliente(rs.getInt("codice_cliente"));
                return Optional.of(c);
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new DAOException("Errore durante il FIND del cliente", e);
        }
        }

        public static Optional<Cliente> findByUsername(Connection connection, String username) {
            try (var stmt = DAOUtils.prepare(connection, Queries.FIND_CLIENTE_BY_USERNAME, username);
                var rs   = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("e_mail"),
                        rs.getString("telefono"),
                        rs.getDate("data_di_nascita") != null 
                            ? rs.getDate("data_di_nascita")
                            : null,
                        rs.getString("username")
                    );
                    c.setCodiceCliente(rs.getInt("codice_cliente"));
                    return Optional.of(c);
                }
                return Optional.empty();
            } catch (Exception e) {
                throw new DAOException("Errore durante il FIND_BY_USERNAME del cliente", e);
            }
        }

        public static List<Cliente> findAll(Connection connection) {
            try (var stmt = DAOUtils.prepare(connection, Queries.LIST_CLIENTI);
                var rs   = stmt.executeQuery()) {
                var list = new ArrayList<Cliente>();
                while (rs.next()) {
                    Cliente c = new Cliente(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("e_mail"),
                        rs.getString("telefono"),
                        rs.getDate("data_di_nascita") != null 
                            ? rs.getDate("data_nascita")
                            : null,
                        rs.getString("username")
                    );
                    c.setCodiceCliente(rs.getInt("codice_cliente"));
                    list.add(c);
                }
                return list;
            } catch (Exception e) {
                throw new DAOException("Errore durante il LIST del cliente", e);
            }
        }

        /**
         * Inserisce un nuovo Cliente nel DB e popola il suo codiceCliente
         * (AUTO_INCREMENT).
         */
        public static void save(Connection connection, Cliente cliente) {
            try (var ps = connection.prepareStatement(
                    Queries.INSERT_CLIENTE,
                    Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, cliente.nome);
                ps.setString(2, cliente.cognome);
                ps.setString(3, cliente.email);
                ps.setString(4, cliente.telefono);
                if (cliente.dataNascita != null) {
                    ps.setDate(5, cliente.dataNascita);
                } else {
                    ps.setNull(5, Types.DATE);
                }
                ps.setString(6, cliente.username);

                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new DAOException("Inserimento cliente fallito, nessuna riga inserita.");
                }

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        cliente.setCodiceCliente(keys.getInt(1));
                    } else {
                        throw new DAOException("Inserimento cliente fallito, nessun ID generato.");
                    }
                }
            } catch (SQLException e) {
                throw new DAOException("Errore durante il SAVE del cliente", e);
            }
        }

        public static void update(Connection connection, Cliente cliente) {
            try (var stmt = DAOUtils.prepare(connection, Queries.UPDATE_CLIENTE,
                                            cliente.nome,
                                            cliente.cognome,
                                            cliente.email,
                                            cliente.telefono,
                                            cliente.dataNascita,
                                            cliente.username,
                                            cliente.codiceCliente)) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("Errore durante l'UPDATE del cliente", e);
            }
        }

        public static void delete(Connection connection, int codiceCliente) {
            try (var stmt = DAOUtils.prepare(connection, Queries.DELETE_CLIENTE, codiceCliente)) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("Errore durante il DELETE del cliente", e);
            }
        }

    }
}
