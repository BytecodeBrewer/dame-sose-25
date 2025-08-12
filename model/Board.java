public class Board {
    private Piece[][] board;
    private final int SIZE = 8;

    public Board() {
        board = new Piece[SIZE][SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
        // Initialize the board with pieces in starting positions
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((i + j) % 2 != 0) {
                    if (i < 3) {
                        board[i][j] = new Piece("black", "man");
                    } else if (i > 4) {
                        board[i][j] = new Piece("white","man");
                    } else {
                        board[i][j] = null;
                    }
                } else {
                    board[i][j] = null;
                }
            }
        }
    }

    public void initialize() {
        // Reset the board to the initial state
        initializeBoard();
    }
}