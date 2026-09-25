package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineLoopKit.swift.
 *  Original: Sources/Ultimate/MineLoopKit.swift (9744 chars, 259 lines).
 *  Swift types: MineOscillator, MinePulseDot, MineBobRow, MineBeatClock, MineBeatTorches, MineBeatGhosts, MineBeatCrystals, MineLoopShowcaseView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineLoopKit {
    private MineLoopKit() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineLoopKit.swift";
    public static final int SWIFT_LINES = 259;
    public static final List<String> SWIFT_TYPES = List.of("MineOscillator", "MinePulseDot", "MineBobRow", "MineBeatClock", "MineBeatTorches", "MineBeatGhosts", "MineBeatCrystals", "MineLoopShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
