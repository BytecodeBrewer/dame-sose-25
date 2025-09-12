
public class Board {

    private Piece[][] board;
    private final int SIZE = 8;
    private Player player1; // Spieler für weiße Steine
    private Player player2; // Spieler für schwarze Steine

    public Board(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        board = new Piece[SIZE][SIZE];
    }

    private void clearBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = null;
            }
        }
    }

    private void initializeBoard() {
        System.out.println(" Steine für Spieler 1 (Weiß) gesetzt."); 
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((i + j) % 2 != 0) {
                    if (i < 3) {
                        Piece p = new Piece(Piece.PieceColor.BLACK, Piece.PieceType.MAN, player2, i, j);
                        occupyField(i, j, p);
                        player2.addPiece(p);
                    } else if (i > 4) {
                        Piece p = new Piece(Piece.PieceColor.WHITE, Piece.PieceType.MAN, player1, i, j);
                        occupyField(i, j, p);
                        player1.addPiece(p);
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
        initializeBoard();
    }

    public void getClearBoard() {
        clearBoard();
    }

    public boolean isFieldFree(int x, int y) {
        return board[x][y] == null;
    }

    public void setWhitePiece(int i, int j) {
        Piece p = new Piece(Piece.PieceColor.WHITE, Piece.PieceType.MAN, player1, i, j);
        occupyField(i, j, p);
        player1.addPiece(p);
    }

    public void setBlackPiece(int i, int j) {
        Piece p = new Piece(Piece.PieceColor.BLACK, Piece.PieceType.MAN, player2, i, j);
        occupyField(i, j, p);
        player2.addPiece(p);
    }

    public boolean isFieldOccupiedByOpponent(int x, int y, Player player) {
        Piece piece = board[x][y];
        return piece != null && !piece.getOwner().equals(player);
    }

    // Markiert das Feld als besetzt
    public void occupyField(int x, int y, Piece piece) {
        board[x][y] = piece;
    }

    public void freeField(int x, int y) {
        board[x][y] = null;
    }

    public Piece getPieceAt(int x, int y) {
        return board[x][y];
    }

    public void setPieceAt(int x, int y, Piece piece) {
        board[x][y] = piece;
    }

    public boolean outOfBounds(int x, int y) {
        return x < 0 || x >= SIZE || y < 0 || y >= SIZE;
    }

    public void promotetoDame(int x, int y) {
        Piece piece = getPieceAt(x, y);
        if (piece != null && piece.getType() == Piece.PieceType.MAN) {
            piece.setType(Piece.PieceType.DAME);
        }
    }

    // Logik für das Platzieren von Figuren
    public void placeNormalPieceWhite(int x, int y) {
        Piece piece = new Piece(Piece.PieceColor.WHITE, Piece.PieceType.MAN, null, x, y);
        board[x][y] = piece;
    }

    public void placeNormalPieceBlack(int x, int y) {
        Piece piece = new Piece(Piece.PieceColor.BLACK, Piece.PieceType.MAN, null, x, y);
        board[x][y] = piece;
    }

    public void placeKingPieceWhite(int x, int y) {
        Piece piece = new Piece(Piece.PieceColor.WHITE, Piece.PieceType.DAME, null, x, y);
        board[x][y] = piece;
    }

    public void placeKingPieceBlack(int x, int y) {
        Piece piece = new Piece(Piece.PieceColor.BLACK, Piece.PieceType.DAME, null, x, y);
        board[x][y] = piece;
    }

}