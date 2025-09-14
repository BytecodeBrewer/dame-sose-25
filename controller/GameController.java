import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/*
GameController managt den Spielablauf, Spielerwechsel, Zugvalidierung und Spielstatus.
Es kommuniziert mit dem Board für Spiellogik und dem GamePresenter für UI-Updates.
Zurzeit ist der Gamecontroller sehr umfangreich. In zukünftigen Versionen könnte die Logik
weiter aufgeteilt werden, z.B. in separate Klassen für Zugvalidierung, Spielstatus und Spielregeln.
*/ 
public class GameController {
    private final Board board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private GamePresenter presenter;
    private Mode mode;
    public enum Mode { NORMAL, DEBUG }
    public enum Cell { EMPTY, WM, WK, BM, BK } // White/Black Man/King
    private java.util.List<Point> currentLegalTargets = new ArrayList<>();
    private Point selectedCell = null;
    private String lastError = "";

    // ViewState kapselt den aktuellen Zustand des Spiels für die UI
    public static class ViewState {
        public final Cell[][] grid;
        public final java.util.List<java.awt.Point> legalTargets;
        public final java.awt.Point selected;
        public final String currentPlayerName;
        public final String errorMessage;
        public ViewState(Cell[][] g, List<Point> t, Point s, String n, String err){
            this.grid = g; this.legalTargets = t; this.selected = s;
            this.currentPlayerName = n; this.errorMessage = err;
        }
    }


    // Konstruktor initialisiert Spieler, Brett und Spielmodus
    public GameController(Mode mode) {
        this.player1 = new Player("Player 1");
        this.player2 = new Player("Player 2");
        this.board = new Board(player1, player2);
        this.mode = mode;
        this.currentPlayer = player1;
    }

    // Generiert den aktuellen ViewState für die UI
    // Dies umfasst das Brett, legale Züge, ausgewählte Figur und Fehlermeldungen
    // Es ermöglicht dem Presenter, den UI-Zustand basierend auf der Spiellogik zu rendern
    public ViewState getViewState() {
        return new ViewState(
            buildGrid(),
            currentLegalTargets,
            selectedCell,
            currentPlayer != null ? currentPlayer.getName() : "Unknown",
            lastError
        );
    }

