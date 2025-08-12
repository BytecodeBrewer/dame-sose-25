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

    public boolean hasWon() {
        // Implement logic to check if the player has won
        return pieces.isEmpty(); // Example condition
    }

    public void makeMove(Piece piece, int newX, int newY) {
        // Implement logic for making a move
    }
}