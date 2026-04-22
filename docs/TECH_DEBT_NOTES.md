# Technische Hinweise & Debt-Notizen

Dieses Dokument ist eine **organisatorische** Einordnung, keine direkte Umsetzungsplanung.

## 1) Build/Tooling

- Der Build läuft aktuell manuell über `javac`.
- Für bessere Nachvollziehbarkeit wäre mittelfristig Maven oder Gradle sinnvoll.

## 2) Testbarkeit

- Es gibt keine automatisierten Unit- oder Integrationstests.
- Besonders relevant wären Tests für Zugvalidierung und Capture-Pflichten.

## 3) Architektur-Schnitt

- `GameController` trägt viel Verantwortung (Orchestrierung + Regelprüfung + UI-nahe Zustände).
- Potenzial für spätere Entkopplung in kleinere Services (Regeln, Zustand, Zuggenerator).

## 4) Domain-Modell

- Teile des Zustands hängen implizit voneinander ab (z. B. Spielerfarbe und Piece-Listen).
- Potenziell fragil bei spätem Refactoring ohne Tests.

## 5) Repository-Reife

- Bisher kein CI-Lauf, kein Qualitäts-Gate, keine Releasestrategie.
- Für Außenwirkung hilfreich: klare Versionierung und Changelog.

