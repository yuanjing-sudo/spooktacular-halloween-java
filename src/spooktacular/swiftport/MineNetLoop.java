package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineNetLoop.swift.
 *  Original: Sources/Ultimate/MineNetLoop.swift (14185 chars, 383 lines).
 *  Swift types: MineNetCommand, Kind, MineNetEvent, MineAuthority, Snapshot, MineRelayDemo, MineNetShowcaseView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineNetLoop {
    private MineNetLoop() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineNetLoop.swift";
    public static final int SWIFT_LINES = 383;
    public static final List<String> SWIFT_TYPES = List.of("MineNetCommand", "Kind", "MineNetEvent", "MineAuthority", "Snapshot", "MineRelayDemo", "MineNetShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
