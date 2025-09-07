import java.util.ArrayList;
import java.util.List;
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
    private Player player1; // Spieler für weiße Steine
    private Player player2; // Spieler für schwarze Steine
    // Neue Felder für Listener und Auswahl
    private List<Runnable> changeListeners = new ArrayList<>();
    private boolean[][] selectedSquares;
    private boolean debugMode = false;

    // Error handling
    private ErrMes errmes = ErrMes.NONE;
    private Stack<Piece[][]> history = new Stack<>();

    public Board(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        board = new Piece[SIZE][SIZE];
        selectedSquares = new boolean[SIZE][SIZE];
        if (!debugMode) {
            initializeBoard();
        } else {
            clearBoard();
        }
    }

    public void clearBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = null;
                selectedSquares[i][j] = false;
            }
        }
    }

    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                selectedSquares[i][j] = false;
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

    // Fügt einen Listener für Änderungen hinzu
    public void addChangeListener(Runnable listener) {
        if (!changeListeners.contains(listener)) {
            changeListeners.add(listener);
        }
    }

    // Markiert ein Feld als ausgewählt
    public void selectSquare(int x, int y) {
        // Validiere Koordinaten
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            return;
        }

        // Setze alle Felder zurück
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                selectedSquares[i][j] = false;
            }
        }

        selectedSquares[x][y] = true;
        notifyChangeListeners();
    }

    // Benachrichtigt alle registrierten Listener
    private void notifyChangeListeners() {
        for (Runnable listener : changeListeners) {
            listener.run();
        }
    }

    // Prüft, ob ein Feld ausgewählt ist
    public boolean isSquareSelected(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE && selectedSquares[x][y];
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