package spooktacular.web;

import java.util.ArrayList;

/** Pure game state — UI-free twin of the Java port's rules, Java-11 syntax for TeaVM.
 *  Maze: Engine.carveDFS twin (WasmMaze). Capture: GhostCapture zap/net twin
 *  (app/GhostCapture.java). Candy/combo: CandyCollection twin. XP: manager twin
 *  (level*100). Smash: MiniGames.PumpkinSmash twin. Scoring: Engine.comboMult twin. */
public final class WasmGame {
    public static final int TITLE = 0;
    public static final int MAZE = 1;
    public static final int BATTLE = 2;
    public static final int SMASH = 3;

    public int state = TITLE;
    public int depth = 1;
    public WasmMaze maze;
    public int px = 1;
    public int pz = 1;
    public int qdx = 0;
    public int qdz = 0;
    public int gx = 5;
    public int gz = 5;
    public boolean ghostActive = true;
    public double ghostTimer = 0;
    public double moveTimer = 0;
    public int ghostHp = 30;
    public int ghostMaxHp = 30;
    public int ghostPower = 10;
    public String ghostName = "Boo Berry";
    public int battleFails = 0;
    public int lastZap = 0;
    public boolean lastCrit = false;
    public double fleeTimer = 0;
    public final ArrayList<int[]> candies = new ArrayList<int[]>();
    public int combo = 0;
    public int bestCombo = 0;
    public long score = 0;
    public int xp = 0;
    public int level = 1;
    public int captured = 0;
    public int candyTaken = 0;
    public final boolean[] smashLit = new boolean[9];
    public int smashTicks = 0;
    public double smashTimer = 0;
    public String message = "";
    public double messageTimer = 0;
    public long seed = 1;

    private WasmRng rng = new WasmRng(1);

    private static final String[] GHOST_NAMES = {
        "Boo Berry", "Misty", "Wailin' Winnie", "Sir Rattles", "Pumpkin King"
    };
    private static final int[] GHOST_HP = { 30, 50, 80, 140, 220 };
    private static final int[] GHOST_POW = { 10, 20, 35, 60, 99 };

    public void start(long seed) {
        this.seed = seed;
        rng = new WasmRng(seed);
        depth = 1;
        score = 0;
        xp = 0;
        level = 1;
        captured = 0;
        combo = 0;
        bestCombo = 0;
        candyTaken = 0;
        newDepth();
        state = MAZE;
        say("Collect every candy. Avoid the ghost... or catch it!");
    }

    public void newDepth() {
        int w = Math.min(25, 13 + depth * 2);
        int d = Math.min(17, 9 + depth * 2);
        maze = WasmMaze.carveDFS(w, d, seed ^ (depth * 0x9E3779B9L));
        px = 1;
        pz = 1;
        qdx = 0;
        qdz = 0;
        int[] spawn = maze.farthestOpen(px, pz);
        gx = spawn[0];
        gz = spawn[1];
        ghostActive = true;
        fleeTimer = 0;
        int gi = Math.min(depth - 1, GHOST_NAMES.length - 1);
        ghostName = GHOST_NAMES[gi];
        ghostMaxHp = GHOST_HP[gi];
        ghostHp = ghostMaxHp;
        ghostPower = GHOST_POW[gi];
        battleFails = 0;
        candies.clear();
        int want = 4 + depth;
        int guard = 0;
        while (candies.size() < want && guard < 2000) {
            guard++;
            int cx = 1 + rng.nextInt(maze.w - 1);
            int cz = 1 + rng.nextInt(maze.d - 1);
            if (!maze.isOpen(cx, cz) || (cx == 1 && cz == 1)) {
                continue;
            }
            boolean dup = false;
            for (int i = 0; i < candies.size(); i++) {
                int[] c = candies.get(i);
                if (c[0] == cx && c[1] == cz) {
                    dup = true;
                    break;
                }
            }
            if (!dup) {
                candies.add(new int[] { cx, cz });
            }
        }
        moveTimer = 0;
        ghostTimer = 0;
    }

    public double ghostThink() {
        double t = 0.55 - (depth - 1) * 0.05;
        return t < 0.28 ? 0.28 : t;
    }

    public void say(String s) {
        message = s;
        messageTimer = 4.0;
    }

    public static double comboMult(int c) {
        if (c <= 0) {
            return 1.0;
        }
        double m = 1.0 + c * 0.05;
        return m > 3.0 ? 3.0 : m;
    }

    public void addXp(int n) {
        xp += n;
        int need = level * 100;
        if (xp >= need) {
            xp -= need;
            level++;
            say("LEVEL UP! You are now level " + level);
        }
    }

