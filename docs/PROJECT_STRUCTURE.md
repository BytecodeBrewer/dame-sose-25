# Projektstruktur (Überblick)

## Architektur

Das Projekt orientiert sich an einem einfachen MVC-Schnitt:

- **Model** (`model/`): Zustand und Regeln auf Datenebene.
- **View** (`view/`): Swing-UI, Rendering und Nutzerinteraktion.
- **Controller** (`controller/`): Spiellogik, Validierung, Ablaufsteuerung.

## Dateien

### Root
- `Main.java`
  - Einstiegspunkt, startet das Swing-Startfenster.

### `controller/`
- `GameController.java`
  - Zentraler Ablaufcontroller (Zuglogik, Player-Wechsel, Spielstatus).
- `GamePresenter.java`
  - Presenter-Interface für UI-Updates.

### `model/`
- `Board.java`
  - Brettdaten, Initialisierung, Feldzugriff.
- `Piece.java`
  - Steinlogik (Man/Dame, Capture-Regeln auf Stein-Ebene).
- `Player.java`
  - Spielerverwaltung und Steinlisten.

### `view/`
- `StartFrame.java`
  - Startscreen mit Moduswahl.
- `MainView.java`
  - Hauptfenster inkl. Statusbar und Debug-Controls.
- `BoardView.java`
  - Zeichenfläche des Bretts und Mausklick-Verarbeitung.

## Aktueller technischer Stand

- Kein Build-System (Maven/Gradle) hinterlegt.
- Keine Testsuite im Repository.
- Dokumentation bisher auf grundlegenden Einstieg fokussiert.

