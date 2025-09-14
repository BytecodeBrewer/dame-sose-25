import java.awt.Point;

public interface GamePresenter {
    // Wird nach jedem Zustandswechsel vom Controller aufgerufen
    void render(GameController.ViewState vs);

    // UI-Hinweise / Dialoge
    void showError(String message);
    void showWinnerDialog(String playerName);

    // Optional, falls du Koordinaten o. ä. hervorheben willst:
    default void highlight(Point cell) {}
}
