package it.unibo.model;

import it.unibo.data.Piatto;

public class RigaCarrello {
    public final Piatto piatto;
    public int quantita;

    public RigaCarrello(Piatto piatto, int quantita) {
        this.piatto = piatto;
        this.quantita = quantita;
    }
}