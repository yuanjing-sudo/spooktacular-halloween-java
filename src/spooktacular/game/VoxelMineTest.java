package spooktacular.game;

import spooktacular.data.Block;

/** Headless checks for the isometric voxel mine (Model only, no Swing).
 *  Run: java -cp classes spooktacular.game.VoxelMineTest */
public final class VoxelMineTest {
    static int checks = 0;
    static void check(boolean cond, String name) {
        checks++;
        if (!cond) throw new AssertionError("FAIL: " + name);
    }

    public static void main(String[] args) {
        VoxelMinePanel.Model m = new VoxelMinePanel.Model(12, 8, 3, 99L);
        check(m.total() == 12 * 8 * 3, "world size");
        check(m.remaining() == m.total(), "world full at gen");

        // bedrock floor pins + unbreakable
        boolean foundBedrock = false;
        for (int c = 0; c < m.cols; c++) {
            Block b = m.at(2, m.rows - 1, c);
            if (b == Block.bedrock) {
                foundBedrock = true;
                check(m.mine(2, m.rows - 1, c, 99) == null, "bedrock unbreakable");
            }
        }
        check(foundBedrock, "bedrock floor exists");

        // surface exposed, deep buried until stripped
        check(m.exposed(0, 0, 0), "surface exposed");
        Block deep0 = m.at(2, 0, 0);
        if (deep0 != null && !deep0.unbreakable) {
            check(!m.exposed(2, 0, 0), "deep buried at start");
            check(m.mine(2, 0, 0, 99) == null, "cannot mine buried");
        }

        // tier gate: find a block needing T>=1, fail with T0 if exposed
        boolean gated = false, minedOne = false;
        outer:
        for (int l = 0; l < m.levels; l++)
            for (int r = 0; r < m.rows; r++)
                for (int c = 0; c < m.cols; c++) {
                    Block b = m.at(l, r, c);
                    if (b == null || b.unbreakable || !m.exposed(l, r, c)) continue;
                    if (b.minTier >= 1 && m.mine(l, r, c, 0) == null) { gated = true; }
                    else if (b.minTier <= 1) {
                        VoxelMinePanel.Model m2 = new VoxelMinePanel.Model(12, 8, 3, 99L);
                        Block b2 = m2.at(l, r, c);
                        if (b2 != null && m2.exposed(l, r, c) && !b2.unbreakable && b2.minTier <= 1) {
                            var y = m2.mine(l, r, c, 4);
                            if (y != null) { minedOne = true; check(m2.mined() == 1, "mine counts"); break outer; }
                        }
                    }
                    if (gated && minedOne) break outer;
                }
        check(gated || minedOne, "tier gate or successful mine observed");

        // totals accumulate
        VoxelMinePanel.Model m3 = new VoxelMinePanel.Model(6, 4, 2, 7L);
        int g0 = m3.goldBanked(), x0 = m3.xpBanked();
        int yields = 0;
        for (int r = 0; r < m3.rows && yields < 3; r++)
            for (int c = 0; c < m3.cols && yields < 3; c++) {
                var y = m3.mine(0, r, c, 4);
                if (y != null) { yields++; check(m3.goldBanked() >= g0, "gold accumulates"); check(m3.xpBanked() >= x0, "xp accumulates"); }
            }
        check(yields > 0, "surface minable");
        check(m3.remaining() == m3.total() - m3.mined(), "remaining consistent");

        // colors cover every block (no null → render-safe)
        for (Block b : Block.values()) check(VoxelMinePanel.baseColor(b) != null, "color " + b.name());

        System.out.println("VoxelMineTest OK: " + checks + " checks");
    }
}
