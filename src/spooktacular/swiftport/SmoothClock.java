package spooktacular.swiftport;

import java.util.List;

/** Java expression of SmoothClock.swift.
 *  Original: Sources/Ultimate/SmoothClock.swift (3840 chars, 109 lines).
 *  Swift types: GameClock
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class SmoothClock {
    private SmoothClock() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/SmoothClock.swift";
    public static final int SWIFT_LINES = 109;
    public static final List<String> SWIFT_TYPES = List.of("GameClock");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
