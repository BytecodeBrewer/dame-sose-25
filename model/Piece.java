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
    private int x, y; // Position on board

    public Piece(PieceColor color, PieceType type, Player owner, int x, int y) {
        this.color = color;
        this.type = type;
        this.isCaptured = false;
        this.owner = owner;
        this.x = x;
        this.y = y;
    }

    // Copy constructor for undo functionality
    public Piece copy() {
        Piece copy = new Piece(this.color, this.type, this.owner, this.x, this.y);
        if (this.isCaptured)
            copy.capture();
        return copy;
    }

    public PieceColor getColor() {
        return color;
    }

    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }

    public boolean isCaptured() {
        return isCaptured;
    }

    public void capture() {
        this.isCaptured = true;
    }

    public boolean canMove(Board board, int toX, int toY, int x, int y) {
        // Logik, um zu überprüfen, ob das Stück zu der angegebenen Position erlaubt ist
        if (!isMan()) {
            // Dame: move any number of squares diagonally, path must be clear
            if (board.isFieldFree(toX, toY) && !board.isFieldOccupiedByOpponent(toX, toY, owner)
                    && Math.abs(toX - x) == Math.abs(toY - y) && !mustCapture(board)) {
                int dx = (toX - x) > 0 ? 1 : -1;
                int dy = (toY - y) > 0 ? 1 : -1;
                int steps = Math.abs(toX - x);
                for (int i = 1; i < steps; i++) {
                    int checkX = x + dx * i;
                    int checkY = y + dy * i;
                    if (!board.isFieldFree(checkX, checkY)) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            // Logik für normale Steine
            if (board.isFieldFree(toX, toY) && !board.isFieldOccupiedByOpponent(toX, toY, owner)
                    && Math.abs(toX - x) == 1 && Math.abs(toY - y) == 1 && !mustCapture(board)) {
                return true;
            }
        }
        return false;
    }

    public void move(Board board, int x, int y, int newX, int newY) {
        // Spiel-Logik für das Bewegen des Stücks
        if (canMove(board, newX, newY, x, y)) {
            board.freeField(x, y);
            board.occupyField(newX, newY, this);
            x = newX;
            y = newY;
        }
    }

    public boolean isAtPromotionRow(int y) {
        // Logik, um zu überprüfen, ob das Stück die Beförderungsreihe erreicht hat
        return (color == PieceColor.WHITE && y == 0) || (color == PieceColor.BLACK && y == 7);
    }

    public boolean isMan() {
        return type == PieceType.MAN;
    }

    public void uncapture() {
        this.isCaptured = false;
    }

    public Player getOwner() {
        return owner;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    @Override
    public String toString() {
        return color + " " + type + " at (" + x + "," + y + ")";
    }

    public boolean mustCapture(Board board) {
        // Checkt, ob dieser Stein einen Schlagzug machen muss
        if (!isMan()) {
            // Logic for Dame pieces
            for (int dx = -1; dx <= 1; dx += 2) {
                for (int dy = -1; dy <= 1; dy += 2) {
                    if (canCapture(board, x + dx, y + dy, x + 2 * dx, y + 2 * dy)) {
                        return true;
                    }
                }
            }
        } else {
            // Logic for normal pieces
            if (this.color == PieceColor.WHITE) {
                // Logic for the white player
                for (int dx = -1; dx <= 1; dx += 2) {
                    if (canCapture(board, x + dx, y - 1, x + 2 * dx, y - 2)) {
                        return true;
                    }
                }
            } else {
                // Logic for the black player
                for (int dx = -1; dx <= 1; dx += 2) {
                    if (canCapture(board, x + dx, y + 1, x + 2 * dx, y + 2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean canCapture(Board board, int midX, int midY, int toX, int toY) {
        // Check bounds before accessing board
        if (midX < 0 || midX > 7 || midY < 0 || midY > 7 || toX < 0 || toX > 7 || toY < 0 || toY > 7) {
            return false;
        }
        return board.isFieldOccupiedByOpponent(midX, midY, owner) && board.isFieldFree(toX, toY);
    }
}