    // Baut das 8x8 Spielfeld-Array für die UI, um die Figuren darzustellen, basierend auf dem Board-Zustand
    // Jede Zelle wird in einen Cell-Enum-Wert umgewandelt, der den Figurentyp und die Farbe repräsentiert
    // Leere Felder werden als EMPTY dargestellt
    private Cell[][] buildGrid() {
        Cell[][] grid = new Cell[8][8];
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                Piece p = board.getPieceAt(row, column);
                grid[row][column] = mapPieceToCell(p);
            }
        }
        return grid;
    }

    // Hilfsmethode, die eine Piece-Instanz in den entsprechenden Cell-Enum-Wert umwandelt
    private Cell mapPieceToCell(Piece p) {
        // Wenn kein Stein vorhanden ist, gib EMPTY zurück
        if (p == null) return Cell.EMPTY;
        boolean isWhite = (p.getColor() == Piece.PieceColor.WHITE);
        boolean isMan = p.isMan();
        
        // Rückgabe des entsprechenden Cell-Werts basierend auf Farbe und Typ
        if (isWhite) {
            return isMan ? Cell.WM : Cell.WK;
        } else {
            return isMan ? Cell.BM : Cell.BK;
        }
    }

    // Handhabt Klicks auf das Spielfeld
    public void onCellClick(int row, int column) {
    // Wenn keine Zelle ausgewählt ist, überprüfe ob eine Auswahl möglich ist
    if (handleInitialSelection(row, column)) return;
    // Wenn bereits eine Zelle ausgewählt ist, überprüfe ob es ein Deselektieren möglich ist
    if (handleDeselect(row, column)) return;
    // Wenn eine Zelle ausgewählt ist, überprüfe ob es ein Reselect möglich ist
    if (handleReselect(row, column)) return;
    // Nachdem eine Zelle ausgewählt wurde, versuche den Zug auszuführen
    handleMoveAttempt(row, column);
    // Aktualisiere die UI nach jedem Zug
    refresh();
    }

    // Handhabt die anfängliche Auswahl einer Zelle
    private boolean handleInitialSelection(int row, int col) {
        // Nur wenn noch keine Zelle ausgewählt ist
        if (selectedCell != null) return false;

        Piece piece = board.getPieceAt(row, col);
        // Nur Auswahl erlauben, wenn eine Figur des aktuellen Spielers vorhanden ist
        if (piece != null && piece.getColor() == currentPlayer.getColor()) {
            selectedCell = new Point(row, col);
            lastError = "";
        } else {
            lastError = "Ungültige Auswahl.";
            if (presenter != null) presenter.showError(lastError);
        }
        refresh();
        // Auswahl erfolgreich
        return true;
    }

    // Handhabt das Deselektieren der aktuell ausgewählten Zelle
    private boolean handleDeselect(int row, int col) {
        // Wenn keine Zelle ausgewählt ist, nichts zu tun
        if (selectedCell == null) return false;
        // Wenn die angeklickte Zelle nicht die ausgewählte ist, nichts zu tun
        if (selectedCell.x != row || selectedCell.y != col) return false;

        selectedCell = null;
        lastError = "";
        refresh();
        // Deselektieren erfolgreich
        return true;
    }

    // Handhabt das Reselecten einer anderen Zelle mit einer eigenen Figur
    private boolean handleReselect(int row, int col) {
        // Wenn keine Zelle ausgewählt ist, nichts zu tun
        if (selectedCell == null) return false;

        Piece piece = board.getPieceAt(row, col);
        // Wenn die angeklickte Zelle dem aktuellen Spieler nicht gehört, nichts zu tun
        if (piece == null || piece.getColor() != currentPlayer.getColor()) return false;

        selectedCell = new Point(row, col);
        lastError = "";
        refresh();
        // Reselect erfolgreich
        return true;
    }

    // Handhabt den Versuch, einen Zug von der ausgewählten Zelle zur angeklickten Zelle auszuführen
    private void handleMoveAttempt(int row, int col) {
        // Prüft, ob der Zug gültig ist und speichert das Ergebnis
        boolean success = makeMove(selectedCell.x, selectedCell.y, row, col);

        // Setzt Fehlermeldung oder löscht sie basierend auf dem Ergebnis
        if (!success) {
            lastError = "Ungültiger Zug.";
            if (presenter != null) presenter.showError(lastError);
        } else {
            lastError = "";
            selectedCell = null;
        }
    }

    // Setter für den Presenter, um UI-Updates zu ermöglichen
    public void setPresenter(GamePresenter presenter) {
        this.presenter = presenter;
        refresh();
    }

    // Aktualisiert die UI durch Aufruf des Presenters mit dem aktuellen ViewState
    private void refresh() {
        if (presenter != null) presenter.render(getViewState());
    }

    // Startet das Spiel basierend auf dem Modus (NORMAL oder DEBUG)
    public void startGame() {
        if (mode == Mode.NORMAL) {
            // Standardbrett initialisieren
            board.initialize();
            currentPlayer = player1;
            refresh();
        } else {
            // Leeres Brett für Debug-Modus
            board.getClearBoard();
            currentPlayer = player1;
            refresh();
        }
    }

    // Getter für den aktuellen Spielmodus, damit die UI entsprechend reagieren kann
    public Mode getMode() {
        return mode;
    }

    // Getter für den aktuellen Spieler
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    // Methode zum Setzen eines weißen Steins an einer bestimmten Position
    // Wird im Debug-Modus verwendet, um das Brett manuell zu konfigurieren
    public void setWhitePiece(int i, int j) {
        board.setWhitePiece(i, j);
        // Wenn der weiße Stein auf die letzte Reihe gesetzt wird, wird er sofort zur Dame befördert
        if ((i == 0)) {
            board.promotetoDame(i, j);
        }
    }

    // Methode zum Setzen eines schwarzen Steins an einer bestimmten Position
    // Wird im Debug-Modus verwendet, um das Brett manuell zu konfigurieren
    public void setBlackPiece(int i, int j) {
        board.setBlackPiece(i, j);
        // Wenn der schwarze Stein auf die letzte Reihe gesetzt wird, wird er sofort zur Dame befördert
        if ((i == 7)) {
            board.promotetoDame(i, j);
        }
    }

    // Validiert, ob ein Zug von Startposition nach Zielposition erlaubt ist
    public boolean isValidMove(int fromX, int fromY, int toX, int toY) {
        Piece piece = board.getPieceAt(fromX, fromY);
        // Prüfte, ob eine Figur an der Startposition vorhanden ist
        if (piece == null)
            return false;
        
        // Prüft, ob der Stein dem aktuellen Spieler gehört
        if (piece.getOwner() != currentPlayer)
            return false;
        
        // Prüft ob ein Schlagzwang existiert, wenn ja, dann muss zuerst geschlagen werden
        if (hasCaptureMoves(currentPlayer)) {
            // Wenn der Zug kein Schlag ist, ist er ungültig
            return isValidCapture(fromX, fromY, toX, toY);
        }

        // Prüft, ob das Zielfeld frei ist
        if (!board.isFieldFree(toX, toY))
            return false;
        
        // Prüft zuerst um welche Art von Stein es sich handelt
        if (piece.getType() == Piece.PieceType.MAN) {
            return validMoveForMan(toX, toY, fromX, fromY);
        } else {
            return validMoveForKing(toX, toY, fromX, fromY);
        }
    }

    private boolean validMoveForMan(int toX,int toY,int fromX,int fromY) {
        Piece piece = board.getPieceAt(fromX, fromY);
        // Weiße Steine dürfen nur nach oben
        if (piece.getColor() == Piece.PieceColor.WHITE && toX >= fromX) {
            return false;
        }
        // Schwarze Steine dürfen nur nach unten
        if (piece.getColor() == Piece.PieceColor.BLACK && toX <= fromX) {
            return false;
        }
        return Math.abs(toX - fromX) == 1 && Math.abs(toY - fromY) == 1;
    }

    private boolean validMoveForKing(int toX,int toY,int fromX,int fromY) {
        Piece piece = board.getPieceAt(fromX, fromY);
        // Weiße Steine dürfen nur nach oben
        // Logik für Dame-Steine, dass sie sich diagonal über beliebig viele Felder bewegen können
            if (board.isFieldFree(toX, toY) && !board.isFieldOccupiedByOpponent(toX, toY, piece.getOwner())
                    && Math.abs(toX - fromX) == Math.abs(toY - fromY) && !piece.mustCapture(board)) {
                int dx = (toX - fromX) > 0 ? 1 : -1;
                int dy = (toY - fromY) > 0 ? 1 : -1;
                int steps = Math.abs(toX - fromX);
                for (int i = 1; i < steps; i++) {
                    int checkX = fromX + dx * i;
                    int checkY = fromY + dy * i;
                    if (!board.isFieldFree(checkX, checkY)) {
                        return false;
                    }
                }
                return true;
            }
        return false;
    }

    private boolean hasCaptureMoves(Player player) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board.getPieceAt(x, y);
                if (piece != null && piece.getOwner() == player) {
                    // Prüfe alle möglichen Schlagrichtungen
                    if (canCaptureInAnyDirection(x, y)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean canCaptureInAnyDirection(int x, int y) {
    Piece piece = board.getPieceAt(x, y);
    if (piece == null) return false;

    if (piece.getType() == Piece.PieceType.MAN) {
        // Nur 2er-Sprünge
        int[][] directions = {{2,2}, {2,-2}, {-2,2}, {-2,-2}};
        for (int[] dir : directions) {
            int toX = x + dir[0];
            int toY = y + dir[1];
            if (!board.outOfBounds(toX, toY) && isValidCapture(x, y, toX, toY)) {
                return true;
            }
        }
    } else {
        // Dame: alle Felder auf den Diagonalen durchgehen
        int[][] directions = {{1,1}, {1,-1}, {-1,1}, {-1,-1}};
        for (int[] dir : directions) {
            int stepX = dir[0], stepY = dir[1];
            int toX = x + stepX, toY = y + stepY;
            while (!board.outOfBounds(toX, toY)) {
                if (isValidCapture(x, y, toX, toY)) {
                    return true;
                }
                toX += stepX;
                toY += stepY;
            }
        }
    }
    return false;
}


    private boolean isValidCapture(int fromX, int fromY, int toX, int toY) {
        Piece piece = board.getPieceAt(fromX, fromY);
        if (piece == null) return false;

        if (piece.getOwner() != currentPlayer) return false;

        // Ziel muss frei sein und Diagonale bleiben
        if (!board.isFieldFree(toX, toY)) return false;
        int dx = toX - fromX;
        int dy = toY - fromY;
        if (Math.abs(dx) != Math.abs(dy)) return false;

        if (piece.getType() == Piece.PieceType.MAN) {
            // Klassischer 2er-Sprung
            if (Math.abs(dx) != 2 || Math.abs(dy) != 2) return false;
            int mx = (fromX + toX) / 2;
            int my = (fromY + toY) / 2;
            Piece mid = board.getPieceAt(mx, my);
            return mid != null && mid.getOwner() != currentPlayer;
        } else {
            // Dame: "fliegender" Schlag – genau EINE gegnerische Figur auf dem Weg
            int stepx = Integer.signum(dx);
            int stepy = Integer.signum(dy);
            int x = fromX + stepx, y = fromY + stepy;
            Piece captured = null;
            int capturedX = -1, capturedY = -1;

            while (x != toX && y != toY) {
                Piece p = board.getPieceAt(x, y);
                if (p == null) {
                    // leer -> weiter
                } else if (p.getOwner() == currentPlayer) {
                    // Eigene Figur blockiert
                    return false;
                } else {
                    // Gegnerische Figur
                    if (captured != null) return false; // schon eine gesehen -> ungültig
                        captured = p;
                        capturedX = x;
                        capturedY = y;
                }
                x += stepx; y += stepy;
            }
            // gültig nur, wenn genau eine gegnerische Figur übersprungen wurde und das Zielfeld hinter ihr liegt
            return captured != null
                && toX == capturedX + stepx
                && toY == capturedY + stepy;
        }
    }

    public boolean makeMove(int fromX, int fromY, int toX, int toY) {
        
        Piece piece = board.getPieceAt(fromX, fromY);
        if (piece == null) return false;

        boolean isCapture = isValidCapture(fromX, fromY, toX, toY);
        boolean isSimpleMove = !isCapture && isValidMove(fromX, fromY, toX, toY);

        if (!isCapture && !isSimpleMove) {
            if (presenter != null) presenter.showError("Ungültiger Zug.");
            return false;
        }

        // ggf. geschlagene Figur entfernen
        if (isCapture) {
            if (piece.getType() == Piece.PieceType.MAN) {
                int mx = (fromX + toX) / 2;
                int my = (fromY + toY) / 2;
                Piece cap = board.getPieceAt(mx, my);
                if (cap != null) { 
                    board.freeField(mx, my);
                    cap.getOwner().removePiece(cap);
                }
            } else {
                // Dame: finde die übersprungene gegnerische Figur entlang der Diagonale
                int dx = Integer.signum(toX - fromX);
                int dy = Integer.signum(toY - fromY);
                int cx = fromX + dx, cy = fromY + dy;
                while (cx != toX && cy != toY) {
                    Piece pth = board.getPieceAt(cx, cy);
                    if (pth != null && pth.getOwner() != currentPlayer) {
                        board.freeField(cx, cy);
                        pth.getOwner().removePiece(pth);
                        break; // genau eine Figur muss entfernt werden
                    }
                    cx += dx; cy += dy;
                }
            }
        }

        // Figur ziehen
        board.freeField(fromX, fromY);
        board.occupyField(toX, toY, piece);
        piece.move(board, fromX, fromY, toX, toY);



        // Nur wenn der aktuelle Zug ein Schlag war UND noch ein Schlag möglich ist,
        // bleibt der Spieler dran (Mehrfachschlag). Nach einem normalen Zug IMMER Wechsel.
        if (isCapture && canCaptureInAnyDirection(toX, toY)) {
            arePiecesLeft();
            arePieceStuck();
            if (piece.getType() == Piece.PieceType.MAN) {
            if ((piece.getColor() == Piece.PieceColor.WHITE && toX == 0) ||
                (piece.getColor() == Piece.PieceColor.BLACK && toX == 7)) {
                board.promotetoDame(toX, toY);
                switchPlayer();
            }
        }
        } else {
            if (piece.getType() == Piece.PieceType.MAN) {
            if ((piece.getColor() == Piece.PieceColor.WHITE && toX == 0) ||
                (piece.getColor() == Piece.PieceColor.BLACK && toX == 7)) {
                board.promotetoDame(toX, toY);
            }
            }
            arePiecesLeft();
            arePieceStuck();
            switchPlayer();
        }

        if (presenter != null) presenter.render(getViewState());
        return true;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        if (presenter != null) {
            presenter.render(getViewState());
        }
    }

    private void arePiecesLeft() {
        if (player1.getPieceCount() == 0) {
            if (presenter != null) {
                presenter.showWinnerDialog(player2.getName());
                return;
            }
        } else if (player2.getPieceCount() == 0) {
            if (presenter != null) {
                presenter.showWinnerDialog(player1.getName());
                return;
            }
        }
    }

    private void arePieceStuck() {
        if (player1.getPieceCount() == 0 || player2.getPieceCount() == 0) {
            return; // Spiel ist schon vorbei
        }
        if (!hasAnyValidMoves(player1)) {
            if (presenter != null) {
                presenter.showWinnerDialog(player2.getName());
            }
        } else if (!hasAnyValidMoves(player2)) {
            if (presenter != null) {
                presenter.showWinnerDialog(player1.getName());
            }
        }
        player1.clearCapturedPieces();
        player2.clearCapturedPieces();
    }

    private boolean hasAnyValidMoves(Player player) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board.getPieceAt(x, y);
                if (piece != null && piece.getOwner() == player) {
                    // Prüfe alle möglichen Zielfelder
                    if(isPieceStuck(piece, x, y)) {
                        player.capturePiece(piece);
                    }
                }
            }
        }
        return player.getCapturedPieces().size() < player.getPieceCount();
    }

    private boolean isPieceStuck(Piece piece, int fromX, int fromY) {
        // Prüfe ob der Zug gültig ist
        // Prüfe, ob das Zielfeld frei ist oder außerhalb des Brettes liegt
        if (stuckForMoving(piece, fromX, fromY)) {
            if(stuckForCapturing(piece, fromX, fromY)) {
                return true;
            } else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    private boolean stuckForMoving(Piece piece, int fromX, int fromY) {
        if (piece.getColor() == Piece.PieceColor.WHITE) {
            if (piece.getType() == Piece.PieceType.MAN) {
                int toX = fromX - 1;
                int toY1 = fromY - 1;
                int toY2 = fromY + 1;
                if (board.outOfBounds(toX, toY1) || !board.isFieldFree(toX, toY1)){
                    if (board.outOfBounds(toX, toY2) || !board.isFieldFree(toX, toY2)) {
                        return true;
                    }
                    else return false;
                }

                
            } else {
                    int x = fromX;
                    int y = fromY;
                    for (int step = 1; step < 8; step++) {
                        int[][] directions = {{step, step}, {step, -step}, {-step, step}, {-step, -step}};
                        for (int[] dir : directions) {
                            int toX = x + dir[0];
                            int toY = y + dir[1];
                            if (board.outOfBounds(toX, toY)) {
                                continue;
                            }
                            if (!board.isFieldFree(toX, toY) && !board.isFieldOccupiedByOpponent(toX, toY, piece.getOwner())) {
                                continue;
                            }
                            if (!board.isFieldFree(toX, toY) && board.isFieldOccupiedByOpponent(toX, toY, piece.getOwner())) {
                                continue;
                            }
                            if (isValidMove(toX, toY, fromX, fromY)) {
                                return false;
                            }
                        }
                    }
                return true;
            } 
        }
        else {
            if (piece.getType() == Piece.PieceType.MAN) {
                int toX = fromX + 1;
                int toY1 = fromY - 1;
                int toY2 = fromY + 1;
                if (board.outOfBounds(toX, toY1) || !board.isFieldFree(toX, toY1)){
                    if (board.outOfBounds(toX, toY2) || !board.isFieldFree(toX, toY2)) return true;
                    else return false;
                }
                
            } else {
                    int x = fromX;
                    int y = fromY;
                    for (int step = 1; step < 8; step++) {
                        int[][] directions = {{step, step}, {step, -step}, {-step, step}, {-step, -step}};
                        for (int[] dir : directions) {
                            int toX = x + dir[0];
                            int toY = y + dir[1];
                            if (board.outOfBounds(toX, toY)) {
                                continue;
                            }
                            if (!board.isFieldFree(toX, toY) && !board.isFieldOccupiedByOpponent(toX, toY, piece.getOwner())) {
                                continue;
                            }
                            if (!board.isFieldFree(toX, toY) && board.isFieldOccupiedByOpponent(toX, toY, piece.getOwner())) {
                                continue;
                            }
                            if (isValidMove(toX, toY, fromX, fromY)) {
                                return false;
                            }
                        }
                    }
                return true;
            }
        }
        return false;
    }

    private boolean stuckForCapturing(Piece piece, int fromX, int fromY) {
        if(piece.getType() == Piece.PieceType.MAN) {
            // Klassischer 2er-Sprung
            int[][] directions = {{2,2}, {2,-2}, {-2,2}, {-2,-2}};

            for (int[] dir : directions) {
                int toX = fromX + dir[0];
                int toY = fromY + dir[1];
                if (board.outOfBounds(toX, toY)) {
                    continue;
                }
                if (isValidCapture(fromX, fromY, toX, toY)) {
                    return false;
                }
            }
            return true;
        } else {
            // Dame: "fliegender" Schlag – genau EINE gegnerische Figur auf dem Weg
            int[][] directions = {{2,2}, {2,-2}, {-2,2}, {-2,-2}};
            for (int[] dir : directions) {
                int toX = fromX + dir[0];
                int toY = fromY + dir[1];
                if (board.outOfBounds(toX, toY)) {
                    continue;
                }
                if (isValidCapture(fromX, fromY, toX, toY)) {
                    return false;
                }
            }
            return true;
        }
    }
}