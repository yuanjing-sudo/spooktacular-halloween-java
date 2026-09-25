package spooktacular.game;

import spooktacular.data.Block;
import spooktacular.engine.Engine;
import spooktacular.systems.PerlinNoise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/** Isometric software-3D voxel mine — Java expression of the voxel-mine 3D parts:
 *  AbandonedMine.swift (SceneKit third-person voxel mine, 1/3-size blocks, ores /
 *  beams / lava, depth layers, pick tiers), python-3d/mine3d_ursina.py (Ursina
 *  voxel mine) and docs/mine3d.js (web 3D mine). Pure JDK Swing, zero deps:
 *  painter's-algorithm isometric cubes, depth fog, torch glow, Perlin strata.
 *
 *  <p>Model is UI-free so headless tests can drive it (see VoxelMineTest). */
public class VoxelMinePanel extends JPanel implements MouseListener, KeyListener {
    /** UI-free voxel world: levels stack surface(0)..deep(2); a block can be mined
     *  only when exposed (nothing unmined directly above it). */
    public static final class Model {
        public final int cols, rows, levels;
        private final Block[][][] grid; // [level][row][col], null = mined/empty
        private int goldBanked = 0, xpBanked = 0, mined = 0;
        private final List<String> minedNames = new ArrayList<>();

