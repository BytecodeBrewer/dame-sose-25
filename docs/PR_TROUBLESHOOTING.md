# PR Troubleshooting

## Fehler: "Binary files are not supported"

Wenn beim Erstellen eines PR die Meldung erscheint, hilft meist einer dieser Wege:

### 1) Line-Endings/Attribute neu anwenden (empfohlen)

```bash
git add --renormalize .
git commit -m "chore: renormalize text files"
git push
```

### 2) Betroffene Datei neu als Text einchecken

Für README-Probleme:

```bash
git rm --cached README.md
git add README.md
git commit -m "fix: re-add README as text"
git push
```

### 3) Neuen Branch von aktuellem `develop` erstellen

Wenn der alte Branch historisch eine problematische Binary-Version enthält, kann ein frischer Branch helfen:

```bash
git checkout develop
git pull
git checkout -b fix/readme-text
# Änderungen neu anwenden
git push -u origin fix/readme-text
```

### 4) Encoding prüfen

README sollte UTF-8 sein, ohne Null-Bytes.

```bash
python - <<'PY'
from pathlib import Path
b = Path('README.md').read_bytes()
print('utf8_bom', b.startswith(b'\xef\xbb\xbf'))
print('contains_nul', b'\x00' in b)
PY
```

Erwartung:
- `utf8_bom` = `False` oder `True` (beides okay)
- `contains_nul` = `False`

