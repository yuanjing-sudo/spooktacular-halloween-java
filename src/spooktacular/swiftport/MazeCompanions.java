package spooktacular.swiftport;

import java.util.List;

/** Java expression of MazeCompanions.swift.
 *  Original: Sources/Ultimate/MazeCompanions.swift (5644 chars, 136 lines).
 *  Swift types: MazeCompanionView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MazeCompanions {
    private MazeCompanions() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MazeCompanions.swift";
    public static final int SWIFT_LINES = 136;
    public static final List<String> SWIFT_TYPES = List.of("MazeCompanionView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
