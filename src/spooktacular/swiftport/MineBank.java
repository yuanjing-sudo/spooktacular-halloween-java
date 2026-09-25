package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineBank.swift.
 *  Original: Sources/Ultimate/MineBank.swift (6600 chars, 171 lines).
 *  Swift types: MineBankView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineBank {
    private MineBank() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineBank.swift";
    public static final int SWIFT_LINES = 171;
    public static final List<String> SWIFT_TYPES = List.of("MineBankView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
