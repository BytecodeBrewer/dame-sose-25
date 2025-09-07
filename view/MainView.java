import javax.swing.*;
import java.awt.*;

public class MainView {
    private final GameController gameController;
    private final StartFrame startFrame;
    private JFrame frame;

    // NEU: Status & Fehler
    private JLabel currentPlayerLabel; // "Am Zug: ..."
    private JLabel errorLabel;         // Platzhalter für Fehlermeldungen

    public MainView(StartFrame startFrame) {
        this.startFrame = startFrame;
        this.gameController = new GameController();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Dame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BoardView boardView = new BoardView(gameController);
        frame.add(boardView, BorderLayout.CENTER);

        // --- NEU: Status-/Fehlerleiste unten
        JPanel statusPanel = new JPanel(new BorderLayout(10, 0));
        currentPlayerLabel = new JLabel("Am Zug: —");
        errorLabel = new JLabel(" "); // leerer Platzhalter
        errorLabel.setForeground(Color.RED);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        statusPanel.add(currentPlayerLabel, BorderLayout.WEST);
        statusPanel.add(errorLabel, BorderLayout.EAST);
        frame.add(statusPanel, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        JMenuItem resetItem = new JMenuItem("Reset Game");
        JMenuItem endItem   = new JMenuItem("End Game");

        resetItem.addActionListener(_ -> {
            gameController.resetGame();
            // Option: Fehlermeldung zurücksetzen & Spieleranzeige ggf. aktualisieren
            clearError();
            // setCurrentPlayerDisplay("Weiß"); // wenn ihr den Spielerzustand habt
            frame.repaint();
        });

        endItem.addActionListener(_ -> {
            frame.dispose();
            if (startFrame != null) {
                startFrame.setVisible(true);
            }
        });

        gameMenu.add(resetItem);
        gameMenu.add(endItem);
        menuBar.add(gameMenu);
        frame.setJMenuBar(menuBar);

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // ---- OPTIONAL: Demo-Leuchtziele zum Testen (später Controller ruft boardView.setLegalTargets(...))
        // java.util.List<java.awt.Point> demo = java.util.List.of(new java.awt.Point(2,3), new java.awt.Point(3,4));
        // boardView.setLegalTargets(demo);
        // setCurrentPlayerDisplay("Weiß");
    }

    // NEU: öffentliche Helfer, damit Controller/andere Klassen Text setzen können
    public void setCurrentPlayerDisplay(String name) {
        if (currentPlayerLabel != null) currentPlayerLabel.setText("Am Zug: " + name);
    }

    public void showError(String message) {
        if (errorLabel != null) errorLabel.setText((message != null && !message.isEmpty()) ? message : " ");
    }

    public void clearError() {
        if (errorLabel != null) errorLabel.setText(" ");
    }
}