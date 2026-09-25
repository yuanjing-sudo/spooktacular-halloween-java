package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineDeepDive.swift.
 *  Original: Sources/Ultimate/MineDeepDive.swift (10440 chars, 235 lines).
 *  Swift types: MineDiveContract, MineDiveGuide, MineDeepDive, MineDeepDiveView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineDeepDive {
    private MineDeepDive() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineDeepDive.swift";
    public static final int SWIFT_LINES = 235;
    public static final List<String> SWIFT_TYPES = List.of("MineDiveContract", "MineDiveGuide", "MineDeepDive", "MineDeepDiveView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
