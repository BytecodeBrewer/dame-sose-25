public class Main {
    public static void main(String[] args) {        
        // Es ist das Kernstück des MVC Musters
        // Es wird der Controller instanziert, der die Logik des Spiels steuert
        // und die Kommunikation zwischen Model (Board, Piece, Player) und View (MainView) übernimmt
        // Der StartFrame wird in der Swing-Event-Dispatching-Thread gestartet
        // Das Sartframe ist das erste Fenster, das der Benutzer sieht
        // und von dem aus das eigentliche Spiel gestartet wird
        // Es ermöglicht die Auswahl des Spielmodus (z.B. Normal oder Debug)
        // und initialisiert den GameController und die MainView entsprechend
        javax.swing.SwingUtilities.invokeLater(() -> {
            new StartFrame();

        });

    }
}