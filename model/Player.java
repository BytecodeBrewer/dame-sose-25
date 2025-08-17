import java.util.List;
import java.util.ArrayList;

public class Player {
    private String name;
    private List<Piece> pieces;

    public Player(String name) {
        this.name = name;
        this.pieces = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void addPiece(Piece piece) {
        pieces.add(piece);
    }

    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }

    public boolean hasWon() {
        // Beispiel: Spieler gewinnt, wenn der Gegner keine Figuren mehr hat
        return pieces.isEmpty();
    }

    public void makeMove(Piece piece, int newX, int newY) {
        piece.move(newX, newY);
    }

    public int getPieceCount() {
        return pieces.size();
    }

    public boolean hasNoPieces() {
        return pieces.isEmpty();
    }
}