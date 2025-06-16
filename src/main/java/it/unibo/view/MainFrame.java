package it.unibo.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainFrame extends JFrame {
    
    private static final Color PRIMARY_COLOR = new Color(102, 126, 234);
    private static final Color SECONDARY_COLOR = new Color(118, 75, 162);
    private static final Color ACCENT_COLOR = new Color(255, 107, 107);
    private static final Color WHITE = Color.WHITE;
    private static final Color DARK_GRAY = new Color(51, 51, 51);
    
    private JPanel mainPanel;
    private JLabel logoLabel;
    private JLabel taglineLabel;
    
    public MainFrame() {
        initializeFrame();
        createComponents();
        layoutComponents();
        addEventHandlers();
    }
    
    private void initializeFrame() {
        setTitle("PL8 - Food Delivery Platform");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Icona dell'applicazione
        try {
            // Puoi sostituire con un'icona personalizzata
            setIconImage(createLogoIcon());
        } catch (Exception e) {
            System.err.println("Impossibile caricare l'icona: " + e.getMessage());
        }
    }
    
    private void createComponents() {
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR,
                    getWidth(), getHeight(), SECONDARY_COLOR
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        // Logo
        logoLabel = new JLabel("PL8", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 72));
        logoLabel.setForeground(WHITE);
        
        // Tagline
        taglineLabel = new JLabel("La tua piattaforma di delivery preferita", SwingConstants.CENTER);
        taglineLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        taglineLabel.setForeground(WHITE);
    }
    
    private void layoutComponents() {
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Logo
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(logoLabel, gbc);
        
        // Tagline
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 50, 0);
        mainPanel.add(taglineLabel, gbc);
        
        // Panel per i bottoni di accesso
        JPanel buttonPanel = createButtonPanel();
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 30, 30));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        // Bottone Cliente
        JButton clienteBtn = createAccessButton(
            "👤 CLIENTE", 
            "Ordina dal tuo ristorante preferito",
            new Color(76, 175, 80)
        );
        
        // Bottone Amministratore
        JButton adminBtn = createAccessButton(
            "⚙️ AMMINISTRATORE", 
            "Gestisci la piattaforma",
            new Color(255, 152, 0)
        );
        
        // Bottone Rider
        JButton riderBtn = createAccessButton(
            "🚴 RIDER", 
            "Effettua le consegne",
            new Color(33, 150, 243)
        );
        
        // Bottone Ristorante
        JButton ristoranteBtn = createAccessButton(
            "🍽️ RISTORANTE", 
            "Gestisci il tuo locale",
            new Color(156, 39, 176)
        );
        
        panel.add(clienteBtn);
        panel.add(adminBtn);
        panel.add(riderBtn);
        panel.add(ristoranteBtn);
        
        return panel;
    }
    
    private JButton createAccessButton(String title, String description, Color color) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background con gradiente
                if (getModel().isPressed()) {
                    g2d.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, color.brighter(),
                        0, getHeight(), color
                    );
                    g2d.setPaint(gradient);
                } else {
                    g2d.setColor(color);
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Ombra interna per effetto pressed
                if (getModel().isPressed()) {
                    g2d.setColor(new Color(0, 0, 0, 50));
                    g2d.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 18, 18);
                }
                
                // Testo
                g2d.setColor(WHITE);
                FontMetrics titleMetrics = g2d.getFontMetrics(new Font("Arial", Font.BOLD, 16));
                FontMetrics descMetrics = g2d.getFontMetrics(new Font("Arial", Font.PLAIN, 12));
                
                int titleY = (getHeight() / 2) - 10;
                int descY = titleY + 25;
                
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                int titleX = (getWidth() - titleMetrics.stringWidth(title)) / 2;
                g2d.drawString(title, titleX, titleY);
                
                g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                int descX = (getWidth() - descMetrics.stringWidth(description)) / 2;
                g2d.drawString(description, descX, descY);
            }
        };
        
        button.setPreferredSize(new Dimension(200, 120));
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Tooltip
        button.setToolTipText(description);
        
        return button;
    }
    
    private void addEventHandlers() {
        // Trova tutti i bottoni nel buttonPanel e aggiungi gli event handlers
        findButtons(mainPanel).forEach(button -> {
            String text = getButtonTitle(button);
            button.addActionListener(e -> handleButtonClick(text));
        });
    }
    
    private java.util.List<JButton> findButtons(Container container) {
        java.util.List<JButton> buttons = new java.util.ArrayList<>();
        for (Component component : container.getComponents()) {
            if (component instanceof JButton) {
                buttons.add((JButton) component);
            } else if (component instanceof Container) {
                buttons.addAll(findButtons((Container) component));
            }
        }
        return buttons;
    }
    
    private String getButtonTitle(JButton button) {
        // Estrae il titolo dal rendering personalizzato
        String tooltip = button.getToolTipText();
        if (tooltip != null) {
            if (tooltip.contains("ristorante")) return "RISTORANTE";
            if (tooltip.contains("consegne")) return "RIDER";
            if (tooltip.contains("piattaforma")) return "AMMINISTRATORE";
            if (tooltip.contains("Ordina")) return "CLIENTE";
        }
        return "UNKNOWN";
    }
    
    private void handleButtonClick(String userType) {
        switch (userType) {
            case "CLIENTE":
                openClienteView();
                break;
            case "AMMINISTRATORE":
                openAdminView();
                break;
            case "RIDER":
                openRiderView();
                break;
            case "RISTORANTE":
                openRistoranteView();
                break;
            default:
                showMessage("Funzionalità non ancora implementata");
        }
    }
    
    private void openClienteView() {
        showMessage("Apertura interfaccia Cliente...");
        // Qui integrerai con il tuo Controller per aprire la vista cliente
        // controller.userRequestedClienteView();
    }
    
    private void openAdminView() {
        showMessage("Apertura pannello Amministratore...");
        // Qui integrerai con il Controller per aprire la vista admin
        // controller.userRequestedAdminView();
    }
    
    private void openRiderView() {
        showMessage("Apertura interfaccia Rider...");
        // Qui integrerai con il Controller per aprire la vista rider
        // controller.userRequestedRiderView();
    }
    
    private void openRistoranteView() {
        showMessage("Apertura pannello Ristorante...");
        // Qui integrerai con il Controller per aprire la vista ristorante
        // controller.userRequestedRistoranteView();
    }
    
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "PL8", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private Image createLogoIcon() {
        // Crea un'icona semplice per l'applicazione
        BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Background circolare
        g2d.setColor(PRIMARY_COLOR);
        g2d.fillOval(2, 2, 28, 28);
        
        // Testo PL8
        g2d.setColor(WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "PL8";
        int x = (32 - fm.stringWidth(text)) / 2;
        int y = (32 + fm.getAscent()) / 2;
        g2d.drawString(text, x, y);
        
        g2d.dispose();
        return icon;
    }
}
