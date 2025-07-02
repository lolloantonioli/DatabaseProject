package it.unibo.view;

public final class CardName {

    public static final CardName MENU = new CardName("MENU");
    public static final CardName CLIENTE = new CardName("CLIENTE");
    public static final CardName AMMINISTRATORE = new CardName("AMMINISTRATORE");
    public static final CardName RIDER = new CardName("RIDER");
    public static final CardName RISTORANTE = new CardName("RISTORANTE");
    public static final CardName CHECKOUT = new CardName("CHECKOUT");
    public static final CardName CLIENTE_ACCESS = new CardName("CLIENTE_ACCESS");
    public static final CardName RIDER_ACCESS = new CardName("RIDER_ACCESS");
    public static final CardName RISTORANTE_ACCESS = new CardName("RISTORANTE_ACCESS");

    private final String name;

    private CardName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
