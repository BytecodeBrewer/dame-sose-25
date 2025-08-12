import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.BorderLayout;

public class StartFrame extends JFrame {
 
    public StartFrame() {
        setTitle("Dame Spiel - Start");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton startButton = new JButton("Start Game");
        JButton endButton = new JButton("End Game");

        startButton.addActionListener(_ -> {
            new MainView(); // Erstellt MainView ohne Parameter
            setVisible(false);
        });

        endButton.addActionListener(_ -> System.exit(0));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));
        panel.add(startButton);
        panel.add(endButton);

        add(panel, BorderLayout.CENTER);
    }

    public void initialize() {
        // Method to set up the start frame
    }

    public void display() {
        // Method to display the start frame
    }

    // Additional methods and properties can be added as needed
    
}
