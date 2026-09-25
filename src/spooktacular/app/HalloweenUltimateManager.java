package spooktacular.app;

import java.util.*;

/** Central state — Java expression of HalloweenUltimateManager in ContentView.swift.
 *  Owns ghosts, candy, potions, XP/level, quests, achievements, streaks.
 *  UI-agnostic; Swing views observe via listener. Persisted via SpookyStore. */
public final class HalloweenUltimateManager {
    public interface Listener { void onChange(String what); }

    private final List<Listener> listeners = new ArrayList<>();
    private final SpookyStore store;

    private final List<Models.Ghost> captured = new ArrayList<>();
    private final Map<String, Integer> candy = new HashMap<>();
    private final List<Models.PotionItem> potions = new ArrayList<>();
    private int xp = 0, level = 1, bestScore = 0, streakDays = 0;
    private String lastPlayDay = "";

    public HalloweenUltimateManager(SpookyStore store) {
        this.store = store;
        this.xp = store.getInt("xp", 0);
        this.level = Math.max(1, store.getInt("level", 1));
        this.bestScore = store.getInt("bestScore", 0);
        this.streakDays = store.getInt("streakDays", 0);
        this.lastPlayDay = store.getString("lastPlayDay", "");
        touchStreak();
    }

    public void addListener(Listener l) { listeners.add(l); }
    private void fire(String what) { for (Listener l : listeners) l.onChange(what); persist(); }

    private void persist() {
        store.setInt("xp", xp); store.setInt("level", level);
        store.setInt("bestScore", bestScore); store.setInt("streakDays", streakDays);
        store.setString("lastPlayDay", lastPlayDay); store.save();
    }

    private void touchStreak() {
        String today = SpookyStore.todayString();
        if (!today.equals(lastPlayDay)) {
            if (!lastPlayDay.isEmpty()) {
                try {
                    var last = java.time.LocalDate.parse(lastPlayDay);
                    var now = java.time.LocalDate.parse(today);
                    streakDays = last.plusDays(1).equals(now) ? streakDays + 1 : 1;
                } catch (Exception e) { streakDays = 1; }
            } else streakDays = 1;
            lastPlayDay = today;
        }
    }

    // ---- Ghosts (mirrors captureGhost in Swift) ----
    public void captureGhost(Models.Ghost g) { captured.add(g); addXp(25 + g.power()); fire("ghosts"); }
    public List<Models.Ghost> capturedGhosts() { return Collections.unmodifiableList(captured); }
    public int ghostCount() { return captured.size(); }

    // ---- Candy ----
    public void collectCandy(String id, int n) { candy.merge(id, n, Integer::sum); addXp(n * 2); fire("candy"); }
    public int candyCount(String id) { return candy.getOrDefault(id, 0); }
    public int totalCandy() { return candy.values().stream().mapToInt(Integer::intValue).sum(); }

    // ---- Potions ----
    public void addPotion(Models.PotionItem p) { potions.add(p); addXp(15); fire("potions"); }
    public List<Models.PotionItem> potions() { return Collections.unmodifiableList(potions); }

    // ---- XP / level ----
    public void addXp(int n) {
        xp += n;
        int need = level * 100;
        if (xp >= need) { xp -= need; level++; }
        bestScore = Math.max(bestScore, xp);
        fire("xp");
    }
    public int getXp() { return xp; }
    public int getLevel() { return level; }
    public int getBestScore() { return bestScore; }
    public int getStreakDays() { return streakDays; }

    public String statusLine() {
        return "Lv " + level + " · " + xp + " XP · " + ghostCount() + " ghosts · "
            + totalCandy() + " candy · " + potions.size() + " potions · " + streakDays + "-day streak";
    }
}
