import javax.swing.*;
import java.awt.*;

public class MainView {
    private JFrame frame;
    private Board board;
    private BoardView boardView;

    public MainView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Dame Spiel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        board = new Board();
        boardView = new BoardView(board, this);
        frame.add(boardView, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem resetItem = new JMenuItem("Reset Game");
        JMenuItem endItem = new JMenuItem("End Game");
        gameMenu.add(resetItem);
        gameMenu.add(endItem);
        menuBar.add(gameMenu);
        frame.setJMenuBar(menuBar);

        frame.setVisible(true);
    }

    public void updateBoard() {
        boardView.repaint();
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainView());
    }
}