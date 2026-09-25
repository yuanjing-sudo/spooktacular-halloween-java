package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineBootSequence.swift.
 *  Original: Sources/Ultimate/MineBootSequence.swift (9095 chars, 223 lines).
 *  Swift types: MineBootSequenceView, MineWelcomeCard
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineBootSequence {
    private MineBootSequence() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineBootSequence.swift";
    public static final int SWIFT_LINES = 223;
    public static final List<String> SWIFT_TYPES = List.of("MineBootSequenceView", "MineWelcomeCard");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
