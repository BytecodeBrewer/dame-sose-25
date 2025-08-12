public class Board {
    private Piece[][] board;
    private final int SIZE = 8;

    public Board() {
        board = new Piece[SIZE][SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
        // Initialisiere das Schachbrett mit den Anfangspositionen der Stücke
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
        // Neues Spiel initialisieren
        initializeBoard();
    }

    public boolean isValidMove(int fromX, int fromY, int toX, int toY) {
        // Prüfen, ob der Zug gültig ist
        return true; // Platzhalter für die Boolean-Funktion
    }

    public void movePiece(int fromX, int fromY, int toX, int toY) {
        // Logik, um ein Stück zu bewegen
        Piece piece = board[fromX][fromY];
        if (piece != null && piece.canMove(toX, toY)) {
            board[toX][toY] = piece;
            board[fromX][fromY] = null;
            piece.move(toX, toY);
        }
    }


}