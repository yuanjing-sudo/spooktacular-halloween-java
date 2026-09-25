package spooktacular.swiftport;

import java.util.List;

/** Java expression of MazeMotion.swift.
 *  Original: Sources/Ultimate/MazeMotion.swift (17715 chars, 438 lines).
 *  Swift types: MazeAnimatedExpeditionRow, MazeAnimatedRegionRow, MazeAnimatedBondBar, MazeCelebrationConfetti, MazeComboReel, MazeMotionShowcaseView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MazeMotion {
    private MazeMotion() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MazeMotion.swift";
    public static final int SWIFT_LINES = 438;
    public static final List<String> SWIFT_TYPES = List.of("MazeAnimatedExpeditionRow", "MazeAnimatedRegionRow", "MazeAnimatedBondBar", "MazeCelebrationConfetti", "MazeComboReel", "MazeMotionShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
