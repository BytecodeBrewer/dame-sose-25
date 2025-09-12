import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.List;


public class BoardView extends JPanel {
    private final GameController gameController;
    private int selectedRow = -1;
    private int selectedCol = -1;
    // Spielauswahl (bleibt aktiv)
    private MouseListener gameplayListener;
    // Nur für Debug/Setup: Steine platzieren
    private MouseListener placementListener;

    // Sperre, damit nach Spielstart kein Platzieren mehr möglich ist
    private boolean placementLocked = false;

    // NEU: vom Controller setzbare Ziel-Felder (leuchten)
    private List<Point> legalTargets = Collections.emptyList();

    public BoardView(GameController gameController) {
        this.gameController = gameController;

        // Spiel-Listener einmalig anlegen und merken
        this.gameplayListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int cellSize = Math.min(getWidth(), getHeight()) / 8;
                int col = e.getX() / cellSize; // Spalte
                int row = e.getY() / cellSize; // Zeile
                // Klicken außerhalb des Brettes ignorieren
                if (row < 0 || row >= 8 || col < 0 || col >= 8) {
                    return;
                }
                // Kein Stein ausgewählt -> Auswahl setzen (wenn Stein da)
                if (selectedRow == -1 && selectedCol == -1) {
                    Piece piece = gameController.getBoard().getPieceAt(row, col);
                    if (piece != null) {
                        selectedRow = row;
                        selectedCol = col;
                        repaint();
                    }
                } else {
                    // gleiches Feld -> Auswahl aufheben
                    if (row == selectedRow && col == selectedCol) {
                        selectedRow = -1;
                        selectedCol = -1;
                        repaint();
                        return;
                    }
                    // Optional: besetztes Zielfeld -> Auswahl aufheben (keine Logik, nur Feedback)
                    Piece dest = gameController.getBoard().getPieceAt(row, col);
                    if (dest != null) {
                        selectedRow = -1;
                        selectedCol = -1;
                        repaint();
                        return;
                    }

                    boolean moved = gameController.makeMove(selectedRow, selectedCol, row, col);
                    if (moved) {
                        selectedRow = -1;
                        selectedCol = -1;
                        repaint();
                    } else {
                        // ungültig -> Auswahl zurücksetzen (Fehlertext zeigt später MainView)
                        selectedRow = -1;
                        selectedCol = -1;
                        repaint();
                    }
                }
            }
        };
        addMouseListener(this.gameplayListener);
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

    // NEU: vom Controller/außen setzbar – welche Felder sollen leuchten?
    public void setLegalTargets(List<Point> targets) {
        this.legalTargets = (targets != null) ? targets : Collections.emptyList();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Board currentBoard = gameController.getBoard();
        if (currentBoard == null) return;

        int size = 8;
        int tileSize = Math.min(getWidth(), getHeight()) / size;

        // 1) Brett + Auswahl (gelb) + Stein (in dieser Reihenfolge)
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // Feld
                g.setColor(((i + j) % 2 == 0) ? Color.LIGHT_GRAY : Color.DARK_GRAY);
                g.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);

                // AUSWAHL-Highlight UNTER DEM STEIN (nur die gelbe Fläche)
                if (selectedRow == i && selectedCol == j) {
                    g.setColor(new Color(255, 255, 0, 120)); // halbtransparentes Gelb
                    g.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                }

                // Stein
                Piece piece = currentBoard.getPieceAt(i, j);
                if (piece != null) {
                    g.setColor((piece.getColor() == Piece.PieceColor.BLACK) ? Color.BLACK : Color.WHITE);
                    g.fillOval(j * tileSize + 10, i * tileSize + 10, tileSize - 20, tileSize - 20);

                    // Dame-Kennzeichnung (Krone ♕ oder "D")
                    if (!piece.isMan()) {
                        g.setFont(g.getFont().deriveFont(Font.BOLD, Math.max(14f, tileSize * 0.6f)));
                        g.setColor((piece.getColor() == Piece.PieceColor.BLACK) ? Color.WHITE : Color.BLACK);
                        String text = "\u2655"; // ♕  (alternativ: "D")
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
            g.setColor(Color.RED);
            g.drawRect(selectedCol * tileSize, selectedRow * tileSize, tileSize - 1, tileSize - 1);
        }

        // 3) Leuchtende Ziel-Felder (über allem)
        if (legalTargets != null && !legalTargets.isEmpty()) {
            for (Point p : legalTargets) {
                int r = p.x, c = p.y;
                int cx = c * tileSize + tileSize / 2;
                int cy = r * tileSize + tileSize / 2;
                int rad = Math.max(6, tileSize / 6);
                g.setColor(new Color(0, 255, 0, 160)); // grün, halbtransparent
                g.fillOval(cx - rad, cy - rad, 2 * rad, 2 * rad);
                g.setColor(new Color(0, 120, 0, 200));
                g.drawOval(cx - rad, cy - rad, 2 * rad, 2 * rad);
            }
        }
    }
}