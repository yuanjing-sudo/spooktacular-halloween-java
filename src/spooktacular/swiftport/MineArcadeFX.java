package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineArcadeFX.swift.
 *  Original: Sources/Ultimate/MineArcadeFX.swift (14761 chars, 395 lines).
 *  Swift types: MinePumpkinSquash, MineRhythmHighway, MineTriviaOption, MineTriviaDemo, MineCoinPusher, MineWhackMole, MineArcadeShowcaseView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineArcadeFX {
    private MineArcadeFX() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineArcadeFX.swift";
    public static final int SWIFT_LINES = 395;
    public static final List<String> SWIFT_TYPES = List.of("MinePumpkinSquash", "MineRhythmHighway", "MineTriviaOption", "MineTriviaDemo", "MineCoinPusher", "MineWhackMole", "MineArcadeShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
