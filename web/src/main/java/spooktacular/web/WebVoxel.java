package spooktacular.web;

import java.util.ArrayList;
import java.util.List;

import spooktacular.data.Block;
import spooktacular.engine.Engine;
import spooktacular.systems.PerlinNoise;

/** Isometric voxel-mine model twin of game/VoxelMinePanel.Model (desktop Swing).
 *  Real Block bands, real PerlinNoise strata, exposed/strata rule, pick tiers.
 *  Colors as packed 0xRRGGBB ints (no java.awt in WASM). */
public final class WebVoxel {
    public final int cols;
    public final int rows;
    public final int levels;
    private final Block[][][] grid;
    private int goldBanked = 0;
    private int xpBanked = 0;
    private int mined = 0;
    private final List<String> minedNames = new ArrayList<String>();

    public WebVoxel(int cols, int rows, int levels, long seed) {
        this.cols = cols;
        this.rows = rows;
        this.levels = levels;
        this.grid = new Block[levels][rows][cols];
        Engine.RNG rng = new Engine.RNG(seed);
        PerlinNoise perlin = new PerlinNoise(seed ^ 0x5EEDL);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double h = perlin.fbm(c * 0.35, r * 0.35, 3, 2.0, 0.5);
                for (int l = 0; l < levels; l++) {
                    double roll = rng.nextDouble();
                    Block b;
                    if (l == 0) {
                        b = pickSurface(roll, h);
                    } else if (l == 1) {
                        b = pickMid(roll, h);
                    } else {
                        b = pickDeep(roll, h);
                    }
                    if (l == 1 && c % 5 == 0 && rng.nextDouble() < 0.6) {
                        b = Block.woodBeam;
                    }
                    grid[l][r][c] = b;
                }
            }
        }
        for (int c = 0; c < cols; c++) {
            grid[levels - 1][rows - 1][c] = Block.bedrock;
        }
    }

    private static Block pickSurface(double roll, double h) {
        if (roll < 0.55) {
            return Block.stone;
        }
        if (roll < 0.68) {
            return Block.dirt;
        }
        if (roll < 0.78) {
            return Block.coalOre;
        }
        if (roll < 0.86) {
            return Block.ironOre;
        }
        if (h > 0.45) {
            return Block.goldOre;
        }
        if (h < -0.45) {
            return Block.lapisOre;
        }
        return Block.gravel;
    }

    private static Block pickMid(double roll, double h) {
        if (roll < 0.35) {
            return Block.deepslate;
        }
        if (roll < 0.47) {
            return Block.goldOre;
        }
        if (roll < 0.55) {
            return Block.lapisOre;
        }
        if (roll < 0.62) {
            return Block.redstoneOre;
        }
        if (roll < 0.68) {
            return Block.crystalCube;
        }
        if (h > 0.5) {
            return Block.emeraldOre;
        }
        if (roll < 0.80) {
            return Block.ironOre;
        }
        return Block.coalOre;
    }

    private static Block pickDeep(double roll, double h) {
        if (roll < 0.20) {
            return Block.deepslate;
        }
        if (roll < 0.30) {
            return Block.diamondOre;
        }
        if (roll < 0.37) {
            return Block.emeraldOre;
        }
        if (roll < 0.43) {
            return Block.rubyOre;
        }
        if (roll < 0.49) {
            return Block.opalOre;
        }
        if (roll < 0.56) {
            return Block.crystalSpike;
        }
        if (roll < 0.62) {
            return Block.crystalOrb;
        }
        if (roll < 0.67) {
            return Block.frostOre;
        }
        if (roll < 0.71) {
            return Block.glacierCrystal;
        }
        if (roll < 0.74) {
            return Block.lava;
        }
        if (roll < 0.78) {
            return Block.closetCrate;
        }
        return Block.goldOre;
    }

    public Block at(int l, int r, int c) {
        if (l < 0 || l >= levels || r < 0 || r >= rows || c < 0 || c >= cols) {
            return null;
        }
        return grid[l][r][c];
    }

    public boolean exposed(int l, int r, int c) {
        if (at(l, r, c) == null) {
            return false;
        }
        if (l == 0) {
            return true;
        }
        return at(l - 1, r, c) == null;
    }

    public static final class Yield {
        public final Block block;
        public final int gold;
        public final int xp;
        public Yield(Block block, int gold, int xp) {
            this.block = block;
            this.gold = gold;
            this.xp = xp;
        }
    }

    public Yield mine(int l, int r, int c, int pickTier) {
        Block b = at(l, r, c);
        if (b == null || !exposed(l, r, c) || b.unbreakable || pickTier < b.minTier) {
            return null;
        }
        grid[l][r][c] = null;
        mined++;
        goldBanked += b.gold;
        xpBanked += b.xp;
        minedNames.add(b.displayName);
        return new Yield(b, b.gold, b.xp);
    }

    public int remaining() {
        int n = 0;
        for (int l = 0; l < levels; l++) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (grid[l][r][c] != null) {
                        n++;
                    }
                }
            }
        }
        return n;
    }

    public int total() { return cols * rows * levels; }
    public int mined() { return mined; }
    public int goldBanked() { return goldBanked; }
    public int xpBanked() { return xpBanked; }
    public List<String> minedNames() { return minedNames; }

    /** Packed 0xRRGGBB twin of VoxelMinePanel.baseColor (no java.awt). */
    public static int colorRgb(Block b) {
        switch (b) {
            case stone:
            case deepslate:
            case gravel:
                return 0x8A8A92;
            case dirt:
                return 0x8A5A2E;
            case coalOre:
                return 0x2B2B30;
            case ironOre:
                return 0xA06A3A;
            case goldOre:
                return 0xE8B820;
            case lapisOre:
                return 0x2A4AD8;
            case redstoneOre:
                return 0xD82A2A;
            case emeraldOre:
                return 0x2AC85A;
            case rubyOre:
                return 0xE84A8A;
            case diamondOre:
                return 0x4AE8E8;
            case opalOre:
                return 0x9A6AE8;
            case crystalCube:
            case crystalSpike:
            case crystalOrb:
                return 0x8A4AD8;
            case frostOre:
                return 0xAADDF0;
            case glacierCrystal:
                return 0xC8F0FF;
            case snowstone:
                return 0xE8E8E8;
            case woodBeam:
            case planks:
                return 0x6A421A;
            case closetCrate:
                return 0xB07A30;
            case lava:
                return 0xF05A10;
            default:
                return 0x1A1A1E;
        }
    }
}
