import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class BoardView extends JPanel {
    private final GameController gameController;
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

                gameController.onCellClick(row, col);
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

                int baseX = j * tileSize + 10;
                int baseY = i * tileSize + 10;
                int stoneSize = tileSize - 20;

                // Grauer Rand (etwas größerer Kreis)
                g.setColor(Color.GRAY);
                g.fillOval(baseX - 2, baseY - 2, stoneSize + 4, stoneSize + 4);

                // Eigentliche Spielfigur
                g.setColor(isWhite ? Color.WHITE : Color.BLACK);
                g.fillOval(baseX, baseY, stoneSize, stoneSize);

                // Wenn Dame → zweiten Stein leicht versetzt zeichnen
                if (!isMan) {
                    int offset = Math.max(4, tileSize / 12); // Versatz nach oben
                    // oberer Rand
                    g.setColor(Color.GRAY);
                    g.fillOval(baseX - 2, baseY - offset - 2, stoneSize + 4, stoneSize + 4);

                    // oberer Stein
                    g.setColor(isWhite ? Color.WHITE : Color.BLACK);
                    g.fillOval(baseX, baseY - offset, stoneSize, stoneSize);
                }
            }
            }
        }

        // 2) Optional: Roter Rand ÜBER allem (damit klar sichtbar)
        if (vs.selected != null) {
            g.setColor(new Color(255, 255, 0, 80));
            g.fillRect(vs.selected.y * tileSize, vs.selected.x * tileSize, tileSize, tileSize);

            g.setColor(Color.RED);
            g.drawRect(vs.selected.y * tileSize, vs.selected.x * tileSize, tileSize - 1, tileSize - 1);
        }
    }
}