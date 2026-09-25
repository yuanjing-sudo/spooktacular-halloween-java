package spooktacular.web;

import java.util.ArrayList;
import java.util.List;

import org.teavm.jso.browser.AnimationFrameCallback;
import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.events.KeyboardEvent;
import org.teavm.jso.dom.events.MouseEvent;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;

import spooktacular.app.GhostCapture;
import spooktacular.app.MiniGames;
import spooktacular.app.Models;
import spooktacular.combat.Bestiary;
import spooktacular.data.Block;
import spooktacular.data.CritterCodex;
import spooktacular.data.Data;
import spooktacular.data.LayerCodex;
import spooktacular.data.OreCodex;
import spooktacular.quests.Achievement;
import spooktacular.quests.AchievementBoard;
import spooktacular.quests.AchievementCatalog;
import spooktacular.quests.EEvent;
import spooktacular.quests.Expedition;
import spooktacular.quests.ExpeditionBoard;
import spooktacular.quests.ExpeditionCatalog;
import spooktacular.quests.QEvent;
import spooktacular.quests.Quest;
import spooktacular.quests.QuestBoard;
import spooktacular.quests.QuestCatalog;
import spooktacular.systems.GameStore;

/** Full-game browser shell — Java compiled by TeaVM to WebAssembly.
 *  All 7 desktop tabs, same engines: Maze (WasmGame), Candy (Data catalogs),
 *  Mine (WebMine twin of MineSim + REAL QuestBoard), World (REAL Bestiary +
 *  GhostCapture duels), Explore (WebVoxel iso mine + REAL codexes),
 *  Games (REAL MiniGames), Achieve (REAL Quest/Expedition/Achievement boards).
 *  Entry: main() via teavm.exports.main([]). */
public class Client {
    private static final int W = 720;
    private static final int H = 600;
    private static final int TABH = 30;
    private static final int HUD = 96;
    private static final String[] TABS = { "MAZE", "CANDY", "MINE", "WORLD",
            "EXPLORE", "GAMES", "GOALS" };

    private static int tab = 0;
    private static final WasmGame game = new WasmGame();
    private static final QuestBoard quests = new QuestBoard();
    private static final ExpeditionBoard exps = new ExpeditionBoard();
    private static final AchievementBoard achs = new AchievementBoard();
    private static final WebManager mgr = new WebManager(quests);
    private static WebMine mine;
    private static WebVoxel voxel;
    private static MiniGames.MemoryMatch mem;
    private static final MiniGames.PumpkinSmash psmash = new MiniGames.PumpkinSmash(777L);
    private static final GhostCapture duels = new GhostCapture(4242L);
    private static GhostCapture.Battle duel = null;
    private static int duelIdx = 0;
    private static int worldPage = 0;
    private static int exploreTier = 1;
    private static String exploreMsg = "Click a cube to mine. Keys 1-4 pick tier.";
    private static final boolean[] layerKnown = new boolean[3];
    private static final boolean[] candyGot = new boolean[18];
    private static String candyMsg = "Click candy to trick-or-treat. B brews a potion.";
    private static Models.PotionEffect brewNext = Models.PotionEffect.HEAL;
    private static int lastMazeScore = 0;
    private static int lastMazeCaptured = 0;
    private static int lastSteps = 0;
    private static int lastCandyTaken = 0;
    private static double achTimer = 0;
    private static double smashTimer = 0;
    private static String toast = "";
    private static double toastTimer = 0;
    private static final ArrayList<int[]> zones = new ArrayList<int[]>();

    private static CanvasRenderingContext2D ctx;
    private static double lastTs = -1;

    public static void main(String[] args) {
        WebSave.restore(GameStore.standard);
        mgr.load();
        mine = new WebMine(quests, achs, 20261031L);
        mine.player().gold = numExtra("m_gold");
        mine.grantCoal(numExtra("m_coal"));
        mine.player().pickTier = Math.min(Data.PICKS.length - 1, numExtra("m_pick"));
        mine.player().level = Math.max(1, numExtra("m_level"));
        mine.player().experience = numExtra("m_xp");
        mine.player().rebirths = numExtra("m_reb");
        voxel = new WebVoxel(12, 8, 3, 20261031L);
        mem = new MiniGames.MemoryMatch(6, 11L);
        exps.onReward = new java.util.function.BiConsumer<Integer, Integer>() {
            @Override
            public void accept(Integer score, Integer gold) {
                mine.player().gold += gold.intValue();
                mgr.addXp(gold.intValue() / 2 + 10);
            }
        };

        HTMLDocument document = HTMLDocument.current();
        document.getBody().setAttribute("style", "margin:0;background:#000;");
        HTMLCanvasElement canvas = (HTMLCanvasElement) document.createElement("canvas");
        canvas.setWidth(W);
        canvas.setHeight(H);
        document.getBody().appendChild(canvas);
        ctx = (CanvasRenderingContext2D) canvas.getContext("2d");

        Window window = Window.current();
        window.listenKeyDown(Client::onKey);
        window.listenClick(Client::onClick);
        Window.requestAnimationFrame(new AnimationFrameCallback() {
            @Override
            public void onAnimationFrame(double ts) {
                frame(ts);
                Window.requestAnimationFrame(this);
            }
        });
    }

