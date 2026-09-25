package spooktacular.app;

import java.util.*;

/** Candy collection + combos — Java expression of the candy systems in ContentView.swift
 *  and MazeDailyHub / MineDailyHub streak logic. */
public final class CandyCollection {
    private final HalloweenUltimateManager mgr;
    private int combo = 0, comboBest = 0;

    public CandyCollection(HalloweenUltimateManager mgr) { this.mgr = mgr; }

    /** Collect n of a candy; consecutive collects within a session build combo bonus XP. */
    public int collect(Models.CandyItem candy, int n) {
        mgr.collectCandy(candy.id(), n);
        combo++;
        comboBest = Math.max(comboBest, combo);
        if (combo % 5 == 0) mgr.addXp(10); // combo bonus every 5
        return mgr.candyCount(candy.id());
    }

    public void breakCombo() { combo = 0; }
    public int combo() { return combo; }
    public int bestCombo() { return comboBest; }
}
