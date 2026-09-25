package spooktacular.swiftport;

import java.util.List;

/** Java expression of MazeTrinkets.swift.
 *  Original: Sources/Ultimate/MazeTrinkets.swift (6167 chars, 136 lines).
 *  Swift types: MazeTrinket, MazeTrinketGuide, MazeTrinketBoxView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MazeTrinkets {
    private MazeTrinkets() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MazeTrinkets.swift";
    public static final int SWIFT_LINES = 136;
    public static final List<String> SWIFT_TYPES = List.of("MazeTrinket", "MazeTrinketGuide", "MazeTrinketBoxView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
