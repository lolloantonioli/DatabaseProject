package it.unibo.model;

import java.util.List;

public class CarrelloInfo {
    public final List<DettaglioInfo> dettagli;
    public final double totaleParziale;
    public final double scontoPromozioni;
    public final String descScontoPromozioni;
    public final double scontoPunti;
    public final String descScontoPunti;
    public final double totaleFinale;

    public CarrelloInfo(List<DettaglioInfo> dettagli,
                        double totaleParziale,
                        double scontoPromozioni,
                        String descScontoPromozioni,
                        double scontoPunti,
                        String descScontoPunti,
                        double totaleFinale) {
        this.dettagli = dettagli;
        this.totaleParziale = totaleParziale;
        this.scontoPromozioni = scontoPromozioni;
        this.descScontoPromozioni = descScontoPromozioni;
        this.scontoPunti = scontoPunti;
        this.descScontoPunti = descScontoPunti;
        this.totaleFinale = totaleFinale;
    }
}
