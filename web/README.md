# Spooktacular Web — Java compiled to WebAssembly

Live game: **https://yuanjing-sudo.github.io/spooktacular-halloween-java/**
(served from `docs/` via GitHub Pages; needs HTTP + a WasmGC browser).

## What this is

The desktop Java port (`src/spooktacular/...`, Swing) re-expressed for the
browser: game rules in UI-free Java (`spooktacular.web`), Canvas rendering +
input in `Client.java`, compiled by **TeaVM 0.15.0 (`WEBASSEMBLY_GC`)** to
`classes.wasm` + JS loader. Same algorithms, same seeds, same behavior —
proven by `WebCheck` (592 checks: RNG stream identity, per-cell maze equality,
full loop).

## Algorithm mapping (web core -> desktop Java port)

| Web class | Desktop twin | Rules |
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
