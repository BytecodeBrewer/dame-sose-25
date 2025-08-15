import javax.swing.*;
import java.awt.*;

public class MainView {
    private final GameController gameController;
    private JFrame frame;

    public MainView(StartFrame startFrame) {
        this.gameController = new GameController();
        initialize(startFrame);
    }

    private void initialize(StartFrame startFrame) {
        frame = new JFrame("Dame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BoardView boardView = new BoardView(gameController);
        frame.add(boardView, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        JMenuItem resetItem = new JMenuItem("Reset Game");
        JMenuItem endItem = new JMenuItem("End Game");

        resetItem.addActionListener(e -> {
            gameController.resetGame();
            boardView.repaint();
        });

        endItem.addActionListener(e -> {
            frame.dispose();
            if (startFrame != null) {
                startFrame.setVisible(true);
            }
        });

        gameMenu.add(resetItem);
        gameMenu.add(endItem);
        menuBar.add(gameMenu);
        frame.setJMenuBar(menuBar);

        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

//another test
//C:\Users\tuan_\eclipse-workspace