import java.util.ArrayList;
import java.util.List;

public class Board {
    private Piece[][] board;
    private final int SIZE = 8;
    private Player player1; // Spieler für weiße Steine
    private Player player2; // Spieler für schwarze Steine
    // Neue Felder für Listener und Auswahl
    private List<Runnable> changeListeners = new ArrayList<>();
    private boolean[][] selectedSquares;

    public Board(Player player1, Player player2) {
        this.player1 = player1; // Weiß
        this.player2 = player2; // Schwarz
        board = new Piece[SIZE][SIZE];
        selectedSquares = new boolean[SIZE][SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                selectedSquares[i][j] = false;
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
    
    public void setPieceAt(int x, int y, Piece piece) {
        board[x][y] = piece;
    }

    public Piece[][] getBoard() {
        return board;
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
}