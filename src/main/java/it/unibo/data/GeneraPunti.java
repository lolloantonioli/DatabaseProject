package it.unibo.data;

import java.sql.Connection;
<<<<<<< HEAD
import java.sql.PreparedStatement;
import java.sql.ResultSet;

=======
>>>>>>> 3e54bf10720f374e7df872fff18693aa72e69b7a
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GeneraPunti {
    public final int codiceOrdine;
    public final int puntiGenerati;
    public final int codiceRaccolta;

    public GeneraPunti(int codiceOrdine, int puntiGenerati, int codiceRaccolta) {
        this.codiceOrdine = codiceOrdine;
        this.puntiGenerati = puntiGenerati;
        this.codiceRaccolta = codiceRaccolta;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || !(other instanceof GeneraPunti)) return false;
        var gp = (GeneraPunti) other;
        return gp.codiceOrdine == this.codiceOrdine &&
               gp.puntiGenerati == this.puntiGenerati &&
               gp.codiceRaccolta == this.codiceRaccolta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceOrdine, puntiGenerati, codiceRaccolta);
    }

    @Override
    public String toString() {
        return Printer.stringify("GeneraPunti", List.of(
            Printer.field("codice ordine", codiceOrdine),
            Printer.field("punti generati", puntiGenerati),
            Printer.field("codice raccolta", codiceRaccolta)
        ));
    }

    public static final class DAO {
        public Optional<GeneraPunti> findByOrdine(Connection connection, int codiceOrdine){
            try (var stmt = DAOUtils.prepare(connection, Queries.PUNTI_GENERATI_BY_ORDINE, codiceOrdine);
                 var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int punti = rs.getInt("punti_generati");
                    int raccolta = rs.getInt("codice_raccolta");
                    return Optional.of(new GeneraPunti(codiceOrdine, punti, raccolta));
                }
            } catch (Exception e) {
                throw new DAOException("Errore durante la ricerca dei punti generati per l'ordine con codice " + codiceOrdine, e);
            }
            return Optional.empty();
        }
    }
}
