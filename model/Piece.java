public class Piece {
    private String color;
    private String type;
    private boolean isCaptured;
    private Player owner;

    public Piece(String color, String type, Player owner) {
        this.color = color;
        this.type = type;
        this.isCaptured = false;
        this.owner = owner;
    }

    public String getColor() {
        return color;
    }

    public String getType() {
        return type;
    }

    public boolean isCaptured() {
        return isCaptured;
    }

    public void capture() {
        this.isCaptured = true;
    }

    public boolean canMove(Board board, int targetX, int targetY, int x, int y) {
        // Logik, um zu überprüfen, ob das Stück zu der angegebenen Position erlaubt ist
        if (isDame()) {
            // Logik für Dame-Bewegungen
            if (board.isFieldFree(targetX, targetY) && !board.isFieldOccupiedByOpponent(targetX, targetY, owner)
                    && Math.abs(targetX - x) == Math.abs(targetY - y)) {
                return true;
            }
        } else {
            // Logik für normale Steine
            if (board.isFieldFree(targetX, targetY) && !board.isFieldOccupiedByOpponent(targetX, targetY, owner)
                    && Math.abs(targetX - x) == 1 && Math.abs(targetY - y) == 1) {
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
            promoteToDame(y);
        }
    }

    public void promoteToDame(int y) {
        if (isAtPromotionRow(y)) {
            this.type = "Dame";
        }
    }

    public boolean isAtPromotionRow(int y) {
        // Logik, um zu überprüfen, ob das Stück die Beförderungsreihe erreicht hat
        return ("white".equals(color) && y == 0) || ("black".equals(color) && y == 7);
    }

    public boolean isDame() {
        return "Dame".equals(type);
    }

    public Player getOwner() {
        return owner;
    }
}