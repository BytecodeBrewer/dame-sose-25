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

    public boolean canMove(int targetX, int targetY) {
        // Implement checkers movement rules here
        return true;
    }

    @Override
    public String toString() {
        return color + " " + type + " at (" + x + "," + y + ")";
    }
}