    private static int numExtra(String k) {
        String v = WebSave.extra.get(k);
        if (v == null) {
            return 0;
        }
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static void persistAll() {
        mgr.save();
        WebSave.extra.put("m_gold", Integer.toString(mine.player().gold));
        WebSave.extra.put("m_coal", Long.toString(mine.coal()));
        WebSave.extra.put("m_pick", Integer.toString(mine.player().pickTier));
        WebSave.extra.put("m_level", Integer.toString(mine.player().level));
        WebSave.extra.put("m_xp", Integer.toString(mine.player().experience));
        WebSave.extra.put("m_reb", Integer.toString(mine.player().rebirths));
        WebSave.persist(GameStore.standard);
    }

    private static void frame(double ts) {
        double dt = 0.016;
        if (lastTs >= 0) {
            dt = (ts - lastTs) / 1000.0;
            if (dt > 0.1) {
                dt = 0.1;
            }
            if (dt < 0) {
                dt = 0;
            }
        }
        lastTs = ts;
        if (game.state != WasmGame.TITLE) {
            game.update(dt);
            feedMazeEvents();
        }
        achTimer += dt;
        if (achTimer > 1.0) {
            achTimer = 0;
            List<Achievement> fresh = achs.refresh(mine.snapshot());
            if (!fresh.isEmpty()) {
                toast = "Achievement unlocked: " + fresh.get(0).title() + "!";
                toastTimer = 5.0;
                persistAll();
            }
        }
        if (toastTimer > 0) {
            toastTimer -= dt;
        }
        if (tab == 5) {
            smashTimer += dt;
            if (smashTimer > 0.8) {
                smashTimer = 0;
                psmash.tick();
            }
        }
        render();
    }

    /** Feed maze-tab deltas into the REAL expedition board + manager. */
    private static void feedMazeEvents() {
        int ds = (int) (game.score - lastMazeScore);
        if (ds > 0) {
            exps.record(new EEvent.ScoreEarned(ds));
        }
        lastMazeScore = (int) game.score;
        if (game.captured != lastMazeCaptured) {
            int n = game.captured - lastMazeCaptured;
            for (int i = 0; i < n; i++) {
                exps.record(new EEvent.MonsterSlain());
                mgr.capture(game.ghostPower);
            }
            lastMazeCaptured = game.captured;
            persistAll();
        }
        int dst = game.steps - lastSteps;
        if (dst > 0) {
            exps.record(new EEvent.DistanceBanked(dst));
        }
        lastSteps = game.steps;
        if (game.candyTaken != lastCandyTaken) {
            int n = game.candyTaken - lastCandyTaken;
            mgr.collectCandy("maze-mix", n);
            lastCandyTaken = game.candyTaken;
        }
        if (game.state == WasmGame.SMASH && game.smashTicks == 0) {
            exps.record(new EEvent.RegionMapped(game.depth));
        }
    }

    // ================= input =================
    private static void onKey(KeyboardEvent evt) {
        String k = evt.getKey();
        if (k == null) {
            return;
        }
        if (game.state == WasmGame.TITLE && tab == 0) {
            if (k.equals("Enter") || k.equals(" ")) {
                game.start((long) lastTs * 1000003L + 0x71C3L);
            }
            return;
        }
        if (tab == 0) {
            mazeKeys(k);
        } else if (tab == 1 && (k.equals("b") || k.equals("B"))) {
            brew();
        } else if (tab == 3 && duel != null && !duel.over) {
            if (k.equals("z") || k.equals("Z")) {
                duels.zap(duel);
            } else if (k.equals("x") || k.equals("X")) {
                if (duels.throwNet(duel, false)) {
                    mgr.capture(duel.ghost.power());
                    exps.record(new EEvent.MonsterSlain());
                    persistAll();
                }
            }
        } else if (tab == 4) {
            if (k.equals("1")) {
                exploreTier = 0;
            } else if (k.equals("2")) {
                exploreTier = 1;
            } else if (k.equals("3")) {
                exploreTier = 2;
            } else if (k.equals("4")) {
                exploreTier = 4;
            }
        }
    }

    private static void mazeKeys(String k) {
        if (k.equals("ArrowUp") || k.equals("w") || k.equals("W")) {
            game.qdx = 0;
            game.qdz = -1;
        } else if (k.equals("ArrowDown") || k.equals("s") || k.equals("S")) {
            game.qdx = 0;
            game.qdz = 1;
        } else if (k.equals("ArrowLeft") || k.equals("a") || k.equals("A")) {
            game.qdx = -1;
            game.qdz = 0;
        } else if (k.equals("ArrowRight") || k.equals("d") || k.equals("D")) {
            game.qdx = 1;
            game.qdz = 0;
        } else if ((k.equals("z") || k.equals("Z")) && game.state == WasmGame.BATTLE) {
            game.zap();
        } else if ((k.equals("x") || k.equals("X") || k.equals("Enter"))
                && game.state == WasmGame.BATTLE) {
            game.throwNet();
            persistAll();
        }
    }

    private static void onClick(MouseEvent evt) {
        int x = evt.getClientX();
        int y = evt.getClientY();
        if (y < TABH) {
            int nt = x / (W / TABS.length);
            if (nt >= 0 && nt < TABS.length && nt != tab) {
                tab = nt;
                persistAll();
            }
            return;
        }
        if (tab == 0) {
            if (game.state == WasmGame.TITLE) {
                game.start((long) lastTs * 1000003L + 0x71C3L);
            } else if (game.state == WasmGame.SMASH) {
                smashClick(x, y);
            }
        } else if (tab == 1) {
            candyClick(x, y);
        } else if (tab == 2) {
            mineClick(x, y);
        } else if (tab == 3) {
            worldClick(x, y);
        } else if (tab == 4) {
            voxelClick(x, y);
        } else if (tab == 5) {
            gamesClick(x, y);
        } else if (tab == 6) {
            goalsClick(x, y);
        }
    }

    // ================= per-tab actions =================
    private static void brew() {
        ArrayList<Data.Candy> have = new ArrayList<Data.Candy>();
        for (int i = 0; i < Data.CANDIES.length && have.size() < 2; i++) {
            if (mgr.candyCount(Data.CANDIES[i].key()) > 0) {
                have.add(Data.CANDIES[i]);
            }
        }
        if (have.size() < 2) {
            candyMsg = "Need 2+ candy kinds banked to brew.";
            return;
        }
        int sweet = have.get(0).points() + have.get(1).points();
        Models.PotionQuality q;
        if (sweet >= 20) {
            q = Models.PotionQuality.WITCHY;
        } else if (sweet >= 15) {
            q = Models.PotionQuality.STRONG;
        } else if (sweet >= 10) {
            q = Models.PotionQuality.NORMAL;
        } else {
            q = Models.PotionQuality.WEAK;
        }
        String nm = brewNext.name().charAt(0)
                + brewNext.name().substring(1).toLowerCase().replace('_', ' ') + " draught";
        mgr.addPotion(new Models.PotionItem("p" + mgr.potions.size(), nm, brewNext, q,
                sweet / 2 + 2));
        Models.PotionEffect[] all = Models.PotionEffect.values();
        brewNext = all[(brewNext.ordinal() + 1) % all.length];
        candyMsg = "Brewed " + nm + " [" + q.name() + "].";
        persistAll();
    }

    private static void mineClick(int x, int y) {
        int gx0 = 30;
        int gy0 = HUD + 8;
        int cw = 44;
        int ch = 32;
        if (y >= gy0 && y < gy0 + WebMine.ROWS * ch && x >= gx0 && x < gx0 + WebMine.COLS * cw) {
            mine.swing((y - gy0) / ch, (x - gx0) / cw);
            return;
        }
        int by = gy0 + WebMine.ROWS * ch + 8;
        String[] acts = { "SELL", "PICK", "PACK", "EGG", "REBIRTH", "VEIN", "CLAIM", "FISH" };
        for (int i = 0; i < acts.length; i++) {
            int bx = 30 + i * 86;
            if (x >= bx && x < bx + 80 && y >= by && y < by + 26) {
                if (i == 0) {
                    mine.sell();
                } else if (i == 1) {
                    mine.buyPick();
                } else if (i == 2) {
                    mine.upgradePack();
                } else if (i == 3) {
                    mine.hatchEgg();
                } else if (i == 4) {
                    mine.rebirth();
                } else if (i == 5) {
                    mine.newVein();
                } else if (i == 6) {
                    mine.claimAll();
                } else {
                    mine.fish("Fresh");
                }
                persistAll();
                return;
            }
        }
    }

    private static void worldClick(int x, int y) {
        List<Models.Ghost> ghosts = Models.starterGhosts();
        for (int i = 0; i < ghosts.size(); i++) {
            int ry = HUD + 40 + i * 30;
            if (x >= 20 && x < 330 && y >= ry && y < ry + 26) {
                duelIdx = i;
                duel = duels.start(ghosts.get(i));
                return;
            }
        }
        if (duel != null && !duel.over) {
            if (x >= 380 && x < 500 && y >= HUD + 150 && y < HUD + 178) {
                duels.zap(duel);
                return;
            }
            if (x >= 510 && x < 640 && y >= HUD + 150 && y < HUD + 178) {
                if (duels.throwNet(duel, false)) {
                    mgr.capture(duel.ghost.power());
                    exps.record(new EEvent.MonsterSlain());
                    persistAll();
                }
                return;
            }
        }
        int py = H - 60;
        if (y >= py && y < py + 24) {
            if (x >= 380 && x < 470) {
                worldPage = Math.max(0, worldPage - 1);
            } else if (x >= 480 && x < 570) {
                worldPage = worldPage + 1;
            }
        }
    }

    private static void voxelClick(int x, int y) {
        double best = 1e9;
        int bl = 0;
        int br = 0;
        int bc = 0;
        for (int l = 0; l < voxel.levels; l++) {
            for (int r = 0; r < voxel.rows; r++) {
                for (int c = 0; c < voxel.cols; c++) {
                    double[] p = cubeTop(l, r, c);
                    double d = (p[0] - x) * (p[0] - x) + (p[1] - y) * (p[1] - y);
                    if (d < best) {
                        best = d;
                        bl = l;
                        br = r;
                        bc = c;
                    }
                }
            }
        }
        if (best < 40 * 40) {
            WebVoxel.Yield yd = voxel.mine(bl, br, bc, exploreTier);
            if (yd != null) {
                quests.record(new QEvent.BlockBroken());
                if (yd.block.ore) {
                    quests.record(new QEvent.OreMined(yd.block.displayName, 1));
                }
                if (!layerKnown[bl]) {
                    layerKnown[bl] = true;
                    quests.record(new QEvent.LayerReached(Data.LAYERS[bl]));
                }
                mgr.addXp(yd.xp);
                exploreMsg = "Mined " + yd.block.displayName + " (+" + yd.gold + "g, +" + yd.xp + "xp).";
                persistAll();
            } else {
                Block b = voxel.at(bl, br, bc);
                if (b == null) {
                    exploreMsg = "Empty socket.";
                } else if (b.unbreakable) {
                    exploreMsg = b.displayName + " is unbreakable.";
                } else if (exploreTier < b.minTier) {
                    exploreMsg = b.displayName + " needs pick T" + b.minTier + ".";
                } else {
                    exploreMsg = "Buried - mine the block above first.";
                }
            }
        }
    }

    private static void gamesClick(int x, int y) {
        int gx0 = 30;
        int gy0 = HUD + 40;
        if (x >= gx0 && x < gx0 + 4 * 64 && y >= gy0 && y < gy0 + 3 * 64) {
            int i = ((y - gy0) / 64) * 4 + (x - gx0) / 64;
            if (i < mem.board.length) {
                int r = mem.flip(i);
                if (r == 1 && mem.won()) {
                    mgr.addXp(mem.score() / 10 + 20);
                    toast = "Memory solved! +" + (mem.score() / 10 + 20) + " XP. New deal coming.";
                    toastTimer = 4.0;
                    mem = new MiniGames.MemoryMatch(6, (long) (lastTs + mem.moves));
                    persistAll();
                }
            }
            return;
        }
        int sx0 = 400;
        int sy0 = HUD + 40;
        if (x >= sx0 && x < sx0 + 3 * 64 && y >= sy0 && y < sy0 + 3 * 64) {
            int i = ((y - sy0) / 64) * 3 + (x - sx0) / 64;
            psmash.smash(i);
        }
    }

    private static void goalsClick(int x, int y) {
        for (int i = 0; i < zones.size(); i++) {
            int[] z = zones.get(i);
            if (x >= z[0] && x < z[0] + z[2] && y >= z[1] && y < z[1] + z[3]) {
                if (z[4] == 0) {
                    List<Quest> act = quests.active();
                    if (z[5] < act.size() && quests.claim(act.get(z[5]))) {
                        toast = "Quest claimed!";
                        toastTimer = 3.0;
                        persistAll();
                    }
                } else if (z[4] == 1) {
                    List<Expedition> act = exps.active();
                    if (z[5] < act.size() && exps.claim(act.get(z[5]))) {
                        toast = "Expedition claimed!";
                        toastTimer = 3.0;
                        persistAll();
                    }
                } else {
                    List<Achievement> all = AchievementCatalog.all();
                    int n = 0;
                    for (int k = 0; k < all.size() && n <= z[5]; k++) {
                        Achievement a = all.get(k);
                        if (achs.isUnlocked(a.id()) && !achs.isClaimed(a.id())) {
                            if (n == z[5] && achs.claim(a)) {
                                toast = "Achievement claimed: " + a.title();
                                toastTimer = 3.0;
                                persistAll();
                            }
                            n++;
                        }
                    }
                }
                return;
            }
        }
    }

    private static void candyClick(int x, int y) {
        int gx0 = 30;
        int gy0 = HUD + 30;
        for (int i = 0; i < Data.CANDIES.length; i++) {
            int cx = gx0 + (i % 6) * 112;
            int cy = gy0 + (i / 6) * 64;
            if (x >= cx && x < cx + 106 && y >= cy && y < cy + 58 && !candyGot[i]) {
                candyGot[i] = true;
                mgr.collectCandy(Data.CANDIES[i].key(), 1);
                if (Data.CANDIES[i].points() >= 15) {
                    exps.record(new EEvent.TreasureFound());
                }
                boolean all = true;
                for (int k = 0; k < candyGot.length; k++) {
                    if (!candyGot[k]) {
                        all = false;
                    }
                }
                if (all) {
                    for (int k = 0; k < candyGot.length; k++) {
                        candyGot[k] = false;
                    }
                    mgr.addXp(50);
                    candyMsg = "Sweet haul! +50 XP. Board restocked.";
                } else {
                    candyMsg = "Got " + Data.CANDIES[i].name() + " (+" + Data.CANDIES[i].points() + ").";
                }
                persistAll();
                return;
            }
        }
    }

    private static void smashClick(int x, int y) {
        int gx0 = W / 2 - 96;
        int gy0 = HUD + 60;
        int cx = (x - gx0) / 64;
        int cy = (y - gy0) / 64;
        if (cx >= 0 && cx < 3 && cy >= 0 && cy < 3) {
            game.smashAt(cy * 3 + cx);
        }
    }

    // ================= render =================
    private static String css(int rgb) {
        String h = Integer.toHexString(rgb);
        while (h.length() < 6) {
            h = "0" + h;
        }
        return "#" + h;
    }

    private static void render() {
        zones.clear();
        ctx.setFillStyle("#0d081c");
        ctx.fillRect(0, 0, W, H);
        tabs();
        hud();
        if (tab == 0) {
            if (game.state == WasmGame.TITLE) {
                title();
            } else {
                maze();
                if (game.state == WasmGame.BATTLE) {
                    battle();
                } else if (game.state == WasmGame.SMASH) {
                    smash();
                }
            }
        } else if (tab == 1) {
            candyTab();
        } else if (tab == 2) {
            mineTab();
        } else if (tab == 3) {
            worldTab();
        } else if (tab == 4) {
            exploreTab();
        } else if (tab == 5) {
            gamesTab();
        } else {
            goalsTab();
        }
        if (toastTimer > 0) {
            ctx.setFillStyle("#7de38b");
            ctx.setFont("bold 15px monospace");
            ctx.setTextAlign("center");
            ctx.fillText(toast, W / 2, H - 12);
            ctx.setTextAlign("left");
        }
    }

    private static void tabs() {
        int tw = W / TABS.length;
        ctx.setFont("bold 13px monospace");
        ctx.setTextAlign("center");
        for (int i = 0; i < TABS.length; i++) {
            ctx.setFillStyle(i == tab ? "#ff8c00" : "#241640");
            ctx.fillRect(i * tw + 1, 1, tw - 2, TABH - 2);
            ctx.setFillStyle(i == tab ? "#000000" : "#e8e8e8");
            ctx.fillText(TABS[i], i * tw + tw / 2, 20);
        }
        ctx.setTextAlign("left");
    }

    private static void hud() {
        ctx.setFont("14px monospace");
        ctx.setFillStyle("#ffe14d");
        ctx.fillText(mgr.statusLine(), 12, TABH + 20);
        ctx.setFillStyle("#9a8ac0");
        ctx.fillText("Mine Au " + mine.player().gold + " coal " + mine.coal() + " "
                + Data.PICKS[mine.player().pickTier].name()
                + " | Q " + quests.doneCount() + "/" + quests.totalCount()
                + " E " + exps.doneCount() + "/" + ExpeditionCatalog.all().size()
                + " | depth " + game.depth + " score " + game.score, 12, TABH + 40);
    }

    private static void title() {
        ctx.setTextAlign("center");
        ctx.setFillStyle("#ff8c00");
        ctx.setFont("bold 40px monospace");
        ctx.fillText("SPOOKTACULAR", W / 2, 200);
        ctx.setFillStyle("#b06ae0");
        ctx.setFont("bold 22px monospace");
        ctx.fillText("all 7 tabs, Java -> WebAssembly", W / 2, 236);
        ctx.setFillStyle("#e8e8e8");
        ctx.setFont("16px monospace");
        ctx.fillText("MAZE CANDY MINE WORLD EXPLORE GAMES GOALS up top.", W / 2, 290);
        ctx.fillText("Maze: arrows/WASD, Z zap, X net. Mine: click. All feeds quests.", W / 2, 316);
        ctx.setFillStyle("#ffe14d");
        ctx.setFont("bold 19px monospace");
        ctx.fillText("ENTER or CLICK to start", W / 2, 370);
        ctx.setTextAlign("left");
    }

    private static int mazeCell() {
        int cw = (W - 24) / game.maze.w;
        int ch = (H - HUD - 24) / game.maze.d;
        return Math.min(cw, ch);
    }

    private static int mazeX() { return (W - mazeCell() * game.maze.w) / 2; }
    private static int mazeY() { return HUD + (H - HUD - mazeCell() * game.maze.d) / 2; }

    private static void maze() {
        int c = mazeCell();
        int x0 = mazeX();
        int y0 = mazeY();
        for (int x = 0; x < game.maze.w; x++) {
            for (int z = 0; z < game.maze.d; z++) {
                ctx.setFillStyle(game.maze.isOpen(x, z) ? "#171029" : "#3b1d5e");
                ctx.fillRect(x0 + x * c, y0 + z * c, c, c);
            }
        }
        ctx.setFillStyle("#ffb020");
        for (int i = 0; i < game.candies.size(); i++) {
            int[] cd = game.candies.get(i);
            ctx.beginPath();
            ctx.arc(x0 + cd[0] * c + c / 2.0, y0 + cd[1] * c + c / 2.0, c / 5.0, 0, Math.PI * 2);
            ctx.fill();
        }
        double ppx = x0 + game.px * c + c / 2.0;
        double ppy = y0 + game.pz * c + c / 2.0;
        ctx.setGlobalAlpha(0.06);
        ctx.setFillStyle("#ffb020");
        ctx.beginPath();
        ctx.arc(ppx, ppy, c * 4.0, 0, Math.PI * 2);
        ctx.fill();
        ctx.setGlobalAlpha(1.0);
        ctx.setFillStyle("#b06ae0");
        ctx.beginPath();
        ctx.arc(ppx, ppy, c / 3.0, 0, Math.PI * 2);
        ctx.fill();
        if (game.ghostActive) {
            double gx = x0 + game.gx * c + c / 2.0;
            double gy = y0 + game.gz * c + c / 2.0;
            ctx.setFillStyle("#f2f2f2");
            ctx.beginPath();
            ctx.arc(gx, gy, c / 3.0, 0, Math.PI * 2);
            ctx.fill();
            ctx.setFillStyle("#d82828");
            ctx.fillRect(gx - c / 6.0, gy - c / 8.0, c / 9.0, c / 9.0);
            ctx.fillRect(gx + c / 6.0 - c / 9.0, gy - c / 8.0, c / 9.0, c / 9.0);
        }
    }

    private static void battle() {
        int bw = 460;
        int bh = 190;
        int bx = (W - bw) / 2;
        int by = (H - bh) / 2;
        ctx.setGlobalAlpha(0.9);
        ctx.setFillStyle("#1c0f38");
        ctx.fillRect(bx, by, bw, bh);
        ctx.setGlobalAlpha(1.0);
        ctx.setStrokeStyle("#ff8c00");
        ctx.strokeRect(bx, by, bw, bh);
        ctx.setTextAlign("center");
        ctx.setFillStyle("#ff8c00");
        ctx.setFont("bold 20px monospace");
        ctx.fillText(game.ghostName + " HP " + game.ghostHp + "/" + game.ghostMaxHp, W / 2, by + 32);
        ctx.setFillStyle("#3a2a12");
        ctx.fillRect(bx + 40, by + 48, bw - 80, 16);
        ctx.setFillStyle("#7de38b");
        ctx.fillRect(bx + 40, by + 48, (bw - 80) * game.weakness(), 16);
        ctx.setFillStyle("#e8e8e8");
        ctx.setFont("16px monospace");
        ctx.fillText("weakness " + (int) (game.weakness() * 100) + "% fails "
                + game.battleFails + "/3", W / 2, by + 92);
        if (game.lastZap > 0) {
            ctx.fillText("ZAP -" + game.lastZap + (game.lastCrit ? " CRIT!" : ""), W / 2, by + 118);
        }
        ctx.setFillStyle("#ffe14d");
        ctx.setFont("bold 17px monospace");
        ctx.fillText("[Z] ZAP  [X] NET", W / 2, by + 152);
        ctx.setTextAlign("left");
    }

    private static void smash() {
        int gx0 = W / 2 - 96;
        int gy0 = HUD + 60;
        ctx.setTextAlign("center");
        ctx.setFillStyle("#ff8c00");
        ctx.setFont("bold 22px monospace");
        ctx.fillText("PUMPKIN SMASH (" + game.smashTicks + "/6)", W / 2, gy0 - 16);
        for (int i = 0; i < 9; i++) {
            int x = gx0 + (i % 3) * 64;
            int y = gy0 + (i / 3) * 64;
            ctx.setFillStyle("#241640");
            ctx.fillRect(x, y, 56, 56);
            if (game.smashLit[i]) {
                ctx.setFillStyle("#ff8c00");
                ctx.beginPath();
                ctx.arc(x + 28, y + 28, 20, 0, Math.PI * 2);
                ctx.fill();
            }
        }
        ctx.setTextAlign("left");
    }

    private static void candyTab() {
        ctx.setFont("13px monospace");
        int gx0 = 30;
        int gy0 = HUD + 30;
        for (int i = 0; i < Data.CANDIES.length; i++) {
            int cx = gx0 + (i % 6) * 112;
            int cy = gy0 + (i / 6) * 64;
            ctx.setFillStyle(candyGot[i] ? "#241640" : "#3b1d5e");
            ctx.fillRect(cx, cy, 106, 58);
            ctx.setFillStyle(candyGot[i] ? "#5a4a70" : "#ffe14d");
            ctx.fillText(Data.CANDIES[i].name(), cx + 6, cy + 22);
            ctx.setFillStyle("#9a8ac0");
            ctx.fillText(Data.CANDIES[i].points() + "pts x" + mgr.candyCount(Data.CANDIES[i].key()),
                    cx + 6, cy + 42);
        }
        ctx.setFillStyle("#7de38b");
        ctx.setFont("14px monospace");
        ctx.fillText(candyMsg, 30, gy0 + 3 * 64 + 24);
        ctx.setFillStyle("#e8e8e8");
        ctx.fillText("Potions (" + mgr.potions.size() + "):", 30, gy0 + 3 * 64 + 46);
        for (int i = 0; i < Math.min(3, mgr.potions.size()); i++) {
            Models.PotionItem p = mgr.potions.get(mgr.potions.size() - 1 - i);
            ctx.fillText("- " + p.name() + " [" + p.quality().name() + "]", 30, gy0 + 3 * 64 + 66 + i * 20);
        }
        ctx.setFillStyle("#9a8ac0");
        ctx.fillText("Next brew: " + brewNext.name() + " (sweetness = candy points)", 380, gy0 + 3 * 64 + 46);
    }

    private static String blockCss(Block b) {
        return css(WebVoxel.colorRgb(b));
    }

    private static void mineTab() {
        int gx0 = 30;
        int gy0 = HUD + 8;
        int cw = 44;
        int ch = 32;
        ctx.setFont("16px monospace");
        ctx.setTextAlign("center");
        for (int r = 0; r < WebMine.ROWS; r++) {
            for (int c = 0; c < WebMine.COLS; c++) {
                Block b = mine.blockAt(r, c);
                ctx.setFillStyle(blockCss(b));
                ctx.fillRect(gx0 + c * cw + 1, gy0 + r * ch + 1, cw - 2, ch - 2);
                ctx.setFillStyle("#ffffff");
                ctx.fillText(b.emoji, gx0 + c * cw + cw / 2, gy0 + r * ch + 22);
            }
        }
        ctx.setTextAlign("left");
        ctx.setFont("13px monospace");
        ctx.setFillStyle("#ffe14d");
        ctx.fillText("Au " + mine.player().gold + " coal " + mine.coal()
                + " Lv" + mine.player().level + " " + Data.PICKS[mine.player().pickTier].name()
                + " pack " + mine.player().backpackUsed() + "/" + mine.player().backpackCapacity
                + " sell " + mine.sellValue() + " pets " + mine.player().pets.size()
                + " reb " + mine.player().rebirths, 30, gy0 + WebMine.ROWS * ch - 6 + 32);
        int by = gy0 + WebMine.ROWS * ch + 8;
        String[] acts = { "SELL", "PICK", "PACK", "EGG", "REBIRTH", "VEIN", "CLAIM", "FISH" };
        ctx.setFont("bold 12px monospace");
        ctx.setTextAlign("center");
        for (int i = 0; i < acts.length; i++) {
            int bx = 30 + i * 86;
            ctx.setFillStyle("#3b1d5e");
            ctx.fillRect(bx, by, 80, 26);
            ctx.setFillStyle("#ffe14d");
            ctx.fillText(acts[i], bx + 40, by + 17);
        }
        ctx.setTextAlign("left");
        ctx.setFont("13px monospace");
        ctx.setFillStyle("#7de38b");
        ctx.fillText(mine.message, 30, by + 44);
    }

    private static void worldTab() {
        List<Models.Ghost> ghosts = Models.starterGhosts();
        ctx.setFont("14px monospace");
        ctx.setFillStyle("#ff8c00");
        ctx.fillText("Capture duels (real GhostCapture engine):", 20, HUD + 22);
        for (int i = 0; i < ghosts.size(); i++) {
            Models.Ghost g = ghosts.get(i);
            int ry = HUD + 40 + i * 30;
            ctx.setFillStyle(i == duelIdx ? "#3b1d5e" : "#171029");
            ctx.fillRect(20, ry, 310, 26);
            ctx.setFillStyle("#e8e8e8");
            ctx.fillText(g.name() + " [" + g.rarity().name() + "] HP" + g.maxHp(), 28, ry + 18);
        }
        if (duel != null) {
            ctx.setFillStyle("#ff8c00");
            ctx.fillText(duel.ghost.name() + " HP " + duel.hp + "/" + duel.maxHp, 380, HUD + 40);
            ctx.setFillStyle("#3a2a12");
            ctx.fillRect(380, HUD + 52, 260, 14);
            ctx.setFillStyle("#7de38b");
            ctx.fillRect(380, HUD + 52, 260 * duel.weakness(), 14);
            ctx.setFillStyle("#e8e8e8");
            ctx.fillText(duel.over ? (duel.captured ? "CAPTURED!" : "done") : "weakness "
                    + (int) (duel.weakness() * 100) + "%", 380, HUD + 92);
            ctx.setFillStyle("#3b1d5e");
            ctx.fillRect(380, HUD + 150, 120, 28);
            ctx.fillRect(510, HUD + 150, 130, 28);
            ctx.setFillStyle("#ffe14d");
            ctx.setFont("bold 13px monospace");
            ctx.fillText("[Z] ZAP", 415, HUD + 168);
            ctx.fillText("[X] NET", 550, HUD + 168);
            ctx.setFont("14px monospace");
        } else {
            ctx.setFillStyle("#9a8ac0");
            ctx.fillText("Pick a ghost on the left.", 380, HUD + 60);
        }
        List<Bestiary.Note> notes = Bestiary.all();
        int per = 2;
        int pages = (notes.size() + per - 1) / per;
        if (worldPage >= pages) {
            worldPage = pages - 1;
        }
        if (worldPage < 0) {
            worldPage = 0;
        }
        ctx.setFillStyle("#ff8c00");
        ctx.fillText("Bestiary (" + notes.size() + " notes, p" + (worldPage + 1) + "/" + pages + "):",
                380, H - 150);
        ctx.setFont("12px monospace");
        for (int i = 0; i < per; i++) {
            int k = worldPage * per + i;
            if (k >= notes.size()) {
                break;
            }
            Bestiary.Note n = notes.get(k);
            ctx.setFillStyle("#e8e8e8");
            ctx.fillText(n.monster() + " @ " + n.habitat(), 380, H - 128 + i * 44);
            ctx.setFillStyle("#9a8ac0");
            String tac = n.tactic();
            ctx.fillText(tac.substring(0, Math.min(52, tac.length())), 380, H - 112 + i * 44);
        }
        ctx.setFillStyle("#3b1d5e");
        ctx.fillRect(380, H - 60, 90, 24);
        ctx.fillRect(480, H - 60, 90, 24);
        ctx.setFillStyle("#ffe14d");
        ctx.setFont("bold 12px monospace");
        ctx.fillText("< PREV", 392, H - 44);
        ctx.fillText("NEXT >", 492, H - 44);
        ctx.setFont("14px monospace");
        ctx.setFillStyle("#9a8ac0");
        ctx.fillText("Catalog: " + Data.GHOSTS.length + " ghosts, " + CritterCodex.all().size()
                + " critters.", 20, H - 30);
    }

    private static double[] cubeTop(int l, int r, int c) {
        int tileW = 40;
        int tileH = 20;
        int cubeH = 22;
        int ox = 210;
        int oy = 190;
        return new double[] { ox + (c - r) * tileW / 2.0, oy + (c + r) * tileH / 2.0 - l * cubeH };
    }

    private static void cubePath(double x, double y, int hw, int hh) {
        ctx.beginPath();
        ctx.moveTo(x - hw, y);
        ctx.lineTo(x, y + hh);
        ctx.lineTo(x + hw, y);
        ctx.lineTo(x, y - hh);
        ctx.closePath();
    }

    private static int shade(int rgb, double f) {
        if (f > 1.3) {
            f = 1.3;
        }
        if (f < 0) {
            f = 0;
        }
        int r = (rgb >> 16) & 255;
        int g = (rgb >> 8) & 255;
        int b = rgb & 255;
        r = Math.min(255, (int) (r * f));
        g = Math.min(255, (int) (g * f));
        b = Math.min(255, (int) (b * f));
        return (r << 16) | (g << 8) | b;
    }

    private static void exploreTab() {
        int tileW = 40;
        int hw = tileW / 2;
        int hh = 10;
        int cubeH = 22;
        for (int l = voxel.levels - 1; l >= 0; l--) {
            for (int s = 0; s < voxel.rows + voxel.cols - 1; s++) {
                for (int r = 0; r < voxel.rows; r++) {
                    int c = s - r;
                    if (c < 0 || c >= voxel.cols) {
                        continue;
                    }
                    double[] p = cubeTop(l, r, c);
                    double x = p[0];
                    double y = p[1];
                    Block b = voxel.at(l, r, c);
                    if (b == null) {
                        ctx.setFillStyle("#05030a");
                        cubePath(x, y, hw, hh);
                        ctx.fill();
                        continue;
                    }
                    int base = WebVoxel.colorRgb(b);
                    double li = (1.0 - l * 0.16) * 0.9;
                    ctx.setFillStyle(css(shade(base, li * 0.7)));
                    ctx.beginPath();
                    ctx.moveTo(x - hw, y);
                    ctx.lineTo(x, y + hh);
                    ctx.lineTo(x, y + hh + cubeH);
                    ctx.lineTo(x - hw, y + cubeH);
                    ctx.closePath();
                    ctx.fill();
                    ctx.setFillStyle(css(shade(base, li * 0.55)));
                    ctx.beginPath();
                    ctx.moveTo(x + hw, y);
                    ctx.lineTo(x, y + hh);
                    ctx.lineTo(x, y + hh + cubeH);
                    ctx.lineTo(x + hw, y + cubeH);
                    ctx.closePath();
                    ctx.fill();
                    ctx.setFillStyle(css(shade(base, li + 0.2)));
                    cubePath(x, y, hw, hh);
                    ctx.fill();
                    if (b.ore) {
                        ctx.setFillStyle("#ffffff");
                        ctx.beginPath();
                        ctx.arc(x, y, 2.5, 0, Math.PI * 2);
                        ctx.fill();
                    }
                }
            }
        }
        ctx.setFont("13px monospace");
        ctx.setFillStyle("#ffe14d");
        ctx.fillText("3D VOXEL MINE  pick T" + exploreTier + " (keys 1-4)", 440, HUD + 20);
        ctx.setFillStyle("#e8e8e8");
        ctx.fillText("mined " + voxel.mined() + "/" + voxel.total() + " Au+" + voxel.goldBanked()
                + " xp+" + voxel.xpBanked(), 440, HUD + 40);
        List<LayerCodex.Layer> layers = LayerCodex.all();
        for (int i = 0; i < layers.size() && i < 3; i++) {
            ctx.setFillStyle(layerKnown[i] ? "#7de38b" : "#5a4a70");
            ctx.fillText((layerKnown[i] ? "* " : "- ") + layers.get(i).title(), 440, HUD + 66 + i * 20);
        }
        ctx.setFillStyle("#9a8ac0");
        ctx.fillText(OreCodex.all().size() + " ores catalogued.", 440, HUD + 140);
        ctx.setFillStyle("#7de38b");
        ctx.fillText(exploreMsg, 30, H - 16);
    }

    private static void gamesTab() {
        ctx.setFont("14px monospace");
        ctx.setFillStyle("#ff8c00");
        ctx.fillText("Memory Match (moves " + mem.moves + " found " + mem.found + "/6):", 30, HUD + 20);
        int gx0 = 30;
        int gy0 = HUD + 40;
        ctx.setTextAlign("center");
        ctx.setFont("bold 18px monospace");
        for (int i = 0; i < mem.board.length; i++) {
            int x = gx0 + (i % 4) * 64;
            int y = gy0 + (i / 4) * 64;
            boolean up = mem.faceUp[i] || mem.matched[i];
            ctx.setFillStyle(mem.matched[i] ? "#1d3a24" : up ? "#e8b820" : "#3b1d5e");
            ctx.fillRect(x, y, 56, 56);
            ctx.setFillStyle(up ? "#000000" : "#9a8ac0");
            ctx.fillText(up ? "" + mem.board[i] : "?", x + 28, y + 36);
        }
        ctx.setTextAlign("left");
        ctx.setFillStyle("#ff8c00");
        ctx.fillText("Pumpkin Smash (" + psmash.score + "pts):", 400, HUD + 20);
        int sx0 = 400;
        int sy0 = HUD + 40;
        ctx.setTextAlign("center");
        for (int i = 0; i < 9; i++) {
            int x = sx0 + (i % 3) * 64;
            int y = sy0 + (i / 3) * 64;
            ctx.setFillStyle("#241640");
            ctx.fillRect(x, y, 56, 56);
            if (psmash.lit[i]) {
                ctx.setFillStyle("#ff8c00");
                ctx.beginPath();
                ctx.arc(x + 28, y + 28, 20, 0, Math.PI * 2);
                ctx.fill();
            }
        }
        ctx.setTextAlign("left");
        ctx.setFont("12px monospace");
        ctx.setFillStyle("#9a8ac0");
        ctx.fillText("Arcade (" + Data.MINIGAMES.length + "):", 30, HUD + 260);
        for (int i = 0; i < Data.MINIGAMES.length; i++) {
            boolean play = Data.MINIGAMES[i].key().equals("memoryMatch")
                    || Data.MINIGAMES[i].key().equals("pumpkinSmash");
            ctx.setFillStyle(play ? "#7de38b" : "#5a4a70");
            ctx.fillText((play ? "* " : "- ") + Data.MINIGAMES[i].name(), 30 + (i / 6) * 330,
                    HUD + 280 + (i % 6) * 20);
        }
    }

    private static void goalsTab() {
        ctx.setFont("13px monospace");
        int y = HUD + 20;
        ctx.setFillStyle("#ff8c00");
        ctx.fillText("QUESTS " + quests.doneCount() + "/" + QuestCatalog.all().size(), 20, y);
        y += 22;
        List<Quest> qa = quests.active();
        for (int i = 0; i < qa.size(); i++) {
            Quest q = qa.get(i);
            ctx.setFillStyle("#e8e8e8");
            ctx.fillText(q.title() + " - " + quests.progressOf(q) + "/" + quests.targetOf(q),
                    20, y);
            ctx.setFillStyle("#3a2a12");
            ctx.fillRect(20, y + 6, 300, 8);
            ctx.setFillStyle("#7de38b");
            ctx.fillRect(20, y + 6, 300 * quests.fractionOf(q), 8);
            if (quests.isDone(q)) {
                ctx.setFillStyle("#ff8c00");
                ctx.fillRect(330, y - 12, 70, 22);
                ctx.setFillStyle("#000000");
                ctx.fillText("CLAIM", 340, y + 3);
                zones.add(new int[] { 330, (int) (y - 12), 70, 22, 0, i });
            }
            y += 34;
        }
        y += 6;
        ctx.setFillStyle("#ff8c00");
        ctx.fillText("EXPEDITIONS " + exps.doneCount() + "/" + ExpeditionCatalog.all().size(), 20, y);
        y += 22;
        List<Expedition> ea = exps.active();
        for (int i = 0; i < ea.size(); i++) {
            Expedition e = ea.get(i);
            ctx.setFillStyle("#e8e8e8");
            ctx.fillText(e.title() + " - " + exps.progressOf(e) + "/" + e.target(), 20, y);
            ctx.setFillStyle("#3a2a12");
            ctx.fillRect(20, y + 6, 300, 8);
            ctx.setFillStyle("#7de38b");
            ctx.fillRect(20, y + 6, 300 * exps.fractionOf(e), 8);
            if (exps.isDone(e)) {
                ctx.setFillStyle("#ff8c00");
                ctx.fillRect(330, y - 12, 70, 22);
                ctx.setFillStyle("#000000");
                ctx.fillText("CLAIM", 340, y + 3);
                zones.add(new int[] { 330, (int) (y - 12), 70, 22, 1, i });
            }
            y += 34;
        }
        y += 6;
        ctx.setFillStyle("#ff8c00");
        ctx.fillText("ACHIEVEMENTS (catalog " + AchievementCatalog.all().size() + "):", 20, y);
        y += 22;
        List<Achievement> all = AchievementCatalog.all();
        int shown = 0;
        int idx = 0;
        for (int k = 0; k < all.size() && shown < 4; k++) {
            Achievement a = all.get(k);
            if (!achs.isUnlocked(a.id()) || achs.isClaimed(a.id())) {
                continue;
            }
            ctx.setFillStyle("#e8e8e8");
            ctx.fillText("* " + a.title() + " (+" + a.rewardGold() + "g, +" + a.rewardXP() + "xp)",
                    20, y);
            ctx.setFillStyle("#ff8c00");
            ctx.fillRect(560, y - 12, 70, 22);
            ctx.setFillStyle("#000000");
            ctx.fillText("CLAIM", 570, y + 3);
            zones.add(new int[] { 560, (int) (y - 12), 70, 22, 2, idx });
            idx++;
            shown++;
            y += 30;
        }
        if (shown == 0) {
            ctx.setFillStyle("#9a8ac0");
            ctx.fillText("Play mine/maze - unlocks appear here.", 20, y);
        }
    }
}
