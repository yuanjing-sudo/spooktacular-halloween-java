package spooktacular.web;

import java.util.ArrayList;

/** DFS maze carver — algorithm twin of Engine.carveDFS
 *  (src/spooktacular/engine/Engine.java). Plain classes, Java-11 syntax. */
public final class WasmMaze {
    public final int w;
    public final int d;
    private final boolean[][] open;

    private WasmMaze(int w, int d, boolean[][] open) {
        this.w = w;
        this.d = d;
        this.open = open;
    }

    public boolean isOpen(int x, int z) {
        if (x < 0 || z < 0 || x >= w || z >= d) {
            return false;
        }
        return open[x][z];
    }

    public static WasmMaze carveDFS(int w, int d, long seed) {
        WasmRng rng = new WasmRng(seed);
        w = Math.max(3, w | 1);
        d = Math.max(3, d | 1);
        boolean[][] grid = new boolean[w][d];
        grid[1][1] = true;
        ArrayList<int[]> stack = new ArrayList<int[]>();
        stack.add(new int[] { 1, 1 });
        int[][] dirs = { { 2, 0 }, { -2, 0 }, { 0, 2 }, { 0, -2 } };
        int steps = 0;
        while (!stack.isEmpty() && steps < w * d * 4) {
            steps++;
            int[] cur = stack.get(stack.size() - 1);
            ArrayList<int[]> opts = new ArrayList<int[]>();
            for (int i = 0; i < dirs.length; i++) {
                int nx = cur[0] + dirs[i][0];
                int nz = cur[1] + dirs[i][1];
                if (nx > 0 && nx < w - 1 && nz > 0 && nz < d - 1 && !grid[nx][nz]) {
                    opts.add(new int[] { dirs[i][0], dirs[i][1], nx, nz });
                }
            }
            if (!opts.isEmpty()) {
                int[] c = opts.get(rng.nextInt(opts.size()));
                grid[cur[0] + c[0] / 2][cur[1] + c[1] / 2] = true;
                grid[c[2]][c[3]] = true;
                stack.add(new int[] { c[2], c[3] });
            } else {
                stack.remove(stack.size() - 1);
            }
        }
        return new WasmMaze(w, d, grid);
    }

    /** Manhattan-farthest open cell from (x,z): ghost spawn twin of farthest-spawn. */
    public int[] farthestOpen(int x, int z) {
        int best = -1;
        int bx = 1;
        int bz = 1;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < d; j++) {
                if (open[i][j]) {
                    int dist = Math.abs(i - x) + Math.abs(j - z);
                    if (dist > best) {
                        best = dist;
                        bx = i;
                        bz = j;
                    }
                }
            }
        }
        return new int[] { bx, bz };
    }
}
