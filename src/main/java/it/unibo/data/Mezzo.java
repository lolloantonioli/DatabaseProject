package it.unibo.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Mezzo {

    public final int codiceRider;
    public final int codiceMezzo;
    public final String tipo;
    public final String targa;
    public final String modello;

    public Mezzo(int codiceRider, int codiceMezzo, String tipo, String targa, String modello) {
        this.codiceRider = codiceRider;
        this.codiceMezzo = codiceMezzo;
        this.tipo = tipo;
        this.targa = targa;
        this.modello = modello;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof Mezzo)) return false;
        var m = (Mezzo) other;
        return m.codiceRider == this.codiceRider &&
               m.codiceMezzo == this.codiceMezzo &&
               m.tipo.equals(this.tipo) &&
               m.targa.equals(this.targa) &&
               m.modello.equals(this.modello);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceRider, codiceMezzo, tipo, targa, modello);
    }

    @Override
    public String toString() {
        return Printer.stringify("Mezzo", List.of(
            Printer.field("codice rider", codiceRider),
            Printer.field("codice mezzo", codiceMezzo),
            Printer.field("tipo", tipo),
            Printer.field("targa", targa),
            Printer.field("modello", modello)
        ));
    }

    public static final class DAO {

        public static List<Mezzo> listByRider(Connection connection,int codiceRider) {
            List<Mezzo> result = new ArrayList<>();
            try (var ps = DAOUtils.prepare(connection, Queries.MEZZI_BY_RIDER, codiceRider)) {
                var rs = ps.executeQuery();
                    while (rs.next()) {
                        int idMezzo = rs.getInt("codice_mezzo");
                        String tipo = String.valueOf("tipo");
                        String targa = rs.getString("targa");
                        String modello = rs.getString("modello");
                        result.add(new Mezzo(codiceRider, idMezzo, tipo, targa, modello));
                    }
                
            } catch ( Exception e) {
                throw new DAOException("Errore durante la ricerca dei mezzi per il rider con codice " + codiceRider, e);
            }
            return result;
        }
        /**
         * Inserisce un nuovo mezzo per un rider
         */
        public static void insertMezzo(Connection conn, Mezzo m) {
            try (var ps = DAOUtils.prepare(conn,
                                        Queries.INSERT_MEZZO,
                                        m.codiceRider,
                                        m.codiceMezzo,
                                        m.tipo,
                                        m.targa,
                                        m.modello)) {
                ps.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore inserimento mezzo per rider " + m.codiceRider, e);
            }
        }

        public static int getNextCodiceMezzo(Connection conn, int codiceRider) {
            String sql = "SELECT COALESCE(MAX(Codice_Mezzo), 0) FROM Mezzi WHERE Codice_Rider = ?";
            try (var ps = conn.prepareStatement(sql)) {
                ps.setInt(1, codiceRider);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) + 1;
                    }
                }
            } catch (SQLException e) {
                throw new DAOException("Errore calcolo Codice_Mezzo", e);
            }
            return 1;
        }
    }
}
