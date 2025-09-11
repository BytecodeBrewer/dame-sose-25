public class App {
    public static void main(String[] args) {        
        // Controller für das Spiel erstellen
        // Es ist das Kernstück des MVC Musters
        // Es steuert die Logik des Spiels und interagiert mit dem Modell
        // und der Ansicht.
        javax.swing.SwingUtilities.invokeLater(() -> {
            new StartFrame();  // StartFrame anzeigen

        });

    }
}