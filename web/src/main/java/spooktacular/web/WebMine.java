package spooktacular.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spooktacular.data.Block;
import spooktacular.data.Data;
import spooktacular.data.Entities;
import spooktacular.engine.Engine;
import spooktacular.quests.Achievement;
import spooktacular.quests.AchievementBoard;
import spooktacular.quests.AchievementCatalog;
import spooktacular.quests.QEvent;
import spooktacular.quests.Quest;
import spooktacular.quests.QuestBoard;
import spooktacular.quests.Snapshot;
import spooktacular.systems.Fishing;

/** Mining-tycoon logic twin of game/MineSim.java (Swing-free).
 *  Same bands, toughness/tiers, coal-for-picks, pack upgrades, pets, rebirth,
 *  fishing, QEvent feed, Snapshot for the real AchievementBoard. */
public final class WebMine {
    public static final int COLS = 14;
    public static final int ROWS = 9;

    public static final class Cell {
        public final Block block;
        public final double hp;
        public Cell(Block block, double hp) {
            this.block = block;
            this.hp = hp;
        }
    }

    private static final Block[] BAND0 = { Block.stone, Block.stone, Block.dirt,
            Block.coalOre, Block.ironOre };
    private static final Block[] BAND1 = { Block.stone, Block.deepslate, Block.coalOre,
            Block.ironOre, Block.goldOre, Block.lapisOre, Block.redstoneOre, Block.crystalCube };
    private static final Block[] BAND2 = { Block.deepslate, Block.goldOre, Block.emeraldOre,
            Block.rubyOre, Block.diamondOre, Block.opalOre, Block.crystalSpike,
            Block.crystalOrb, Block.frostOre, Block.glacierCrystal, Block.closetCrate };

    private final Cell[][] grid = new Cell[ROWS][COLS];
    private final Engine.RNG rng;
    private final QuestBoard quests;
    private final AchievementBoard achievements;
    private final Entities.MNPlayer player = new Entities.MNPlayer();
    private long coal = 0;
    private int sellValue = 0;
    private int bestSale = 0;
    private int hatched = 0;
    private final List<Entities.CrystalCave> caves = new ArrayList<Entities.CrystalCave>();
    private final List<Entities.ClosetCache> closets = new ArrayList<Entities.ClosetCache>();
    private final List<Entities.FrostPocket> frost = new ArrayList<Entities.FrostPocket>();
    private final List<Entities.BoxyCritter> critters = new ArrayList<Entities.BoxyCritter>();
    private final Map<String, Integer> fishCaught = new HashMap<String, Integer>();
    public String message = "Swing at glowing ore!";

    public WebMine(QuestBoard quests, AchievementBoard achievements, long seed) {
        this.quests = quests;
        this.achievements = achievements;
        this.rng = new Engine.RNG(seed);
        quests.onReward = new java.util.function.BiConsumer<Integer, Integer>() {
            @Override
            public void accept(Integer g, Integer x) {
                player.gold += g.intValue();
                earnXP(x.intValue());
            }
        };
        achievements.onReward = new java.util.function.BiConsumer<Integer, Integer>() {
            @Override
            public void accept(Integer g, Integer x) {
                player.gold += g.intValue();
                earnXP(x.intValue());
            }
        };
        newVein();
    }

