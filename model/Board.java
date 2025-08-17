import java.util.Stack;

public class Board {
    public enum ErrMes {
        NONE,
        INVALID_MOVE,
        FIELD_OCCUPIED,
        OUT_OF_BOUNDS
        // Add more error types as needed
    }

    private Piece[][] board;
    private final int SIZE = 8;
    private Player player1; // White
    private Player player2; // Black
    private ErrMes errmes = ErrMes.NONE;
    private Stack<Piece[][]> history = new Stack<>();

    public Board(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        board = new Piece[SIZE][SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
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

    public boolean isFieldFree(int x, int y) {
        return board[x][y] == null;
    }

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

    // Error handling
    public ErrMes getErrmes() {
        return errmes;
    }

    // Move logic with error handling and undo
    public boolean tryMove(int fromX, int fromY, int toX, int toY) {
        saveState();
        if (outOfBounds(fromX, fromY) || outOfBounds(toX, toY)) {
            errmes = ErrMes.OUT_OF_BOUNDS;
            return false;
        }
        if (!isFieldFree(toX, toY)) {
            errmes = ErrMes.FIELD_OCCUPIED;
            return false;
        }
        Piece piece = getPieceAt(fromX, fromY);
        if (piece == null) {
            errmes = ErrMes.INVALID_MOVE;
            return false;
        }
        setPieceAt(toX, toY, piece);
        freeField(fromX, fromY);
        piece.move(toX, toY);
        errmes = ErrMes.NONE;
        // Promotion logic
        if ((piece.getColor() == Piece.PieceColor.WHITE && toX == 0) ||
                (piece.getColor() == Piece.PieceColor.BLACK && toX == SIZE - 1)) {
            promotetoDame(toX, toY);
        }
        return true;
    }

    private boolean outOfBounds(int x, int y) {
        return x < 0 || x >= SIZE || y < 0 || y >= SIZE;
    }

    public void promotetoDame(int x, int y) {
        Piece piece = getPieceAt(x, y);
        if (piece != null && piece.getType() == Piece.PieceType.MAN) {
            piece.setType(Piece.PieceType.DAME);
        }
    }

    // Undo functionality
    private void saveState() {
        Piece[][] copy = new Piece[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Piece p = board[i][j];
                if (p != null) {
                    copy[i][j] = p.copy();
                }
            }
        }
        history.push(copy);
    }

    public void undoMove() {
        if (!history.isEmpty()) {
            board = history.pop();
        }
    }
}