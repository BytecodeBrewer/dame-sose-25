public class GameController {
    private Board board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private MainView mainView;

    public GameController() {
        this.player1 = new Player("Player 1"); // Weiß
        this.player2 = new Player("Player 2"); // Schwarz
        this.board = new Board(player1, player2);
        this.currentPlayer = player1; // Weiß beginnt
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public void startGame() {
        board.initialize();
        currentPlayer = player1; // Weiß beginnt immer
    }

    // Prüft, ob der Zug gültig ist: das startfeld enthält eine Spielfigur des
    // aktuellen Spielers,
    // das Zielfeld ist frei und die Bewegung ist laut Piece erlaubt
    public boolean isValidMove(int fromX, int fromY, int toX, int toY) {
        Piece piece = board.getPieceAt(fromX, fromY);
        // Prüfe zuerst, ob überhaupt ein Stein vorhanden ist
        if (piece == null)
            return false;
        // Prüfe, ob der Stein dem aktuellen Spieler gehört
        if (piece.getOwner() != currentPlayer)
            return false;
        
        
        // Prüfe, ob das Zielfeld frei ist
        if (!board.isFieldFree(toX, toY))
            return false;
            // Prüfe Bewegungsrichtung für normale Steine
        
        if (piece.getType() == Piece.PieceType.MAN) {
            // Weiße Steine dürfen nur nach oben
            if (piece.getColor() == Piece.PieceColor.WHITE && toX >= fromX) {
                return false;
            }
            // Schwarze Steine dürfen nur nach unten
            if (piece.getColor() == Piece.PieceColor.BLACK && toX <= fromX) {
                return false;
            }
        }
        
        return piece.canMove(board, toX, toY, fromX, fromY);
    }

    public boolean makeMove(int fromX, int fromY, int toX, int toY) {
        if (isValidMove(fromX, fromY, toX, toY)) {
            Piece piece = board.getPieceAt(fromX, fromY);
            board.freeField(fromX, fromY);
            board.occupyField(toX, toY, piece);
            piece.move(board, fromX, fromY, toX, toY);

            // Prüfe Promotion
            if (piece.getType() == Piece.PieceType.MAN) {
                if ((piece.getColor() == Piece.PieceColor.WHITE && toX == 0) ||
                    (piece.getColor() == Piece.PieceColor.BLACK && toX == 7)) {
                    board.promotetoDame(toX, toY);
                }
            }

            switchPlayer();
            if (mainView != null) {
                mainView.clearError();  // NEU: Fehler zurücksetzen bei erfolgreichen Zügen
            }
            return true;
        } else {
            // NEU: Fehlermeldung anzeigen
            if (mainView != null) {
                mainView.showError("Ungültiger Zug!");
            }
            return false;
        }
    }

    public void addBoardChangeListener(Runnable listener) {
        board.addChangeListener(listener);
    }

    public void onSquareClicked(int x, int y) {
        // Diese Methode kann verwendet werden, um auf Klicks auf dem Brett zu reagieren
        // z.B. um eine Auswahl anzuzeigen oder einen Zug vorzubereiten
        board.selectSquare(x, y);
    }

    // ich halte diese Methode für nicht notwendig, da es eher etwas für Debugging
    // ist. Daher soll es später entfernt werden
    public void onMoveAttempt(int fromX, int fromY, int toX, int toY, boolean moved) {
        // Diese Methode zeigt im Terminal die Zugversuche an
        if (moved) {
            System.out.println("Zug erfolgreich: " + fromX + "," + fromY + " -> " + toX + "," + toY);
        } else {
            System.out.println("Ungültiger Zug: " + fromX + "," + fromY + " -> " + toX + "," + toY);
        }
    }

    public void resetGame() {
        board.initialize();
        currentPlayer = player1;
        // NEU: Aktualisiere Spieleranzeige nach Reset
        if (mainView != null) {
            mainView.setCurrentPlayerDisplay(currentPlayer.getName());
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        if (mainView != null) {
            mainView.setCurrentPlayerDisplay(currentPlayer.getName());
        }
    }
}