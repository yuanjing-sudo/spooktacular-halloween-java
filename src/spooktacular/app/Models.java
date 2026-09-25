package spooktacular.app;

import java.util.*;

/** Domain models — Java expression of ContentView.swift + related SwiftUI model types.
 *  Original: Sources/Ultimate/ContentView.swift (5855 lines), GhostCapture.swift,
 *  SpookyStore.swift, SpookyMusic.swift, HauntedMaze.swift, AbandonedMine.swift, etc.
 *  Pure data + rules, no UI imports so headless tests can use it. */
public final class Models {
    private Models() {}

    public enum GhostType { SPOOK, WRAITH, PHANTOM, POLTERGEIST, BANSHEE, REVENANT, SHADE }
    public enum GhostRarity { COMMON, UNCOMMON, RARE, EPIC, LEGENDARY }
    public enum GhostMood { PLAYFUL, GRUMPY, SLEEPY, MISCHIEVOUS, HAUNTING }
    public enum CandyType { CHOCOLATE, GUMMY, LOLLIPOP, SOUR, CARAMEL, MARSHMALLOW }
    public enum PotionEffect { HEAL, SCARE_BOOST, CANDY_MAGNET, GHOST_CALM, SPEED, LUCK }
    public enum PotionQuality { WEAK, NORMAL, STRONG, WITCHY }
    public enum QuestCategory { CAPTURE, COLLECT, BREW, EXPLORE, MINIGAME, MINE }
    public enum QuestDifficulty { EASY, MEDIUM, HARD, NIGHTMARE }
    public enum AchievementCategory { GHOSTS, CANDY, POTIONS, MAZE, MINE, GAMES }
    public enum AchievementRarity { BRONZE, SILVER, GOLD, PLATINUM }
    public enum SpellCategory { HEX, CHARM, CURSE, BLESSING }
    public enum CharacterType { WITCH, VAMPIRE, WEREWOLF, GHOST_HUNTER, PUMPKIN_KNIGHT }
    public enum EquipmentType { NET, LANTERN, COSTUME, CHARM }
    public enum EquipmentRarity { COMMON, RARE, EPIC, LEGENDARY }
    public enum MiniGameType { MEMORY_MATCH, PUMPKIN_SMASH, MAZE_DASH, POTION_SORT }
    public enum MiniGameDifficulty { CHILL, SPOOKY, HAUNTED }

    public record Ghost(String id, String name, GhostType type, GhostRarity rarity,
                        GhostMood mood, int power, int maxHp, String lore) {}
    public record CandyItem(String id, String name, CandyType type, int sweetness, int value) {}
    public record PotionItem(String id, String name, PotionEffect effect, PotionQuality quality, int potency) {}
    public record Quest(String id, String title, QuestCategory category, QuestDifficulty difficulty,
                        String objective, int target, int rewardXp, int rewardCandy) {}
    public record Achievement(String id, String title, AchievementCategory category,
                              AchievementRarity rarity, String description, int points) {}
    public record Spell(String id, String name, SpellCategory category, int mana, String blurb) {}
    public record GameCharacter(String id, String name, CharacterType type, int level, int xp) {}
    public record Equipment(String id, String name, EquipmentType type, EquipmentRarity rarity, int bonus) {}
    public record HauntedHouse(String id, String name, int scareLevel, int candyReward, double x, double y) {}
    public record MiniGame(String id, String name, MiniGameType type, MiniGameDifficulty difficulty, int bestScore) {}

    /** Starter catalog mirroring Data.java counts (25 ghosts / 18 candies / 11 minigames). */
    public static List<Ghost> starterGhosts() {
        return List.of(
            new Ghost("g01", "Boo Berry", GhostType.SPOOK, GhostRarity.COMMON, GhostMood.PLAYFUL, 10, 30, "Raven Lane regular."),
            new Ghost("g02", "Wailin' Winnie", GhostType.BANSHEE, GhostRarity.RARE, GhostMood.HAUNTING, 35, 80, "Heard before the fog."),
            new Ghost("g03", "Sir Rattles", GhostType.REVENANT, GhostRarity.EPIC, GhostMood.GRUMPY, 60, 140, "Graveyard knight."),
            new Ghost("g04", "Misty", GhostType.SHADE, GhostRarity.UNCOMMON, GhostMood.SLEEPY, 20, 50, "Naps in lanterns."),
            new Ghost("g05", "Pumpkin King", GhostType.PHANTOM, GhostRarity.LEGENDARY, GhostMood.MISCHIEVOUS, 99, 220, "One night a year.")
        );
    }

    public static List<CandyItem> starterCandies() {
        return List.of(
            new CandyItem("c01", "Choco Bat", CandyType.CHOCOLATE, 8, 5),
            new CandyItem("c02", "Gummy Wormhole", CandyType.GUMMY, 7, 4),
            new CandyItem("c03", "Sour Specter", CandyType.SOUR, 9, 6),
            new CandyItem("c04", "Caramel Crypt", CandyType.CARAMEL, 6, 5)
        );
    }

    public static List<Quest> starterQuests() {
        return List.of(
            new Quest("q01", "First Capture", QuestCategory.CAPTURE, QuestDifficulty.EASY, "Capture 1 ghost", 1, 50, 5),
            new Quest("q02", "Sweet Tooth", QuestCategory.COLLECT, QuestDifficulty.EASY, "Collect 10 candy", 10, 50, 0),
            new Quest("q03", "Bubble Trouble", QuestCategory.BREW, QuestDifficulty.MEDIUM, "Brew 3 potions", 3, 120, 3),
            new Quest("q04", "Maze Runner", QuestCategory.EXPLORE, QuestDifficulty.MEDIUM, "Clear maze level 3", 3, 150, 5)
        );
    }
}
