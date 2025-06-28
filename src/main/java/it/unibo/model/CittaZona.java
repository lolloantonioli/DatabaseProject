package it.unibo.model;

public class CittaZona {
    public final String citta;
    public final String cap;
    public final int codiceZona;
    public CittaZona(String citta, String cap, int codiceZona) {
        this.citta = citta;
        this.cap = cap;
        this.codiceZona = codiceZona;
    }
    @Override
    public String toString() {
        return citta + " (" + cap + ")";
    }
    public static final CittaZona[] CITTA_ZONA = {
        new CittaZona("Roma", "00100", 1),
        new CittaZona("Milano", "20100", 2),
        new CittaZona("Napoli", "80100", 3),
        new CittaZona("Torino", "10100", 4),
        new CittaZona("Palermo", "90100", 5),
        new CittaZona("Genova", "16100", 6),
        new CittaZona("Bologna", "40100", 7),
        new CittaZona("Firenze", "50100", 8),
        new CittaZona("Bari", "70100", 9),
        new CittaZona("Catania", "95100", 10),
        new CittaZona("Venezia", "30100", 11),
        new CittaZona("Verona", "37100", 12),
        new CittaZona("Messina", "98100", 13),
        new CittaZona("Padova", "35100", 14),
        new CittaZona("Trieste", "34100", 15),
    };
}
