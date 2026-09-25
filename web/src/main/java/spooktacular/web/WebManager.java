package spooktacular.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import spooktacular.app.Models;
import spooktacular.engine.Engine;
import spooktacular.quests.QEvent;
import spooktacular.quests.QuestBoard;

/** Cross-tab state twin of HalloweenUltimateManager (app/, desktop Swing).
 *  Real Engine.applyXP for levels; candy combos per CandyCollection rules
 *  (+2 XP each, +10 every 5th); potions per PotionBrewing (+15 XP).
 *  Persists via WebSave.extra (boards persist via GameStore/localStorage). */
public final class WebManager {
    public int xp = 0;
    public int level = 1;
    public int bestScore = 0;
    public int captured = 0;
    public boolean lastLevelUp = false;
    public final Map<String, Integer> candy = new HashMap<String, Integer>();
    public final ArrayList<Models.PotionItem> potions = new ArrayList<Models.PotionItem>();
    private final QuestBoard quests;

    public WebManager(QuestBoard quests) {
        this.quests = quests;
    }

    public void addXp(int n) {
        if (n <= 0) {
            return;
        }
        Engine.XP r = Engine.applyXP(level, xp, n);
        lastLevelUp = r.leveled();
        level = r.level();
        xp = r.xp();
        if (xp > bestScore) {
            bestScore = xp;
        }
        quests.record(new QEvent.XpEarned(n));
    }

    public void collectCandy(String id, int n) {
        Integer cur = candy.get(id);
        candy.put(id, (cur == null ? 0 : cur.intValue()) + n);
        addXp(n * 2);
    }

    public int candyCount(String id) {
        Integer v = candy.get(id);
        return v == null ? 0 : v.intValue();
    }

    public int totalCandy() {
        int t = 0;
        for (Integer v : candy.values()) {
            t += v.intValue();
        }
        return t;
    }

    public void addPotion(Models.PotionItem p) {
        potions.add(p);
        addXp(15);
    }

    public void capture(int power) {
        captured++;
        addXp(25 + power);
    }

    public String statusLine() {
        return "Lv " + level + " " + xp + "XP best " + bestScore
                + " | ghosts " + captured + " | candy " + totalCandy()
                + " | potions " + potions.size();
    }

    public void save() {
        WebSave.extra.put("xp", Integer.toString(xp));
        WebSave.extra.put("level", Integer.toString(level));
        WebSave.extra.put("best", Integer.toString(bestScore));
        WebSave.extra.put("captured", Integer.toString(captured));
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Integer> e : candy.entrySet()) {
            if (!first) {
                sb.append(';');
            }
            first = false;
            sb.append(e.getKey()).append('=').append(e.getValue());
        }
        WebSave.extra.put("candy", sb.toString());
        StringBuilder pb = new StringBuilder();
        first = true;
        for (int i = 0; i < potions.size(); i++) {
            Models.PotionItem p = potions.get(i);
            if (!first) {
                pb.append(';');
            }
            first = false;
            pb.append(p.name()).append('|').append(p.effect().name())
                    .append('|').append(p.quality().name()).append('|').append(p.potency());
        }
        WebSave.extra.put("potions", pb.toString());
    }

    public void load() {
        xp = num("xp");
        level = Math.max(1, num("level"));
        if (level <= 0) {
            level = 1;
        }
        bestScore = num("best");
        captured = num("captured");
        candy.clear();
        String c = WebSave.extra.get("candy");
        if (c != null) {
            int start = 0;
            for (int i = 0; i <= c.length(); i++) {
                if (i == c.length() || c.charAt(i) == ';') {
                    String pair = c.substring(start, i);
                    int eq = pair.indexOf('=');
                    if (eq > 0) {
                        try {
                            candy.put(pair.substring(0, eq), Integer.parseInt(pair.substring(eq + 1)));
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    start = i + 1;
                }
            }
        }
        potions.clear();
        String ps = WebSave.extra.get("potions");
        if (ps != null && !ps.isEmpty()) {
            int start = 0;
            for (int i = 0; i <= ps.length(); i++) {
                if (i == ps.length() || ps.charAt(i) == ';') {
                    String rec = ps.substring(start, i);
                    ArrayList<String> f = new ArrayList<String>();
                    int fs = 0;
                    for (int j = 0; j <= rec.length(); j++) {
                        if (j == rec.length() || rec.charAt(j) == '|') {
                            f.add(rec.substring(fs, j));
                            fs = j + 1;
                        }
                    }
                    if (f.size() == 4) {
                        try {
                            Models.PotionEffect ef = Models.PotionEffect.valueOf(f.get(1));
                            Models.PotionQuality q = Models.PotionQuality.valueOf(f.get(2));
                            potions.add(new Models.PotionItem("p" + potions.size(), f.get(0), ef, q,
                                    Integer.parseInt(f.get(3))));
                        } catch (Exception ignored) {
                        }
                    }
                    start = i + 1;
                }
            }
        }
    }

    private static int num(String k) {
        String v = WebSave.extra.get(k);
        if (v == null) {
            return 0;
        }
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
