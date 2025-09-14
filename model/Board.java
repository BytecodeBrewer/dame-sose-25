public class Board {
    private Piece[][] board;
    private final int SIZE = 8;
    private Player player1; // Spieler für weiße Steine
    private Player player2; // Spieler für schwarze Steine

    // Konstruktor für das Spielfeld
    public Board(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        board = new Piece[SIZE][SIZE];
    }

    // Leeres Brett erstellen für Debug-Modus
    private void clearBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = null;
            }
        }
    }

    // Initialisiert das Brett mit den Startpositionen der Steine
    private void initializeBoard() {
    for (int i = 0; i < SIZE; i++) {
        for (int j = 0; j < SIZE; j++) {
            initializeSquare(i, j);
        }
    }
}

    // Initialisiert ein einzelnes Feld
    private void initializeSquare(int row, int col) {
        if (isPlayableSquare(row, col)) {
            placeInitialPieceIfNeeded(row, col);
        } else {
            freeField(row, col);
        }
    }

    // Prüft, ob ein Feld dunkel ist (spielbar)
    private boolean isPlayableSquare(int row, int col) {
        return (row + col) % 2 != 0;
    }

    // Platziert Figuren abhängig von der Reihe
    private void placeInitialPieceIfNeeded(int row, int col) {
        if (row < 3) {
            placePiece(row, col, Piece.PieceColor.BLACK, player2);
        } else if (row > 4) {
            placePiece(row, col, Piece.PieceColor.WHITE, player1);
        } else {
            freeField(row, col);
        }
    }

    // Hilfsmethode zum Setzen einer Figur
    private void placePiece(int row, int col, Piece.PieceColor color, Player owner) {
        Piece piece = new Piece(color, Piece.PieceType.MAN, owner, row, col);
        occupyField(row, col, piece);
        owner.addPiece(piece);
    }


    // Getter, um das Spielfeld zu erhalten für ein normales Spiel
    public void initialize() {
        initializeBoard();
    }

    // Getter, um das Spielfeld zu erhalten für ein Debug-Spiel
    public void getClearBoard() {
        clearBoard();
    }

    // Prüft, ob ein Feld frei ist
    public boolean isFieldFree(int x, int y) {
        return board[x][y] == null;
    }

    // Setzt einen weißen oder schwarzen Stein auf das Brett (für Debug-Modus)
    public void setWhitePiece(int i, int j) {
        Piece p = new Piece(Piece.PieceColor.WHITE, Piece.PieceType.MAN, player1, i, j);
        occupyField(i, j, p);
        player1.addPiece(p);
    }

    // Setzt einen weißen oder schwarzen Stein auf das Brett (für Debug-Modus)
    public void setBlackPiece(int i, int j) {
        Piece p = new Piece(Piece.PieceColor.BLACK, Piece.PieceType.MAN, player2, i, j);
        occupyField(i, j, p);
        player2.addPiece(p);
    }

    // Prüft, ob ein Feld von einem gegnerischen Stein besetzt ist
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

    // Gibt den Stein an den angegebenen Koordinaten zurück
    public Piece getPieceAt(int x, int y) {
        return board[x][y];
    }

    // Setzt einen Stein an die angegebenen Koordinaten
    public void setPieceAt(int x, int y, Piece piece) {
        board[x][y] = piece;
    }

    // Prüft, ob die angegebenen Koordinaten außerhalb des Spielfelds liegen
    public boolean outOfBounds(int x, int y) {
        return x < 0 || x >= SIZE || y < 0 || y >= SIZE;
    }

    // Macht aus einem normalen Stein eine Dame
    public void promotetoDame(int x, int y) {
        Piece piece = getPieceAt(x, y);
        if (piece != null && piece.getType() == Piece.PieceType.MAN) {
            piece.setType(Piece.PieceType.DAME);
        }
    }

    // Setzt einen normalen oder Dame Stein auf das Brett (für Debug-Modus)
    public void placeNormalPieceWhite(int x, int y) {
        Piece piece = new Piece(Piece.PieceColor.WHITE, Piece.PieceType.MAN, null, x, y);
        board[x][y] = piece;
    }

}