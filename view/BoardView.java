import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class BoardView extends JPanel {
    private final GameController gameController;
    private int selectedRow = -1;
    private int selectedCol = -1;;

    // Nur für Debug/Setup: Steine platzieren
    private MouseListener placementListener;

    // Sperre, damit nach Spielstart kein Platzieren mehr möglich ist
    private boolean placementLocked = false;
    

    public BoardView(GameController gameController) {
        this.gameController = gameController;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int cellSize = Math.min(getWidth(), getHeight()) / 8;
                int col = e.getX() / cellSize; // Spalte
                int row = e.getY() / cellSize; // Zeile
            
                if (row < 0 || row >= 8 || col < 0 || col >= 8) return;

                if (selectedRow == -1) {
                // Erste Klick: Nur selektieren, wenn dort eine Figur ist
                GameController.ViewState vs = gameController.getViewState();
                GameController.Cell cell = vs.grid[row][col];
                if (cell != GameController.Cell.EMPTY) {
                    selectedRow = row;
                    selectedCol = col;
                    repaint();
                }
                } else {
                // Zweiter Klick: Zug versuchen
                boolean moved = gameController.makeMove(selectedRow, selectedCol, row, col);

                // Auswahl immer zurücksetzen (optisch klar)
                selectedRow = -1;
                selectedCol = -1;

                // Egal ob true/false: neu zeichnen (bei false zeigt MainView ggf. Fehler)
                repaint();
                }
                
            }
        });
    }

    public void setPlacingPieceColor(String color) {
        if (placementLocked) return; // nach Spielstart ignorieren

        // Vorherigen Platzier-Listener abklemmen (sonst stapelst du sie!)
        detachPlacementListener();

        // Neuen, farbspezifischen Platzier-Listener setzen
        this.placementListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int cellSize = Math.min(getWidth(), getHeight()) / 8;
                int c = e.getX() / cellSize;
                int r = e.getY() / cellSize;
                if ("WHITE".equals(color)) {
                    gameController.setWhitePiece(r, c);
                } else if ("BLACK".equals(color)) {
                    gameController.setBlackPiece(r, c);
                }
                repaint();
            }
        };
        addMouseListener(this.placementListener);
    }

    public void lockPlacement() {
        this.placementLocked = true;
        detachPlacementListener();
        repaint();
    }

    private void detachPlacementListener() {
        if (this.placementListener != null) {
            removeMouseListener(this.placementListener);
            this.placementListener = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        GameController.ViewState vs = gameController.getViewState();
        if (vs == null) return;

        int size = vs.grid.length;
        int tileSize = Math.min(getWidth(), getHeight()) / size;

        // 1) Brett + Auswahl (gelb) + Stein (in dieser Reihenfolge)
          for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
            // Feld
            g.setColor(((i + j) % 2 == 0) ? Color.LIGHT_GRAY : Color.DARK_GRAY);
            g.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);

            // Figur (aus Snapshot)
            GameController.Cell cell = vs.grid[i][j];
            if (cell != GameController.Cell.EMPTY) {
                boolean isWhite = (cell == GameController.Cell.WM || cell == GameController.Cell.WK);
                boolean isMan   = (cell == GameController.Cell.WM || cell == GameController.Cell.BM);

                g.setColor(isWhite ? Color.WHITE : Color.BLACK);
                g.fillOval(j * tileSize + 10, i * tileSize + 10, tileSize - 20, tileSize - 20);

                if (!isMan) { // Dame
                g.setFont(g.getFont().deriveFont(Font.BOLD, Math.max(14f, tileSize * 0.6f)));
                g.setColor(isWhite ? Color.BLACK : Color.WHITE);
                String text = "\u2655";
                FontMetrics fm = g.getFontMetrics();
                int tx = j * tileSize + (tileSize - fm.stringWidth(text)) / 2;
                int ty = i * tileSize + (tileSize + fm.getAscent() - fm.getDescent()) / 2;
                g.drawString(text, tx, ty);
                }
            }
            }
        }

        // 2) Optional: Roter Rand ÜBER allem (damit klar sichtbar)
        if (selectedRow >= 0 && selectedCol >= 0) {
            g.setColor(new Color(255, 255, 0, 80));
            g.fillRect(selectedCol * tileSize, selectedRow * tileSize, tileSize, tileSize);

            g.setColor(Color.RED);
            g.drawRect(selectedCol * tileSize, selectedRow * tileSize, tileSize - 1, tileSize - 1);
        }
    }
}