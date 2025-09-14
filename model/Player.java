import java.util.List;
import java.util.ArrayList;

public class Player {
    private String name;
    private List<Piece> pieces;
    private List<Piece> stuckPieces = new ArrayList<>();

    // Konstruktor für einen Spieler
    public Player(String name) {
        this.name = name;
        this.pieces = new ArrayList<>();
    }

    // Getter und Setter
    // Um den Spielernamen zu erhalten
    public String getName() {
        return name;
    }

    // Lagert die Steine dieses Spielers
    public List<Piece> getPieces() {
        return pieces;
    }

    // Lagert die gefangenen Steine dieses Spielers
    public List<Piece> getStuckPieces() {
        return stuckPieces;
    }

    // Fügt einen Stein zu den gefangenen Steinen hinzu
    public void stuckPiece(Piece piece) {
        stuckPieces.add(piece);
    }

    // Entfernt alle gefangenen Steine aus der Liste
    public void clearStuckPieces() {
        stuckPieces.clear();
    }

    // Fügt einen Stein zu den Steinen dieses Spielers hinzu
    public void addPiece(Piece piece) {
        pieces.add(piece);
    }

    // Entfernt einen Stein aus den Steinen dieses Spielers
    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }

    // Führt einen Zug für diesen Spieler aus
    public void makeMove(Piece piece, int newX, int newY) {
        piece.move(newX, newY);
    }

    // Gibt die Anzahl der Steine dieses Spielers zurück
    public int getPieceCount() {
        return pieces.size();
    }

    // Entfernt alle Steine dieses Spielers (für ein neues Spiel)
    public void clearPieces() {
        pieces.clear();
    }

    // Gibt die Farbe der Steine dieses Spielers zurück
    public Piece.PieceColor getColor() {
        return pieces.isEmpty() ? null : pieces.get(0).getColor();
    }
}