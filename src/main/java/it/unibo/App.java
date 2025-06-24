package it.unibo;

import it.unibo.controller.Controller;

public class App {
    public static void main(String[] args) {
        new Controller().goToMenu();
    }
}
// BISOGNA MODIFICARE LE QUERY E I DAO PERCHE I NOMI DEI CAMPI NON SONO QUELLI DDELLE TABELLE
// BISOGNA SISTEMARE GESTIONE DATI CLIENTE, MODIFICANDO TUTTI I LOCALDATE IN DATE DI SQL