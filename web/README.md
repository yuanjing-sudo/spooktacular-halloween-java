# Spooktacular Web — full game in Java compiled to WebAssembly

Live game: **https://yuanjing-sudo.github.io/spooktacular-halloween-java/**
(served from `docs/` via GitHub Pages; needs HTTP + a WasmGC browser).

## What this is

All 7 desktop tabs in the browser: MAZE, CANDY, MINE, WORLD, EXPLORE, GAMES,
GOALS. The REAL desktop sources (`engine`, `data`, `combat`, `quests`,
`systems`, `app/Models`, `app/GhostCapture`, `app/MiniGames`) compile
UNMODIFIED into the WASM (TeaVM 0.15.0 `WEBASSEMBLY_GC`, Java 17: records,
sealed interfaces, pattern matching all supported). Only three shims exist:
`systems/GameStore` (localStorage-backed, same API — desktop uses
java.util.prefs), `WebSave`/`WebManager`/`WebMine`/`WebVoxel` (Swing-free twins
of the Swing-bound `SpookyStore`/`HalloweenUltimateManager`/`MineSim`/
`VoxelMinePanel.Model`, same rules). Canvas UI + input in `Client.java`
(`classes.wasm`, ~216 KB).

Proof: `WebCheck` (592 checks: RNG/maze identity) + `WebCheck2` (65 checks:
real 69/40/60 catalogs, real boards claim flow, real minigames/duels, mine
economy, voxel rules, manager levels).

## Source mapping (web <- desktop)

| Web tab | Desktop sources used |
|---|---|
| MAZE (DFS maze, candy, ghost chase, ZAP/NET) | `WasmGame` (twin of `Engine`+`GhostCapture`+`CandyCollection` rules); feeds REAL `ExpeditionBoard` (`ScoreEarned`/`MonsterSlain`/`DistanceBanked`/`RegionMapped`) |
| CANDY (18-candy trick-or-treat + brewing) | REAL `Data.CANDIES`, REAL `Models.PotionItem`, `WebManager` (XP/combo twins) |
| MINE (14x9 tycoon: swing/sell/picks/packs/pets/rebirth/fish) | `WebMine` (rule twin of `MineSim`) + REAL `Block`, `Data.PICKS`, `Entities`, `Fishing`, `QuestBoard`, `AchievementBoard`, `Engine.applyXP` |
| WORLD (5 capture duels + bestiary) | REAL `GhostCapture`, `Models`, `Bestiary`, `CritterCodex`, `Data.GHOSTS` |
| EXPLORE (isometric 3D voxel mine + codexes) | `WebVoxel` (twin of `VoxelMinePanel.Model`) + REAL `Block`, `PerlinNoise`, `LayerCodex`, `OreCodex`, `Data.LAYERS` |
| GAMES (Memory Match + Pumpkin Smash + arcade list) | REAL `MiniGames`, REAL `Data.MINIGAMES` |
| GOALS (quests/expeditions/achievements + CLAIM) | REAL `QuestBoard`+`QuestCatalog` (69), `ExpeditionBoard`+`ExpeditionCatalog` (40), `AchievementBoard`+`AchievementCatalog` (60), `Snapshot` |
| Persistence | REAL boards over shim `systems/GameStore` -> `WebSave` -> localStorage |

## Algorithm mapping (twins only; everything else is the real source)

| Web twin | Desktop twin | Rules |
|---|---|---|
| `WasmRng.java` | `engine/Engine.java` RNG | `state += 0x6D2B79F5L` mix, `nextDouble`, `nextInt` — identical stream |
| `WasmMaze.java` | `engine/Engine.java` carveDFS | DFS carve, odd dims, `w*d*4` step cap, farthest-open spawn |
| `WasmGame.java` start/update | `game/MazePanel.java`, `app/HalloweenUltimateManager.java` | depths scale `min(25,13+2d)` x `min(17,9+2d)`, ghost think `0.55-0.05(d-1)` floor 0.28, XP threshold `level*100` |
| `WasmGame.zap/throwNet` | `app/GhostCapture.java` | zap `8+rng(7)`, 15% crit x2, `-power/10`, floor 1, regen `0-4`; net `0.25+0.65*weakness` clamped `[0.05,0.97]`; fail `+maxHp/4`, flee after 3 |
| candy/combo | `app/CandyCollection.java`, `engine/Engine.java` comboMult | `+2 XP`, `5*mult`, `mult = min(3,1+0.05c)`, combo bonus every 5th |
| `smashTick/smashAt` | `app/MiniGames.java` PumpkinSmash | light `1+rng(3)` of 9, hit `+10`, miss `-2` floor 0 |
| `Client.java` | `game/MazePanel.java` view, `game/Tabs.java` shell | top-down maze + torch glow + HUD + battle overlay + smash grid on Canvas 2D |

Ghost chase is greedy 4-neighborhood (RNG tie-break); desktop uses A*
(`Engine.astar`) — same walkability, shortest-ish pursuit, cheaper per tick.

## Play

- Move: arrows / WASD. Z: zap. X / Enter: throw net. Click: lit pumpkins (bonus).
- Goal: clear every candy per depth, capture ghosts, smash bonus, descend forever.

## Build

```bat
cd web
set JAVA_HOME=C:\Program Files\Microsoft\jdk-21.0.12.101-hotspot
C:\Users\%USERNAME%\AppData\Local\Temp\opencode\maven\apache-maven-3.9.9\bin\mvn.cmd -B package
```

Output: `target/web-1.0/` (`index.html` + `teavm/classes.wasm` + loader).
Copy those three files to `docs/` (+ `docs/teavm/`) to publish. Local play:
`python -m http.server` in `docs/` (WASM needs HTTP + `application/wasm` MIME).
