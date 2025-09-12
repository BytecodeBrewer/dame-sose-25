public class GameController {
    private Board board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private MainView mainView;
    private StartFrame mode;
    private String convertMode = null;

    public GameController() {
        this.player1 = new Player("Player 1"); // Weiß
        this.player2 = new Player("Player 2"); // Schwarz
        this.board = new Board(player1, player2);
        this.currentPlayer = player1; // Weiß beginnt
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    public void setDebugMode(MainView mainView) {
        this.mainView = mainView;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public void setWhitePiece(int i, int j) {
        board.setWhitePiece(i, j);
        if ((i == 0)) {
            board.promotetoDame(i, j);
        }
    }

    public void setBlackPiece(int i, int j) {
        board.setBlackPiece(i, j);
        if ((i == 7)) {
            board.promotetoDame(i, j);
        }
    }

    public void startDebugMode() {
        board.getClearBoard();
        currentPlayer = player1;
    }

    public void startGame() {
        board.initialize();
        currentPlayer = player1;
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
        
                // Prüfe ob ein Schlagzwang existiert
        if (hasCaptureMoves(currentPlayer)) {
            // Wenn Schlagzwang existiert, muss der Zug ein Schlagzug sein
            return isValidCapture(fromX, fromY, toX, toY);
        }

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
            return Math.abs(toX - fromX) == 1 && Math.abs(toY - fromY) == 1;
        }
        
        return piece.canMove(board, toX, toY, fromX, fromY);
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
        // Prüfe alle 4 möglichen Schlagrichtungen
        int[][] directions = {{2,2}, {2,-2}, {-2,2}, {-2,-2}};

        for (int[] dir : directions) {
            if (board.outOfBounds(x + dir[0], y + dir[1])) {
                continue;
            }
            int toX = x + dir[0];
            int toY = y + dir[1];
            
            if (isValidCapture(x, y, toX, toY)) {
                return true;
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
                }
                x += stepx; y += stepy;
            }
            // gültig nur, wenn genau eine gegnerische Figur übersprungen wurde
            return captured != null;
        }
    }

    public boolean makeMove(int fromX, int fromY, int toX, int toY) {
        
        Piece piece = board.getPieceAt(fromX, fromY);
        if (piece == null) return false;

        boolean isCapture = isValidCapture(fromX, fromY, toX, toY);
        boolean isSimpleMove = !isCapture && isValidMove(fromX, fromY, toX, toY);

        if (!isCapture && !isSimpleMove) {
            if (mainView != null) mainView.showError("Ungültiger Zug!");
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

        // Promotion bei normalem Stein
        if (piece.getType() == Piece.PieceType.MAN) {
            if ((piece.getColor() == Piece.PieceColor.WHITE && toX == 0) ||
                (piece.getColor() == Piece.PieceColor.BLACK && toX == 7)) {
                board.promotetoDame(toX, toY);
            }
        }

        // Nur wenn der aktuelle Zug ein Schlag war UND noch ein Schlag möglich ist,
        // bleibt der Spieler dran (Mehrfachschlag). Nach einem normalen Zug IMMER Wechsel.
        if (isCapture && canCaptureInAnyDirection(toX, toY)) {
            arePiecesLeft();
            arePieceStuck();
        } else {
            arePiecesLeft();
            arePieceStuck();
            switchPlayer();
        }

        if (mainView != null) mainView.clearError();
        return true;
    }


    public void addBoardChangeListener(Runnable listener) {
        board.addChangeListener(listener);
    }

    // Diese Methode kann verwendet werden, um auf Klicks auf dem Brett zu reagieren
    // z.B. um eine Auswahl anzuzeigen oder einen Zug vorzubereiten
    public void onSquareClicked(int x, int y) {
        board.selectSquare(x, y);
    }

    public void resetGame() {
        convertMode = mode.getMode();

        if ("debug".equals(convertMode)) {
            board.getClearBoard();
        } else {
            board.initialize();
        }
        currentPlayer = player1;
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

    private void arePiecesLeft() {
        if (player1.getPieceCount() == 0) {
            if (mainView != null) {
                mainView.showWinnerDialog(player2.getName());
                return;
            }
        } else if (player2.getPieceCount() == 0) {
            if (mainView != null) {
                mainView.showWinnerDialog(player1.getName());
                return;
            }
        }
    }

    private void arePieceStuck() {
        if (player1.getPieceCount() == 0 || player2.getPieceCount() == 0) {
            return; // Spiel ist schon vorbei
        }
        if (!hasAnyValidMoves(player1)) {
            if (mainView != null) {
                mainView.showWinnerDialog(player2.getName());
            }
        } else if (!hasAnyValidMoves(player2)) {
            if (mainView != null) {
                mainView.showWinnerDialog(player1.getName());
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
        System.out.println("Anzahl gefangene Steine von " + player.getName() + ": " + player.getCapturedPieces().size());
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
                        System.out.println("Stein bei (" + fromX + "," + fromY + ") kann sich nicht bewegen. 1");
                        return true;
                    }
                    else return false;
                }

                
            } else {
                // Dame: move any number of squares diagonally, path must be clear
                    int x = fromX;
                    int y = fromY;
                    for (int step = 1; step < 8; step++) {
                        // Check all four diagonal directions
                        int[][] directions = {{step, step}, {step, -step}, {-step, step}, {-step, -step}};
                        for (int[] dir : directions) {
                            int toX = x + dir[0];
                            int toY = y + dir[1];
                            if (board.outOfBounds(toX, toY)) {
                                continue;
                            }
                            if (!board.isFieldFree(toX, toY) && !board.isFieldOccupiedByOpponent(toX, toY, piece.getOwner())) {
                                System.out.println("Stein bei (" + fromX + "," + fromY + ") kann sich nicht bewegen. 11");
                                continue; // Blocked by own piece
                            }
                            if (!board.isFieldFree(toX, toY) && board.isFieldOccupiedByOpponent(toX, toY, piece.getOwner())) {
                                System.out.println("Stein bei (" + fromX + "," + fromY + ") kann sich nicht bewegen. 2");
                                continue; // Blocked by opponent's piece
                            }
                            // Valid move found
                            if (piece.canMove(board, toX, toY, fromX, fromY)) {
                                System.out.println("Stein bei (" + fromX + "," + fromY + ") kann sich bewegen.");
                                return false; // Found a valid move
                            }
                        }
                    }
                return true; // No valid moves found in this direction
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
                // Dame: move any number of squares diagonally, path must be clear
                    int x = fromX;
                    int y = fromY;
                    for (int step = 1; step < 8; step++) {
                        // Check all four diagonal directions
                        int[][] directions = {{step, step}, {step, -step}, {-step, step}, {-step, -step}};
                        for (int[] dir : directions) {
                            int toX = x + dir[0];
                            int toY = y + dir[1];
                            if (board.outOfBounds(toX, toY)) {
                                continue;
                            }
                            if (!board.isFieldFree(toX, toY) && !board.isFieldOccupiedByOpponent(toX, toY, piece.getOwner())) {
                                System.out.println("Stein bei (" + fromX + "," + fromY + ") kann sich nicht bewegen. 11");
                                continue; // Blocked by own piece
                            }
                            if (!board.isFieldFree(toX, toY) && board.isFieldOccupiedByOpponent(toX, toY, piece.getOwner())) {
                                System.out.println("Stein bei (" + fromX + "," + fromY + ") kann sich nicht bewegen. 2");
                                continue; // Blocked by opponent's piece
                            }
                            // Valid move found
                            if (piece.canMove(board, toX, toY, fromX, fromY)) {
                                System.out.println("Stein bei (" + fromX + "," + fromY + ") kann sich bewegen.");
                                return false; // Found a valid move
                            }
                        }
                    }
                return true; // No valid moves found in this direction
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
                    System.out.println("out of bounds: (" + toX + "," + toY + ")");
                    continue;
                }
                if (isValidCapture(fromX, fromY, toX, toY)) {
                    System.out.println("Stein bei (" + fromX + "," + fromY + ") kann schlagen.");
                    return false;
                }
            }
            return true;
        }
    }
}