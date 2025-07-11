package it.unibo.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Indirizzo {

    public int codiceIndirizzo;
    public final String via;
    public final String numeroCivico;
    public final String cap;
    public final int interno;
    public final int scala;
    public final int codiceZona; 

    public Indirizzo(String via, String numeroCivico, String cap, Integer interno, Integer scala, int codiceZona) {
        this.via = via;
        this.numeroCivico = numeroCivico;
        this.cap = cap;
        this.interno = interno == null ? 0 : interno;
        this.scala = scala == null ? 0 : scala;
        this.codiceZona = codiceZona;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof Indirizzo)) return false;
        var i = (Indirizzo) other;
        return i.codiceIndirizzo == this.codiceIndirizzo &&
               i.via.equals(this.via) &&
               i.numeroCivico.equals(this.numeroCivico) &&
               i.cap.equals(this.cap) &&
               i.interno == this.interno &&
               i.scala == this.scala &&
               i.codiceZona == this.codiceZona;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceIndirizzo, via, numeroCivico, cap, interno, scala, codiceZona);
    }

    @Override
    public String toString() {
        return Printer.stringify("Indirizzo", List.of(
            Printer.field("codice indirizzo", codiceIndirizzo),
            Printer.field("via", via),
            Printer.field("numero civico", numeroCivico),
            Printer.field("cap", cap),
            Printer.field("interno", interno),
            Printer.field("scala", scala),
            Printer.field("codice zona", codiceZona)
        ));
    }

    public void setCodiceIndirizzo(final int codiceIndirizzo) {
        this.codiceIndirizzo = codiceIndirizzo;
    }

    public static final class DAO {
        /**
         * Restituisce tutti gli indirizzi di un cliente
         */
        public static List<Indirizzo> listByCliente(Connection connection, int codiceCliente) {
            try (var stmt = DAOUtils.prepare(connection, Queries.INDIRIZZI_BY_CLIENTE, codiceCliente);
                 var rs = stmt.executeQuery()) {
                
                var list = new ArrayList<Indirizzo>();
                while (rs.next()) {
                    int id = rs.getInt("codice_indirizzo");
                    String via = rs.getString("via");
                    String numero = rs.getString("numero_civico");
                    String cap = rs.getString("cap");
                    int interno = rs.getInt("interno");
                    int scala = rs.getInt("scala");
                    int zona = rs.getInt("codice_zona");
                    Indirizzo ind = new Indirizzo(via, numero, cap, interno, scala, zona);
                    ind.setCodiceIndirizzo(id);
                    list.add(ind);
                }
                return list;
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento dei ristoranti per zona", e);
            }
        }

        /**
         * Restituisce la zona geografica corrispondente a un indirizzo
         */
        public static Optional<ZonaGeografica> findZonaByIndirizzo(Connection connection, int codiceIndirizzo) {
            try (var stmt = DAOUtils.prepare(connection, Queries.ZONA_BY_INDIRIZZO, codiceIndirizzo);
                 var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int idZona = rs.getInt("codice_zona");
                    String nome = rs.getString("nome");
                    return Optional.of(new ZonaGeografica(idZona, nome));
                }
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento della zona geografica per l'indirizzo", e);
            }
            return Optional.empty();
        }
        /**
         * Inserisce un nuovo indirizzo e lo associa al cliente
         * @return il codice identificativo generato per l'indirizzo
         */
        public static void insertIndirizzoEAssociaResidenza(Connection connection, Indirizzo ind, int codiceCliente) {
            try (var psInd = connection.prepareStatement(
                 Queries.INSERT_INDIRIZZO, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                psInd.setString(1, ind.via);
                psInd.setString(2, ind.numeroCivico);
                psInd.setString(3, ind.cap);
                psInd.setInt(4, ind.interno);
                psInd.setInt(5, ind.scala);
                psInd.setInt(6, ind.codiceZona);
                psInd.executeUpdate();
                try (var keys = psInd.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        try (var psRes = DAOUtils.prepare(connection, Queries.INSERT_RESIDENZA,
                                                         codiceCliente, id)) {
                            psRes.executeUpdate();
                        }
                    } else {
                        throw new DAOException("Nessun ID generato per il nuovo indirizzo");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("Errore durante l'inserimento dell'indirizzo e della residenza", e);
            }
        }

        public static void deleteIndirizzo(Connection connection, int codiceCliente, int codiceIndirizzo) {
            try (
                var ps1 = DAOUtils.prepare(connection, Queries.DELETE_RESIDENZA, codiceCliente, codiceIndirizzo);
                var ps2 = DAOUtils.prepare(connection, Queries.DELETE_INDIRIZZO, codiceIndirizzo)
            ) {
                ps1.executeUpdate();
                ps2.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore durante la rimozione dell'indirizzo e della residenza", e);
            }
        }
    }

}
