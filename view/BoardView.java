import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class BoardView extends JPanel {
    private final GameController gameController;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public BoardView(GameController gameController) {
        this.gameController = gameController;

        // NEU: Repaint automatisch, wenn Controller sagt "Board hat sich geändert"
        gameController.addBoardChangeListener(() -> repaint()); // <--- EINZEILER

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int cellSize = Math.min(getWidth(), getHeight()) / 8;
                int y = e.getX() / cellSize; // <— Klarheit: Spalte
                int x = e.getY() / cellSize; // <— Zeile

                // NEU: Klick-Info an Controller (ohne Verhaltensänderung)
                gameController.onSquareClicked(x, y); // <--- EINZEILER

                if (selectedRow == -1 && selectedCol == -1) {
                    Piece piece = gameController.getBoard().getPieceAt(x, y);
                    if (piece != null) {
                        selectedRow = x;
                        selectedCol = y;
                        repaint();
                    }
                } else {
                    // Prüfe ob auf das gleiche Feld geklickt wurde
                    if (x == selectedRow && y == selectedCol) {
                        selectedRow = -1;
                        selectedCol = -1;
                        repaint();
                        return;
                    }
                    
                    // Prüfe ob auf ein besetztes Feld geklickt wurde
                    if (!gameController.getBoard().isFieldFree(x, y)) {
                        selectedRow = -1;
                        selectedCol = -1;
                        repaint();
                        return;
                    }

                    boolean moved = gameController.makeMove(selectedRow, selectedCol, x, y);
                    gameController.onMoveAttempt(selectedRow, selectedCol, x, y, moved); // <--- EINZEILER

                    if (moved) {
                        selectedRow = -1;
                        selectedCol = -1;
                        repaint(); // (bleibt)
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Board currentBoard = gameController.getBoard();
        if (currentBoard == null) return;

        int size = 8;
        int tileSize = Math.min(getWidth(), getHeight()) / size;

        // Brett + Steine
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                g.setColor(((i + j) % 2 == 0) ? Color.LIGHT_GRAY : Color.DARK_GRAY);
                g.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);

                Piece piece = currentBoard.getPieceAt(i, j);
                if (piece != null) {
                    if (piece.getColor() == Piece.PieceColor.BLACK) {
                        g.setColor(Color.BLACK);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillOval(j * tileSize + 10, i * tileSize + 10, tileSize - 20, tileSize - 20);
                }
            }
        }

        // MARKIERUNG: jetzt am ENDE (nachdem Brett/Steine gezeichnet wurden)
        if (selectedRow >= 0 && selectedCol >= 0) {
            g.setColor(new Color(255, 255, 0, 120)); // halbtransparent
            g.fillRect(selectedCol * tileSize, selectedRow * tileSize, tileSize, tileSize);
            g.setColor(Color.RED);
            g.drawRect(selectedCol * tileSize, selectedRow * tileSize, tileSize - 1, tileSize - 1);
        }
    }
}
