package spooktacular.swiftport;

import java.util.List;

/** Java expression of ContentView.swift.
 *  Original: Sources/Ultimate/ContentView.swift (232793 chars, 5856 lines).
 *  Swift types: GhostType, GhostRarity, CandyType, PotionEffect, Ghost, GhostMood, Quest, QuestCategory, QuestObjective, QuestReward, QuestDifficulty, Achievement
 *  Port: spooktacular.app.Models + HalloweenUltimateManager + SpookyApp (manager, ghosts/candy/potions/quests/achievements, 7-tab Swing shell). */
public final class ContentView {
    private ContentView() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/ContentView.swift";
    public static final int SWIFT_LINES = 5856;
    public static final List<String> SWIFT_TYPES = List.of("GhostType", "GhostRarity", "CandyType", "PotionEffect", "Ghost", "GhostMood", "Quest", "QuestCategory", "QuestObjective", "QuestReward", "QuestDifficulty", "Achievement", "AchievementCategory", "AchievementRarity", "Spell", "SpellCategory", "Character", "CharacterType", "Equipment", "EquipmentType", "EquipmentRarity", "HauntedHouse", "MiniGame", "MiniGameType", "MiniGameDifficulty");
    public static final String PORT = "spooktacular.app.Models + HalloweenUltimateManager + SpookyApp (manager, ghosts/candy/potions/quests/achievements, 7-tab Swing shell)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
