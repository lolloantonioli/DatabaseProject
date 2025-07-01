package it.unibo.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Ristorante {

    public final String piva;
    public final String nome;
    public final String indirizzo;
    public final String orario;
    public final int codiceZona;

    public Ristorante(String piva, String nome, String indirizzo, String orario, int codiceZona) {
        this.piva = piva;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.orario = orario;
        this.codiceZona = codiceZona;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof Ristorante)) return false;
        var r = (Ristorante) other;
        return r.piva.equals(this.piva) &&
               r.nome.equals(this.nome) &&
               r.indirizzo.equals(this.indirizzo) &&
               r.orario.equals(this.orario) &&
               r.codiceZona == this.codiceZona;
    }

    @Override
    public int hashCode() {
        return Objects.hash(piva, nome, indirizzo, orario, codiceZona);
    }

    @Override
    public String toString() {
        return Printer.stringify("Ristorante", List.of(
            Printer.field("piva", piva),
            Printer.field("nome", nome),
            Printer.field("indirizzo", indirizzo),
            Printer.field("orario", orario),
            Printer.field("codiceZona", codiceZona)
        ));
    }

    public static final class DAO {

        public static Optional<Ristorante> find(Connection connection, String piva) {
            try (var stmt = DAOUtils.prepare(connection, Queries.FIND_RISTORANTE, piva);
                 var rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    return Optional.of(new Ristorante(
                        rs.getString("p_iva"),
                        rs.getString("nome"),
                        rs.getString("indirizzo"),
                        rs.getString("orario"),
                        rs.getInt("codice_zona")
                    ));
                }
                return Optional.empty();
                
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento del ristorante", e);
            }
        }

        public static List<Ristorante> findByZona(Connection connection, int codiceZona) {
            try (var stmt = DAOUtils.prepare(connection, Queries.RISTORANTI_BY_ZONA, codiceZona);
                 var rs = stmt.executeQuery()) {
                
                var ristoranti = new ArrayList<Ristorante>();
                while (rs.next()) {
                    ristoranti.add(new Ristorante(
                        rs.getString("p_iva"),
                        rs.getString("nome"),
                        rs.getString("indirizzo"),
                        rs.getString("orario"),
                        rs.getInt("codice_zona")
                    ));
                }
                return ristoranti;
                
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento dei ristoranti per zona", e);
            }
        }
        /**
         * Inserisce un nuovo ristorante
         */
        public static void insertRistorante(Connection conn, Ristorante r) {
            try (var ps = DAOUtils.prepare(conn,
                                        Queries.INSERT_RISTORANTE,
                                        r.piva,
                                        r.nome,
                                        r.indirizzo,
                                        r.orario,
                                        r.codiceZona)) {
                ps.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore inserimento ristorante " + r.piva, e);
            }
        }

        public static Optional<String> findPivaByNome(Connection connection, String nome) {
            try (var stmt = DAOUtils.prepare(connection, Queries.FIND_PIVA_BY_NOME, nome);
                var rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(rs.getString("p_iva"));
                }
                return Optional.empty();

            } catch (Exception e) {
                throw new DAOException("Errore durante la ricerca della P.IVA del ristorante", e);
            }
        }

        public static List<Ristorante> listAllRistoranti(Connection connection) {
            var list = new ArrayList<Ristorante>();
            try (var stmt = DAOUtils.prepare(connection, Queries.ALL_RISTORANTI);
                var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Ristorante(
                        rs.getString("P_IVA"),
                        rs.getString("Nome"),
                        rs.getString("Indirizzo"),
                        rs.getString("Orario"),
                        rs.getInt("Codice_Zona")
                    ));
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            }
            return list;
        }

        public static List<Object[]> ristorantiTopSpesa(Connection connection) {
            List<Object[]> list = new ArrayList<>();
            try (var stmt = DAOUtils.prepare(connection, Queries.RISTORANTI_SPESA_MEDIA);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[] {
                        rs.getString("P_IVA"),
                        rs.getBigDecimal("SpesaMedia")
                    });
                }
            } catch (Exception e) {
                throw new DAOException("Errore caricamento ristoranti con media ordini > 30€", e);
            }
            return list;
        }

        public static List<Object[]> top10RistorantiPerRecensioni(Connection connection) {
            List<Object[]> list = new ArrayList<>();
            try (var stmt = DAOUtils.prepare(connection, Queries.TOP10_RISTORANTI);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[] {
                        rs.getString("P_IVA"),
                        rs.getDouble("MediaStelle")
                    });
                }
            } catch (Exception e) {
                throw new DAOException("Errore caricamento ristoranti con media ordini > 30€", e);
            }
            return list;
        }
    }

}
