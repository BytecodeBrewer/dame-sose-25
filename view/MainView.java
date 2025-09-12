import javax.swing.*;
import java.awt.*;


public class MainView extends JPanel implements GamePresenter{
    private final GameController gameController;
    private BoardView boardView;
    private JLabel currentPlayerLabel;   // "Am Zug: ..."
    private JLabel errorLabel;
    private JLabel showWinnerLabel; // Platzhalter für Gewinneranzeige
    private boolean gameStarted = false;
    private JFrame frame;
    private JPanel pieceSelectionPanel;


    public MainView(GameController gameController) {
        this.gameController = gameController;
        initialize();   // kein mode-Parameter mehr
        this.gameController.setPresenter(this);
        gameController.startGame();
    }

    private void initialize() {
        
        frame = new JFrame("Dame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        boardView = new BoardView(gameController);
        frame.add(boardView, BorderLayout.CENTER);

        // --- NEU: Status-/Fehlerleiste unten
        JPanel statusPanel = new JPanel(new BorderLayout(10, 0));
        var vs = gameController.getViewState();
        currentPlayerLabel = new JLabel("Am Zug: " + vs.currentPlayerName);
        errorLabel = new JLabel(" "); // leerer Platzhalter
        errorLabel.setForeground(Color.RED);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        statusPanel.add(currentPlayerLabel, BorderLayout.WEST);
        statusPanel.add(errorLabel, BorderLayout.EAST);
        frame.add(statusPanel, BorderLayout.SOUTH);
        
        if(gameController.getMode() == GameController.Mode.DEBUG) {
            pieceSelectionPanel = new JPanel(new GridLayout(1, 2, 10, 0));
            JButton startButton = new JButton("Start Game");
            JButton setWhitePieceButton  = new JButton("White");
            JButton setBlackPieceButton   = new JButton("Black");
            startButton.setBackground(new Color(75,75,75));
            startButton.setForeground(Color.WHITE);
            setWhitePieceButton.setBackground(new Color(75,75,75));
            setWhitePieceButton.setForeground(Color.WHITE);
            setBlackPieceButton.setBackground(new Color(75,75,75));
            setBlackPieceButton.setForeground(Color.WHITE);
            pieceSelectionPanel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            pieceSelectionPanel.add(setWhitePieceButton);
            pieceSelectionPanel.add(setBlackPieceButton);
            pieceSelectionPanel.add(startButton);
            frame.add(pieceSelectionPanel, BorderLayout.NORTH);

            setWhitePieceButton.addActionListener(_ -> {
                if (gameStarted) return; // Guard
                else { boardView.setPlacingPieceColor("WHITE");
                setWhitePieceButton.setBackground(new Color(150,150,150));
                setBlackPieceButton.setBackground(new Color(75,75,75));
                clearError();
                frame.repaint();
                }
                
            });
            setBlackPieceButton.addActionListener(_ -> {
                if (gameStarted) return; // Guard
                else {
                boardView.setPlacingPieceColor("BLACK");
                setBlackPieceButton.setBackground(new Color(150,150,150));
                setWhitePieceButton.setBackground(new Color(75,75,75));
                clearError();
                frame.repaint();
                }
            });
            startButton.addActionListener(_ -> onStartGame(setWhitePieceButton, setBlackPieceButton, startButton));
        }

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
        });

        gameMenu.add(resetItem);
        gameMenu.add(endItem);
        menuBar.add(gameMenu);
        frame.setJMenuBar(menuBar);

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        setCurrentPlayerDisplay(gameController.getCurrentPlayer().getName()); //Anzeige dafür wer Dran ist

    }

    private void onStartGame(JButton whiteBtn, JButton blackBtn, JButton startBtn) {
        if (gameStarted) return;
        gameStarted = true;

        // 1) Platzieren auf dem Board endgültig sperren
        boardView.lockPlacement();

        // 2) Buttons zuverlässig abschalten
        killButton(whiteBtn);
        killButton(blackBtn);
        killButton(startBtn);
        
        // 3) Panel mit den Buttons entfernen
        frame.remove(pieceSelectionPanel);
        frame.revalidate();
        frame.repaint();

        clearError();
    }

    private static void killButton(AbstractButton b) {
        for (var al : b.getActionListeners()) b.removeActionListener(al);
        b.setEnabled(false);
        b.setFocusable(false);
    }


    // NEU: öffentliche Helfer, damit Controller/andere Klassen Text setzen können
    public void setCurrentPlayerDisplay(String name) {
        if (currentPlayerLabel != null) currentPlayerLabel.setText("Am Zug: " + name);
    }

    @Override
    public void render(GameController.ViewState vs) {
        currentPlayerLabel.setText("Am Zug: " + vs.currentPlayerName);
        errorLabel.setText(vs.errorMessage == null ? "" : vs.errorMessage);
        boardView.repaint();
    }

    @Override
    public void showError(String message) {
        errorLabel.setText(message == null ? "" : message);
        // optional: JOptionPane.showMessageDialog(this, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }

    public void clearError() {
        if (errorLabel != null) errorLabel.setText(" ");
    }
    
    public void showWinnerDialog(String playerName) {
        JDialog dlg = new JDialog(frame, "Spielende", true);
        dlg.setSize(300, 200);

        showWinnerLabel = new JLabel("Gewonnen hat: " + playerName, SwingConstants.CENTER);

        

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton endButton = new JButton("OK");
        buttonPanel.add(endButton);

        endButton.addActionListener(_ -> {
            dlg.dispose();
            frame.dispose();
        });

        dlg.setLayout(new BorderLayout());
        dlg.add(showWinnerLabel, BorderLayout.CENTER);
        dlg.add(buttonPanel, BorderLayout.SOUTH);
        dlg.setLocationRelativeTo(frame);
        dlg.setVisible(true);
    }
    
}