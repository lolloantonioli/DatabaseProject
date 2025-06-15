package it.unibo.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Indirizzo {

    public final int codiceIndirizzo;
    public final String via;
    public final String numeroCivico;
    public final String cap;
    public final int interno;
    public final int scala;
    public final int codiceZona; 

    public Indirizzo(int codiceIndirizzo, String via, String numeroCivico, String cap, Integer interno, Integer scala, int codiceZona) {
        this.codiceIndirizzo = codiceIndirizzo;
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
                    list.add(new Indirizzo(id, via, numero, cap, interno, scala, zona));
                }
                return list;
            } catch (Exception e) {
                throw new DAOException("Errore durante il caricamento dei ristoranti per zona", e);
            }
        }

        /**
         * Restituisce la zona geografica corrispondente a un indirizzo
         */
        public Optional<ZonaGeografica> findZonaByIndirizzo(Connection connection, int codiceIndirizzo) {
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
        
    }

}
