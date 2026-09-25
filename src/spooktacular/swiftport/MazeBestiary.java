package spooktacular.swiftport;

import java.util.List;

/** Java expression of MazeBestiary.swift.
 *  Original: Sources/Ultimate/MazeBestiary.swift (19914 chars, 400 lines).
 *  Swift types: MazeBestiaryNote, MazeBestiary, MazeBestiaryView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MazeBestiary {
    private MazeBestiary() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MazeBestiary.swift";
    public static final int SWIFT_LINES = 400;
    public static final List<String> SWIFT_TYPES = List.of("MazeBestiaryNote", "MazeBestiary", "MazeBestiaryView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
