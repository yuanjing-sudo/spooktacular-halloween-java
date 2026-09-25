package spooktacular.web;

import org.teavm.jso.browser.AnimationFrameCallback;
import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.events.KeyboardEvent;
import org.teavm.jso.dom.events.MouseEvent;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;

/** Browser shell — Java compiled by TeaVM to WebAssembly (WEBASSEMBLY_GC).
 *  Canvas renderer + input + loop. Rules live in WasmGame (twin of the
 *  spooktacular.* Java port: Engine, GhostCapture, CandyCollection,
 *  MiniGames.PumpkinSmash). Entry: main() via teavm.exports.main([]). */
public class Client {
    private static final int W = 720;
    private static final int H = 560;
    private static final int HUD = 92;

    private static WasmGame game = new WasmGame();
    private static CanvasRenderingContext2D ctx;
    private static double lastTs = -1;

    public static void main(String[] args) {
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
        }
        render();
    }

    private static void onKey(KeyboardEvent evt) {
        String k = evt.getKey();
        if (k == null) {
            return;
        }
        if (game.state == WasmGame.TITLE) {
            if (k.equals("Enter") || k.equals(" ")) {
                game.start((long) lastTs * 1000003L + 0x71C3L);
            }
            return;
        }
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
        } else if ((k.equals("x") || k.equals("X") || k.equals("Enter")) && game.state == WasmGame.BATTLE) {
            game.throwNet();
        }
    }

    private static void onClick(MouseEvent evt) {
        if (game.state == WasmGame.TITLE) {
            game.start((long) lastTs * 1000003L + 0x71C3L);
            return;
        }
        if (game.state != WasmGame.SMASH) {
            return;
        }
        int gx0 = W / 2 - 96;
        int gy0 = HUD + 60;
        int cx = (evt.getClientX() - gx0) / 64;
        int cy = (evt.getClientY() - gy0) / 64;
        if (cx >= 0 && cx < 3 && cy >= 0 && cy < 3) {
            game.smashAt(cy * 3 + cx);
        }
    }

    private static int cell() {
        int cw = (W - 24) / game.maze.w;
        int ch = (H - HUD - 24) / game.maze.d;
        return Math.min(cw, ch);
    }

    private static int mazeX() {
        return (W - cell() * game.maze.w) / 2;
    }

    private static int mazeY() {
        return HUD + (H - HUD - cell() * game.maze.d) / 2;
    }

    private static void render() {
        ctx.setFillStyle("#0d081c");
        ctx.fillRect(0, 0, W, H);
        if (game.state == WasmGame.TITLE) {
            title();
            return;
        }
        hud();
        maze();
        if (game.state == WasmGame.BATTLE) {
            battle();
        } else if (game.state == WasmGame.SMASH) {
            smash();
        }
    }

    private static void title() {
        ctx.setTextAlign("center");
        ctx.setFillStyle("#ff8c00");
        ctx.setFont("bold 44px monospace");
        ctx.fillText("SPOOKTACULAR", W / 2, 150);
        ctx.setFillStyle("#b06ae0");
        ctx.setFont("bold 24px monospace");
        ctx.fillText("Java -> WebAssembly edition", W / 2, 190);
        ctx.setFillStyle("#e8e8e8");
        ctx.setFont("17px monospace");
        ctx.fillText("Explore the maze. Grab every candy.", W / 2, 250);
        ctx.fillText("ZAP ghosts weak (Z), then throw the NET (X).", W / 2, 278);
        ctx.fillText("Clear a floor for a pumpkin-smash bonus.", W / 2, 306);
        ctx.setFillStyle("#ffe14d");
        ctx.setFont("bold 20px monospace");
        ctx.fillText("MOVE: arrows / WASD    ENTER or CLICK to start", W / 2, 370);
        ctx.setTextAlign("left");
    }

    private static void hud() {
        ctx.setTextAlign("left");
        ctx.setFont("15px monospace");
        ctx.setFillStyle("#ffe14d");
        ctx.fillText("DEPTH " + game.depth + "   SCORE " + game.score
                + "   LV " + game.level + " (" + game.xp + "/" + (game.level * 100) + ")"
                + "   GHOSTS " + game.captured, 12, 22);
        ctx.setFillStyle("#e8e8e8");
        ctx.fillText("COMBO x" + game.combo + " (best " + game.bestCombo + ")"
                + "   CANDY LEFT " + game.candies.size()
                + "   TAKEN " + game.candyTaken, 12, 44);
        if (game.messageTimer > 0) {
            ctx.setFillStyle("#7de38b");
            ctx.fillText(game.message, 12, 66);
        }
    }

    private static void maze() {
        int c = cell();
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
        ctx.setGlobalAlpha(0.05);
        ctx.setFillStyle("#ffb020");
        ctx.beginPath();
        ctx.arc(ppx, ppy, c * 5.0, 0, Math.PI * 2);
        ctx.fill();
        ctx.setGlobalAlpha(0.07);
        ctx.beginPath();
        ctx.arc(ppx, ppy, c * 3.0, 0, Math.PI * 2);
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
        ctx.setGlobalAlpha(0.88);
        ctx.setFillStyle("#1c0f38");
        ctx.fillRect(bx, by, bw, bh);
        ctx.setGlobalAlpha(1.0);
        ctx.setStrokeStyle("#ff8c00");
        ctx.strokeRect(bx, by, bw, bh);
        ctx.setTextAlign("center");
        ctx.setFillStyle("#ff8c00");
        ctx.setFont("bold 20px monospace");
        ctx.fillText(game.ghostName + "  HP " + game.ghostHp + "/" + game.ghostMaxHp, W / 2, by + 32);
        double weak = game.weakness();
        ctx.setFillStyle("#3a2a12");
        ctx.fillRect(bx + 40, by + 48, bw - 80, 16);
        ctx.setFillStyle("#7de38b");
        ctx.fillRect(bx + 40, by + 48, (bw - 80) * weak, 16);
        ctx.setFillStyle("#e8e8e8");
        ctx.setFont("16px monospace");
        ctx.fillText("weakness " + (int) (weak * 100) + "%   fails " + game.battleFails + "/3", W / 2, by + 92);
        if (game.lastZap > 0) {
            ctx.fillText("ZAP -" + game.lastZap + (game.lastCrit ? " CRIT!" : ""), W / 2, by + 118);
        }
        ctx.setFillStyle("#ffe14d");
        ctx.setFont("bold 17px monospace");
        ctx.fillText("[Z] ZAP weakens     [X] THROW NET", W / 2, by + 152);
        ctx.setTextAlign("left");
    }

    private static void smash() {
        int gx0 = W / 2 - 96;
        int gy0 = HUD + 60;
        ctx.setTextAlign("center");
        ctx.setFillStyle("#ff8c00");
        ctx.setFont("bold 22px monospace");
        ctx.fillText("PUMPKIN SMASH BONUS  (" + game.smashTicks + "/6)", W / 2, gy0 - 16);
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
                ctx.setFillStyle("#3a1c00");
                ctx.fillRect(x + 24, y + 4, 8, 8);
            }
        }
        ctx.setTextAlign("left");
    }
}