        public Model(int cols, int rows, int levels, long seed) {
            this.cols = cols; this.rows = rows; this.levels = levels;
            this.grid = new Block[levels][rows][cols];
            Engine.RNG rng = new Engine.RNG(seed);
            PerlinNoise perlin = new PerlinNoise(seed ^ 0x5EEDL);
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    double h = perlin.fbm(c * 0.35, r * 0.35, 3, 2.0, 0.5); // -1..1
                    for (int l = 0; l < levels; l++) {
                        double roll = rng.nextDouble();
                        Block b;
                        if (l == 0) b = pickSurface(rng, roll, h);
                        else if (l == 1) b = pickMid(rng, roll, h);
                        else b = pickDeep(rng, roll, h, r);
                        // timber supports every 5th column on mid level, lava pockets deep
                        if (l == 1 && c % 5 == 0 && rng.nextDouble() < 0.6) b = Block.woodBeam;
                        grid[l][r][c] = b;
                    }
                }
            }
            // bedrock floor pins the deep level corners so the world has a bottom
            for (int c = 0; c < cols; c++) { grid[levels - 1][rows - 1][c] = Block.bedrock; }
        }

        private static Block pickSurface(Engine.RNG rng, double roll, double h) {
            if (roll < 0.55) return Block.stone;
            if (roll < 0.68) return Block.dirt;
            if (roll < 0.78) return Block.coalOre;
            if (roll < 0.86) return Block.ironOre;
            if (h > 0.45) return Block.goldOre;
            if (h < -0.45) return Block.lapisOre;
            return Block.gravel;
        }

        private static Block pickMid(Engine.RNG rng, double roll, double h) {
            if (roll < 0.35) return Block.deepslate;
            if (roll < 0.47) return Block.goldOre;
            if (roll < 0.55) return Block.lapisOre;
            if (roll < 0.62) return Block.redstoneOre;
            if (roll < 0.68) return Block.crystalCube;
            if (h > 0.5) return Block.emeraldOre;
            if (roll < 0.80) return Block.ironOre;
            return Block.coalOre;
        }

        private static Block pickDeep(Engine.RNG rng, double roll, double h, int r) {
            if (roll < 0.20) return Block.deepslate;
            if (roll < 0.30) return Block.diamondOre;
            if (roll < 0.37) return Block.emeraldOre;
            if (roll < 0.43) return Block.rubyOre;
            if (roll < 0.49) return Block.opalOre;
            if (roll < 0.56) return Block.crystalSpike;
            if (roll < 0.62) return Block.crystalOrb;
            if (roll < 0.67) return Block.frostOre;
            if (roll < 0.71) return Block.glacierCrystal;
            if (roll < 0.74) return Block.lava;
            if (roll < 0.78) return Block.closetCrate;
            return Block.goldOre;
        }

        public Block at(int l, int r, int c) {
            if (l < 0 || l >= levels || r < 0 || r >= rows || c < 0 || c >= cols) return null;
            return grid[l][r][c];
        }

        /** True when no unmined block sits above (l-1 is up). Surface is always exposed. */
        public boolean exposed(int l, int r, int c) {
            if (at(l, r, c) == null) return false;
            if (l == 0) return true;
            return at(l - 1, r, c) == null;
        }

        /** Mine result: null = cannot mine (empty/hidden/unbreakable/tier too low). */
        public record Yield(Block block, int gold, int xp) {}

        public Yield mine(int l, int r, int c, int pickTier) {
            Block b = at(l, r, c);
            if (b == null || !exposed(l, r, c) || b.unbreakable || pickTier < b.minTier) return null;
            grid[l][r][c] = null;
            mined++;
            goldBanked += b.gold;
            xpBanked += b.xp;
            minedNames.add(b.displayName);
            return new Yield(b, b.gold, b.xp);
        }

        public int remaining() {
            int n = 0;
            for (int l = 0; l < levels; l++)
                for (int r = 0; r < rows; r++)
                    for (int c = 0; c < cols; c++)
                        if (grid[l][r][c] != null) n++;
            return n;
        }

        public int total() { return cols * rows * levels; }
        public int mined() { return mined; }
        public int goldBanked() { return goldBanked; }
        public int xpBanked() { return xpBanked; }
        public List<String> minedNames() { return List.copyOf(minedNames); }
    }

    private final Model model;
    private int cursorC = 2, cursorR = 2, cursorL = 0;
    private int pickTier = 1;
    private int tileW = 56, tileH = 28, cubeH = 30;
    private double torchPhase = 0;
    private final JLabel status = new JLabel("", SwingConstants.CENTER);
    private final Timer timer;

    public VoxelMinePanel() { this(new Model(12, 8, 3, 20261031L)); }

    public VoxelMinePanel(Model model) {
        super(new BorderLayout());
        this.model = model;
        add(status, BorderLayout.SOUTH);
        addMouseListener(this);
        setFocusable(true);
        addKeyListener(this);
        setPreferredSize(new Dimension(760, 560));
        timer = new Timer(120, e -> { torchPhase += 0.25; repaint(); });
        timer.start();
        refresh();
    }

    public Model model() { return model; }

    private void refresh() {
        status.setText("Mine 3D · cursor [" + cursorL + "," + cursorR + "," + cursorC + "] pick T" + pickTier
            + " · mined " + model.mined() + "/" + model.total()
            + " · gold " + model.goldBanked() + " · xp " + model.xpBanked()
            + " · click/arrows+space mine, R regen hint, 1-4 pick");
    }

    // ---------- isometric projection ----------
    private int originX() { return getWidth() / 2; }
    private int originY() { return 90; }

    private Point cubeTop(int l, int r, int c) {
        int x = originX() + (c - r) * tileW / 2;
        int y = originY() + (c + r) * tileH / 2 - l * cubeH;
        return new Point(x, y);
    }

    static Color baseColor(Block b) {
        return switch (b) {
            case stone, deepslate, gravel -> new Color(0x8a, 0x8a, 0x92);
            case dirt -> new Color(0x8a, 0x5a, 0x2e);
            case coalOre -> new Color(0x2b, 0x2b, 0x30);
            case ironOre -> new Color(0xa0, 0x6a, 0x3a);
            case goldOre -> new Color(0xe8, 0xb8, 0x20);
            case lapisOre -> new Color(0x2a, 0x4a, 0xd8);
            case redstoneOre -> new Color(0xd8, 0x2a, 0x2a);
            case emeraldOre -> new Color(0x2a, 0xc8, 0x5a);
            case rubyOre -> new Color(0xe8, 0x4a, 0x8a);
            case diamondOre -> new Color(0x4a, 0xe8, 0xe8);
            case opalOre -> new Color(0x9a, 0x6a, 0xe8);
            case crystalCube, crystalSpike, crystalOrb -> new Color(0x8a, 0x4a, 0xd8);
            case frostOre -> new Color(0xaa, 0xdd, 0xf0);
            case glacierCrystal -> new Color(0xc8, 0xf0, 0xff);
            case snowstone -> new Color(0xe8, 0xe8, 0xe8);
            case woodBeam, planks -> new Color(0x6a, 0x42, 0x1a);
            case closetCrate -> new Color(0xb0, 0x7a, 0x30);
            case lava -> new Color(0xf0, 0x5a, 0x10);
            case bedrock -> new Color(0x1a, 0x1a, 0x1e);
        };
    }

    private Color shade(Color c, double f) {
        f = Math.max(0, Math.min(1.35, f));
        return new Color(
            Math.min(255, (int) (c.getRed() * f)),
            Math.min(255, (int) (c.getGreen() * f)),
            Math.min(255, (int) (c.getBlue() * f)));
    }

    /** Depth fog + torch glow: deeper = darker, near cursor = warmer/brighter. */
    private double light(int l, int r, int c) {
        double fog = 1.0 - l * 0.16;
        double d = Math.hypot(c - cursorC, r - cursorR) + Math.abs(l - cursorL) * 0.7;
        double flicker = 0.06 * Math.sin(torchPhase * 2.1);
        double glow = Math.max(0, 1.0 - d / 5.5) * (0.55 + flicker);
        return fog * (0.72 + glow);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(0x0d, 0x08, 0x1c));
        g.fillRect(0, 0, getWidth(), getHeight());
        // painter's order: deep levels first? draw bottom-up back-to-front:
        for (int l = model.levels - 1; l >= 0; l--) {
            for (int s = 0; s < model.rows + model.cols - 1; s++) {
                for (int r = 0; r < model.rows; r++) {
                    int c = s - r;
                    if (c < 0 || c >= model.cols) continue;
                    drawCube(g, l, r, c);
                }
            }
        }
        g.dispose();
    }

    private void drawCube(Graphics2D g, int l, int r, int c) {
        Point top = cubeTop(l, r, c);
        int x = top.x, y = top.y;
        int hw = tileW / 2, hh = tileH / 2;
        Block b = model.at(l, r, c);
        boolean cursor = (l == cursorL && r == cursorR && c == cursorC);
        if (b == null) {
            // mined-out hole: dark socket + faint strata ring
            g.setColor(new Color(0x05, 0x03, 0x0a));
            g.fillPolygon(new int[]{x - hw, x, x + hw, x}, new int[]{y, y + hh, y, y - hh}, 4);
            if (cursor) { g.setColor(Color.YELLOW); g.drawPolygon(new int[]{x - hw, x, x + hw, x}, new int[]{y, y + hh, y, y - hh}, 4); }
            return;
        }
        Color base = baseColor(b);
        double li = light(l, r, c);
        Color topC = shade(base, li + 0.22);
        Color leftC = shade(base, li * 0.78);
        Color rightC = shade(base, li * 0.60);
        if (b == Block.lava) { // animated glow, never minable
            double pulse = 0.5 + 0.5 * Math.sin(torchPhase * 1.7 + r + c);
            topC = new Color(0xf0, (int) (0x40 + 60 * pulse), 0x10);
            leftC = new Color(0xb0, 0x30, 0x08);
            rightC = new Color(0x80, 0x20, 0x06);
        }
        int[] tx = {x - hw, x, x + hw, x};
        int[] ty = {y, y + hh, y, y - hh};
        // left + right walls
        g.setColor(leftC);
        g.fillPolygon(new int[]{x - hw, x, x, x - hw}, new int[]{y, y + hh, y + hh + cubeH, y + cubeH}, 4);
        g.setColor(rightC);
        g.fillPolygon(new int[]{x + hw, x, x, x + hw}, new int[]{y, y + hh, y + hh + cubeH, y + cubeH}, 4);
        // top face
        g.setColor(topC);
        g.fillPolygon(tx, ty, 4);
        g.setColor(new Color(0, 0, 0, 90));
        g.drawPolygon(tx, ty, 4);
        // ore sparkle
        if (b.ore || b == Block.diamondOre || b == Block.opalOre) {
            g.setColor(new Color(255, 255, 255, 160));
            g.fillOval(x - 3, y - 2, 6, 5);
        }
        if (!model.exposed(l, r, c)) { // buried: dim veil so levels read as 3D strata
            g.setColor(new Color(0x0d, 0x08, 0x1c, 110));
            g.fillPolygon(tx, ty, 4);
        }
        if (b.unbreakable) {
            g.setColor(new Color(255, 255, 255, 70));
            g.drawLine(x - hw + 4, y, x + hw - 4, y);
        }
        if (cursor) {
            g.setColor(Color.YELLOW);
            g.setStroke(new BasicStroke(2.5f));
            g.drawPolygon(tx, ty, 4);
            g.setStroke(new BasicStroke(1f));
        }
    }

    private void tryMine() {
        Model.Yield y = model.mine(cursorL, cursorR, cursorC, pickTier);
        if (y != null) {
            status.setText("Mined " + y.block().displayName + " (+" + y.gold() + "g, +" + y.xp() + "xp) · gold "
                + model.goldBanked() + " · xp " + model.xpBanked());
        } else {
            Block b = model.at(cursorL, cursorR, cursorC);
            if (b == null) status.setText("Empty socket — move the torch.");
            else if (b.unbreakable) status.setText(b.displayName + " is unbreakable (bedrock/lava).");
            else if (pickTier < b.minTier) status.setText(b.displayName + " needs pick T" + b.minTier + " (you: T" + pickTier + ").");
            else status.setText("Buried — mine the block above it first (3D strata rule).");
        }
        refreshStatusKeepMessage();
        repaint();
    }

    private void refreshStatusKeepMessage() {
        String msg = status.getText();
        refresh();
        if (!msg.startsWith("Mine 3D")) status.setText(msg + "   |   " + status.getText());
    }

    // ---------- input ----------
    @Override public void mouseClicked(MouseEvent e) {
        // pick nearest cube top within radius
        double best = 1e9; int bl = cursorL, br = cursorR, bc = cursorC;
        for (int l = 0; l < model.levels; l++)
            for (int r = 0; r < model.rows; r++)
                for (int c = 0; c < model.cols; c++) {
                    Point p = cubeTop(l, r, c);
                    double d = Math.hypot(p.x - e.getX(), p.y - e.getY());
                    if (d < best) { best = d; bl = l; br = r; bc = c; }
                }
        if (best < tileW) { cursorL = bl; cursorR = br; cursorC = bc; tryMine(); }
        requestFocusInWindow();
    }
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    @Override public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> cursorC = Math.max(0, cursorC - 1);
            case KeyEvent.VK_RIGHT -> cursorC = Math.min(model.cols - 1, cursorC + 1);
            case KeyEvent.VK_UP -> cursorR = Math.max(0, cursorR - 1);
            case KeyEvent.VK_DOWN -> cursorR = Math.min(model.rows - 1, cursorR + 1);
            case KeyEvent.VK_PAGE_UP -> cursorL = Math.max(0, cursorL - 1);
            case KeyEvent.VK_PAGE_DOWN -> cursorL = Math.min(model.levels - 1, cursorL + 1);
            case KeyEvent.VK_SPACE -> { tryMine(); return; }
            case KeyEvent.VK_1 -> pickTier = 0;
            case KeyEvent.VK_2 -> pickTier = 1;
            case KeyEvent.VK_3 -> pickTier = 2;
            case KeyEvent.VK_4 -> pickTier = 4;
            default -> { return; }
        }
        refresh();
        repaint();
    }
    @Override public void keyReleased(KeyEvent e) {}
}