    public void update(double dt) {
        if (messageTimer > 0) {
            messageTimer -= dt;
        }
        if (state == MAZE) {
            moveTimer += dt;
            while (moveTimer >= 0.12) {
                moveTimer -= 0.12;
                stepPlayer();
            }
            if (!ghostActive) {
                fleeTimer -= dt;
                if (fleeTimer <= 0) {
                    int[] spawn = maze.farthestOpen(px, pz);
                    gx = spawn[0];
                    gz = spawn[1];
                    ghostActive = true;
                    ghostHp = ghostMaxHp;
                    battleFails = 0;
                    say(ghostName + " is back...");
                }
            } else {
                ghostTimer += dt;
                if (ghostTimer >= ghostThink()) {
                    ghostTimer = 0;
                    ghostStep();
                }
                if (gx == px && gz == pz) {
                    state = BATTLE;
                    battleFails = 0;
                    say(ghostName + " blocks your path! ZAP (Z) then NET (X)!");
                }
            }
            checkPickup();
            if (candies.isEmpty()) {
                state = SMASH;
                smashTicks = 0;
                smashTimer = 0;
                for (int i = 0; i < 9; i++) {
                    smashLit[i] = false;
                }
                say("Depth clear! BONUS: smash lit pumpkins (click)!");
            }
        } else if (state == SMASH) {
            smashTimer += dt;
            if (smashTimer >= 0.8) {
                smashTimer = 0;
                smashTick();
                smashTicks++;
                if (smashTicks >= 6) {
                    depth++;
                    newDepth();
                    state = MAZE;
                    say("Depth " + depth + ": " + ghostName + " haunts this floor. HP " + ghostMaxHp);
                }
            }
        }
    }

    private void stepPlayer() {
        if (qdx == 0 && qdz == 0) {
            return;
        }
        int nx = px + qdx;
        int nz = pz + qdz;
        if (maze.isOpen(nx, nz)) {
            px = nx;
            pz = nz;
        }
    }

    /** Greedy chase twin: 4-neighborhood, walkable, min Manhattan, RNG tie order. */
    private void ghostStep() {
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        int start = rng.nextInt(4);
        int best = Math.abs(gx - px) + Math.abs(gz - pz);
        int bx = gx;
        int bz = gz;
        for (int k = 0; k < 4; k++) {
            int[] dd = dirs[(start + k) % 4];
            int nx = gx + dd[0];
            int nz = gz + dd[1];
            if (!maze.isOpen(nx, nz)) {
                continue;
            }
            int dist = Math.abs(nx - px) + Math.abs(nz - pz);
            if (dist < best) {
                best = dist;
                bx = nx;
                bz = nz;
            }
        }
        gx = bx;
        gz = bz;
    }

    private void checkPickup() {
        for (int i = 0; i < candies.size(); i++) {
            int[] c = candies.get(i);
            if (c[0] == px && c[1] == pz) {
                candies.remove(i);
                candyTaken++;
                combo++;
                if (combo > bestCombo) {
                    bestCombo = combo;
                }
                addXp(2);
                score += Math.round(5 * comboMult(combo));
                if (combo % 5 == 0) {
                    addXp(10);
                    say("Combo x" + combo + "! Sweet!");
                }
                return;
            }
        }
    }

    /** GhostCapture.zap twin: 8-14 + power scaling, 15% crit x2, regen 0-4. */
    public void zap() {
        if (state != BATTLE) {
            return;
        }
        int dmg = 8 + rng.nextInt(7);
        lastCrit = rng.nextDouble() < 0.15;
        if (lastCrit) {
            dmg *= 2;
        }
        lastZap = dmg;
        ghostHp = ghostHp - dmg - ghostPower / 10;
        if (ghostHp < 1) {
            ghostHp = 1;
        }
        ghostHp = ghostHp + rng.nextInt(5);
        if (ghostHp > ghostMaxHp) {
            ghostHp = ghostMaxHp;
        }
    }

    public double weakness() {
        return 1.0 - (ghostHp / (double) ghostMaxHp);
    }

    /** GhostCapture.throwNet twin: 0.25 + 0.65*weakness, clamped. */
    public boolean throwNet() {
        if (state != BATTLE) {
            return false;
        }
        double p = 0.25 + 0.65 * weakness();
        if (p > 0.97) {
            p = 0.97;
        }
        if (p < 0.05) {
            p = 0.05;
        }
        boolean ok = rng.nextDouble() < p;
        if (ok) {
            captured++;
            addXp(25 + ghostPower);
            score += 50 + ghostPower * 2;
            combo++;
            if (combo > bestCombo) {
                bestCombo = combo;
            }
            int[] spawn = maze.farthestOpen(px, pz);
            gx = spawn[0];
            gz = spawn[1];
            ghostHp = ghostMaxHp;
            battleFails = 0;
            state = MAZE;
            say("CAPTURED " + ghostName + "! +" + (25 + ghostPower) + " XP");
        } else {
            ghostHp = ghostHp + ghostMaxHp / 4;
            if (ghostHp > ghostMaxHp) {
                ghostHp = ghostMaxHp;
            }
            battleFails++;
            if (battleFails >= 3) {
                ghostActive = false;
                fleeTimer = 8.0;
                combo = 0;
                state = MAZE;
                say("It burst free and fled! Combo lost.");
            } else {
                say("The net missed! (" + battleFails + "/3) ZAP it weaker!");
            }
        }
        return ok;
    }

    /** PumpkinSmash.tick twin: light 1-3 of 9 cells. */
    public void smashTick() {
        for (int i = 0; i < 9; i++) {
            smashLit[i] = false;
        }
        int n = 1 + rng.nextInt(3);
        for (int k = 0; k < n; k++) {
            smashLit[rng.nextInt(9)] = true;
        }
    }

    /** PumpkinSmash.smash twin: +10 lit, -2 otherwise. */
    public int smashAt(int i) {
        if (state != SMASH || i < 0 || i >= 9) {
            return 0;
        }
        if (smashLit[i]) {
            smashLit[i] = false;
            score += 10;
            addXp(1);
            return 10;
        }
        if (score >= 2) {
            score -= 2;
        }
        return -2;
    }
}
