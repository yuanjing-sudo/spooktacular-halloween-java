package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineQuests.swift.
 *  Original: Sources/Ultimate/MineQuests.swift (39711 chars, 1031 lines).
 *  Swift types: MineQuestEvent, MineQuestTrigger, MineQuest, MineQuestCatalog, MineQuestBoard, MineQuestView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineQuests {
    private MineQuests() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineQuests.swift";
    public static final int SWIFT_LINES = 1031;
    public static final List<String> SWIFT_TYPES = List.of("MineQuestEvent", "MineQuestTrigger", "MineQuest", "MineQuestCatalog", "MineQuestBoard", "MineQuestView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
