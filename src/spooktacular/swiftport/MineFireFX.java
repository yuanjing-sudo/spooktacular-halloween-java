package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineFireFX.swift.
 *  Original: Sources/Ultimate/MineFireFX.swift (12070 chars, 325 lines).
 *  Swift types: MineCampfire, MineTorchArray, MineForgeBed, MineFireflyDusk, MineFirework, MineFireworkFinale, MineFireShowcaseView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineFireFX {
    private MineFireFX() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineFireFX.swift";
    public static final int SWIFT_LINES = 325;
    public static final List<String> SWIFT_TYPES = List.of("MineCampfire", "MineTorchArray", "MineForgeBed", "MineFireflyDusk", "MineFirework", "MineFireworkFinale", "MineFireShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
