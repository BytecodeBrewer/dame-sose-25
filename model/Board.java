public class Board {
    private Piece[][] board;
    private final int SIZE = 8;
    private Player player1; // Spieler für weiße Steine
    private Player player2; // Spieler für schwarze Steine

    public Board(Player player1, Player player2) {
        this.player1 = player1; // Weiß
        this.player2 = player2; // Schwarz
        board = new Piece[SIZE][SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((i + j) % 2 != 0) {
                    if (i < 3) {
                        occupyField(i, j, new Piece("black", "man", player2));
                    } else if (i > 4) {
                        occupyField(i, j, new Piece("white", "man", player1));
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

    // Liefert zurück, ob das Feld frei ist
    public boolean isFieldFree(int x, int y) {
        return board[x][y] == null;
    }

    public boolean isFieldOccupiedByOpponent(int x, int y, Player player) {
        Piece piece = board[x][y];
        return piece != null && !piece.getOwner().equals(player);
    }

    // Markiert das Feld als besetzt
    public void occupyField(int x, int y, Piece piece) {
        board[x][y] = piece;
    }

    // Markiert das Feld als frei
    public void freeField(int x, int y) {
        board[x][y] = null;
    }

    public Piece getPieceAt(int x, int y) {
        return board[x][y];
    }

    public Piece[][] getBoard() {
        return board;
    }
}