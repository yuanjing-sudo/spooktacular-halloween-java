package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineRelics.swift.
 *  Original: Sources/Ultimate/MineRelics.swift (9600 chars, 208 lines).
 *  Swift types: MNRelic, MNRelicCatalog, MineRelicVaultView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineRelics {
    private MineRelics() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineRelics.swift";
    public static final int SWIFT_LINES = 208;
    public static final List<String> SWIFT_TYPES = List.of("MNRelic", "MNRelicCatalog", "MineRelicVaultView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
