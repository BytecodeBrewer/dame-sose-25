import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.List;


public class BoardView extends JPanel {
    private final GameController gameController;
    private int selectedRow = -1;
    private int selectedCol = -1;

    // NEU: vom Controller setzbare Ziel-Felder (leuchten)
    private List<Point> legalTargets = Collections.emptyList();

    public BoardView(GameController gameController) {
        this.gameController = gameController;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int cellSize = Math.min(getWidth(), getHeight()) / 8;
                int col = e.getX() / cellSize; // Spalte
                int row = e.getY() / cellSize; // Zeile

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
        });
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

        // Brett + Steine
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                g.setColor(((i + j) % 2 == 0) ? Color.LIGHT_GRAY : Color.DARK_GRAY);
                g.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);

                Piece piece = currentBoard.getPieceAt(i, j);
                if (piece != null) {
                    // Stein
                    g.setColor(
                        (piece.getColor() == Piece.PieceColor.BLACK) ? Color.BLACK : Color.WHITE
                    );
                    g.fillOval(j * tileSize + 10, i * tileSize + 10, tileSize - 20, tileSize - 20);

                    // NEU: Dame-Kennzeichnung "D" (falls dein Getter anders heißt, z. B. isDame(), bitte dort anpassen)
                    try {
                        // Annahme: Piece hat isKing()
                        java.lang.reflect.Method m = piece.getClass().getMethod("isKing");
                        Object r = m.invoke(piece);
                        if (r instanceof Boolean && (Boolean) r) {
                            g.setFont(g.getFont().deriveFont(Font.BOLD, Math.max(14f, tileSize * 0.5f)));
                            g.setColor((piece.getColor() == Piece.PieceColor.BLACK) ? Color.WHITE : Color.BLACK);
                            String text = "D";
                            FontMetrics fm = g.getFontMetrics();
                            int tx = j * tileSize + (tileSize - fm.stringWidth(text)) / 2;
                            int ty = i * tileSize + (tileSize + fm.getAscent() - fm.getDescent()) / 2;
                            g.drawString(text, tx, ty);
                        }
                    } catch (Exception ignore) {
                        // falls es kein isKing() gibt, einfach nichts schreiben
                    }
                }
            }
        }

        // Auswahl-Hervorhebung (oben drauf)
        if (selectedRow >= 0 && selectedCol >= 0) {
            g.setColor(new Color(255, 255, 0, 120));
            g.fillRect(selectedCol * tileSize, selectedRow * tileSize, tileSize, tileSize);
            g.setColor(Color.RED);
            g.drawRect(selectedCol * tileSize, selectedRow * tileSize, tileSize - 1, tileSize - 1);
        }

        // NEU: Leuchtende Ziel-Felder (vom Controller via setLegalTargets gesetzt)
        if (legalTargets != null && !legalTargets.isEmpty()) {
            for (Point p : legalTargets) {
                int r = p.x; // row
                int c = p.y; // col
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