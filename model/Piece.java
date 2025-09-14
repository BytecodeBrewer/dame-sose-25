public class Piece {
    public enum PieceType {
        MAN, DAME
    }

    public enum PieceColor {
        WHITE, BLACK
    }

    private PieceColor color;
    private PieceType type;
    private boolean isCaptured;
    private Player owner;
    private int x, y;

    // Konstruktor für einen Stein
    public Piece(PieceColor color, PieceType type, Player owner, int x, int y) {
        this.color = color;
        this.type = type;
        this.isCaptured = false;
        this.owner = owner;
        this.x = x;
        this.y = y;
    }

    // Erstellt eine Kopie dieses Steins, um Seiteneffekte zu vermeiden
    // Denn es kann vorkommen, dass ein Stein in einem simulierten Zug geschlagen wird,
    // aber im echten Spielzug nicht geschlagen wird.
    public Piece copy() {
        Piece copy = new Piece(this.color, this.type, this.owner, this.x, this.y);
        if (this.isCaptured)
            copy.capture();
        return copy;
    }

    // Ein Spielstein umsetzen lassen
    public void move(Board board, int x, int y, int newX, int newY) {
        board.freeField(x, y);
        board.occupyField(newX, newY, this);
        x = newX;
        y = newY;
    }

    // Getter und Setter
    // Gibt die Farbe zurück vom Spielstein
    public PieceColor getColor() {
        return color;
    }

    // Gibt zurück, ob es eine Dame oder ein normaler Stein ist
    public PieceType getType() {
        return type;
    }

    // Gibt den Spieler zurück, wem der Stein gehört
    public Player getPlayer() {
        return owner;
    }

    // Legt den Typen eines Stein fest
    public void setType(PieceType type) {
        this.type = type;
    }

    // Prüft, ob dieser Stein geschlagen ist
    public boolean isCaptured() {
        return isCaptured;
    }

    // Markiert diesen Stein als geschlagen
    public void capture() {
        this.isCaptured = true;
    }

    // Prüft, ob dieser Stein ein normaler Stein (kein Dame) ist
    public boolean isMan() {
        return type == PieceType.MAN;
    }

    // Markiert diesen Stein als nicht geschlagen
    public void uncapture() {
        this.isCaptured = false;
    }

    // Gibt den Besitzer dieses Steins zurück
    public Player getOwner() {
        return owner;
    }

    // Gibt die x Koordinate zurück
    public int getX() {
        return x;
    }

    // Gibt die y Koordinate zurück
    public int getY() {
        return y;
    }

    // Bewegt diesen Stein zu den neuen Koordinaten
    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

   // Prüft, ob dieser Stein einen Schlagzug machen muss 
    public boolean mustCapture(Board board) {
        if (!isMan()) {
            return mustKingCapture(board);
        } else {
            return mustManCapture(board);
        }
    }

    // Schlagprüfung für normale Steine (Männer)
    private boolean mustManCapture(Board board) {
        int direction = (color == PieceColor.WHITE) ? -1 : 1; // Weiß zieht nach oben, Schwarz nach unten

        for (int deltaCol : new int[]{-1, 1}) {
            int midRow = x + direction;
            int midCol = y + deltaCol;
            int targetRow = x + 2 * direction;
            int targetCol = y + 2 * deltaCol;

            if (canCapture(board, midRow, midCol, targetRow, targetCol)) {
                return true;
            }
        }
        return false;
    }

    // Schlagprüfung für Dame (King)
    private boolean mustKingCapture(Board board) {
        for (int deltaRow : new int[]{-1, 1}) {
            for (int deltaCol : new int[]{-1, 1}) {
                int midRow = x + deltaRow;
                int midCol = y + deltaCol;
                int targetRow = x + 2 * deltaRow;
                int targetCol = y + 2 * deltaCol;

                if (canCapture(board, midRow, midCol, targetRow, targetCol)) {
                    return true;
                }
            }
        }
        return false;
    }


    // Gibt zurück, ob man es möglich ist ein Stein zu schlagen
    public boolean canCapture(Board board, int midX, int midY, int toX, int toY) {
        // Chheckt, ob wo man hin kann, um ein Stein zu schlagen, innerhalb des Feldes liegt
        if (midX < 0 || midX > 7 || midY < 0 || midY > 7 || toX < 0 || toX > 7 || toY < 0 || toY > 7) {
            return false;
        }
        return board.isFieldOccupiedByOpponent(midX, midY, owner) && board.isFieldFree(toX, toY);
    }


}