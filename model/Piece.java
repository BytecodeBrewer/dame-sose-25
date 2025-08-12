public class Piece {
    private String color;
    private String type;
    private boolean isCaptured;

    public Piece(String color, String type) {
        this.color = color;
        this.type = type;
        this.isCaptured = false;
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
        // Logic for moving the piece to a new position
    }

    public boolean canMove(int targetX, int targetY) {
        // Logic to check if the piece can move to the target position
        return true; // Placeholder return value
    }
}