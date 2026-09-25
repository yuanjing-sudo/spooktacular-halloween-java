package spooktacular.swiftport;

import java.util.List;

/** Java expression of MazeBonds.swift.
 *  Original: Sources/Ultimate/MazeBonds.swift (20229 chars, 534 lines).
 *  Swift types: BoxyBond, BoxyBondRank, BoxyBondLedger, BoxyBondView, MazeJournalView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MazeBonds {
    private MazeBonds() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MazeBonds.swift";
    public static final int SWIFT_LINES = 534;
    public static final List<String> SWIFT_TYPES = List.of("BoxyBond", "BoxyBondRank", "BoxyBondLedger", "BoxyBondView", "MazeJournalView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
