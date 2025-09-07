import javax.swing.*;
import java.awt.*;

public class MainView {
    private final GameController gameController;
    private JFrame frame;

    public MainView(GameController gameController) {
        this.gameController = gameController;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Dame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BoardView boardView = new BoardView(gameController);
        frame.add(boardView, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        JMenuItem resetItem = new JMenuItem("Reset Game");
        JMenuItem endItem = new JMenuItem("End Game");

        resetItem.addActionListener(_ -> {
            gameController.resetGame();
            boardView.repaint();
        });

        endItem.addActionListener(_ -> {
            frame.dispose();
            // Hier könnte man eine Meldung anzeigen, dass das Spiel beendet wurde
        });

        gameMenu.add(resetItem);
        gameMenu.add(endItem);
        menuBar.add(gameMenu);
        frame.setJMenuBar(menuBar);

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    public void show() {
        if (frame != null) {
            frame.setVisible(true);
        } else {
            initialize();
        }
    }
}