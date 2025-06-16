package it.unibo;

import javax.swing.SwingUtilities;


import it.unibo.view.MainFrame;

public class App {
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
        new MainFrame().setVisible(true);
        });
    
    }
}
