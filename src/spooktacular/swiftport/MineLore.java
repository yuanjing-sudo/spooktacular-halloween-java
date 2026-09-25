package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineLore.swift.
 *  Original: Sources/Ultimate/MineLore.swift (29912 chars, 692 lines).
 *  Swift types: MineLoreRule, MineLoreFragment, MineLoreCatalog, MineLoreView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineLore {
    private MineLore() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineLore.swift";
    public static final int SWIFT_LINES = 692;
    public static final List<String> SWIFT_TYPES = List.of("MineLoreRule", "MineLoreFragment", "MineLoreCatalog", "MineLoreView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
