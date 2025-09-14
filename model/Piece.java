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

    public void move(Board board, int x, int y, int newX, int newY) {
        board.freeField(x, y);
        board.occupyField(newX, newY, this);
        x = newX;
        y = newY;
    }

    public PieceColor getColor() {
        return color;
    }

    public PieceType getType() {
        return type;
    }

    public Player getPlayer() {
        return owner;
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
        Piece piece = board.getPieceAt(0, 0); // Dummy-Wert, wird nicht verwendet
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
            if (piece.getColor() == Piece.PieceColor.WHITE) {
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