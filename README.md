# Spooktacular Halloween — Java Port

Java expression of [yuanjing-sudo/spooktacular-halloween-app](https://github.com/yuanjing-sudo/spooktacular-halloween-app)
(Spooktacular Halloween Ultimate Simulator — iOS SwiftUI app with ghost capturing, candy collection, potion brewing, and mini-games).

100% JDK, zero external dependencies. Swing UI + headless-tested logic.

## Live web game (Java -> WebAssembly)

Play now: **https://yuanjing-sudo.github.io/spooktacular-halloween-java/**

`web/` holds the browser edition: rules in UI-free Java (`spooktacular.web.*`,
algorithm twins of `Engine`, `GhostCapture`, `CandyCollection`,
`MiniGames.PumpkinSmash`), Canvas shell in `Client.java`, compiled by TeaVM
0.15.0 (`WEBASSEMBLY_GC`) to a real `classes.wasm` (27 KB) + loader in `docs/`.
`WebCheck` (592 checks) proves same-seed RNG/maze identity with the desktop
engine. See `web/README.md` for the mapping table and build steps.

## What was ported

| Origin (Swift) | Java |
|---|---|
| `Sources/Ultimate/ContentView.swift` (5855 lines: manager, Ghost/Candy/Potion/Quest/Achievement/Spell/Character/Equipment/HauntedHouse/MiniGame) | `spooktacular.app.Models`, `HalloweenUltimateManager`, `SpookyApp` |
| `Sources/Ultimate/GhostCapture.swift` (capture battle) | `spooktacular.app.GhostCapture` |
| `Sources/Ultimate/*` candy/streak logic | `spooktacular.app.CandyCollection` |
| `Sources/Ultimate/*` potion systems | `spooktacular.app.PotionBrewing` |
| `Sources/Ultimate/ArcadeGames.swift`, `ArcadeGames2.swift` | `spooktacular.app.MiniGames` (Memory Match + Pumpkin Smash, playable) |
| `Sources/Ultimate/SpookyStore.swift` | `spooktacular.app.SpookyStore` |
| `Sources/Ultimate/SpookyMusic.swift` | `spooktacular.app.SpookyMusic` |
| `Sources/Ultimate/HauntedMaze.swift`, `TunnelMaze.swift` (maze 3D) | `spooktacular.game.MazePanel` (software raycaster: textured walls, sprites, zbuffer, fog, minimap) + `spooktacular.systems.Raycaster` |
| `Sources/Ultimate/AbandonedMine.swift` (SceneKit voxel mine) + `python-3d/mine3d_ursina.py` + `docs/mine3d.js` (mine 3D) | `spooktacular.game.VoxelMinePanel` (isometric software-3D voxel mine: 12x8x3 strata, Perlin bands, pick tiers, torch glow, fog) + `spooktacular.game.MineSim` (tycoon sim) |
| `Sources/Ultimate/AvatarWorld.swift`, `CemeteryOfShadows.swift`, … (remaining files, ~2.3 MB total) | `spooktacular.engine`, `spooktacular.systems`, `spooktacular.data`, `spooktacular.combat`, `spooktacular.quests`, `spooktacular.game` (exact engine port from `java-3d/`) + `spooktacular.swiftport.*` 1:1 file map (72 classes, one per Swift file) |
| `Tests/SpookyLogicTests.swift`, `SpookyDeepTests.swift` | `spooktacular.game.TestEngine` (83 checks) + `spooktacular.app.AppTest` (27 checks) + `spooktacular.game.VoxelMineTest` (52 checks) |
| `docs/` web + `python-3d/` + `java-3d/` | behavior preserved in Swing tabs; see mapping in `spooktacular.swiftport.*.describe()` |

## Layout

```
src/spooktacular/app/        <- SwiftUI app layer in Java (manager, capture, candy, brew, games, store, music, Swing shell)
src/spooktacular/engine/ ... <- exact engine port (RNG, DFS maze, A*, XP) + systems/data/combat/quests/game (from java-3d)
src/spooktacular/swiftport/  <- 1 Java class per Swift file (72 files) — guarantees "everything" is expressed
```

## Run (Windows)

```bat
spooktacular-halloween-java\run.bat
```

Runs headless tests (`TestEngine` + `AppTest` + `VoxelMineTest`), then opens the Swing app (8 tabs: Capture, Candy, Engine, Mine 3D, Potions, Music, Status; Engine tab itself holds Maze/Candy/Mine/World/Explore/Games/Achieve).

## Run (macOS/Linux, JDK 17+)

```sh
cd spooktacular-halloween-java
javac -encoding UTF-8 -d classes $(find src -name '*.java')
java -cp classes spooktacular.game.TestEngine
java -cp classes spooktacular.app.AppTest
java -cp classes spooktacular.game.VoxelMineTest
java -cp classes spooktacular.app.SpookyApp
```

## Verify

- `TestEngine` — 83 engine checks (maze, A*, scoring, quests/expeditions/achievements, tycoon sim).
- `AppTest` — 27 app-layer checks (capture battle, candy combos, brewing, memory/smash, store round-trip + corruption recovery, music switchboard, streaks).
- `VoxelMineTest` — 52 voxel-mine checks (world gen, bedrock unbreakable, exposed/strata rule, pick-tier gate, gold/xp totals, per-block colors).

## How it was built

Ported "by any means": pure-logic engine copied verbatim from the repo's own `java-3d/` Java edition, SwiftUI shell re-expressed as testable Java (no SwiftUI in Java — Swing is the host), and a 1:1 `swiftport` class generated per Swift file so no source file is left without a Java expression.
