package spooktacular.app;

import java.util.*;

/** Mini-games — Java expression of ArcadeGames.swift + ArcadeGames2.swift.
 *  Memory Match + Pumpkin Smash, both playable headless or via Swing. */
public final class MiniGames {

    /** Memory Match: pairs board, flip logic, move counter. */
    public static final class MemoryMatch {
        public final int pairs;
        public final int[] board; // shuffled pair ids
        public final boolean[] matched, faceUp;
        public int moves = 0, found = 0;
        private int first = -1;

        public MemoryMatch(int pairs, long seed) {
            this.pairs = pairs;
            List<Integer> cards = new ArrayList<>();
            for (int i = 0; i < pairs; i++) { cards.add(i); cards.add(i); }
            Collections.shuffle(cards, new Random(seed));
            board = cards.stream().mapToInt(Integer::intValue).toArray();
            matched = new boolean[board.length];
            faceUp = new boolean[board.length];
        }

        /** Flip card i. Returns: -1 invalid, 0 first of pair, 1 match, 2 mismatch. */
        public int flip(int i) {
            if (i < 0 || i >= board.length || matched[i] || faceUp[i]) return -1;
            faceUp[i] = true;
            if (first < 0) { first = i; return 0; }
            moves++;
            int j = first; first = -1;
            if (board[i] == board[j]) { matched[i] = matched[j] = true; found++; return 1; }
            faceUp[i] = false; faceUp[j] = false;
            return 2;
        }

        public boolean won() { return found == pairs; }
        public int score() { return Math.max(0, pairs * 100 - moves * 5); }
    }

    /** Pumpkin Smash: 3x3 grid, smash pumpkins before they vanish. Tick-based. */
    public static final class PumpkinSmash {
        public final boolean[] lit = new boolean[9];
        public int score = 0, ticks = 0;
        private final Random rng;
        public PumpkinSmash(long seed) { this.rng = new Random(seed); }
        public void tick() {
            ticks++;
            Arrays.fill(lit, false);
            int n = 1 + rng.nextInt(3);
            for (int k = 0; k < n; k++) lit[rng.nextInt(9)] = true;
        }
        /** Smash cell i: +10 if lit, -2 otherwise. */
        public int smash(int i) {
            if (i < 0 || i >= 9) return 0;
            if (lit[i]) { lit[i] = false; score += 10; return 10; }
            score = Math.max(0, score - 2); return -2;
        }
    }
}
