import javax.swing.*;
import java.awt.*;

// Hauptansicht des Spiels
// Verwaltet das Spielbrett, Statusanzeigen und Debug-Funktionen
public class MainView extends JPanel implements GamePresenter{
    private final GameController gameController;
    private BoardView boardView;
    private JLabel currentPlayerLabel;
    private JLabel errorLabel;
    private JFrame frame;
    private JPanel pieceSelectionPanel;


    public MainView(GameController gameController) {
        this.gameController = gameController;
        initialize();
        this.gameController.setPresenter(this);
        gameController.startGame();
    }

    // Initialisiert die Hauptansicht:
    // - Spielbrett in der Mitte
    // - Statusleiste unten (Spieler + Fehler)
    // - Menüleiste oben
    private void initialize() {
        frame = createMainFrame();

        boardView = new BoardView(gameController);
        frame.add(boardView, BorderLayout.CENTER);

        frame.add(createStatusPanel(), BorderLayout.SOUTH);

        if (gameController.getMode() == GameController.Mode.DEBUG) {
            initDebugPanel();
        }

        frame.setJMenuBar(createMenuBar());

        configureAndShowFrame();
        setCurrentPlayerDisplay(gameController.getCurrentPlayer().getName());
    }

    // Erstellt das Hauptfenster
    private JFrame createMainFrame() {
        JFrame f = new JFrame("Dame");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return f;
    }

    // Baut die Statusleiste mit Spieleranzeige und Fehlermeldung
    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout(10, 0));

        var vs = gameController.getViewState();
        currentPlayerLabel = new JLabel("Am Zug: " + vs.currentPlayerName);
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);

        statusPanel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        statusPanel.add(currentPlayerLabel, BorderLayout.WEST);
        statusPanel.add(errorLabel, BorderLayout.EAST);

        return statusPanel;
    }

    // Baut die Menüleiste
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        JMenuItem endItem = new JMenuItem("Runde beenden");
        endItem.addActionListener(z -> {
            frame.dispose();
            SwingUtilities.invokeLater(StartFrame::new);
        });

        gameMenu.add(endItem);
        menuBar.add(gameMenu);

        return menuBar;
    }

    // Setzt Standardgröße und zeigt den Frame
    private void configureAndShowFrame() {
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }



    // Erstellt Debug-Kontrollpanel mit:
    // - Button für weiße Steine
    // - Button für schwarze Steine
    // - Start-Button zum Spielbeginn
    // Ermöglicht manuelle Platzierung von Spielsteinen
    private void initDebugPanel() {
    pieceSelectionPanel = new JPanel(new GridLayout(1, 3, 10, 0));
    pieceSelectionPanel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

    JButton setWhitePieceButton = createDebugButton("White");
    JButton setBlackPieceButton = createDebugButton("Black");
    JButton startButton         = createDebugButton("Start Game");

    addDebugButtonListeners(setWhitePieceButton, setBlackPieceButton, startButton);

    pieceSelectionPanel.add(setWhitePieceButton);
    pieceSelectionPanel.add(setBlackPieceButton);
    pieceSelectionPanel.add(startButton);

    frame.add(pieceSelectionPanel, BorderLayout.NORTH);
    }

    // Erstellt einen einheitlichen Button für Debug-Panel
    private JButton createDebugButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(75, 75, 75));
        button.setForeground(Color.WHITE);
        return button;
    }

    // Fügt den Buttons ihre Listener hinzu
    private void addDebugButtonListeners(JButton whiteBtn, JButton blackBtn, JButton startBtn) {
        whiteBtn.addActionListener(e -> {
            boardView.setPlacingPieceColor("WHITE");
            highlightActiveButton(whiteBtn, blackBtn);
        });

        blackBtn.addActionListener(r-> {
            boardView.setPlacingPieceColor("BLACK");
            highlightActiveButton(blackBtn, whiteBtn);
        });

        startBtn.addActionListener(z -> onStartGame(whiteBtn, blackBtn, startBtn));
    }

    // Hebt den aktiven Button hervor
    private void highlightActiveButton(JButton active, JButton inactive) {
        active.setBackground(new Color(150, 150, 150));
        inactive.setBackground(new Color(75, 75, 75));
        clearError();
        frame.repaint();
    }



    // Wird beim Start eines Debug-Spiels aufgerufen
    // - Sperrt weitere Figurenplatzierung
    // - Deaktiviert Debug-Buttons
    // - Entfernt Debug-Panel
    private void onStartGame(JButton whiteBtn, JButton blackBtn, JButton startBtn) {

        boardView.lockPlacement();

        killButton(whiteBtn);
        killButton(blackBtn);
        killButton(startBtn);
        
        frame.remove(pieceSelectionPanel);
        frame.revalidate();
        frame.repaint();

        clearError();
    }

    // Zeigt Dialog mit Gewinner an
    // Bietet Option zum Neustart oder Beenden
    private static void killButton(AbstractButton b) {
        for (var al : b.getActionListeners()) b.removeActionListener(al);
        b.setEnabled(false);
        b.setFocusable(false);
    }

    // Aktualisiert die Anzeige des aktuellen Spielers
    // Wird beim Spielerwechsel aufgerufen
    public void setCurrentPlayerDisplay(String name) {
        if (currentPlayerLabel != null) currentPlayerLabel.setText("Am Zug: " + name);
    }

    // Aktualisiert die gesamte Spielansicht:
    // - Aktuellen Spieler
    // - Fehlermeldungen
    // - Spielbrett
    public void render(GameController.ViewState vs) {
        currentPlayerLabel.setText("Am Zug: " + vs.currentPlayerName);
        errorLabel.setText(vs.errorMessage == null ? "" : vs.errorMessage);
        boardView.repaint();
    }

    // Zeigt eine Fehlermeldung in der Statusleiste an
    // Null-Messages werden als leerer String angezeigt
    public void showError(String message) {
        errorLabel.setText(message == null ? "" : message);
    }

    // Löscht die aktuelle Fehlermeldung
    // Setzt die Anzeige auf Leerzeichen zurück
    public void clearError() {
        if (errorLabel != null) errorLabel.setText(" ");
    }
    
    // Zeigt beim Spielende ein Dialogfenster, wer gewonnen hat
    // Das Spiel kann durch den OK Button beendet werden
    public void showWinnerDialog(String playerName) {
        JDialog dlg = new JDialog(frame, "Spielende", true);
        dlg.setSize(300, 200);

        JLabel showWinnerLabel = new JLabel("Gewonnen hat: " + playerName, SwingConstants.CENTER);

        

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton endButton = new JButton("OK");
        buttonPanel.add(endButton);

        endButton.addActionListener(end -> {
            dlg.dispose();
            frame.dispose();
            SwingUtilities.invokeLater(() -> new StartFrame());
        });

        dlg.setLayout(new BorderLayout());
        dlg.add(showWinnerLabel, BorderLayout.CENTER);
        dlg.add(buttonPanel, BorderLayout.SOUTH);
        dlg.setLocationRelativeTo(frame);
        dlg.setVisible(true);
    }
    
}