    public void newVein() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Block b;
                if (r == 0 || r == ROWS - 1 || c == 0 || c == COLS - 1) {
                    b = Block.bedrock;
                } else {
                    Block[] band = r < 3 ? BAND0 : r < 6 ? BAND1 : BAND2;
                    b = band[rng.nextInt(band.length)];
                    if (rng.nextDouble() < 0.03) {
                        b = Block.closetCrate;
                    }
                }
                grid[r][c] = new Cell(b, b.toughness);
            }
        }
        if (rng.nextDouble() < 0.4) {
            String[][] sp = { { "Mole", "M" }, { "Bat", "B" }, { "Axolotl", "A" },
                    { "Fox", "F" }, { "Wisp", "W" } };
            String[] s = sp[rng.nextInt(sp.length)];
            critters.add(new Entities.BoxyCritter(s[0], s[1], true));
            quests.record(new QEvent.CritterGreeted());
            message = "A boxy " + s[0] + " visits and says hi!";
        }
    }

    public double pickDamage() {
        double d = Data.PICKS[player.pickTier].speed();
        for (int i = 0; i < player.pets.size(); i++) {
            Entities.Pet p = player.pets.get(i);
            if (p.equipped() && p.boostKind().equals("Speed")) {
                d *= 1 + p.boostValue();
            }
        }
        return d;
    }

    public double goldMult() {
        double m = 1 + 0.15 * player.rebirths;
        for (int i = 0; i < player.pets.size(); i++) {
            Entities.Pet p = player.pets.get(i);
            if (p.equipped() && p.boostKind().equals("Gold")) {
                m *= 1 + p.boostValue();
            }
        }
        return m;
    }

    public void swing(int r, int c) {
        if (r < 0 || c < 0 || r >= ROWS || c >= COLS) {
            return;
        }
        Cell cell = grid[r][c];
        if (cell == null) {
            return;
        }
        Block b = cell.block;
        if (b.unbreakable) {
            message = b.displayName + " is unbreakable.";
            return;
        }
        if (player.pickTier < b.minTier) {
            message = "Too tough - needs " + Data.PICKS[b.minTier].name() + ".";
            return;
        }
        double hp = cell.hp - pickDamage();
        if (hp > 0) {
            grid[r][c] = new Cell(b, hp);
            return;
        }
        breakBlock(r, c, b);
    }

    private void breakBlock(int r, int c, Block b) {
        boolean valuable = b.gold > 0 || b == Block.coalOre;
        if (valuable && player.backpackFull()) {
            message = "Backpack full - sell first!";
            return;
        }
        player.blocksMined++;
        quests.record(new QEvent.BlockBroken());
        if (b == Block.coalOre) {
            coal++;
        } else if (b.gold > 0) {
            addOre(1);
            sellValue += b.gold;
            for (int i = 0; i < player.pets.size(); i++) {
                Entities.Pet p = player.pets.get(i);
                if (p.equipped() && p.boostKind().equals("Luck")
                        && rng.nextDouble() < p.boostValue()) {
                    addOre(1);
                    sellValue += b.gold;
                    message = "Lucky double-drop!";
                }
            }
        }
        if (b.name().startsWith("crystal") || b == Block.glacierCrystal) {
            caves.add(new Entities.CrystalCave(true, false));
            quests.record(new QEvent.CaveHarvested());
            quests.record(new QEvent.CaveUnlocked());
        }
        if (b == Block.frostOre) {
            frost.add(new Entities.FrostPocket(true));
        }
        if (b == Block.closetCrate) {
            closets.add(new Entities.ClosetCache(true));
            quests.record(new QEvent.ClosetOpened());
            player.gold += 5;
        }
        if (b.ore) {
            quests.record(new QEvent.OreMined(b.displayName, 1));
        }
        earnXP(b.xp);
        grid[r][c] = new Cell(Block.stone, Block.stone.toughness);
        if (message == null || message.isEmpty()) {
            message = "Broke " + b.displayName + ".";
        }
    }

    private void addOre(int n) {
        Integer v = player.ores.get("ore");
        player.ores.put("ore", (v == null ? 0 : v.intValue()) + n);
    }

    public void earnXP(int n) {
        if (n <= 0) {
            return;
        }
        Engine.XP r = Engine.applyXP(player.level, player.experience, n);
        if (r.leveled()) {
            message = "Level " + r.level() + "! +25 gold";
            player.gold += 25;
        }
        player.level = r.level();
        player.experience = r.xp();
        quests.record(new QEvent.XpEarned(n));
    }

    public void sell() {
        if (sellValue <= 0) {
            message = "Backpack empty - break some ore first.";
            return;
        }
        int g = (int) Math.round(sellValue * goldMult());
        player.gold += g;
        if (g > bestSale) {
            bestSale = g;
        }
        quests.record(new QEvent.GoldSold(g));
        player.ores.clear();
        sellValue = 0;
        message = "Sold for " + g + " gold. Coal stays banked: " + coal;
    }

    public void buyPick() {
        if (player.pickTier + 1 >= Data.PICKS.length) {
            message = "Void Drill is max!";
            return;
        }
        int cost = Data.PICKS[player.pickTier + 1].cost();
        if (coal < cost) {
            message = "Needs " + cost + " coal (have " + coal + ").";
            return;
        }
        coal -= cost;
        player.pickTier++;
        quests.record(new QEvent.PickForged());
        message = "Forged " + Data.PICKS[player.pickTier].name() + "!";
    }

    public void upgradePack() {
        int cost = 50 * (1 << player.backpackTier);
        if (player.gold < cost) {
            message = "Pack upgrade needs " + cost + " gold.";
            return;
        }
        player.gold -= cost;
        player.backpackTier++;
        player.backpackCapacity += 25;
        quests.record(new QEvent.PackUpgraded());
        message = "Backpack Mk " + (player.backpackTier + 1) + " (" + player.backpackCapacity + " cap).";
    }

    public void hatchEgg() {
        if (player.gold < 150) {
            message = "Eggs cost 150 gold.";
            return;
        }
        player.gold -= 150;
        hatched++;
        Entities.Pet pet = Entities.hatch(hatched, 1 + rng.nextInt(100), hatched);
        int equipped = 0;
        for (int i = 0; i < player.pets.size(); i++) {
            if (player.pets.get(i).equipped()) {
                equipped++;
            }
        }
        player.pets.add(new Entities.Pet(pet.species(), pet.emoji(), pet.rarity(),
                pet.boostKind(), pet.boostValue(), equipped < 3));
        quests.record(new QEvent.PetHatched());
        message = "Hatched " + pet.rarity() + " " + pet.species() + "!";
    }

    public void rebirth() {
        if (player.level < 15) {
            message = "Rebirth needs level 15 (now " + player.level + ").";
            return;
        }
        player.rebirths++;
        player.level = 1;
        player.experience = 0;
        player.gold = 0;
        player.ores.clear();
        sellValue = 0;
        quests.record(new QEvent.Rebirthed());
        message = "Rebirth #" + player.rebirths + " - gold gains boosted forever.";
    }

    public void fish(String water) {
        Data.Fish f = Fishing.cast(water, 0, new Engine.RNG(rng.next()));
        long gv = Math.round(f.value() * goldMult());
        player.gold += (int) gv;
        Integer v = fishCaught.get(f.name());
        fishCaught.put(f.name(), (v == null ? 0 : v.intValue()) + 1);
        message = "Caught " + f.name() + " (+" + gv + "g).";
    }

    public int claimAll() {
        int n = 0;
        List<Quest> claimable = quests.claimable();
        for (int i = 0; i < claimable.size(); i++) {
            if (quests.claim(claimable.get(i))) {
                n++;
            }
        }
        List<Achievement> all = AchievementCatalog.all();
        for (int i = 0; i < all.size(); i++) {
            Achievement a = all.get(i);
            if (!achievements.isClaimed(a.id()) && achievements.isUnlocked(a.id())
                    && achievements.claim(a)) {
                n++;
            }
        }
        message = n == 0 ? "Nothing claimable yet." : "Claimed " + n + " reward(s)!";
        return n;
    }

    public Snapshot snapshot() {
        return new Snapshot(player, new ArrayList<Data.Relic>(),
                new HashMap<String, Integer>(fishCaught), 0, 0,
                new ArrayList<Entities.BoxyCritter>(critters),
                new ArrayList<Entities.CrystalCave>(caves),
                new ArrayList<Entities.ClosetCache>(closets),
                new ArrayList<Entities.FrostPocket>(frost),
                quests, 0, bestSale, 0);
    }

    public Block blockAt(int r, int c) { return grid[r][c].block; }
    public double hpAt(int r, int c) { return grid[r][c].hp; }
    public Entities.MNPlayer player() { return player; }
    public long coal() { return coal; }
    public int sellValue() { return sellValue; }
    public int bestSale() { return bestSale; }
    public void grantCoal(long n) { coal += n; }
    public void grantGold(int n) { player.gold += n; }
}
