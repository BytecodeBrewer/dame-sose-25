import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BoardView extends JPanel {
    private Board board;
    // Das MainView-Objekt, um die Ansicht zu aktualisieren
    private int selectedX = -1, selectedY = -1;

    public BoardView(Board board, MainView mainView) {
        this.board = board;

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int size = 8;
                int tileSize = Math.min(getWidth(), getHeight()) / size;
                int x = e.getY() / tileSize;
                int y = e.getX() / tileSize;

                if (selectedX == -1 && board.getPieceAt(x, y) != null) {
                    selectedX = x;
                    selectedY = y;
                } else if (selectedX != -1) {
                    if (board.isValidMove(selectedX, selectedY, x, y)) {
                        board.movePiece(selectedX, selectedY, x, y);
                    }
                    selectedX = -1;
                    selectedY = -1;
                }
                repaint();
            }
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (board == null) return;

        int size = 8;
        int tileSize = Math.min(getWidth(), getHeight()) / size;

        // Schachbrett zeichnen
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ((i + j) % 2 == 0) {
                    g.setColor(Color.LIGHT_GRAY);
                } else {
                    g.setColor(Color.DARK_GRAY);
                }
                g.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);

                // Steine zeichnen
                Piece piece = board.getPieceAt(i, j);
                if (piece != null) {
                    if ("black".equals(piece.getColor())) {
                        g.setColor(Color.BLACK);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillOval(j * tileSize + 10, i * tileSize + 10, tileSize - 20, tileSize - 20);
                }
            }
        }
    }
}

//git testpush