package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineChoreography.swift.
 *  Original: Sources/Ultimate/MineChoreography.swift (15981 chars, 404 lines).
 *  Swift types: MineWailCanon, MineCrystalWave, MineParadeDrill, MineUnlockRitual, MineCannonade, MineChoreographyShowcaseView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineChoreography {
    private MineChoreography() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineChoreography.swift";
    public static final int SWIFT_LINES = 404;
    public static final List<String> SWIFT_TYPES = List.of("MineWailCanon", "MineCrystalWave", "MineParadeDrill", "MineUnlockRitual", "MineCannonade", "MineChoreographyShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
