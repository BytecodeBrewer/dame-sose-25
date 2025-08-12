public class GameController {
    private int currentPlayer;
    public GameController() {

    }

    public void startGame() {

    }

    public boolean makeMove() {
        // Example: after making a move, switch the player
        switchPlayer();
        if (currentPlayer == 1) {
            System.out.println("Player 1's turn.");
            return true;
        } else {
            System.out.println("Player 2's turn.");
            return false;
        }
        
    }

    public void resetGame() {

    }

    private void switchPlayer() {
        System.out.println("Switching player...");
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
        System.out.println("Current player is now: " + currentPlayer);
    }
}