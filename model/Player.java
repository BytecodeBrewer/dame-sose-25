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
        // Logik, um zu überprüfen, ob der Spieler gewonnen hat
        // Zum Beispiel, wenn keine eigenen Stücke mehr vorhanden sind
        return pieces.isEmpty(); // Beispielhafte Rückgabe
    }

    public void makeMove(Piece piece, int newX, int newY) {
        // Platzhalter für die Logik, um einen Zug zu machen
    }
}