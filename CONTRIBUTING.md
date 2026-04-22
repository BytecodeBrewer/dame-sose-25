# Contributing Guide

Danke für dein Interesse am Projekt.

## Grundregeln

1. Änderungen bitte klein und nachvollziehbar halten.
2. Commit-Nachrichten klar formulieren (was + warum).
3. Für größere Änderungen zuerst ein kurzes Issue/Design-Note anlegen.
4. Java-Logik nicht „nebenbei“ umbauen, ohne den Impact zu dokumentieren.

## Empfohlener Workflow

1. Branch erstellen (`feature/...`, `fix/...`, `docs/...`).
2. Änderungen lokal implementieren.
3. Kompilieren:

   ```bash
   javac Main.java controller/*.java view/*.java model/*.java
   ```

4. Kurz testen (Startfenster, Normal/Debug Start).
5. Commit + Pull Request mit kurzer Risiko-Einschätzung.

## Pull-Request-Checkliste

- [ ] Ziel der Änderung im PR-Text beschrieben.
- [ ] Lokale Kompilierung erfolgreich.
- [ ] Dokumentation angepasst (falls Verhalten/Struktur geändert wurde).
- [ ] Keine unnötigen Refactorings ohne Begründung.

## Code-Style (leichtgewichtig)

- Lesbare Methodennamen und sprechende Variablen.
- Kommentare nur dort, wo sie Mehrwert liefern.
- Keine toten Codepfade / auskommentierten Blöcke einchecken.

