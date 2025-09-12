import java.util.List;
import java.util.ArrayList;

public class Player {
    private String name;
    private List<Piece> pieces;
    private List<Piece> capturedPieces = new ArrayList<>();

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

    public List<Piece> getCapturedPieces() {
        return capturedPieces;
    }

    public void capturePiece(Piece piece) {
        capturedPieces.add(piece);
    }

    public void clearCapturedPieces() {
        capturedPieces.clear();
    }

    public void addPiece(Piece piece) {
        pieces.add(piece);
    }

    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }

    public void makeMove(Piece piece, int newX, int newY) {
        piece.move(newX, newY);
    }

    public int getPieceCount() {
        return pieces.size();
    }

    public void clearPieces() {
        pieces.clear();
    }

    public Piece.PieceColor getColor() {
        
        return pieces.isEmpty() ? null : pieces.get(0).getColor();
    }
}