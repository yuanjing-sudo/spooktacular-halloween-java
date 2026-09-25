package spooktacular.swiftport;

import java.util.List;

/** Java expression of SpooktacularApp.swift.
 *  Original: Sources/App/SpooktacularApp.swift (627 chars, 23 lines).
 *  Swift types: SpooktacularApp
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class SpooktacularApp {
    private SpooktacularApp() {}
    public static final String ORIGINAL_SWIFT = "Sources/App/SpooktacularApp.swift";
    public static final int SWIFT_LINES = 23;
    public static final List<String> SWIFT_TYPES = List.of("SpooktacularApp");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
