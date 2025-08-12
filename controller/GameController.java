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

    public boolean makeMove(int fromX, int fromY, int toX, int toY) {
            if (board.isValidMove(fromX, fromY, toX, toY)) {
            board.movePiece(fromX, fromY, toX, toY);
            switchPlayer();
            return true;
        }
        return false;

        }

    public void resetGame() {
        board.initialize();
        currentPlayer = player1; // Zurücksetzen auf Spieler 1
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }
}