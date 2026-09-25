package spooktacular.swiftport;

import java.util.List;

/** Java expression of MazeBossRush.swift.
 *  Original: Sources/Ultimate/MazeBossRush.swift (6515 chars, 167 lines).
 *  Swift types: MazeRushBoss, MazeRushGuide, MazeRushBoard, MazeRushView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MazeBossRush {
    private MazeBossRush() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MazeBossRush.swift";
    public static final int SWIFT_LINES = 167;
    public static final List<String> SWIFT_TYPES = List.of("MazeRushBoss", "MazeRushGuide", "MazeRushBoard", "MazeRushView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
