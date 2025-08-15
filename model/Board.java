public class Board {
    private Piece[][] board;
    private final int SIZE = 8;
    private Player player1;
    private Player player2;

    public Board() {
        board = new Piece[SIZE][SIZE];
        player1 = new Player("Spieler 1");
        player2 = new Player("Spieler 2");
        initializeBoard();
    }

    public boolean isFieldFree(int x, int y) {
        // Überprüfen, ob das Feld frei ist
        return board[x][y] == null;
    }

    public void occupyField(int x, int y, Piece piece) {
        // Setzt ein Stück auf das angegebene Feld
        if (isFieldFree(x, y)) {
            board[x][y] = piece;
        } else {
            throw new IllegalArgumentException("Das Feld ist bereits besetzt.");
        }
    }

    public void freeField(int x, int y) {
        // Macht das angegebene Feld frei
        if (!isFieldFree(x, y)) {
            board[x][y] = null;
        } else {
            throw new IllegalArgumentException("Das Feld ist bereits frei.");
        }
    }

    private void initializeBoard() {
        // Initialisiere das Schachbrett mit den Anfangspositionen der Stücke
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((i + j) % 2 != 0) {
                    if (i < 3) {
                        occupyField(i, j, new Piece("black", "man", player1));
                    } else if (i > 4) {
                        occupyField(i, j, new Piece("white", "man", player2));
                    } else {
                        freeField(i, j);
                    }
                } else {
                    freeField(i, j);
                }
            }
        }
    }

    public void initialize() {
        // Neues Spiel initialisieren
        initializeBoard();
    }

    public Piece getPieceAt(int x, int y) {
        return board[x][y];
    }

    public Piece[][] getBoard() {
        return board;
    }

}