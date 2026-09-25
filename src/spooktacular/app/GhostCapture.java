package spooktacular.app;

import java.util.Random;

/** Interactive ghost-capture battle — Java expression of GhostCapture.swift.
 *  Tap / ZAP weakens (damage floaters, crits, regen pressure), then throw the
 *  net when the ghost is weak. Success -> manager.captureGhost; failure -> struggle. */
public final class GhostCapture {
    public static final class Battle {
        public final Models.Ghost ghost;
        public int hp;
        public final int maxHp;
        public int zaps = 0;
        public boolean over = false, captured = false;

        Battle(Models.Ghost ghost) { this.ghost = ghost; this.hp = ghost.maxHp(); this.maxHp = ghost.maxHp(); }
        public double weakness() { return 1.0 - (hp / (double) maxHp); } // 0 fresh .. 1 fully weak
    }

    private final Random rng;

    public GhostCapture(long seed) { this.rng = new Random(seed); }

    public Battle start(Models.Ghost ghost) { return new Battle(ghost); }

    /** Zap: base 8-14 + power scaling, 15% crit x2, ghost regens 0-4 per turn (pressure). */
    public int zap(Battle b) {
        if (b.over) return 0;
        b.zaps++;
        int dmg = 8 + rng.nextInt(7);
        boolean crit = rng.nextDouble() < 0.15;
        if (crit) dmg *= 2;
        b.hp = Math.max(1, b.hp - dmg - b.ghost.power() / 10);
        b.hp = Math.min(b.maxHp, b.hp + rng.nextInt(5)); // regen pressure
        return dmg;
    }

    /** Net throw: success chance = 0.25 + 0.65 * weakness (+calm bonus). Never 0/100%. */
    public boolean throwNet(Battle b, boolean hasCalmPotion) {
        if (b.over) return b.captured;
        double p = 0.25 + 0.65 * b.weakness() + (hasCalmPotion ? 0.1 : 0);
        p = Math.min(0.97, Math.max(0.05, p));
        boolean ok = rng.nextDouble() < p;
        if (ok) { b.over = true; b.captured = true; }
        else { b.hp = Math.min(b.maxHp, b.hp + b.maxHp / 4); } // bursts free, struggle continues
        return ok;
    }
}
