import javax.swing.*;
import java.awt.*;

public class StartFrame extends JFrame {
    private String mode = null; // "debug" oder null für normalen Modus

    public StartFrame() {
        JFrame frame = new JFrame("Dame Spiel - Start");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel für Buttons
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Buttons anlegen
        JButton startButton = new JButton("Start Game");
        JButton debugButton = new JButton("Debug Mode");
        JButton endButton   = new JButton("End Game");
        
        // Optional: feste Größe (mittelgroß)
        Dimension buttonSize = new Dimension(150, 40);
        startButton.setMaximumSize(buttonSize);
        debugButton.setMaximumSize(buttonSize);
        endButton.setMaximumSize(buttonSize);

        // Zentrierung
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        debugButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        endButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Abstände einfügen
        panel.add(Box.createVerticalGlue()); // schiebt nach Mitte
        panel.add(startButton);
        panel.add(Box.createVerticalStrut(20)); // Abstand
        panel.add(debugButton);
        panel.add(Box.createVerticalStrut(20)); // Abstand
        panel.add(endButton);
        panel.add(Box.createVerticalGlue()); // schiebt nach Mitte

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

        frame.add(panel);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);

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
