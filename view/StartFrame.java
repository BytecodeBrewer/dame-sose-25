import javax.swing.*;
import java.awt.*;

public class StartFrame extends JFrame {

    public StartFrame() {
        this.setTitle("Dame Spiel - Start");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        startButton.addActionListener(s -> {
            GameController controller = new GameController(GameController.Mode.NORMAL);
            MainView main = new MainView(controller);
            main.setVisible(true);
            setVisible(false);
        });

        debugButton.addActionListener(d -> {
            GameController controller = new GameController(GameController.Mode.DEBUG);
            MainView main = new MainView(controller);
            main.setVisible(true);
            setVisible(false);
        });
        endButton.addActionListener(e -> System.exit(0));

        this.add(panel);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);

    }

}
