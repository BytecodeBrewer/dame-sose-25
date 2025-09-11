import java.util.List;
import java.util.ArrayList;

public class Player {
    private String name;
    private List<Piece> pieces;
<<<<<<< HEAD
=======
    private boolean hasWon;
>>>>>>> origin/model/Getter-Setter-Scoreboard

    public Player(String name) {
        this.name = name;
        this.pieces = new ArrayList<>();
<<<<<<< HEAD
=======
        this.hasWon = false;
>>>>>>> origin/model/Getter-Setter-Scoreboard
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

    public boolean hasWon(Player opponent, Board board) {
        if (opponent.hasNoPieces()) {
            return true;
        }
        for (Piece piece : opponent.getPieces()) {
            if (piece.canMoveAnywhere(board)) {
                return false; // Opponent can still move
            }
        }
        return true; // Opponent has no moves left
    }

<<<<<<< HEAD
=======
    public boolean getHasWon() {
        return hasWon;
    }

    public void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }

>>>>>>> origin/model/Getter-Setter-Scoreboard
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