import javax.swing.*;
import java.awt.*;

public class StartFrame extends JFrame {
    private String mode = null; // "debug" oder null für normalen Modus

    public StartFrame() {
        setTitle("Dame Spiel - Start");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(rootPaneCheckingEnabled);

        // Buttons
        JButton startButton = new JButton("Start Game");
        JButton debugButton  = new JButton("Debug Mode");
        JButton endButton   = new JButton("End Game");
        startButton.setBackground(new Color(75,75,75));
        startButton.setForeground(Color.WHITE);
        debugButton.setBackground(new Color(75,75,75));
        debugButton.setForeground(Color.WHITE);
        endButton.setBackground(new Color(75,75,75));
        endButton.setForeground(Color.WHITE);


        // Button-Actions
        startButton.addActionListener(_ -> {
            new MainView(this, null);
            setVisible(false);
        });

        debugButton.addActionListener(_ -> {
            new MainView(this, "debug");
            setVisible(false);
        });

        endButton.addActionListener(_ -> System.exit(0));

        // Panel für Buttons
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(startButton);
        panel.add(debugButton);
        panel.add(endButton);

        add(panel, BorderLayout.CENTER);
    }

    // Wenn du das später mal brauchst, kannst du so das Menü starten:
    public void initialize() {
        // Setup für StartFrame, falls du noch extra Logik einfügen willst
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Dame Spiel - Start");
        setSize(800, 600);
        setLayout(new BorderLayout());

    }

    public String getMode() {
        return mode; // Hier kannst du den Modus zurückgeben, falls benötigt
    }

    public void display() {
        setVisible(true);
    }

}
