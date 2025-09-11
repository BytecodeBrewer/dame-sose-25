import javax.swing.*;
import java.awt.*;

public class MainView {
    private final GameController gameController;
    private final StartFrame startFrame;
    private JFrame frame;

    // NEU: Status & Fehler
    private JLabel currentPlayerLabel; // "Am Zug: ..."
    private JLabel errorLabel;         // Platzhalter für Fehlermeldungen
    private JLabel showWinnerLabel; // Platzhalter für Gewinneranzeige

    public MainView(StartFrame startFrame) {
        this.startFrame = startFrame;
        this.gameController = new GameController();
        this.gameController.setMainView(this);  // NEU: View beim Controller registrieren
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Dame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BoardView boardView = new BoardView(gameController);
        frame.add(boardView, BorderLayout.CENTER);

        // --- NEU: Status-/Fehlerleiste unten
        JPanel statusPanel = new JPanel(new BorderLayout(10, 0));
        currentPlayerLabel = new JLabel("Am Zug: " + gameController.getCurrentPlayer().getName());
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
        setCurrentPlayerDisplay(gameController.getCurrentPlayer().getName()); //Anzeige dafür wer Dran ist

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
    
    public void showWinnerDialog(String winnerName) {
        JDialog dlg = new JDialog(frame, "Spielende", true);
        dlg.setSize(300, 200);

        frame = new JFrame("Dame");
        showWinnerLabel = new JLabel("Gewonnen hat: " + winnerName);
        showWinnerLabel.setHorizontalAlignment(SwingConstants.CENTER);


        JPanel buttonPanel = new JPanel();
        JButton resetButton = new JButton("Neues Spiel");
        JButton endButton = new JButton("Beenden");
        buttonPanel.add(resetButton);
        buttonPanel.add(endButton);

        resetButton.addActionListener(_ -> {
            gameController.resetGame();
            clearError();
            dlg.dispose();
        });

        endButton.addActionListener(_ -> {
            dlg.dispose();
            frame.dispose();
            if (startFrame != null) {
                startFrame.setVisible(true);
            }
        });

        dlg.setLayout(new BorderLayout());
        dlg.add(showWinnerLabel, BorderLayout.CENTER);
        dlg.add(buttonPanel, BorderLayout.SOUTH);
        dlg.setLocationRelativeTo(frame);
        dlg.setVisible(true);
    }
    
}