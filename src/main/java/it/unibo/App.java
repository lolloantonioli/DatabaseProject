package it.unibo;

import it.unibo.controller.Controller;

public class App {
    public static void main(String[] args) {
        new Controller().goToMenu();
    }
}
// BISOGNA MODIFICARE LE QUERY E I DAO PERCHE I NOMI DEI CAMPI NON SONO QUELLI DDELLE TABELLE
// BISOGNA SISTEMARE GESTIONE DATI CLIENTE, MODIFICANDO TUTTI I LOCALDATE IN DATE DI SQL
// SISTEMARE GLI INSERIMENTI COME FATTO IN CLIENTE, PERCHE SONO AUTO INCREMENT adesso devo fare la parte per il rider. Vorrei che ti ispirassi a quella del cliente, quando dal menu scelgo il rider voglio una schermata di accesso come quella del cliente, con login e registrazione. Quando si registra voglio un sistema simile a cliente per scegliere la zona geografica, con le opzioni della città presenti in 