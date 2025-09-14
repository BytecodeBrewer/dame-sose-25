import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Grafikkomponente für das Spielbrett
    // Zeichnet Brett, Figuren und markiert ausgewählte Felder
public class BoardView extends JPanel {
    private final GameController gameController;
    // Nur für Debug/Setup: Steine platzieren
    private MouseListener placementListener;
    // Sperre, damit nach Spielstart kein Steine mehr plaziert werden können
    private boolean placementLocked = false;
    
    // Konstruktor initialisiert:
    // - Verbindung zum GameController
    // - MouseListener für Spielzüge
    // Konvertiert Mausklicks in Brettkoordinaten
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

    // Debug-Modus: Aktiviert Platzierung von Steinen
    // - Entfernt alten Placement-Listener
    // - Erstellt neuen Listener für gewählte Farbe
    // - Ignoriert Aufrufe wenn Platzierung gesperrt
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

    // Sperrt weitere Platzierung von Steinen
    // Wird beim Spielstart im Debug-Modus aufgerufen
    public void lockPlacement() {
        this.placementLocked = true;
        detachPlacementListener();
        repaint();
    }

    // Entfernt den aktuellen Placement-Listener
    // Verhindert mehrfache Listener-Registrierung
    private void detachPlacementListener() {
        if (this.placementListener != null) {
            removeMouseListener(this.placementListener);
            this.placementListener = null;
        }
    }

    // Zeichnet das Spielbrett mit:
    // 1. Schachbrettmuster (hell/dunkel)
    // 2. Spielsteine (weiß/schwarz)
    // 3. Damen (doppelte Steine)
    // 4. Markierungen für ausgewählte Felder
    protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    GameController.ViewState viewState = gameController.getViewState();
    if (viewState == null) return;

    int boardSize = viewState.grid.length;
    int tileSize = Math.min(getWidth(), getHeight()) / boardSize;

    drawBoard(g, viewState, boardSize, tileSize);
    highlightSelectedCell(g, viewState, tileSize);
    }

    // Zeichnet das gesamte Spielfeld inkl. Felder und Steine.
    private void drawBoard(Graphics g, GameController.ViewState viewState, int boardSize, int tileSize) {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                drawTile(g, row, col, tileSize);
                drawPiece(g, viewState.grid[row][col], row, col, tileSize);
            }
        }
    }

    //Zeichnet ein einzelnes Spielfeld (hell/dunkel).
    private void drawTile(Graphics g, int row, int col, int tileSize) {
        boolean isLight = (row + col) % 2 == 0;
        g.setColor(isLight ? Color.LIGHT_GRAY : Color.DARK_GRAY);
        g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
    }

    //Zeichnet eine Spielfigur, falls vorhanden.
    private void drawPiece(Graphics g, GameController.Cell cell, int row, int col, int tileSize) {
        if (cell == GameController.Cell.EMPTY) return;

        boolean isWhite = (cell == GameController.Cell.WM || cell == GameController.Cell.WK);
        boolean isMan   = (cell == GameController.Cell.WM || cell == GameController.Cell.BM);

        int baseX = col * tileSize + 10;
        int baseY = row * tileSize + 10;
        int stoneSize = tileSize - 20;

        // Grauer Rand, damit bei dame erkennt, dass es zwei Steine, die übereinander liegen.
        g.setColor(Color.GRAY);
        g.fillOval(baseX - 2, baseY - 2, stoneSize + 4, stoneSize + 4);

        // Figur selbst
        g.setColor(isWhite ? Color.WHITE : Color.BLACK);
        g.fillOval(baseX, baseY, stoneSize, stoneSize);

        // Krone = zweite Scheibe leicht versetzt
        if (!isMan) {
            drawKingOverlay(g, isWhite, baseX, baseY, stoneSize, tileSize);
        }
    }

    // Zeichnet den "doppelten Stein" für Damen
    private void drawKingOverlay(Graphics g, boolean isWhite, int baseX, int baseY, int stoneSize, int tileSize) {
        int offset = Math.max(4, tileSize / 12);

        // oberer Rand
        g.setColor(Color.GRAY);
        g.fillOval(baseX - 2, baseY - offset - 2, stoneSize + 4, stoneSize + 4);

        // oberer Stein
        g.setColor(isWhite ? Color.WHITE : Color.BLACK);
        g.fillOval(baseX, baseY - offset, stoneSize, stoneSize);
    }

    //Hebt das aktuell ausgewählte Feld hervor
    private void highlightSelectedCell(Graphics g, GameController.ViewState viewState, int tileSize) {
        if (viewState.selected == null) return;

        int selRow = viewState.selected.x;
        int selCol = viewState.selected.y;

        // halbtransparentes Gelb im Hintergrund
        g.setColor(new Color(255, 255, 0, 80));
        g.fillRect(selCol * tileSize, selRow * tileSize, tileSize, tileSize);

        // roter Rand
        g.setColor(Color.RED);
        g.drawRect(selCol * tileSize, selRow * tileSize, tileSize - 1, tileSize - 1);
    }

}