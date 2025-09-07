import javax.swing.*;
import java.awt.*;


public class StartFrame extends JFrame {

    public StartFrame() {
        setTitle("Dame Spiel - Start");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(rootPaneCheckingEnabled);

        // Buttons
        JButton startButton = new JButton("Start Game");
        JButton loadButton  = new JButton("Load Game");
        JButton endButton   = new JButton("End Game");
        startButton.setBackground(new Color(75,75,75));
        startButton.setForeground(Color.WHITE);
        loadButton.setBackground(new Color(75,75,75));
        loadButton.setForeground(Color.WHITE);
        endButton.setBackground(new Color(75,75,75));
        endButton.setForeground(Color.WHITE);


        // Button-Actions
        startButton.addActionListener(_ -> {
            new MainView(this);
            setVisible(false);
        });

        loadButton.addActionListener(_ -> {
            JOptionPane.showMessageDialog(this, "Load Game not implemented yet!");
        });

        endButton.addActionListener(_ -> System.exit(0));

        // Panel für Buttons
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(startButton);
        panel.add(loadButton);
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

    public void display() {
        setVisible(true);
    }

    // Einstiegspunkt
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartFrame startFrame = new StartFrame();
            startFrame.display();
        });
    }
}
