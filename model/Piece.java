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

    public void move(int newX, int newY) {
        // Spiel-Logik für das Bewegen des Stücks
    }

    public boolean canMove(int targetX, int targetY) {
        // Logik, um zu überprüfen, ob das Stück zu der angegebenen Position erlaubt ist
        return true;
    }

    public Player getOwner() {
        return owner;
    }
}