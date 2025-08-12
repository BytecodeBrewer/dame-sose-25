public class GameController {
    private Board board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;

    public GameController() {
        this.board = new Board();
        this.player1 = new Player("Player 1");
        this.player2 = new Player("Player 2");
        this.currentPlayer = player1;

    }

    public void startGame() {
        board.initialize();
    }

    public boolean makeMove() {
            return false;
        }

    public void resetGame() {
        board.initialize();
        currentPlayer = player1; // Reset to the first player
    }

    private void switchPlayer() {
        if (currentPlayer == player1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
    }
}