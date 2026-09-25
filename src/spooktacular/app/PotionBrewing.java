package spooktacular.app;

import java.util.*;

/** Potion brewing — Java expression of the potion systems in ContentView.swift.
 *  Recipes: 2 candies -> potion. Quality scales with sweetness sum. */
public final class PotionBrewing {
    private final HalloweenUltimateManager mgr;
    private final Random rng;

    public PotionBrewing(HalloweenUltimateManager mgr, long seed) {
        this.mgr = mgr; this.rng = new Random(seed);
    }

    public Models.PotionItem brew(List<Models.CandyItem> ingredients, Models.PotionEffect effect) {
        if (ingredients.size() < 2) throw new IllegalArgumentException("need >= 2 candies");
        int sweet = ingredients.stream().mapToInt(Models.CandyItem::sweetness).sum();
        Models.PotionQuality q = sweet >= 20 ? Models.PotionQuality.WITCHY
            : sweet >= 15 ? Models.PotionQuality.STRONG
            : sweet >= 10 ? Models.PotionQuality.NORMAL : Models.PotionQuality.WEAK;
        int potency = sweet / 2 + rng.nextInt(4);
        String id = "p" + System.nanoTime() % 100000;
        Models.PotionItem p = new Models.PotionItem(id, effect.name().charAt(0) + effect.name().substring(1).toLowerCase() + " Draught",
            effect, q, potency);
        mgr.addPotion(p);
        return p;
    }
}
