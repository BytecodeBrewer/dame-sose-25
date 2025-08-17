import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BoardView extends JPanel {
    private final GameController gameController;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public BoardView(GameController gameController) {
        this.gameController = gameController;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int cellSize = Math.min(getWidth(), getHeight()) / 8;
                int y = e.getX() / cellSize;
                int x = e.getY() / cellSize;

                // Für's Erste leer lassen – Controller übernimmt Validierung
                if (selectedRow == -1 && selectedCol == -1) {
                    Piece piece = gameController.getBoard().getPieceAt(x, y); //getBoard() nicht definiert
                    if (piece != null) {
                        selectedRow = x;
                        selectedCol = y;
                        repaint();
                    }
                } else {
                    // statt board.isValidMove → Controller
                    boolean moved = gameController.makeMove(selectedRow, selectedCol, x, y);
                    if (moved) {
                        selectedRow = -1;
                        selectedCol = -1;
                        repaint();
                    }
                }
            }
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Board currentBoard = gameController.getBoard(); //Board noch undefiniert
        if (currentBoard == null) return;

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
                Piece piece = currentBoard.getPieceAt(i, j);
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