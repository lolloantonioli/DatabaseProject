package it.unibo.model;

public class DettaglioInfo {
    public final String nomePiatto;
    public final int quantita;
    public final double prezzoUnitario;

    public DettaglioInfo(String nomePiatto, int quantita, double prezzoUnitario) {
        this.nomePiatto = nomePiatto;
        this.quantita = quantita;
        this.prezzoUnitario = prezzoUnitario;
    }
}