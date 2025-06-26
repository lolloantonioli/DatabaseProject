package it.unibo;

import it.unibo.controller.Controller;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Avvio applicazione PL8...");
                new Controller().goToMenu();
                System.out.println("Applicazione avviata con successo");
            } catch (Exception e) {
                System.err.println("Errore durante l'avvio:");
                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null, 
                    "Errore: " + e.getMessage(), 
                    "Errore Avvio", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
// BISOGNA MODIFICARE LE QUERY E I DAO PERCHE I NOMI DEI CAMPI NON SONO QUELLI DDELLE TABELLE
// BISOGNA SISTEMARE GESTIONE DATI CLIENTE, MODIFICANDO TUTTI I LOCALDATE IN DATE DI SQL
// SISTEMARE GLI INSERIMENTI COME FATTO IN CLIENTE, PERCHE SONO AUTO INCREMENT